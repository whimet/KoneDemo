package demo;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

import java.io.File;

public class JettyServer {

    public static void main(String[] args) throws Exception {
        Server server = new Server(8090);
        WebAppContext webapp = new WebAppContext(webapp(), "/kone");
        server.addHandler(webapp);
        server.start();
    }

    private static String webapp() {
        if (new File("kone.war").exists()) {
            System.out.println("Using kone.war");
            return "kone.war";
        }
        System.out.println("Using src/main/webapp");
        return "src/main/webapp";
    }
}
