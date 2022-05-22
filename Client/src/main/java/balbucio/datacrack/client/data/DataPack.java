/*
 * Copyright (c) 2022.
 * Datacrack é desenvolvido e mantido por SrBalbucio.
 * Não revenda e/ou distribua como seu.
 */

package balbucio.datacrack.client.data;

import balbucio.datacrack.client.Datacrack;
import balbucio.datacrack.client.exception.*;
import balbucio.datacrack.client.socket.SocketInstance;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataPack {

    private JSONObject json;
    private String name;
    private String path;

    public DataPack(JSONObject json){
        this.json = json;
        this.name = json.getString("name");
        this.path = json.getString("path");
    }

    public DataPack(String name, String path) {
        this.json = new JSONObject();
        this.name = name;
        this.path = path;
        json.put("name", name);
        json.put("path", path);
    }

    public DataPack getDataPack(String name) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        if(!json.has(name+"_datapack")){ throw new DataNotExistsException("O DataPack "+name+" não existe!", name); }
        return new DataPack(json.getJSONObject(name+"_datapack"));
    }

    public DataPack createDataPack(String name) throws UserInsufficientPermissionException, RequestErrorException, DataPackLimitException, DataNotExistsException, InvalidCredentialException {
        reload();
        try {
            if (json.has(name + "_datapack")) {
                return getDataPack(name);
            }
        } catch (DataNotExistsException e){ }
        if(this.path.split(",").length == 2){
            throw new DataPackLimitException("O DataPack não pode ser criado dentro de 2 outros DataPacks.", name);
        }
        DataPack pack = new DataPack(name, this.path+","+this.name);
        json.put(name+"_datapack", pack.getSource());
        update();
        return pack;
    }

    public DataPack set(String key, Object value) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        json.put(key, value);
        update();
        return this;
    }

    public DataPack setString(String key, String value) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        json.put(key, value);
        update();
        return this;
    }

    public DataPack setInt(String key, Integer value) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        json.put(key, value);
        update();
        return this;
    }

    public DataPack setFloat(String key, Float value) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        json.put(key, value);
        update();
        return this;
    }

    public DataPack setDouble(String key, Double value) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        json.put(key, value);
        update();
        return this;
    }

    public DataPack setLong(String key, Long value) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        json.put(key, value);
        update();
        return this;
    }

    public DataPack setBoolean(String key, Boolean value) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        json.put(key, value);
        update();
        return this;
    }

    public DataPack setStringList(String key, List<String> value) throws InvalidCredentialException, RequestErrorException, DataNotExistsException, UserInsufficientPermissionException {
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
        update();
        return this;
    }

    public Object get(String key) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        if(!json.has(key)){ throw new DataNotExistsException("O Dado "+key+" não existe!", key); }
        return json.get(key);
    }

    public String getString(String key) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        if(!json.has(key)){ throw new DataNotExistsException("O Dado "+key+" não existe!", key); }
        return json.getString(key);
    }

    public Integer getInt(String key) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        if(!json.has(key)){ throw new DataNotExistsException("O Dado "+key+" não existe!", key); }
        return json.getInt(key);
    }

    public Float getFloat(String key) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        if(!json.has(key)){ throw new DataNotExistsException("O Dado "+key+" não existe!", key); }
        return json.getFloat(key);
    }

    public Double getDouble(String key) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        if(!json.has(key)){ throw new DataNotExistsException("O Dado "+key+" não existe!", key); }
        return json.getDouble(key);
    }

    public Long getLong(String key) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        if(!json.has(key)){ throw new DataNotExistsException("O Dado "+key+" não existe!", key); }
        return json.getLong(key);
    }
    public Boolean getBoolean(String key) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        if(!json.has(key)){ throw new DataNotExistsException("O Dado "+key+" não existe!", key); }
        return json.getBoolean(key);
    }

    public List<String> getStringList(String key) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        if(!json.has(key)){  throw new DataNotExistsException("O Dado "+key+" não existe!", key); }
        return Arrays.asList(json.getString(key).split("-"));
    }

    public List<DataPack> getAllDataPacks() throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        List<DataPack> dataPacks = new ArrayList<>();
        for(String key : json.keySet()){
            if(key.contains("_datapack")){
                dataPacks.add(new DataPack(json.getJSONObject(key)));
            }
        }
        return dataPacks;
    }


    public String getName(){
        return name;
    }

    public String getPath(){
        return path;
    }

    public JSONObject getSource(){
        return json;
    }

    public void update() throws UserInsufficientPermissionException, RequestErrorException, InvalidCredentialException {
        Datacrack.getInstance().getSocketManager().updateSource(SocketInstance.SetterAction.PUTDATAPACK, path+"/"+name, json);
    }

    public void reload() throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        JSONObject response = Datacrack.getInstance().getSocketManager().getSource(SocketInstance.GetterAction.GETDATAPACK, path+","+name);
        if(response == null){ return; }
        this.json = response;
    }
}
