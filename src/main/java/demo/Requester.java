package demo;

import java.io.*;
import java.net.*;

public class Requester {
    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;
    String message;

    Requester() {
    }

    void run() {
        try {
            //1. creating a socket to connect to the server
            socket = new Socket("localhost", 2004);
            System.out.println("Connected to localhost in port 2004");
            //2. get Input and Output streams
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
            //3: Communicating with the server
            do {
                try {
                    message = (String) in.readObject();
                    System.out.println("server>" + message);
                    sendMessage("Hi my server");
                    message = "bye";
                    sendMessage(message);
                } catch (ClassNotFoundException classNot) {
                    System.err.println("data received in unknown format");
                }
            } while (!message.equals("bye"));
        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            //4: Closing connection
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    void sendMessage(String msg) {
        try {
            out.writeObject(msg);
            out.flush();
            System.out.println("client>" + msg);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static void main(String args[]) {
        Requester client = new Requester();
        client.run();
    }
}
