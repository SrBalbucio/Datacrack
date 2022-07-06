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
import org.json.JSONObject;

import java.util.*;

public class TempDataPack {

    private JSONObject json;
    private String name;
    private Manager manager;

    public TempDataPack(String name, Manager manager){
        this.json = new JSONObject();
        this.name = name;
        this.manager = manager;
        json.put("datacrack_updateDate", new Date().getTime());
        update();
    }

    public TempDataPack(JSONObject json, String name, Manager manager) {
        this.json = json;
        this.name = name;
        this.manager = manager;
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
            throw new DataNotExistsException("O Dado " + name + " não existe!", name);
        }
        return json.get(key);
    }

    public String getString(String key) throws Exception {
        reload();
        if (!json.has(key)) {
            throw new DataNotExistsException("O Dado " + name + " não existe!", name);
        }
        return json.getString(key);
    }

    public Integer getInt(String key) throws Exception {
        reload();
        if (!json.has(key)) {
            throw new DataNotExistsException("O Dado " + name + " não existe!", name);
        }
        return json.getInt(key);
    }

    public Float getFloat(String key) throws Exception {
        reload();
        if (!json.has(key)) {
            throw new DataNotExistsException("O Dado " + name + " não existe!", name);
        }
        return json.getFloat(key);
    }

    public Double getDouble(String key) throws Exception {
        reload();
        if (!json.has(key)) {
            throw new DataNotExistsException("O Dado " + name + " não existe!", name);
        }
        return json.getDouble(key);
    }

    public Long getLong(String key) throws Exception {
        reload();
        if (!json.has(key)) {
            throw new DataNotExistsException("O Dado " + name + " não existe!", name);
        }
        return json.getLong(key);
    }

    public Boolean getBoolean(String key) throws Exception {
        reload();
        if (!json.has(key)) {
            throw new DataNotExistsException("O Dado " + name + " não existe!", name);
        }
        return json.getBoolean(key);
    }

    public List<String> getStringList(String key) throws Exception {
        reload();
        if (!json.has(key)) {
            throw new DataNotExistsException("O Dado " + name + " não existe!", name);
        }
        return Arrays.asList(json.getString(key).split("-"));
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

    public Map<String, String> getStringMap(String key){
        reload();
        Map<String, String> map = new HashMap<>();
        String[] mapString = json.getString(key).split("&&");
        for(String k : mapString){
            String[] ops = k.split(":");
            map.put(ops[0], ops[1]);
        }
        return map;
    }

    public UpdateDetails setStringMap(String key, Map<String, String> map){
        reload();
        String mapString = "<null-not>";
        for(String k : map.keySet()){
            if(mapString.equalsIgnoreCase("<null-not>")){
                mapString = k+":"+map.get(k);
            } else{
                mapString += "&&"+ k+":"+map.get(k);
            }
        }
        json.put(key, mapString);
        return update();
    }

    public boolean contains(String key) throws Exception {
        reload();
        return json.has(key);
    }

    public boolean containsCustomData(String key, Class dataType) throws Exception {
        reload();
        return json.has("customdata_"+key+"_"+dataType.getCanonicalName());
    }

    public String getName() {
        return name;
    }

    public UpdateDetails update() {
        json.put("datacrack_updateDate", new Date().getTime());
        return SocketInstance.update(SocketInstance.SetterAction.PUTTEMPDATA, new Details(json, name), manager);
    }

    public void reload() {
        GetDetails details = SocketInstance.get(SocketInstance.GetterAction.GETTEMPDATA, new Details(json, name), manager);
        this.json = details.getSource();
    }
}
