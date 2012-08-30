package demo.communication;

import java.io.IOException;
import java.net.*;

public class UDPServer {
    private int port;
    private UdpListener[] listeners;

    public UDPServer(int port, UdpListener... listeners) {
        this.port = port;
        this.listeners = listeners;
    }

    public void start() throws IOException {
        Thread thread = new Thread(new Runnable() {
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
        DatagramSocket serverSocket = new DatagramSocket(port);
        byte[] receiveData = new byte[1024];
        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            byte[] data = receivePacket.getData();
            byte[] buf = new byte[receivePacket.getLength()];
            System.arraycopy(data, receivePacket.getOffset(), buf, 0, receivePacket.getLength());
            String message = new String(buf, "UTF-8");
            for (int i = 0; i < listeners.length; i++) {
                UdpListener listener = listeners[i];
                listener.onMessage(message);
            }
        }
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