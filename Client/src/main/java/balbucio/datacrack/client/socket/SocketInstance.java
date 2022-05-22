/*
 * Copyright (c) 2022.
 * Datacrack é desenvolvido e mantido por SrBalbucio.
 * Não revenda e/ou distribua como seu.
 */

package balbucio.datacrack.client.socket;

import balbucio.datacrack.client.Datacrack;
import balbucio.datacrack.client.data.DataPack;
import balbucio.datacrack.client.exception.DataNotExistsException;
import balbucio.datacrack.client.exception.InvalidCredentialException;
import balbucio.datacrack.client.exception.RequestErrorException;
import balbucio.datacrack.client.exception.UserInsufficientPermissionException;
import co.gongzh.procbridge.Client;
import org.json.JSONObject;

import java.util.List;

public class SocketInstance {

    private String ip;
    private int port;
    private Client client;

    public SocketInstance(String ip, int port){
        this.ip = ip;
        this.port = port;
        client = new Client(ip, port);
    }

    public JSONObject get(GetterAction action, String argument) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        JSONObject response = (JSONObject) client.request(action.toString().toLowerCase(), argument);
        if(response == null) {
            throw new RequestErrorException("O Client não conseguiu se comunicar com o "+ip+":"+port, action, argument);
        }
        if(response.has("erro") && !response.has("type")){
            throw new RequestErrorException(response.getString("erroMessage"), action, argument);
        }
        if(response.has("erro") && response.getString("type").equalsIgnoreCase("UserInsufficientPermission")){
            throw new UserInsufficientPermissionException(response.getString("erroMessage"), Datacrack.getInstance().getSocketManager().getUserBySocket(this));
        }
        if(response.has("erro") && response.getString("type").equalsIgnoreCase("InvalidCredentialException")){
            throw new InvalidCredentialException(response.getString("erroMessage"), Datacrack.getInstance().getSocketManager().getUserBySocket(this));
        }
        if(response.has("erro") && response.getString("type").equalsIgnoreCase("TempDataNotExists")){
            throw new DataNotExistsException(response.getString("erroMessage"), "");
        }
        return response;
    }

    public void update(SetterAction action, String argument) throws UserInsufficientPermissionException, RequestErrorException, InvalidCredentialException{
        JSONObject response = (JSONObject) client.request(action.toString().toLowerCase(), argument);
        if(response == null) {
            throw new RequestErrorException("O Client não conseguiu se comunicar com o "+ip+":"+port, action, argument);
        }
        if(response.getBoolean("erro") && !response.has("type")){
            throw new RequestErrorException(response.getString("erroMessage"), action, argument);
        }
        if(response.getBoolean("erro") && response.getString("type").equalsIgnoreCase("UserInsufficientPermission")){
            throw new UserInsufficientPermissionException(response.getString("erroMessage"), Datacrack.getInstance().getSocketManager().getUserBySocket(this));
        }
        if(response.has("erro") && response.getString("type").equalsIgnoreCase("InvalidCredentialException")){
            throw new InvalidCredentialException(response.getString("erroMessage"), Datacrack.getInstance().getSocketManager().getUserBySocket(this));
        }
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public enum GetterAction{
        GETROOTPATH, GETDATAPACK, GETTEMPDATA, HASTEMPDATA, GETUSERPACK, GETLISTPACK
    }
    public enum SetterAction{
        PUTROOTPATH, PUTDATAPACK, PUTTEMPDATA, CREATENEWUSER, PUTUSERPACK, PUTLISTPACK
    }
}
