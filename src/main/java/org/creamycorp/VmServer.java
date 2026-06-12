package org.creamycorp;

import org.creamycorp.tools.ListDirTool;
import org.creamycorp.tools.ReadFileTool;
import org.creamycorp.tools.StructureReport;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.util.ArrayList;

public class VmServer extends WebSocketServer {
    public VmServer(InetSocketAddress addr) {
        super(addr);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        // conn.send sends a method to the exsting conn
        // broadcast method sends to every single client.
        conn.send("Welcome to the server!");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("[Log: ] Client "+conn.getRemoteSocketAddress()+" has disconnected!");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("[Log: ] Client "+conn.getRemoteSocketAddress()+" has message: "+message);

        JSONObject messageJson = new JSONObject(message);
        JSONArray obj = messageJson.getJSONArray("tools");

        ArrayList<ToolCall> tools = new ArrayList<>();

        for (int i = 0; i < obj.length(); i++) {
            JSONObject iObj = obj.getJSONObject(i);

            String name = iObj.getString("tool_name");

            switch (name) {
                case ("read_file"):
                    ReadFileTool tool = new ReadFileTool(iObj);
                    tools.add(tool);
                    break;
                case ("structure_report"):
                    StructureReport s_report = new StructureReport(iObj);
                    tools.add(s_report);
                    break;
                case ("list_folder"):
                    ListDirTool l_folder = new ListDirTool(iObj);
                    tools.add(l_folder);
                    break;
            }
        }

        JSONArray response_array = new JSONArray();
        for (ToolCall t : tools) {
            response_array.put(t.run());
        }

        JSONObject finalObj = new JSONObject();
        finalObj.append("responses", response_array);

        conn.send(finalObj.toString());
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.out.println("Error: "+ex);
    }

    @Override
    public void onStart() {
        System.out.println("server started successfully");
    }
}
