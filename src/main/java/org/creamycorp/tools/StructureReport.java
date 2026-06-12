package org.creamycorp.tools;

import org.creamycorp.ToolCall;
import org.json.JSONArray;
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

    public static JSONArray climbDir(File dir) {
        List<File> dir_list = Stream.of(dir.listFiles())
                .filter(x -> x.isDirectory())
                .toList();
        List<File> file_list = Stream.of(dir.listFiles())
                .filter(x -> x.isFile())
                .toList();

        JSONArray files = new JSONArray();

        files.put(dir.getPath());

        for (File fc : file_list) {
            files.put(fc.getPath());
        }

        for (File ff : dir_list) {
            JSONArray subArray = climbDir(ff);
            for (int i = 0; i < subArray.length(); i++) {
                files.put(subArray.get(i));
            }
        }

        return files;
    }

    @Override
    public JSONObject run() {
        File dir = new File(dirPath);
        JSONObject obj = new JSONObject();

        if (!dir.exists()) {
            obj.append("error", "Directory not found!");
            return obj;
        }

        obj.append("directories",dir);

        return obj;
    }
}
