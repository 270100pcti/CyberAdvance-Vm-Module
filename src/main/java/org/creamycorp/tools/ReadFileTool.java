package org.creamycorp.tools;

import org.creamycorp.ToolCall;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ReadFileTool implements ToolCall {
    private final String filePath;

    public ReadFileTool(JSONObject obj) {
        filePath = obj.getString("file_path") == null ? "" : obj.getString("file_path");
    }
    @Override
    public JSONObject run() {
        JSONObject obj = new JSONObject();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            String linesConcat = String.join("\n", lines);

            obj.append("file_content", linesConcat);
        } catch (IOException io_ex) {
            obj.append("error",io_ex.toString());
        }

        return obj;
    }
}
