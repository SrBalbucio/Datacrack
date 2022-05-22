/*
 * Copyright (c) 2022.
 * Datacrack é desenvolvido e mantido por SrBalbucio.
 * Não revenda e/ou distribua como seu.
 */

package balbucio.datacrack.file;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ListFile {

    private File file;
    private YamlConfiguration config;

    public ListFile(File file){
        try {
            this.file = file;
            if (!file.exists()) {
                File folder = new File(file.getParent());
                if (!folder.exists()) {
                    folder.mkdir();
                }
                file.createNewFile();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void save(){
        try {
            this.config.save(file);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public YamlConfiguration getConfig(){
        return config;
    }
}
