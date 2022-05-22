/*
 * Copyright (c) 2022.
 * Datacrack é desenvolvido e mantido por SrBalbucio.
 * Não revenda e/ou distribua como seu.
 */

package balbucio.datacrack.data;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TempManager {

    private static Map<String, JSONObject> tempData = new HashMap<>();

    public static JSONObject getTempData(String name){
        if(!tempData.containsKey(name)) { return null; }
        return tempData.get(name);
    }

    public static void setTempData(String name, JSONObject source){
        if(tempData.containsKey(name)){ tempData.replace(name, source); return; }
        tempData.put(name, source);
    }

    public static String getTempDataPacks(){
        String data = "";
        for(String key : tempData.keySet()){
            if(data.equalsIgnoreCase("")){
                data = key;
            } else{
                data = ","+key;
            }
        }
        return data;
    }

    public static boolean contains(String name){
        return tempData.containsKey(name);
    }
}
