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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TempDataPack {

    private JSONObject json;
    private String name;

    public TempDataPack(String name){
        this.json = new JSONObject();
        this.name = name;
        json.put("datacrack_updateDate", new Date().getTime());
        update();
    }

    public TempDataPack(JSONObject json, String name) {
        this.json = json;
        this.name = name;
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

    public boolean contains(String key) throws Exception {
        reload();
        return json.has(key);
    }

    public String getName() {
        return name;
    }

    public UpdateDetails update() {
        json.put("datacrack_updateDate", new Date().getTime());
        return SocketInstance.update(SocketInstance.SetterAction.PUTTEMPDATA, new Details(json, name));
    }

    public void reload() throws Exception {
        GetDetails details = SocketInstance.get(SocketInstance.GetterAction.GETTEMPDATA, new Details(json, name));
        if(details.hasError()){
            for(Exception e : details.getErros().values()){
                throw e;
            }
        }
        this.json = details.getSource();
    }
}
