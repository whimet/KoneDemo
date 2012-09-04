package demo.web;

import demo.communication.UDPServer;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.sql.Timestamp;

public class IndexServlet extends HttpServlet {

    private static final int ACTIVE_TIMEOUT_IN_MILLIS = 12 * 1000;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Content-Type", "text/html; charset=UTF-8");
        resp.getWriter().write(render());
    }

    private String render() throws IOException {
        UDPServer udpServer = (UDPServer) getServletContext().getAttribute(ContextListener.UDP_SERVER);
        InetAddress clientAddress = udpServer.getLastClientAddress();

        if(clientAddress == null) return readClasspathResource("template/index_blank.html");
        
        return readClasspathResource("template/index.html").replace("$IP$", clientAddress.getHostAddress()).replace("$STATUS$", clientStatus()).replace("$PING$", lastPingTime());
    }

    private String lastPingTime() {
        Long lastPing = (Long) getServletContext().getAttribute(ContextListener.LAST_PING_TIME);
        return new Timestamp(lastPing).toString();
    }

    private String clientStatus() {
        return isClientActive() ? "Active" : "Inactive";
    }

    private boolean isClientActive() {
        Long lastPing = (Long) getServletContext().getAttribute(ContextListener.LAST_PING_TIME);
        return (System.currentTimeMillis() - lastPing) <= ACTIVE_TIMEOUT_IN_MILLIS;
    }

    private String readClasspathResource(String resource) throws IOException {
        InputStream input = null;
        try {
            input = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
            return IOUtils.toString(input);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }
}
