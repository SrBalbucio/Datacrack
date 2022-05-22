/*
 * Copyright (c) 2022.
 * Datacrack é desenvolvido e mantido por SrBalbucio.
 * Não revenda e/ou distribua como seu.
 */

package balbucio.datacrack.users;

import balbucio.datacrack.Main;
import org.bukkit.configuration.file.YamlConfiguration;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserManager {

    private List<User> users = new ArrayList<>();
    private YamlConfiguration config;
    private File file;

    public UserManager(YamlConfiguration config, File file){
        load(config);
        this.config = config;
        this.file = file;
    }

    public boolean containsUser(String username){
        return users.stream().filter(u -> u.getUsername().equals(username)).count() > 0;
    }

    public User getUser(String username){
        if(!containsUser(username)) { return null; }
        return users.stream().filter(u -> u.getUsername().equals(username)).findFirst().get();
    }

    public void createNewUser(JSONObject source){
        String usr = source.getString("username");
        String pswrd = source.getString("password");
        String permissions = source.getString("permissions");
        users.add(new User(usr, pswrd, false, Arrays.asList(permissions.split(","))));
        save(config, file);
    }

    private void load(YamlConfiguration config){
        for(String key : config.getConfigurationSection("users").getKeys(false)){
            String username = config.getString("users."+key+".username");
            String password = config.getString("users."+key+".password");
            boolean isAdmin = config.getBoolean("users."+key+".isAdmin");
            List<String> permissions = config.getStringList("users."+key+".permissions");
            users.add(new User(username, password, isAdmin, permissions));
        }
    }

    private void save(YamlConfiguration config, File file){
        try {
            for (User user : users) {
                config.set("users." + user.getUsername() + ".username", user.getUsername());
                config.set("users." + user.getUsername() + ".password", user.getPassword());
                config.set("users." + user.getUsername() + ".isAdmin", user.isAdmin());
                config.set("users." + user.getUsername() + ".permissions", user.getPermissions());
            }
            config.save(file);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
