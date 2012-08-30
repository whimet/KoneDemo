package demo;

import demo.communication.UDPServer;
import demo.communication.UdpListener;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

public class JettyServer {

    public static void main(String[] args) throws Exception {
        new UDPServer(4567, new UdpListener() {
            @Override
            public void onMessage(String message) {
                System.out.println("Received message: " + message);
            }
        }).start();

        Server server = new Server(8090);
        WebAppContext webapp = new WebAppContext("src/main/webapp", "/kone");
        server.addHandler(webapp);
        server.start();
    }
}
