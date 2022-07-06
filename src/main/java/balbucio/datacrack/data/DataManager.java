/*
 * Copyright (c) 2022.
 * Datacrack é desenvolvido e mantido por SrBalbucio.
 * Não revenda e/ou distribua como seu.
 */

package balbucio.datacrack.data;

import balbucio.datacrack.file.DataFile;
import balbucio.datacrack.file.ListFile;
import org.json.JSONObject;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {

    public static JSONObject getRootPack(String name){
        DataFile data = new DataFile(new File("RootDataPacks", name+".json"), name);
        return data.load();
    }

    public static void updateRootPack(String name, JSONObject obj){
        DataFile data = new DataFile(new File("RootDataPacks", name+".json"), name);
        data.save(obj.toString());
    }

    public static void deleteRootPack(String name){
        File file = new File("RootDataPacks", name+".json");
        file.delete();
    }

    public static JSONObject getDataPack(String path){
        String[] pathSplit = path.split(",");
        String rootPath = pathSplit[0];
        DataFile data = new DataFile(new File("RootDataPacks", rootPath+".json"), rootPath);
        JSONObject rootJSON = data.load();
        JSONObject dataPack = rootJSON;
        for (int i = 1; i < pathSplit.length; i++) {
            dataPack = dataPack.getJSONObject(pathSplit[i] + "_datapack");
        }
        return dataPack;
    }

    public static void updateDataPack(String path, JSONObject obj) {
        String[] args = path.split("/");
        String[] pathSplit = args[0].split(",");
        String rootPath = pathSplit[0];
        DataFile data = new DataFile(new File("RootDataPacks", rootPath + ".json"), rootPath);
        JSONObject rootJSON = data.load();
        if (pathSplit.length == 1) {
            rootJSON.put(args[1] + "_datapack", obj);
            data.save(rootJSON.toString());
            return;
        }
        JSONObject dataPack = rootJSON;
        Map<String, JSONObject> jsonObjects = new HashMap<>();
        for (int i = 1; i < pathSplit.length; i++) {
            dataPack = dataPack.getJSONObject(pathSplit[i] + "_datapack");
            jsonObjects.put(pathSplit[i], dataPack);
        }
        dataPack.put(args[1] + "_datapack", obj);
        if (pathSplit.length == 2) {
            rootJSON.put(dataPack.get("name") + "_datapack", dataPack);
            data.save(rootJSON.toString());
            return;
        }
        JSONObject previousDataPack = dataPack;
        for(int i = pathSplit.length; i > 1; i--){
            JSONObject json = jsonObjects.get(pathSplit[i-1]);
            json.put(previousDataPack.get("name")+"_datapack", previousDataPack);
            previousDataPack = json;
        }
        rootJSON.put(previousDataPack.getString("name")+"_datapack", previousDataPack);
        data.save(rootJSON.toString());
    }

    public static JSONObject getUserDataPack(String name){
        DataFile data = new DataFile(new File("UserDataPacks", name+".json"), name);
        return data.load();
    }

    public static void updateUserDataPack(String name, JSONObject obj){
        DataFile data = new DataFile(new File("UserDataPacks", name+".json"), name);
        data.save(obj.toString());
    }

    public static JSONObject getListDataPack(String name){
        ListFile data = new ListFile(new File("ListDataPacks", name+".yml"));
        return new JSONObject().put("list", data.getConfig().getStringList("ListData"));
    }

    public static void updateListDataPack(String name, List<String> source){
        ListFile data = new ListFile(new File("ListDataPacks", name+".yml"));
        data.getConfig().set(name, source);
    }
}
