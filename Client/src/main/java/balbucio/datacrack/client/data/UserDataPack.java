/*
 * Copyright (c) 2022.
 * Datacrack é desenvolvido e mantido por SrBalbucio.
 * Não revenda e/ou distribua como seu.
 */

package balbucio.datacrack.client.data;

import balbucio.datacrack.client.Datacrack;
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
import balbucio.datacrack.client.user.User;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class UserDataPack {

    private JSONObject json;
    private User user;
    private Manager manager;

    public UserDataPack(User user, Manager manager)  {
        reload();
        this.user = user;
        this.manager = manager;
    }

    public UpdateDetails set(String key, Object value)  {
        reload();
        json.put(key, value);
        return update();
    }

    public UpdateDetails setString(String key, String value)  {
        reload();
        json.put(key, value);
        return update();
    }

    public UpdateDetails setInt(String key, Integer value)  {
        reload();
        json.put(key, value);
        return update();
    }

    public UpdateDetails setFloat(String key, Float value)  {
        reload();
        json.put(key, value);
        return update();
    }

    public UpdateDetails setDouble(String key, Double value)  {
        reload();
        json.put(key, value);
        return update();
    }

    public UpdateDetails setLong(String key, Long value)  {
        reload();
        json.put(key, value);
        return update();
    }

    public UpdateDetails setBoolean(String key, Boolean value)  {
        reload();
        json.put(key, value);
        return update();
    }

    public UpdateDetails setStringList(String key, List<String> value)  {
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
    public UpdateDetails setCustomData(String key, CustomData value)  {
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

    public boolean contains(String key)  {
        reload();
        return json.has(key);
    }

    public boolean containsCustomData(String key, Class dataType)  {
        reload();
        return json.has("customdata_"+key+"_"+dataType.getCanonicalName());
    }
    
    public UpdateDetails update() {
        json.put("datacrack_updateDate", new Date().getTime());
        return SocketInstance.update(SocketInstance.SetterAction.PUTTEMPDATA, new Details(json, user.getUUID().toString()), manager);
    }

    public void reload() {
        CompletableFuture.runAsync(() -> {
            GetDetails details = SocketInstance.get(SocketInstance.GetterAction.GETTEMPDATA, new Details(json, user.getUUID().toString()), manager);
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
