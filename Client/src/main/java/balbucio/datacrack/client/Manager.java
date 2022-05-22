/*
 * Copyright (c) 2022.
 * Datacrack é desenvolvido e mantido por SrBalbucio.
 * Não revenda e/ou distribua como seu.
 */

package balbucio.datacrack.client;

import balbucio.datacrack.client.Datacrack;
import balbucio.datacrack.client.data.RootDataPack;
import balbucio.datacrack.client.data.TempDataPack;
import balbucio.datacrack.client.exception.DataNotExistsException;
import balbucio.datacrack.client.exception.InvalidCredentialException;
import balbucio.datacrack.client.exception.RequestErrorException;
import balbucio.datacrack.client.exception.UserInsufficientPermissionException;
import balbucio.datacrack.client.socket.SocketInstance;
import balbucio.datacrack.client.user.User;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager {

    private List<SocketInstance> sockets = new ArrayList<>();
    private Map<SocketInstance, User> socketUsers = new HashMap<>();

    public Manager(){

    }

    public List<SocketInstance> getSockets(){
        return sockets;
    }

    /** Métodos para gerenciar os sockets **/

    public void addSocket(String ip, int port){
        SocketInstance socket = new SocketInstance(ip, port);
        sockets.add(socket);
        socketUsers.put(socket, Datacrack.getInstance().getUser());
    }

    public void addSocket(SocketInstance socket){
        sockets.add(socket);
        socketUsers.put(socket, Datacrack.getInstance().getUser());
    }

    public void addSocket(String ip, int port, User socketUser){
        SocketInstance socket = new SocketInstance(ip, port);
        sockets.add(socket);
        socketUsers.put(socket, socketUser);
    }

    public void removeSocket(String ip){
        for(SocketInstance socket : sockets){
            if(socket.getIp().equalsIgnoreCase(ip)){
                sockets.remove(socket);
                socketUsers.remove(socket);
            }
        }
    }

    public void removeSocket(SocketInstance socket){
        sockets.remove(socket);
        socketUsers.remove(socket);
    }

    public User getUserBySocket(SocketInstance socket){
        return socketUsers.get(socket);
    }

    /** Método para gerenciar os RootDataPacks **/

    public RootDataPack getDefaultRootPack() throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        JSONObject response;
        switch(Datacrack.getInstance().getOrgazationOption()){
            case FIRST:
                response = getFirstSource(SocketInstance.GetterAction.GETROOTPATH, "root");
                if(response == null){ return null; }
                return new RootDataPack(response);
        }
        return null;
    }

    public RootDataPack getRootPack(String name) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        JSONObject response;
        switch(Datacrack.getInstance().getOrgazationOption()){
            case FIRST:
                response = getFirstSource(SocketInstance.GetterAction.GETROOTPATH, name);
                if(response == null){ return null; }
                return new RootDataPack(response);
        }
        return null;
    }

    public TempDataPack getTempPack(String name) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        JSONObject response;
        switch(Datacrack.getInstance().getOrgazationOption()){
            case FIRST:
                response = getFirstSource(SocketInstance.GetterAction.GETTEMPDATA, name);
                if(response == null){ return null; }
                return new TempDataPack(response, name);
        }
        return null;
    }

    public TempDataPack createTempPack(String name) throws RequestErrorException, InvalidCredentialException {
        return new TempDataPack(name);
    }

    public boolean containsTempPack(String name) throws RequestErrorException, DataNotExistsException, UserInsufficientPermissionException, InvalidCredentialException {
        return getSource(SocketInstance.GetterAction.HASTEMPDATA, name).getBoolean("contains");
    }

    public void createNewUser(String name, String password, List<String> permissions) throws InvalidCredentialException, RequestErrorException, UserInsufficientPermissionException {
        String list = "";
        for(String s : permissions){
            if(list.equalsIgnoreCase("")){
                list = s;
            } else{
                list = list+","+s;
            }
        }
        updateSource(SocketInstance.SetterAction.CREATENEWUSER, "null", new JSONObject().put("username", name).put("password", password).put("permissions", list));
    }

    /** Métodos para extrair o JSON **/

    public JSONObject getSource(SocketInstance.GetterAction action, String name) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        switch(Datacrack.getInstance().getOrgazationOption()){
            case FIRST:
                return getFirstSource(action, name);
        }
        return null;
    }

    public void updateSource(SocketInstance.SetterAction action, String name, JSONObject source) throws UserInsufficientPermissionException, RequestErrorException, InvalidCredentialException {
        for(SocketInstance socket : sockets){
            socket.update(action, getCredential(socketUsers.get(socket), name).put("source", source).toString());
        }
    }

    public void updateList(SocketInstance.SetterAction action, String name, List<String> source) throws UserInsufficientPermissionException, RequestErrorException, InvalidCredentialException {
        for(SocketInstance socket : sockets){
            socket.update(action, getCredential(socketUsers.get(socket), name).put("list", source).toString());
        }
    }

    /** Métodos de Get do Socket **/

    private JSONObject getFirstSource(SocketInstance.GetterAction action, String name) throws UserInsufficientPermissionException, RequestErrorException, DataNotExistsException, InvalidCredentialException {
        JSONObject response = null;
        SocketInstance instance = null;
        for(SocketInstance socket : sockets){
            JSONObject sp = socket.get(action, getCredential(socketUsers.get(socket), name).toString());
            if(sp != null && response == null){
              response = sp;
              instance = socket;
              break;
            }
        }
        return response;
    }


    private JSONObject getCredential(User user, String pathName){
        JSONObject json = new JSONObject();
        json.put("username", user.getUsername());
        json.put("password", user.getPassword());
        json.put("uuid", user.getUUID().toString());
        json.put("path", pathName);
        return json;
    }
}
