package demo.web;

import demo.communication.UDPServer;
import demo.communication.UDPListener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.sql.Timestamp;

public class ContextListener implements ServletContextListener {
    public static long lastPing;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            new UDPServer(9090, new UDPListener() {
                @Override
                public void onMessage(String message) {
                    lastPing = System.currentTimeMillis();
                    System.out.println(new Timestamp(lastPing) + " - received heartbeat: " + message);
                }
            }).start();
            System.out.println("Listening agent heartbeat message at port 9090...");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
