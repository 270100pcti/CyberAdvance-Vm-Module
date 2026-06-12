package org.creamycorp.tools;

import org.creamycorp.ToolCall;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class ListDirTool implements ToolCall {
    private final String dirPath;

    public ListDirTool(JSONObject obj) {
        dirPath = obj.getString("folder_path") == null ? "" : obj.getString("folder_path");
    }
    @Override
    public JSONObject run() {
        File dir = new File(dirPath);
        JSONObject obj = new JSONObject();

        if (!dir.exists()) {
            obj.append("error", "Directory not found!");
            return obj;
        }

        List<String> names = Stream.of(dir.listFiles())
                .filter(x -> x.exists())
                .map(x -> x.getName())
                .toList();
        obj.append("files", names);

        return obj;
    }
}
