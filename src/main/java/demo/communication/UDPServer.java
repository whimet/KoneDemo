package demo.communication;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;

public class UDPServer {
    private int port;
    private UDPListener[] listeners;
    private DatagramSocket serverSocket;
    private InetAddress lastClientAddress;
    private int lastClientPort;
    private Thread thread;

    public UDPServer(int port, UDPListener... listeners) {
        this.port = port;
        this.listeners = listeners;
    }

    public void start() throws IOException {
        serverSocket = new DatagramSocket(port);

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    loop();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private void loop() throws IOException {
        while (true) {
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);

            lastClientAddress = receivePacket.getAddress();
            lastClientPort = receivePacket.getPort();

            String message = Utils.extractMessage(receivePacket);
            for (UDPListener listener : listeners) {
                listener.onMessage(message);
            }
        }
    }

    public void reply(String message) throws IOException {
        if(lastClientAddress == null) throw new IllegalStateException("Unknown destination");
        
        try {
            byte[] sendData = message.getBytes("UTF-8");
            DatagramPacket packet = new DatagramPacket(sendData, sendData.length, lastClientAddress, lastClientPort);
            serverSocket.send(packet);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() throws InterruptedException {
        thread.stop();
        serverSocket.close();
    }

    public static void main(String args[]) throws Exception {
        DatagramSocket serverSocket = new DatagramSocket(3456);
        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];
        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            String sentence = new String(receivePacket.getData());
            System.out.println("RECEIVED: " + sentence);
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            String capitalizedSentence = sentence.toUpperCase();
            sendData = capitalizedSentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
        }
    }
}