package org.creamycorp;

import org.creamycorp.tools.ListDirTool;
import org.creamycorp.tools.ReadFileTool;
import org.creamycorp.tools.StructureReport;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServerTest {
    @Test
    public void testServerConn() {
        assertDoesNotThrow(() -> {
            WebSocketServer server = new VmServer(new InetSocketAddress("localhost", 0));
            server.start();
            server.stop();
        });
    }

    @Test
    public void testListTool() {
        Path tmpDir;
        try {
            tmpDir = Files.createTempDirectory("list_tool_testing");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            Files.createFile(tmpDir.resolve("a.txt"));
            Files.createFile(tmpDir.resolve("b.txt"));
            Files.createDirectory(tmpDir.resolve("folder"));

            JSONObject input = new JSONObject();
            input.put("folder_path", tmpDir.toString());

            JSONObject output = new ListDirTool(input).run();
            System.out.println(output.toString());

            assertTrue(output.has("files"));
            assertEquals(1, output.getJSONArray("files").length());

            Object filesObj = output.getJSONArray("files").get(0);
            List<?> names = assertInstanceOf(List.class, filesObj);

            assertEquals(3, names.size());
            assertTrue(names.contains("a.txt"));
            assertTrue(names.contains("b.txt"));
            assertTrue(names.contains("folder"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try (var stream = Files.walk(tmpDir)) {
                stream.sorted(Comparator.reverseOrder()).forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException ignored) {
                    }
                });
            } catch (IOException ignored) {
            }
        }
    }

    @Test
    public void testReadFileTool() {
        Path tmpFile;
        try {
            tmpFile = Files.createTempFile("read_file_testing", ".txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            Files.writeString(tmpFile, "hello\nworld");

            JSONObject input = new JSONObject();
            input.put("file_path", tmpFile.toString());

            JSONObject output = new ReadFileTool(input).run();
            System.out.println(output.toString());

            assertTrue(output.has("file_content"));
            assertEquals(1, output.getJSONArray("file_content").length());
            assertEquals("hello\nworld", output.getJSONArray("file_content").getString(0));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                Files.deleteIfExists(tmpFile);
            } catch (IOException ignored) {
            }
        }
    }

    @Test
    public void testStructureReport() {
        Path tmpDir;
        try {
            tmpDir = Files.createTempDirectory("structure_report_testing");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            Files.createDirectory(tmpDir.resolve("folderA"));
            Files.createDirectory(tmpDir.resolve("folderA").resolve("folderB"));

            JSONObject input = new JSONObject();
            input.put("dir", tmpDir.toString());

            JSONObject output = new StructureReport(input).run();
            System.out.println(output.toString());

            assertTrue(output.has("directories"));
            assertEquals(1, output.getJSONArray("directories").length());

            Object dirsObj = output.getJSONArray("directories").get(0);
            List<?> dirs = assertInstanceOf(List.class, dirsObj);

            assertTrue(dirs.isEmpty());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try (var stream = Files.walk(tmpDir)) {
                stream.sorted(Comparator.reverseOrder()).forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException ignored) {
                    }
                });
            } catch (IOException ignored) {
            }
        }
    }
}

