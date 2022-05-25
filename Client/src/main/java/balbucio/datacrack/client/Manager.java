/*
 * Copyright (c) 2022.
 * Datacrack é desenvolvido e mantido por SrBalbucio.
 * Não revenda e/ou distribua como seu.
 */

package balbucio.datacrack.client;

import balbucio.datacrack.client.data.RootDataPack;
import balbucio.datacrack.client.data.TempDataPack;
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
        SocketInstance socket = new SocketInstance(ip, port, socketUser);
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

    public RootDataPack getDefaultRootPack() throws Exception {
        GetDetails details = SocketInstance.get(SocketInstance.GetterAction.GETROOTPATH, new Details(null, "root"));
        if(details.hasError()){
            for(Exception e : details.getErros().values()){
                throw e;
            }
        }
        return new RootDataPack(details.getSource());
    }

    public RootDataPack getRootPack(String name) throws Exception {
        GetDetails details = SocketInstance.get(SocketInstance.GetterAction.GETROOTPATH, new Details(null, name));
        if(details.hasError()){
            for(Exception e : details.getErros().values()){
                throw e;
            }
        }
        return new RootDataPack(details.getSource());
    }

    public TempDataPack getTempPack(String name) throws Exception {
        GetDetails details = SocketInstance.get(SocketInstance.GetterAction.GETTEMPDATA, new Details(null, name));
        if(details.hasError()){
            for(Exception e : details.getErros().values()){
                throw e;
            }
        }
        return new TempDataPack(details.getSource(), name);
    }

    public TempDataPack createTempPack(String name) throws RequestErrorException, InvalidCredentialException {
        return new TempDataPack(name);
    }

    public boolean containsTempPack(String name) throws Exception {
        GetDetails details = SocketInstance.get(SocketInstance.GetterAction.HASTEMPDATA, new Details(null, name));
        if(details.hasError()){
            for(Exception e : details.getErros().values()){
                throw e;
            }
        }
        return details.getSource().getBoolean("contains");
    }

    public void createNewUser(String name, String password, List<String> permissions) throws Exception {
        String list = "";
        for (String s : permissions) {
            if (list.equalsIgnoreCase("")) {
                list = s;
            } else {
                list = list + "," + s;
            }
        }
        UpdateDetails details = SocketInstance.update(SocketInstance.SetterAction.CREATENEWUSER, new Details(new JSONObject().put("username", name).put("password", password).put("permissions", list), ""));
        if(details.hasError()){
            for(Exception e : details.getErros().values()){
                throw e;
            }
        }
    }

}
