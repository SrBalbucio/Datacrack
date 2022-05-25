/*
 * Copyright (c) 2022.
 * Datacrack é desenvolvido e mantido por SrBalbucio.
 * Não revenda e/ou distribua como seu.
 */

package balbucio.datacrack.client.data;

import balbucio.datacrack.client.Datacrack;
import balbucio.datacrack.client.exception.DataNotExistsException;
import balbucio.datacrack.client.exception.InvalidCredentialException;
import balbucio.datacrack.client.exception.RequestErrorException;
import balbucio.datacrack.client.exception.UserInsufficientPermissionException;
import balbucio.datacrack.client.socket.Details;
import balbucio.datacrack.client.socket.GetDetails;
import balbucio.datacrack.client.socket.SocketInstance;
import balbucio.datacrack.client.socket.UpdateDetails;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class RootDataPack {

    private JSONObject json;
    private String name;

    public RootDataPack(String name){
        this.name = name;
        this.json = new JSONObject();
        json.put("Name", name);
        json.put("datacrack_updateDate", new Date().getTime());
    }

    public RootDataPack(JSONObject data){
        this.json = data;
        this.name = data.getString("Name");
    }

    public DataPack getDataPack(String name) throws Exception {
        reload();
        if(!json.has(name+"_datapack")){ throw new DataNotExistsException("O DataPack "+name+" não existe!", name); }
        return new DataPack(json.getJSONObject(name+"_datapack"));
    }

    public UpdateDetails addDataPack(DataPack pack) throws Exception {
        reload();
        if (json.has(name + "_datapack")) { return update(); }
        json.put(pack.getName() + "_datapack", pack.getSource());
        return update();
    }

    public DataPack createDataPack(String name) throws Exception {
        reload();
        try {
            if (json.has(name + "_datapack")) {
                return getDataPack(name);
            }
        } catch (DataNotExistsException e){ }
        DataPack pack = new DataPack(name, this.name);
        json.put(name+"_datapack", pack.getSource());
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

    public Object get(String key) throws Exception {
        reload();
        if(!json.has(key)){  throw new DataNotExistsException("O Dado "+key+" não existe!", key); }
        return json.get(key);
    }

    public String getString(String key) throws Exception {
        reload();
        if(!json.has(key)){  throw new DataNotExistsException("O Dado "+key+" não existe!", key); }
        return json.getString(key);
    }

    public Integer getInt(String key) throws Exception {
        reload();
        if(!json.has(key)){  throw new DataNotExistsException("O Dado "+key+" não existe!", key); }
        return json.getInt(key);
    }

    public Float getFloat(String key) throws Exception {
        reload();
        if(!json.has(key)){  throw new DataNotExistsException("O Dado "+key+" não existe!", key); }
        return json.getFloat(key);
    }

    public Double getDouble(String key) throws Exception {
        reload();
        if(!json.has(key)){  throw new DataNotExistsException("O Dado "+key+" não existe!", key); }
        return json.getDouble(key);
    }

    public Long getLong(String key) throws Exception {
        reload();
        if(!json.has(key)){  throw new DataNotExistsException("O Dado "+key+" não existe!", key); }
        return json.getLong(key);
    }
    public Boolean getBoolean(String key) throws Exception {
        reload();
        if(!json.has(key)){  throw new DataNotExistsException("O Dado "+key+" não existe!", key); }
        return json.getBoolean(key);
    }

    public List<String> getStringList(String key) throws Exception {
        reload();
        if(!json.has(key)){  throw new DataNotExistsException("O Dado "+key+" não existe!", key); }
        return Arrays.asList(json.getString(key).split("-"));
    }

    public List<DataPack> getAllDataPacks() throws Exception {
        reload();
        List<DataPack> dataPacks = new ArrayList<>();
        for(String key : json.keySet()){
            if(key.contains("_datapack")){
                dataPacks.add(new DataPack(json.getJSONObject(key)));
            }
        }
        return dataPacks;
    }
    
    public boolean contains(String key) throws Exception {
        reload();
        return json.has(key);
    }

    public UpdateDetails update() throws UserInsufficientPermissionException, RequestErrorException, InvalidCredentialException {
        json.put("datacrack_updateDate", new Date().getTime());
        return SocketInstance.update(SocketInstance.SetterAction.PUTROOTPATH, new Details(json, name));
    }

    public void reload() throws Exception {
        GetDetails details = SocketInstance.get(SocketInstance.GetterAction.GETROOTPATH, new Details(json, name));
        if (details.hasError()) {
            for (Exception e : details.getErros().values()) {
                throw e;
            }
        }
        this.json = details.getSource();
    }
}
