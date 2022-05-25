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
import balbucio.datacrack.client.user.User;
import co.gongzh.procbridge.Client;
import co.gongzh.procbridge.TimeoutException;
import org.json.JSONObject;

import java.util.List;

public class SocketInstance extends Thread {

    private String ip;
    private int port;
    private Client client;
    private User user;

    private SetterAction action;
    private Details details;

    private static List<SocketInstance> sockets;

    public SocketInstance(String ip, int port){
        this.ip = ip;
        this.port = port;
        client = new Client(ip, port);
        this.user = Datacrack.getInstance().getUser();
        sockets.add(this);
    }
    public SocketInstance(String ip, int port, User user){
        this.ip = ip;
        this.port = port;
        client = new Client(ip, port);
        this.user = user;
        sockets.add(this);
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public User getUser() {
        return user;
    }

    public JSONObject sendSetterRequest(SetterAction action, Details details){
        try{
            return (JSONObject) client.request(action.toString().toLowerCase(), details.getRequestCommand());
        }catch (TimeoutException e){
            e.printStackTrace();
        }
        return null;
    }
    public JSONObject sendGetterRequest(GetterAction action, Details details){
        try{
            return (JSONObject) client.request(action.toString().toLowerCase(), details.getRequestCommand());
        }catch (TimeoutException e){
            e.printStackTrace();
        }
        return null;
    }

    public static UpdateDetails update(SetterAction action, Details details){
        UpdateDetails updetails = new UpdateDetails(action, details.getRequestCommand().toString());
        for(SocketInstance socket : sockets){
            updetails.addJsonResponse(socket, socket.sendSetterRequest(action, details));
        }
        return updetails;
    }

    public static GetDetails get(GetterAction action, Details details){
        GetDetails getDetails = new GetDetails(action, details.getRequestCommand().toString());
        for(SocketInstance socket : sockets){
            getDetails.addJsonResponse(socket, socket.sendGetterRequest(action, details));
        }
        return getDetails;
    }

    public enum GetterAction{
        GETROOTPATH, GETDATAPACK, GETTEMPDATA, HASTEMPDATA, GETUSERPACK, GETLISTPACK
    }
    public enum SetterAction{
        PUTROOTPATH, PUTDATAPACK, PUTTEMPDATA, CREATENEWUSER, PUTUSERPACK, PUTLISTPACK
    }
}
