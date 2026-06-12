package org.creamycorp.tools;

import org.creamycorp.ToolCall;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Stream;

public class StructureReport implements ToolCall {
    private final String dirPath;

    public StructureReport(JSONObject obj) {
        dirPath = obj.getString("dir") == null ? "" : obj.getString("dir");
    }

    public static List<String> climbDir(File dir) {
        List<File> dir_list = Stream.of(dir.listFiles())
                .filter(x -> x.isDirectory())
                .toList();

        ArrayList<String> dirs = new ArrayList<>();
        for (File f : dir_list) {
            dirs.addAll(climbDir(f));
        }

        return dirs;
    }

    @Override
    public JSONObject run() {
        File dir = new File(dirPath);
        JSONObject obj = new JSONObject();

        if (!dir.exists()) {
            obj.append("error", "Directory not found!");
            return obj;
        }

        obj.append("directories",climbDir(dir));

        return obj;
    }
}
