package demo.web;

import demo.communication.UDPClient;
import demo.communication.UDPServer;

import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.net.InetAddress;

public class UDPMessageServlet extends HttpServlet {

    protected void sendCommand(String message) throws IOException {
        UDPServer udpServer = (UDPServer) getServletContext().getAttribute(ContextListener.UDP_SERVER);
        InetAddress clientAddress = udpServer.getLastClientAddress();
        if (clientAddress != null) {
            UDPClient udpClient = new UDPClient(clientAddress, getClientPort());
            udpClient.connect();
            udpClient.send(message);
            udpClient.disconnect();
        } else {
            System.err.println("Haven't received client heartbeat yet!");
        }
    }

    private int getClientPort() {
        return Integer.parseInt(System.getProperty("client.port", "9009"));
    }
}
