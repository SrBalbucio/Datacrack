/*
 * Copyright (c) 2022.
 * Datacrack é desenvolvido e mantido por SrBalbucio.
 * Não revenda e/ou distribua como seu.
 */

package balbucio.datacrack.client.data;

import balbucio.datacrack.client.Manager;
import balbucio.datacrack.client.data.custom.CustomData;
import balbucio.datacrack.client.exception.DataNotExistsException;
import balbucio.datacrack.client.exception.InvalidCredentialException;
import balbucio.datacrack.client.exception.RequestErrorException;
import balbucio.datacrack.client.exception.UserInsufficientPermissionException;
import balbucio.datacrack.client.socket.Details;
import balbucio.datacrack.client.socket.GetDetails;
import balbucio.datacrack.client.socket.SocketInstance;
import balbucio.datacrack.client.socket.UpdateDetails;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class RootDataPack {

    private JSONObject json;
    private String name;
    private Manager manager;

    public RootDataPack(String name, Manager manager){
        this.name = name;
        this.json = new JSONObject();
        this.manager = manager;
        json.put("datacrack_Name", name);
        json.put("datacrack_updateDate", new Date().getTime());
    }

    public RootDataPack(JSONObject data, Manager manager){
        this.json = data;
        this.name = data.getString("datacrack_Name");
        this.manager = manager;
    }

    public DataPack getDataPack(String name) throws DataNotExistsException {
        reload();
        if(!json.has(name+"_datapack")){ throw new DataNotExistsException("O DataPack "+name+" não existe!", name); }
        return new DataPack(json.getJSONObject(name+"_datapack"), manager);
    }

    public UpdateDetails addDataPack(DataPack pack){
        reload();
        if (json.has(name + "_datapack")) { return update(); }
        json.put(pack.getName() + "_datapack", pack.getSource());
        return update();
    }

    public DataPack createDataPack(String name) throws DataNotExistsException {
        reload();
        if (json.has(name + "_datapack")) {
            return getDataPack(name);
        }
        DataPack pack = new DataPack(name, this.name, manager);
        json.put(name + "_datapack", pack.getSource());
        update();
        return pack;
    }

    public UpdateDetails set(String key, Object value){
        reload();
        json.put(key, value);
        return update();
    }

    public UpdateDetails setString(String key, String value){
        reload();
        json.put(key, value);
        return update();
    }

    public UpdateDetails setInt(String key, Integer value){
        reload();
        json.put(key, value);
        return update();
    }

    public UpdateDetails setFloat(String key, Float value){
        reload();
        json.put(key, value);
        return update();
    }

    public UpdateDetails setDouble(String key, Double value){
        reload();
        json.put(key, value);
        return update();
    }

    public UpdateDetails setLong(String key, Long value) {
        reload();
        json.put(key, value);
        return update();
    }

    public UpdateDetails setBoolean(String key, Boolean value) {
        reload();
        json.put(key, value);
        return update();
    }

    public UpdateDetails setStringList(String key, List<String> value) {
        reload();
        String list = "";
        for(String a : value){
            if(list.equalsIgnoreCase("")){
                list = a;
            } else{
                list = list+"-"+a;
            }
        }
        json.put(key, list);
        return update();
    }

    public UpdateDetails setCustomData(String key, CustomData value) {
        reload();
        JSONObject js = new JSONObject();
        for(String k : value.getCustomData().keySet()){
            js.put(k, value.getCustomData().get(k));
        }
        json.put("customdata_"+key+"_"+value.getClass().getCanonicalName(), js);
        return update();
    }

    public Object get(String key) throws DataNotExistsException {
        reload();
        if(!json.has(key)){  throw new DataNotExistsException("O Dado "+key+" não existe!", key); }
        return json.get(key);
    }

    public String getString(String key) throws DataNotExistsException {
        reload();
        if(!json.has(key)){  throw new DataNotExistsException("O Dado "+key+" não existe!", key); }
        return json.getString(key);
    }

    public Integer getInt(String key) throws DataNotExistsException {
        reload();
        if(!json.has(key)){  throw new DataNotExistsException("O Dado "+key+" não existe!", key); }
        return json.getInt(key);
    }

    public Float getFloat(String key) throws DataNotExistsException {
        reload();
        if(!json.has(key)){  throw new DataNotExistsException("O Dado "+key+" não existe!", key); }
        return json.getFloat(key);
    }

    public Double getDouble(String key) throws DataNotExistsException {
        reload();
        if(!json.has(key)){  throw new DataNotExistsException("O Dado "+key+" não existe!", key); }
        return json.getDouble(key);
    }

    public Long getLong(String key) throws DataNotExistsException {
        reload();
        if(!json.has(key)){  throw new DataNotExistsException("O Dado "+key+" não existe!", key); }
        return json.getLong(key);
    }
    public Boolean getBoolean(String key) throws DataNotExistsException {
        reload();
        if(!json.has(key)){  throw new DataNotExistsException("O Dado "+key+" não existe!", key); }
        return json.getBoolean(key);
    }

    public List<String> getStringList(String key) throws DataNotExistsException {
        reload();
        if(!json.has(key)){  throw new DataNotExistsException("O Dado "+key+" não existe!", key); }
        return Arrays.asList(json.getString(key).split("-"));
    }

    public List<DataPack> getAllDataPacks(){
        reload();
        List<DataPack> dataPacks = new ArrayList<>();
        for(String key : json.keySet()){
            if(key.contains("_datapack")){
                dataPacks.add(new DataPack(json.getJSONObject(key), manager));
            }
        }
        return dataPacks;
    }

    public CustomData getCustomData(String key, CustomData data) throws DataNotExistsException {
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
    
    public boolean contains(String key) {
        reload();
        return json.has(key);
    }

    public boolean containsCustomData(String key, Class dataType){
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

    public UpdateDetails update(){
        json.put("datacrack_updateDate", new Date().getTime());
        return SocketInstance.update(SocketInstance.SetterAction.PUTROOTPATH, new Details(json, name), manager);
    }

    public void reload() {
        CompletableFuture.runAsync(() -> {
            GetDetails details = SocketInstance.get(SocketInstance.GetterAction.GETTEMPDATA, new Details(json, name), manager);
            if (details.hasError()) {
                for (Exception e : details.getErros().values()) {
                    try {
                        throw e;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            this.json = details.getSource();
        });
    }
}
