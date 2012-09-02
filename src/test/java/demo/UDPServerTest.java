package demo;

import demo.communication.UDPClient;
import demo.communication.UDPServer;
import demo.communication.UDPListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.internal.matchers.IsCollectionContaining.hasItem;

public class UDPServerTest {

    private UDPServer server;

    @Before
    public void setUp() throws Exception {
        server = new UDPServer(3456);
    }

    @After
    public void tearDown() throws InterruptedException {
        server.stop();
    }

    @Test
    public void should_notify_listener_when_received_message() throws IOException, InterruptedException {
        final ArrayList<String> strings = new ArrayList<String>();
        server = new UDPServer(3456, new UDPListener() {
            @Override
            public void onMessage(String message) {
                strings.add(message);
            }
        });
        server.start();

        UDPClient client = new UDPClient("localhost", 3456);
        client.connect();
        client.send("hello UDP");
        client.send("hello UDP2");
        client.disconnect();
        Thread.sleep(100); //TODO: fix this

        assertThat(strings.size(), is(2));
        assertThat(strings, hasItem("hello UDP"));
        assertThat(strings, hasItem("hello UDP2"));
    }

    @Test
    public void shouldReplyUdpClient() throws IOException, InterruptedException {
        server.start();

        UDPClient client = new UDPClient("localhost", 3456);
        client.connect();
        client.send("hello server");
        Thread.sleep(100); //TODO: fix this

        server.reply("hello client");
        String message = client.receive();
        client.disconnect();

        assertThat(message, is("hello client"));
    }
}
