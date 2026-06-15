package org.creamycorp.tools;

import org.creamycorp.ToolCall;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.json.JSONArray;

public class FindFilesTool implements ToolCall {
    private ArrayList<String> matches = new ArrayList<>();
    private String baseDir = "";

    public FindFilesTool(JSONObject obj) {
        JSONArray arr = obj.getJSONArray("matches");
        for (int i = 0; i < arr.length(); i++) {
            matches.add(arr.get(i).toString());
        }

        baseDir = obj.getString("base_dir") == null ? "" : obj.getString("base_dir");
    }

    public static List<File> climbDir(File dir) {
        List<File> dir_list = Stream.of(dir.listFiles())
                .filter(x -> x.isDirectory())
                .toList();
        List<File> file_list = Stream.of(dir.listFiles())
                .filter(x -> x.isFile())
                .toList();

        ArrayList<File> files = new ArrayList<>();

        for (File fc : file_list) {
            files.add(fc);
        }

        for (File ff : dir_list) {
            List<File> subArray = climbDir(ff);
            for (File f : subArray) {
                files.add(f);
            }
        }

        return files;
    }

    @Override
    public JSONObject run() {
        File dir = new File(baseDir);
        JSONObject obj = new JSONObject();

        if (!dir.exists()) {
            obj.append("Error", "File path does not exist");
            return obj;
        }

        List<File> allFiles = climbDir(dir);

        List<String> paths = allFiles.stream()
                .map(x -> x.getPath())
                .filter(x -> {
                    for (String match : matches) {
                        if (x.indexOf(match) != -1) {
                            return true;
                        }
                    }
                    return false;
                })
                .toList();

        obj.append("found", paths);

        return obj;
    }
}
