package demo.web;

import demo.communication.UDPClient;

import javax.servlet.http.HttpServlet;
import java.io.IOException;

public class UdpMessageServlet extends HttpServlet {

    protected void sendCommand(String message) throws IOException {
        UDPClient client = new UDPClient("localhost", 4567);
        client.connect();
        client.send(message);
        client.disconnect();
    }
}
