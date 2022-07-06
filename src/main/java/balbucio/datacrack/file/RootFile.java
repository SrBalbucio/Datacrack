/*
 * Copyright (c) 2022.
 * Datacrack é desenvolvido e mantido por SrBalbucio.
 * Não revenda e/ou distribua como seu.
 */

package balbucio.datacrack.file;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class RootFile {

    private File file;
    private YamlConfiguration config;

    public RootFile(File file, String name){
        this.file = file;
        try {
            if (!file.exists()) {
                File path = new File(file.getParent());
                if (!path.exists()) {
                    path.mkdir();
                }
                file.createNewFile();
            }
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


}
