package org.creamycorp;

import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CyberAdvanceVM {
    private static int port = 6767;
    private static String host = "localhost";

    public static void main(String[] args) {
        loadConfig();

        WebSocketServer server = new VmServer(new InetSocketAddress(host, port));
        server.start();
    }

    public static void loadConfig() {
        try {
            List<String> lines = Files.readAllLines(Paths.get("cyberadv_config.json"));
            String linesConcat = String.join("", lines);

            JSONObject configObject = new JSONObject(linesConcat);
            host = configObject.getString("host");
            port = configObject.getInt("port");

        } catch (IOException ex) {
            System.out.println("Could not read configs, falling back to defaults.");
        } finally {
            System.out.println("Configuration loaded successfully!");
        }
    }
}
