/*
 * Copyright (c) 2022.
 * Datacrack é desenvolvido e mantido por SrBalbucio.
 * Não revenda e/ou distribua como seu.
 */

package balbucio.datacrack;

import balbucio.datacrack.data.DataManager;
import balbucio.datacrack.data.TempManager;
import balbucio.datacrack.users.User;
import co.gongzh.procbridge.IDelegate;
import co.gongzh.procbridge.Server;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.List;

public class WebSocket {

    private Server server;

    public WebSocket(int port){
        this.server = new Server(port, new IDelegate() {
            @Override
            public @Nullable Object handleRequest(@Nullable String method, @Nullable Object payload) {
                try {
                    JSONObject jsonPayload = (JSONObject) payload;
                    String username = jsonPayload.getString("username");
                    String uuid = jsonPayload.getString("uuid");
                    if(!Main.getInstance().getUserManager().containsUser(username)){
                        return new JSONObject().put("erro", true).put("erroMessage", "Não foi possível logar com essa credencial").put("type", "InvalidCredentialException");
                    }
                    User user = Main.getInstance().getUserManager().getUser(username);
                    String password = jsonPayload.getString("password");
                    if(!user.getPassword().equals(password)){
                        return new JSONObject().put("erro", true).put("erroMessage", "Não foi possível logar com essa credencial").put("type", "InvalidCredentialException");
                    }
                    String path = jsonPayload.getString("path");
                    JSONObject source;
                    switch (method) {
                        case "getrootpath":
                            System.out.print("\nUm request GET ROOT (" + path + ") foi executado por " + username + ".");
                            if (!Main.getConfig().getBoolean("pathEqualToUsernameAllow") && !user.getPermissions().contains("all.rootpack." + path) && !user.getPermissions().contains("read.rootpack." + path) && !user.isAdmin()) {
                                return new JSONObject().put("erro", true).put("erroMessage", "Esse usuário não tem permissão para acessar esse RootDataPack!").put("type", "UserInsufficientPermission");
                            }
                            if(Main.getConfig().getBoolean("pathEqualToUsernameAllow") && !path.equals(username) && !user.isAdmin()){
                                return new JSONObject().put("erro", true).put("erroMessage", "Esse usuário não tem permissão para acessar esse DataPack!").put("type", "UserInsufficientPermission");
                            }
                            return DataManager.getRootPack(path);
                        case "putrootpath":
                            System.out.print("\nUm request PUT ROOT (" + path + ") foi executado por " + username + ".");
                            if (!Main.getConfig().getBoolean("pathEqualToUsernameAllow") && !user.getPermissions().contains("all.rootpack." + path) && !user.getPermissions().contains("write.rootpack." + path) && !user.isAdmin()) {
                                return new JSONObject().put("erro", true).put("erroMessage", "Esse usuário não tem permissão para acessar esse RootDataPack!").put("type", "UserInsufficientPermission");
                            }
                            if(Main.getConfig().getBoolean("pathEqualToUsernameAllow") && !path.equals(username) && !user.isAdmin()){
                                return new JSONObject().put("erro", true).put("erroMessage", "Esse usuário não tem permissão para acessar esse RootDataPack!").put("type", "UserInsufficientPermission");
                            }
                            source = jsonPayload.getJSONObject("source");
                            DataManager.updateRootPack(path, source);
                            return new JSONObject().put("erro", false);
                        case "getdatapack":
                            System.out.print("\nUm request GET DATA (" + path + ") foi executado por " + username + ".");
                            if (!Main.getConfig().getBoolean("pathEqualToUsernameAllow") && !user.getPermissions().contains("all.datapack." + path.replace("/", ",")) && !user.getPermissions().contains("read.datapack." + path) && !user.isAdmin()) {
                                return new JSONObject().put("erro", true).put("erroMessage", "Esse usuário não tem permissão para acessar esse DataPack!").put("type", "UserInsufficientPermission");
                            }
                            if(Main.getConfig().getBoolean("pathEqualToUsernameAllow") && !path.equals(username) && !user.isAdmin()){
                                return new JSONObject().put("erro", true).put("erroMessage", "Esse usuário não tem permissão para acessar esse DataPack!").put("type", "UserInsufficientPermission");
                            }
                            return DataManager.getDataPack(path);
                        case "putdatapack":
                            System.out.print("\nUm request PUT DATA (" + path + ") foi executado por " + username + ".");
                            if (!Main.getConfig().getBoolean("pathEqualToUsernameAllow") && !user.getPermissions().contains("all.datapack." + path.replace("/", ",")) && !user.getPermissions().contains("write.datapack." + path) && !user.isAdmin()) {
                                return new JSONObject().put("erro", true).put("erroMessage", "Esse usuário não tem permissão para acessar esse DataPack!").put("type", "UserInsufficientPermission");
                            }
                            if(Main.getConfig().getBoolean("pathEqualToUsernameAllow") && !path.equals(username)){
                                return new JSONObject().put("erro", true).put("erroMessage", "Esse usuário não tem permissão para acessar esse DataPack!").put("type", "UserInsufficientPermission");
                            }
                            source = jsonPayload.getJSONObject("source");
                            DataManager.updateDataPack(path, source);
                            return new JSONObject().put("erro", false);
                        case "gettempdata":
                            System.out.print("\nUm request GET TEMP (" + path + ") foi executado por " + username + ".");
                            if (!user.getPermissions().contains("use.tempdata") && !user.isAdmin()) {
                                return new JSONObject().put("erro", true).put("erroMessage", "Esse usuário não tem permissão para usar TempDataPacks!").put("type", "UserInsufficientPermission");
                            }
                            if (TempManager.getTempData(path) == null) {
                                return new JSONObject().put("erro", true).put("erroMessage", "Não foi possível encontrar esse TempDataPack.").put("type", "TempDataNotExists");
                            }
                            return TempManager.getTempData(path);
                        case "puttempdata":
                            System.out.print("\nUm request PUT TEMP (" + path + ") foi executado por " + username + ".");
                            if (!user.getPermissions().contains("use.tempdata") && !user.isAdmin()) {
                                return new JSONObject().put("erro", true).put("erroMessage", "Esse usuário não tem permissão para usar TempDataPacks!").put("type", "UserInsufficientPermission");
                            }
                            source = jsonPayload.getJSONObject("source");
                            TempManager.setTempData(path, source);
                            return new JSONObject().put("erro", false);
                        case "hastempdata":
                            System.out.print("\nUm request HAS TEMP (" + path + ") foi executado por " + username + ".");
                            if (!user.getPermissions().contains("use.tempdata") && !user.isAdmin()) {
                                return new JSONObject().put("erro", true).put("erroMessage", "Esse usuário não tem permissão para usar TempDataPacks!").put("type", "UserInsufficientPermission");
                            }
                            return new JSONObject().put("contains", TempManager.contains(path));
                        case "createNewUser":
                            System.out.print("\nUm request CRETENEWUSER foi executado por " + username + ".");
                            if (!user.getPermissions().contains("createNewUsers") && !user.isAdmin()) {
                                return new JSONObject().put("erro", true).put("erroMessage", "Esse usuário não tem permissão para criar Usuários!").put("type", "UserInsufficientPermission");
                            }
                            source = jsonPayload.getJSONObject("source");
                            Main.getInstance().getUserManager().createNewUser(source);
                            return new JSONObject().put("erro", false);
                        case "getuserpack":
                            System.out.print("\nUm request GET USER (" + path + ") foi executado por " + username + ".");
                            if (!path.equals(uuid) && !user.getPermissions().contains("all.userpack." + path) && !user.getPermissions().contains("read.userpack." + path)) {
                                return new JSONObject().put("erro", true).put("erroMessage", "Esse usuário não tem permissão para acessar esse RootDataPack!").put("type", "UserInsufficientPermission");
                            }
                            return DataManager.getUserDataPack(path);
                        case "putuserpack":
                            System.out.print("\nUm request PUT USER (" + path + ") foi executado por " + username + ".");
                            if (!path.equals(uuid) && !user.getPermissions().contains("all.userpack") && !user.getPermissions().contains("write.userpack") && !user.isAdmin()) {
                                return new JSONObject().put("erro", true).put("erroMessage", "Esse usuário não tem permissão para acessar esse UserDataPack!").put("type", "UserInsufficientPermission");
                            }
                            source = jsonPayload.getJSONObject("source");
                            DataManager.updateUserDataPack(path, source);
                            return new JSONObject().put("erro", false);
                        case "getlistpack":
                            System.out.print("\nUm request GET LIST (" + path + ") foi executado por " + username + ".");
                            if (!user.getPermissions().contains("all.listpack." + path) && !user.getPermissions().contains("read.listpack." + path) && !user.isAdmin()) {
                                return new JSONObject().put("erro", true).put("erroMessage", "Esse usuário não tem permissão para acessar esse RootDataPack!").put("type", "UserInsufficientPermission");
                            }
                            return DataManager.getListDataPack(path);
                        case "putlistpack":
                            System.out.print("\nUm request PUT LIST (" + path + ") foi executado por " + username + ".");
                            if (!user.getPermissions().contains("all.listpack." + path) && !user.getPermissions().contains("write.listpack." + path) && !user.isAdmin()) {
                                return new JSONObject().put("erro", true).put("erroMessage", "Esse usuário não tem permissão para acessar esse RootDataPack!").put("type", "UserInsufficientPermission");
                            }
                            DataManager.updateListDataPack(path, (List<String>) jsonPayload.get("list"));
                            return new JSONObject().put("erro", false);
                        case "deleterootpath":
                            System.out.print("\nUm request DELETE ROOT (" + path + ") foi executado por " + username + ".");
                            if (!user.getPermissions().contains("all.listpack." + path) && !user.isAdmin()) {
                                return new JSONObject().put("erro", true).put("erroMessage", "Esse usuário não tem permissão para acessar esse RootDataPack!").put("type", "UserInsufficientPermission");
                            }
                            DataManager.deleteRootPack(path);
                            return new JSONObject().put("erro", false);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    System.out.print("\nUm request foi RECUSADO ou teve problemas em sua finalização.");
                    return new JSONObject().put("erro", true).put("erroMessage", "Não foi possível identificar o método utilizado no request.");
                }
                System.out.print("\nUm request foi RECUSADO ou teve problemas em sua finalização.");
                return new JSONObject().put("erro", true).put("erroMessage", "Não foi possível concluir o request.");
            }
        });
        System.out.print("\nO Datacrack está pronto para receber conexões.\nPorta de conexão: "+server.getPort()+"\n");
        server.start();
    }
}
