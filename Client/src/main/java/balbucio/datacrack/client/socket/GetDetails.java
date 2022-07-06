/*
 * Copyright (c) 2022.
 * Datacrack é desenvolvido e mantido por SrBalbucio.
 * Não revenda e/ou distribua como seu.
 */

package balbucio.datacrack.client.socket;

import balbucio.datacrack.client.exception.InvalidCredentialException;
import balbucio.datacrack.client.exception.RequestErrorException;
import balbucio.datacrack.client.exception.UserInsufficientPermissionException;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import org.json.JSONObject;

import java.util.*;

public class GetDetails {

    private UUID uniqueID= UUID.randomUUID();
    private SocketInstance.GetterAction action;
    private String getArgument;
    private HashMap<SocketInstance, JSONObject> responses = new HashMap<>();
    private boolean hasError = false;
    private HashMap<SocketInstance, Exception> socketWithErro = new HashMap<>();

    public GetDetails(SocketInstance.GetterAction action, String getArgument){
        this.action = action;
        this.getArgument = getArgument;
    }

    public UUID getUniqueID() {
        return uniqueID;
    }

    public SocketInstance.GetterAction getAction() {
        return action;
    }

    public String getGetterArgument() {
        return getArgument;
    }

    public HashMap<SocketInstance, JSONObject> getResponses() {
        return responses;
    }

    public boolean hasError() {
        return hasError;
    }

    public HashMap<SocketInstance, Exception> getErros() {
        return socketWithErro;
    }

    public Collection<JSONObject> getSources(){
        return responses.values();
    }

    public JSONObject getSource(){
        JSONObject recent = null;
        Date dateRecent = null;
        if(getSources().size() == 1 && !getSources().isEmpty()){
            return getSources().stream().findFirst().get();
        }
        for(JSONObject json : getSources()){
            if(recent == null) {
                recent = json;
                dateRecent = new Date(json.getLong("datacrack_updateDate"));
            } else{
                Date date = new Date(json.getLong("datacrack_updateDate"));
                if(date.after(dateRecent)){
                    dateRecent = date;
                    recent = json;
                }
            }
        }
        return recent;
    }

    public void addJsonResponse(SocketInstance instance, JSONObject json){
        if(json == null){
            hasError = true;
            socketWithErro.put(instance, new RequestErrorException("Não foi possível se conectar ao Socket. (TIMEOUT)", action, getArgument));
            return;
        }
        responses.put(instance, json);
        if(json.has("erro")){
            if(json.getBoolean("erro")) {
                hasError = true;
                Exception e = null;
                if (!json.has("type")) {
                    e = new RequestErrorException(json.getString("erroMessage"), action, getArgument);
                    return;
                }
                String type = json.getString("type");
                if (type.equalsIgnoreCase("UserInsufficientPermission")) {
                    e = new UserInsufficientPermissionException(json.getString("erroMessage"), instance.getUser());
                } else if (type.equalsIgnoreCase("InvalidCredentialException")) {
                    e = new InvalidCredentialException(json.getString("erroMessage"), instance.getUser());
                }
                socketWithErro.put(instance, e);
            }
        }
    }
}
