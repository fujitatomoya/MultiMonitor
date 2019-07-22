import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

public class MultiMonitor implements Runnable {

    public static void main(String[] args) throws IOException, InterruptedException, Exception {

        // 1st of all parse the json file to cache the each element.
        MonitorParser mp = new MonitorParser("../etc/MonitorConfig.json");
        ArrayList<MonitorElement> me_list = mp.startParse();

        // then make the main object for MultiMonitor
        ArrayList<MultiMonitor> mm_list = new ArrayList<MultiMonitor>();
        for (int i = 0; i < me_list.size(); i++) {
            mm_list.add(
                new MultiMonitor(
                    me_list.get(i).getname(),
                    me_list.get(i).gethost(),
                    me_list.get(i).getport(),
                    me_list.get(i).getuser(),
                    me_list.get(i).getpasswd(),
                    me_list.get(i).getfile(),
                    me_list.get(i).getencode()
                )
            );
        }

        // start monitor thread for each object
        for (int j = 0; j < mm_list.size(); j++) {
            mm_list.get(j).start();
        }

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                // check each object if error is observed.
                for (MultiMonitor mm : mm_list) {
                    if (mm.getException() != null) {
                        mm.terminateAll(mm_list);
                        throw mm.getException();
                    }
                }

                if (!reader.ready()) {
                    Thread.sleep(50);
                    continue;
                }

                // if request "quit", terminate everything.
                if ("quit".equals(reader.readLine())) {
                    mm_list.get(0).terminateAll(mm_list);
                    break;
                }
            }
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e) {
                }
            }
        }
    }

    private static void terminateAll(ArrayList<MultiMonitor> mm_list) throws InterruptedException {
        for (MultiMonitor mm : mm_list) {
            mm.terminate();
        }
    }

    // private variables
    private String name = null;
    private String host = null;
    private int port = -1;
    private String user = null;
    private String passwd = null;
    private String file = null;
    private String encode = null;
    private Thread thread = null;
    private boolean terminated = false;
    private Exception exception = null;

    public MultiMonitor(String name, String host, int port, String user, String passwd, String file, String encode) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.user = user;
        this.passwd = passwd;
        this.file = file;
        this.encode = encode;
    }

    // Create thread for each monitor
    public void start() {
        this.thread = new Thread(this);
        this.thread.start();
        System.out.println("started [" + this.name + "]");
    }

    // Destroy thread to join here
    public void terminate() throws InterruptedException {
        this.terminated = true;
        this.thread.join();
        System.out.println("terminated [" + this.name + "]");
    }

    @Override
    public void run() {
        JSch jsch;
        Session session = null;
        ChannelExec channel = null;
        BufferedWriter writer = null;
        BufferedReader reader = null;

        String currentFilePath = null;
        String newFilePath;

        try {
            currentFilePath = this.file;

            jsch = new JSch();
            session = jsch.getSession(this.user, this.host, this.port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(this.passwd);
            session.connect();

            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand("tail -f " + currentFilePath);
            channel.connect();
            System.out.println("tail [" + this.name + ":" + currentFilePath + "]");

            writer = new BufferedWriter(new OutputStreamWriter(channel.getOutputStream(), this.encode));
            reader = new BufferedReader(new InputStreamReader(channel.getInputStream(), this.encode), 128);

            while (true) {

                if (channel.isClosed()) {
                    throw new IOException("closed [" + this.name + "]");
                }
                if (this.terminated) {
                    throw new InterruptedException("terminated [" + this.name + "]");
                }

                if (!reader.ready()) {
                    Thread.sleep(50);
                    continue;
                }
                writer.write(" ");
                writer.flush();

                System.out.println(this.name + ":" + reader.readLine());
            }
        }
        catch (JSchException | IOException | InterruptedException e) {
            this.exception = e;
        }
        finally {
            if (writer != null) {
                try {
                    writer.close();
                }
                catch (IOException e) {
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e) {
                }
            }
            if (channel != null) {
                try {
                    channel.disconnect();
                }
                catch (Exception e) {
                }
            }
            if (session != null) {
                try {
                    session.disconnect();
                }
                catch (Exception e) {
                }
            }
        }
    }

    public Exception getException() {
        return this.exception;
    }
}
