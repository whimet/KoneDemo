package demo.communication;

import java.io.*;
import java.net.*;

public class UDPClient {
    private int port;
    private DatagramSocket socket;
    private InetAddress server;

    public UDPClient(String server, int port) throws UnknownHostException {
        this(InetAddress.getByName(server), port);
    }

    public UDPClient(InetAddress server, int port) {
        this.server = server;
        this.port = port;
    }


    public void connect() throws SocketException {
        socket = new DatagramSocket();
    }

    public void send(String message) throws IOException {
        byte[] sendData = message.getBytes("UTF-8");
        DatagramPacket packet = new DatagramPacket(sendData, sendData.length, server, port);
        socket.send(packet);
    }

    public void disconnect() {
        socket.close();
    }

    public String receive() throws IOException {
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);
        return Utils.extractMessage(receivePacket);
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