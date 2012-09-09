package demo;

import demo.communication.UDPClient;
import demo.communication.UDPListener;
import demo.communication.UDPServer;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class FakeAgent {

    private static final String SERVER = "localhost";

    public static void main(String[] args) throws IOException {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ping();
            }
        }, 0, 4 * 1000);

        new UDPServer(9009, new UDPListener() {
            @Override
            public void onMessage(String message) {
                System.out.println("Received message: " + message);
            }
        }).start();
        System.out.println("Listening server-to-agent message at port 9009...");
    }

    private static void ping() {
        try {
            UDPClient client = new UDPClient(SERVER, 9090);
            client.connect();
            client.send("ping");
            client.disconnect();
            System.out.println("Ping server once.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
