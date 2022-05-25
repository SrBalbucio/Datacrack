/*
 * Copyright (c) 2022.
 * Datacrack é desenvolvido e mantido por SrBalbucio.
 * Não revenda e/ou distribua como seu.
 */

package balbucio.datacrack.client.socket;

import balbucio.datacrack.client.exception.InvalidCredentialException;
import balbucio.datacrack.client.exception.RequestErrorException;
import balbucio.datacrack.client.exception.UserInsufficientPermissionException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class UpdateDetails {

    private UUID uniqueID= UUID.randomUUID();
    private SocketInstance.SetterAction action;
    private String updateArgument;
    private HashMap<SocketInstance, JSONObject> responses = new HashMap<>();
    private boolean hasError = false;
    private HashMap<SocketInstance, Exception> socketWithErro = new HashMap<>();

    public UpdateDetails(SocketInstance.SetterAction action, String updateArgument){
        this.action = action;
        this.updateArgument = updateArgument;
    }

    public UUID getUniqueID(){
        return uniqueID;
    }

    public void addJsonResponse(SocketInstance instance, JSONObject json){
        if(json == null){
            hasError = true;
            socketWithErro.put(instance, new RequestErrorException("Não foi possível se conectar ao Socket. (TIMEOUT)", action, updateArgument));
            return;
        }
        responses.put(instance, json);
        if(json.has("erro")){
            if(json.getBoolean("erro")) {
                hasError = true;
                Exception e = null;
                if (!json.has("type")) {
                    e = new RequestErrorException(json.getString("erroMessage"), action, updateArgument);
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

    public boolean hasError(){
        return hasError;
    }

    public SocketInstance.SetterAction getAction() {
        return action;
    }

    public String getUpdateArgument() {
        return updateArgument;
    }

    public HashMap<SocketInstance, JSONObject> getResponses() {
        return responses;
    }

    public HashMap<SocketInstance, Exception> getErros() {
        return socketWithErro;
    }
}
