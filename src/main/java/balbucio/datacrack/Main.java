/*
 * Copyright (c) 2022.
 * Datacrack é desenvolvido e mantido por SrBalbucio.
 * Não revenda e/ou distribua como seu.
 */

package balbucio.datacrack;

import balbucio.datacrack.users.UserManager;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.nio.file.Files;

public class Main {

    private static Main instance;
    private static String[] arguments;

    private WebSocket socket;
    private UserManager userManager;
    private File folder = new File("Configuration");
    private File config = new File(folder, "config.yml");
    private File users = new File(folder, "users.yml");

    private static YamlConfiguration configuration, usuarios;

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        setInstance(this);
        System.out.print("Datacrack iniciado com sucesso!");
        loadConfigurations();
        userManager = new UserManager(usuarios, users);
        socket = new WebSocket(configuration.getInt("Socket.Porta"));
    }

    public static Main getInstance() {
        return instance;
    }

    public static void setInstance(Main instance) {
        Main.instance = instance;
    }

    public WebSocket getSocket() {
        return socket;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    private void loadConfigurations(){
        try {
            if (!folder.exists()) {
                folder.mkdir();
            }

            if (!config.exists()) {
                Files.copy(this.getClass().getResourceAsStream("/config.yml"), config.toPath());
            }
            if (!users.exists()) {
                Files.copy(this.getClass().getResourceAsStream("/users.yml"), users.toPath());
            }
            configuration = YamlConfiguration.loadConfiguration(config);
            usuarios = YamlConfiguration.loadConfiguration(users);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.print("\nConfiguração carregada com sucesso!");
    }

}
