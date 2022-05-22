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

import java.util.Arrays;
import java.util.List;

public class TempDataPack {

    private JSONObject json;
    private String name;

    public TempDataPack(String name) throws RequestErrorException,InvalidCredentialException {
        try {
            this.json = new JSONObject();
            this.name = name;
            update();
        } catch (UserInsufficientPermissionException e){ }
    }

    public TempDataPack(JSONObject json, String name){
        this.json = json;
        this.name = name;
    }

    public TempDataPack set(String key, Object value) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        json.put(key, value);
        update();
        return this;
    }

    public TempDataPack setString(String key, String value) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        json.put(key, value);
        update();
        return this;
    }

    public TempDataPack setInt(String key, Integer value) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        json.put(key, value);
        update();
        return this;
    }

    public TempDataPack setFloat(String key, Float value) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        json.put(key, value);
        update();
        return this;
    }

    public TempDataPack setDouble(String key, Double value) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        json.put(key, value);
        update();
        return this;
    }

    public TempDataPack setLong(String key, Long value) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        json.put(key, value);
        update();
        return this;
    }

    public TempDataPack setBoolean(String key, Boolean value) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        json.put(key, value);
        update();
        return this;
    }

    public TempDataPack setStringList(String key, List<String> value) throws InvalidCredentialException, RequestErrorException, DataNotExistsException, UserInsufficientPermissionException {
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
        if(!json.has(key)){ throw new DataNotExistsException("O Dado "+name+" não existe!", name); }
        return json.get(key);
    }

    public String getString(String key) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        if(!json.has(key)){ throw new DataNotExistsException("O Dado "+name+" não existe!", name); }
        return json.getString(key);
    }

    public Integer getInt(String key) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        if(!json.has(key)){ throw new DataNotExistsException("O Dado "+name+" não existe!", name); }
        return json.getInt(key);
    }

    public Float getFloat(String key) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        if(!json.has(key)){ throw new DataNotExistsException("O Dado "+name+" não existe!", name); }
        return json.getFloat(key);
    }

    public Double getDouble(String key) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        if(!json.has(key)){ throw new DataNotExistsException("O Dado "+name+" não existe!", name); }
        return json.getDouble(key);
    }

    public Long getLong(String key) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        if(!json.has(key)){ throw new DataNotExistsException("O Dado "+name+" não existe!", name); }
        return json.getLong(key);
    }
    public Boolean getBoolean(String key) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        if(!json.has(key)){ throw new DataNotExistsException("O Dado "+name+" não existe!", name); }
        return json.getBoolean(key);
    }
    public List<String> getStringList(String key) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        reload();
        if(!json.has(key)){  throw new DataNotExistsException("O Dado "+name+" não existe!", name); }
        return Arrays.asList(json.getString(key).split("-"));
    }


    public String getName(){
        return name;
   }

   public void update() throws RequestErrorException, UserInsufficientPermissionException, InvalidCredentialException {
       Datacrack.getInstance().getSocketManager().updateSource(SocketInstance.SetterAction.PUTTEMPDATA, name, json);
   }

   public void reload() throws RequestErrorException, UserInsufficientPermissionException, DataNotExistsException, InvalidCredentialException {
       JSONObject response = Datacrack.getInstance().getSocketManager().getSource(SocketInstance.GetterAction.GETTEMPDATA, name);
       if(response == null){ return; }
       this.json = response;
   }
}
