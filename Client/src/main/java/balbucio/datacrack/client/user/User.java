package balbucio.datacrack.client.user;

import org.json.JSONObject;

import java.util.UUID;

public class User {

    private String username;
    private String password;
    private UUID uid;

    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.uid = UUID.randomUUID();
    }

    public User(String username, String password, UUID uid){
        this.username = username;
        this.password = password;
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UUID getUUID() {
        return uid;
    }

    public void setUUID(UUID uid) {
        this.uid = uid;
    }
}
