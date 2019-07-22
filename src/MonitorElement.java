import java.io.File;
import java.io.IOException;

public class MonitorElement {
    /*
    default constructor, does nothing actually
    */
    public MonitorElement () {
        // NOP
    }
    
    public MonitorElement(
    String name, String host, int port,
    String user, String passwd, String file, String encode) {
        this.name_ = name;
        this.host_ = host;
        this.port_ = port;
        this.user_ = user;
        this.passwd_ = passwd;
        this.file_ = file;
        this.encode_ = encode;
    };
    
    /*
    all of the methods are public, but variables are private access only,
    since accessors are create to access the data.
    */
    private String name_;
    public void setname(String str) {
        name_ = str;
    }
    public String getname() {
        return name_;
    }
    
    private String host_;
    public void sethost(String str) {
        host_ = str;
    }
    public String gethost() {
        return host_;
    }
    
    private int port_;
    public void setport(int num) {
        port_ = num;
    }
    public int getport() {
        return port_;
    }
    
    private String user_;
    public void setuser(String str) {
        user_ = str;
    }
    public String getuser() {
        return user_;
    }
    
    private String passwd_;
    public void setpasswd(String str) {
        passwd_ = str;
    }
    public String getpasswd() {
        return passwd_;
    }
    
    private String file_;
    public void setfile(String str) {
        file_ = str;
    }
    public String getfile() {
        return file_;
    }

    private String encode_;
    public void setnencode(String str) {
        encode_ = str;
    }
    public String getencode() {
        return encode_;
    }

    // debug usage
    public void printAllElement() {
        System.out.println("name: " + name_);
        System.out.println("host: " + host_);
        System.out.println("port: " + port_);
        System.out.println("user: " + user_);
        System.out.println("passwd: " + passwd_);
        System.out.println("file: " + file_);
        System.out.println("encode: " + encode_);
    }
}
