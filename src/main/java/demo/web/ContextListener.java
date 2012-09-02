package demo.web;

import demo.communication.UDPServer;
import demo.communication.UDPListener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.sql.Timestamp;

public class ContextListener implements ServletContextListener {
    public static final String UDP_SERVER = "UDP_SERVER";
    public static final String LAST_PING_TIME = "LAST_PING_TIME";

    @Override
    public void contextInitialized(final ServletContextEvent sce) {
        try {
            UDPServer udpServer = new UDPServer(9090, new UDPListener() {
                @Override
                public void onMessage(String message) {
                    long now = System.currentTimeMillis();
                    sce.getServletContext().setAttribute(LAST_PING_TIME, now);
                    System.out.println(new Timestamp(now) + " - received heartbeat: " + message);
                }
            });
            udpServer.start();
            System.out.println("Listening agent heartbeat message at port 9090...");

            sce.getServletContext().setAttribute(UDP_SERVER, udpServer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
