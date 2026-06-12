package org.creamycorp;

import org.java_websocket.server.WebSocketServer;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;

public class ServerTest {
    @Test
    public void testServerConn() {
        WebSocketServer server = new VmServer(new InetSocketAddress("localhost", 3001));
        server.start();
    }
}
