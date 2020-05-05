package me.kyllian.spigotconsole.security;

import org.json.JSONObject;

import javax.xml.ws.Response;

public class ResponseBuilder {

    JSONObject object;

    public ResponseBuilder() {
        object = new JSONObject();
    }

    public ResponseBuilder setType(String type) {
        object.put("type", type);
        return this;
    }

    public ResponseBuilder setMessage(String message) {
        object.put("message", message);
        return this;
    }

    public String build() {
        return object.toString();
    }
}
