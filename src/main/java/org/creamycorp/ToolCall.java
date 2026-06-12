package org.creamycorp;

import org.json.JSONObject;

public interface ToolCall {
    public default JSONObject run() {
        return new JSONObject();
    }
}
