import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

public class MonitorParser {
        private String jsonfile;
        public void setFilePath (String filepath) {
                System.out.println("setting filepath " + filepath);
                jsonfile = filepath;
        }
        public String getFilePath () {
                System.out.println("getting filepath " + jsonfile);
                return jsonfile;
        }
    
        /*
          MonitorElement list
        */
        private ArrayList<MonitorElement> al = new ArrayList<MonitorElement>();

        /*
          Parser Start
        */
        public ArrayList<MonitorElement> startParse() {
                if (jsonfile == null) {
                        System.out.println("filepath is not set.");
                        return null; /** failure */
                }

                /*
                  start parsing the json file
                */
                try {
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode root = mapper.readTree(new File(jsonfile));
                        for (JsonNode n : root.get("target")) {
                                String name = n.get("name").asText();
                                String host = n.get("host").asText();
                                int port = n.get("port").asInt();
                                String user = n.get("user").asText();
                                String passwd = n.get("passwd").asText();
                                String file = n.get("file").asText();
                                String encode = n.get("encode").asText();

                                al.add(new MonitorElement(
                                        name, host, port,
                                        user, passwd, file, encode
                                ));
                                }
                } catch (IOException ioe) {
                        ioe.printStackTrace();
                }

                // print all of the element here for debug purpose.
                for (int i = 0; i < al.size(); i++ ) {
                        System.out.println("Element Index is " + i + " ----- ");
                        al.get(i).printAllElement();
                }

                return al; /** success */
        }

        /*
          Get Parsed Information
        */
        
        /** constructor */
        MonitorParser () {}
        MonitorParser (String filepath) {
                jsonfile = filepath;
        }

        /*
          just a temporary test code to debug MonitorParser
        */
        public static void main(String[] args) {
                //System.out.println("Hello! World!");
                MonitorParser mp = new MonitorParser("../etc/MonitorConfig.json");
                mp.startParse();
        }
}
