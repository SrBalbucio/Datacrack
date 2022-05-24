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
import balbucio.datacrack.client.socket.SocketInstance;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RootDataPack {

    private JSONObject json;
    private String name;

    public RootDataPack(String name){
        this.name = name;
        this.json = new JSONObject();
        json.put("Name", name);
    }

    public RootDataPack(JSONObject data){
        this.json = data;
        this.name = data.getString("Name");
    }

    public DataPack getDataPack(String name) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        if(!json.has(name+"_datapack")){ throw new DataNotExistsException("O DataPack "+name+" não existe!", name); }
        return new DataPack(json.getJSONObject(name+"_datapack"));
    }

    public RootDataPack addDataPack(DataPack pack) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        if (json.has(name + "_datapack")) { return this; }
        json.put(pack.getName() + "_datapack", pack.getSource());
        update();
        return this;
    }

    public DataPack createDataPack(String name) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
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

    public RootDataPack set(String key, Object value) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        json.put(key, value);
        update();
        return this;
    }

    public RootDataPack setString(String key, String value) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        json.put(key, value);
        update();
        return this;
    }

    public RootDataPack setInt(String key, Integer value) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        json.put(key, value);
        update();
        return this;
    }

    public RootDataPack setFloat(String key, Float value) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        json.put(key, value);
        update();
        return this;
    }

    public RootDataPack setDouble(String key, Double value) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        json.put(key, value);
        update();
        return this;
    }

    public RootDataPack setLong(String key, Long value) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        json.put(key, value);
        update();
        return this;
    }

    public RootDataPack setBoolean(String key, Boolean value) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        json.put(key, value);
        update();
        return this;
    }

    public RootDataPack setStringList(String key, List<String> value) throws InvalidCredentialException, RequestErrorException, DataNotExistsException, UserInsufficientPermissionException {
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
        if(!json.has(key)){  throw new DataNotExistsException("O Dado "+key+" não existe!", key); }
        return json.get(key);
    }

    public String getString(String key) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        if(!json.has(key)){  throw new DataNotExistsException("O Dado "+key+" não existe!", key); }
        return json.getString(key);
    }

    public Integer getInt(String key) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        if(!json.has(key)){  throw new DataNotExistsException("O Dado "+key+" não existe!", key); }
        return json.getInt(key);
    }

    public Float getFloat(String key) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        if(!json.has(key)){  throw new DataNotExistsException("O Dado "+key+" não existe!", key); }
        return json.getFloat(key);
    }

    public Double getDouble(String key) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        if(!json.has(key)){  throw new DataNotExistsException("O Dado "+key+" não existe!", key); }
        return json.getDouble(key);
    }

    public Long getLong(String key) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        if(!json.has(key)){  throw new DataNotExistsException("O Dado "+key+" não existe!", key); }
        return json.getLong(key);
    }
    public Boolean getBoolean(String key) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        if(!json.has(key)){  throw new DataNotExistsException("O Dado "+key+" não existe!", key); }
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
    
    public boolean contains(String key) throws InvalidCredentialException, RequestErrorException, DataNotExistsException, UserInsufficientPermissionException {
        reload();
        return json.has(key);
    }

    public void update() throws UserInsufficientPermissionException, RequestErrorException, InvalidCredentialException {
        Datacrack.getInstance().getManager().updateSource(SocketInstance.SetterAction.PUTROOTPATH, name, json);
    }

    public void reload() throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        JSONObject response = Datacrack.getInstance().getManager().getSource(SocketInstance.GetterAction.GETROOTPATH, name);
        if(response == null){ return; }
        this.json = response;
    }
}
