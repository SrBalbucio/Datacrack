/*
 * Copyright (c) 2022.
 * Datacrack é desenvolvido e mantido por SrBalbucio.
 * Não revenda e/ou distribua como seu.
 */

package balbucio.datacrack.client.socket;

import balbucio.datacrack.client.user.User;
import org.json.JSONObject;

public class Details {

    private User user;
    private JSONObject json;
    private String path;

    public Details(JSONObject json, String path) {
        this.json = json;
        this.path = path;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public JSONObject getJson() {
        return json;
    }

    public void setJson(JSONObject json) {
        this.json = json;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public JSONObject getRequestCommand() {
        JSONObject json = new JSONObject();
        json.put("username", user.getUsername());
        json.put("password", user.getPassword());
        json.put("uuid", user.getUUID().toString());
        json.put("path", path);
        return json;
    }
}
