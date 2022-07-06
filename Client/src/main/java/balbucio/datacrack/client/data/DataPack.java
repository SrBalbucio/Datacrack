/*
 * Copyright (c) 2022.
 * Datacrack é desenvolvido e mantido por SrBalbucio.
 * Não revenda e/ou distribua como seu.
 */

package balbucio.datacrack.client.data;

import balbucio.datacrack.client.Datacrack;
import balbucio.datacrack.client.Manager;
import balbucio.datacrack.client.data.custom.CustomData;
import balbucio.datacrack.client.exception.*;
import balbucio.datacrack.client.socket.Details;
import balbucio.datacrack.client.socket.GetDetails;
import balbucio.datacrack.client.socket.SocketInstance;
import balbucio.datacrack.client.socket.UpdateDetails;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class DataPack {

    private JSONObject json;
    private String name;
    private String path;
    private Manager manager;

    public DataPack(JSONObject json, Manager manager) {
        this.json = json;
        this.name = json.getString("datacrack_name");
        this.path = json.getString("datacrack_path");
        this.manager = manager;
    }

    public DataPack(String name, String path, Manager manager) {
        this.json = new JSONObject();
        this.name = name;
        this.path = path;
        this.manager = manager;
        json.put("datacrack_name", name);
        json.put("datacrack_path", path);
        json.put("datacrack_updateDate", new Date().getTime());
    }

    public DataPack getDataPack(String name) throws Exception {
        reload();
        if (!json.has(name + "_datapack")) {
            throw new DataNotExistsException("O DataPack " + name + " não existe!", name);
        }
        return new DataPack(json.getJSONObject(name + "_datapack"), manager);
    }

    public DataPack createDataPack(String name) throws Exception {
        reload();
        try {
            if (json.has(name + "_datapack")) {
                return getDataPack(name);
            }
        } catch (DataNotExistsException e) {
        }
        if (this.path.split(",").length == 2) {
            throw new DataPackLimitException("O DataPack não pode ser criado dentro de 2 outros DataPacks.", name);
        }
        DataPack pack = new DataPack(name, this.path + "," + this.name, manager);
        json.put(name + "_datapack", pack.getSource());
        update();
        return pack;
    }

    public UpdateDetails set(String key, Object value) throws Exception {
        reload();
        json.put(key, value);
        return update();
    }

    public UpdateDetails setString(String key, String value) throws Exception {
        reload();
        json.put(key, value);
        return update();
    }

    public UpdateDetails setInt(String key, Integer value) throws Exception {
        reload();
        json.put(key, value);
        return update();
    }

    public UpdateDetails setFloat(String key, Float value) throws Exception {
        reload();
        json.put(key, value);
        return update();
    }

    public UpdateDetails setDouble(String key, Double value) throws Exception {
        reload();
        json.put(key, value);
        return update();
    }

    public UpdateDetails setLong(String key, Long value) throws Exception {
        reload();
        json.put(key, value);
        return update();
    }

    public UpdateDetails setBoolean(String key, Boolean value) throws Exception {
        reload();
        json.put(key, value);
        return update();
    }

    public UpdateDetails setStringList(String key, List<String> value) throws Exception {
        reload();
        String list = "";
        for (String a : value) {
            if (list.equalsIgnoreCase("")) {
                list = a;
            } else {
                list = list + "-" + a;
            }
        }
        json.put(key, list);
        return update();
    }

    public UpdateDetails setCustomData(String key, CustomData value) throws Exception {
        reload();
        JSONObject js = new JSONObject();
        for(String k : value.getCustomData().keySet()){
            js.put(k, value.getCustomData().get(k));
        }
        json.put("customdata_"+key+"_"+value.getClass().getCanonicalName(), js);
        return update();
    }

    public Object get(String key) throws Exception {
        reload();
        if (!json.has(key)) {
            throw new DataNotExistsException("O Dado " + key + " não existe!", key);
        }
        return json.get(key);
    }

    public String getString(String key) throws Exception {
        reload();
        if (!json.has(key)) {
            throw new DataNotExistsException("O Dado " + key + " não existe!", key);
        }
        return json.getString(key);
    }

    public Integer getInt(String key) throws Exception {
        reload();
        if (!json.has(key)) {
            throw new DataNotExistsException("O Dado " + key + " não existe!", key);
        }
        return json.getInt(key);
    }

    public Float getFloat(String key) throws Exception {
        reload();
        if (!json.has(key)) {
            throw new DataNotExistsException("O Dado " + key + " não existe!", key);
        }
        return json.getFloat(key);
    }

    public Double getDouble(String key) throws Exception {
        reload();
        if (!json.has(key)) {
            throw new DataNotExistsException("O Dado " + key + " não existe!", key);
        }
        return json.getDouble(key);
    }

    public Long getLong(String key) throws Exception {
        reload();
        if (!json.has(key)) {
            throw new DataNotExistsException("O Dado " + key + " não existe!", key);
        }
        return json.getLong(key);
    }

    public Boolean getBoolean(String key) throws Exception {
        reload();
        if (!json.has(key)) {
            throw new DataNotExistsException("O Dado " + key + " não existe!", key);
        }
        return json.getBoolean(key);
    }

    public List<String> getStringList(String key) throws Exception {
        reload();
        if (!json.has(key)) {
            throw new DataNotExistsException("O Dado " + key + " não existe!", key);
        }
        return Arrays.asList(json.getString(key).split("-"));
    }

    public List<DataPack> getAllDataPacks() throws Exception {
        reload();
        List<DataPack> dataPacks = new ArrayList<>();
        for (String key : json.keySet()) {
            if (key.contains("_datapack")) {
                dataPacks.add(new DataPack(json.getJSONObject(key), manager));
            }
        }
        return dataPacks;
    }

    public CustomData getCustomData(String key, CustomData data) throws Exception {
        reload();
        if(!containsCustomData(key, data.getClass())){
            throw new DataNotExistsException("O Dado "+key+" não existe!", key);
        }
        JSONObject js = json.getJSONObject("customdata_"+key+"_"+data.getClass().getCanonicalName());
        Map<String, Object> values = new HashMap<>();
        for(String k : js.keySet()){
            values.put(key, js.get(k));
        }
        return data.setCustomData(values);
    }

    public boolean contains(String key) throws Exception {
        reload();
        return json.has(key);
    }

    public boolean containsCustomData(String key, Class dataType) throws Exception {
        reload();
        return json.has("customdata_"+key+"_"+dataType.getCanonicalName());
    }

    public boolean containsDataPack(String key){
        reload();
        return json.has(key+"_datapack");
    }

    public UpdateDetails delete(String key){
        reload();
        json.put(key, (Object) null);
        return update();
    }

    public UpdateDetails remove(String key){
        reload();
        json.put(key, (Object) null);
        return update();
    }


    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public JSONObject getSource() {
        return json;
    }

    public UpdateDetails update(){
        json.put("datacrack_updateDate", new Date().getTime());
        return SocketInstance.update(SocketInstance.SetterAction.PUTDATAPACK, new Details(json, path + "," + name), manager);
    }

    public void reload() {
        CompletableFuture.runAsync(() -> {
            GetDetails details = SocketInstance.get(SocketInstance.GetterAction.GETDATAPACK, new Details(json, path + "/" + name), manager);
            this.json = details.getSource();
        });
    }
}
