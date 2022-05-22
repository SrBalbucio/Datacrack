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
import balbucio.datacrack.client.user.User;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserDataPack {

    private JSONObject json;
    private User user;

    public UserDataPack(User user) throws InvalidCredentialException, RequestErrorException, DataNotExistsException, UserInsufficientPermissionException {
        this.json = Datacrack.getInstance().getSocketManager().getSource(SocketInstance.GetterAction.GETUSERPACK, user.getUUID().toString());
        this.user = user;
    }

    public UserDataPack set(String key, Object value) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        json.put(key, value);
        update();
        return this;
    }

    public UserDataPack setString(String key, String value) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        json.put(key, value);
        update();
        return this;
    }

    public UserDataPack setInt(String key, Integer value) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        json.put(key, value);
        update();
        return this;
    }

    public UserDataPack setFloat(String key, Float value) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        json.put(key, value);
        update();
        return this;
    }

    public UserDataPack setDouble(String key, Double value) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        json.put(key, value);
        update();
        return this;
    }

    public UserDataPack setLong(String key, Long value) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        json.put(key, value);
        update();
        return this;
    }

    public UserDataPack setBoolean(String key, Boolean value) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        json.put(key, value);
        update();
        return this;
    }

    public UserDataPack setStringList(String key, List<String> value) throws InvalidCredentialException, RequestErrorException, DataNotExistsException, UserInsufficientPermissionException {
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
    public void update() throws UserInsufficientPermissionException, RequestErrorException, InvalidCredentialException {
        Datacrack.getInstance().getSocketManager().updateSource(SocketInstance.SetterAction.PUTUSERPACK, user.getUUID().toString(), json);
    }

    public void reload() throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        JSONObject response = Datacrack.getInstance().getSocketManager().getSource(SocketInstance.GetterAction.GETUSERPACK, user.getUUID().toString());
        if(response == null){ return; }
        this.json = response;
    }
}
