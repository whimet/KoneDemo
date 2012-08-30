package demo;

import demo.communication.UDPClient;
import demo.communication.UDPServer;
import demo.communication.UdpListener;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.internal.matchers.IsCollectionContaining.hasItem;

public class UDPServerTest {
    @Test
    public void should_notify_listener_when_received_message() throws IOException {
        final ArrayList<String> strings = new ArrayList<String>();
        UDPServer server = new UDPServer(3456, new UdpListener() {
            @Override
            public void onMessage(String message) {
                strings.add(message);
            }
        });
        server.start();

        UDPClient client = new UDPClient("localhost", 3456);
        client.connect();
        client.sendMessage("hello UDP");
        client.sendMessage("hello UDP2");
        client.disconnect();

        assertThat(strings.size(), is(2));
        assertThat(strings, hasItem("hello UDP"));
        assertThat(strings, hasItem("hello UDP2"));
    }
}
