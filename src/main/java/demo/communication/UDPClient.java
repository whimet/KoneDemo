package demo.communication;

import java.io.*;
import java.net.*;

public class UDPClient {
    private String server;
    private int port;
    private DatagramSocket socket;

    public UDPClient(String server, int port) {
        this.server = server;
        this.port = port;
    }

    public void connect() throws SocketException {
        socket = new DatagramSocket();
    }

    public void sendMessage(String message) throws IOException {
        InetAddress IPAddress = InetAddress.getByName(server);
        byte[] sendData = message.getBytes("UTF-8");
        DatagramPacket packet = new DatagramPacket(sendData, sendData.length, IPAddress, port);
        socket.send(packet);
    }

    public void disconnect() {
        socket.close();
    }

    public static void main(String args[]) throws Exception {
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("localhost");
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];
        String sentence = inFromUser.readLine();
        sendData = sentence.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 3456);
        clientSocket.send(sendPacket);
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);
        String modifiedSentence = new String(receivePacket.getData());
        System.out.println("FROM SERVER:" + modifiedSentence);
        clientSocket.close();
    }
}