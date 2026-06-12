package org.creamycorp.tools;

import org.creamycorp.ToolCall;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class StructureReport implements ToolCall {
    private final String dirPath;

    public StructureReport(JSONObject obj) {
        dirPath = obj.optString("dir", "");
    }

    public static List<String> climbDir(File dir) {
        File[] children = dir.listFiles();
        if (children == null) {
            return List.of(dir.getName());
        }

        List<File> dir_list = Stream.of(children)
                .filter(File::isDirectory)
                .toList();

        ArrayList<String> dirs = new ArrayList<>();
        dirs.add(dir.getName());
        for (File f : dir_list) {
            dirs.add(f.getName());
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
