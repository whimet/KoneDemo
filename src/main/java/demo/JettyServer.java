package demo;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

public class JettyServer {

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        WebAppContext webapp = new WebAppContext("src/main/webapp", "/kone");
        server.addHandler(webapp);
        server.start();
    }
}
