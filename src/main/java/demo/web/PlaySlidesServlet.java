package demo.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PlaySlidesServlet extends UDPMessageServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        sendCommand("play_slides");
        resp.sendRedirect("/kone/result.html");
    }
}
