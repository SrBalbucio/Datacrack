/*
 * Copyright (c) 2022.
 * Datacrack é desenvolvido e mantido por SrBalbucio.
 * Não revenda e/ou distribua como seu.
 */

package balbucio.datacrack.client;
import balbucio.datacrack.client.user.User;

public class Datacrack {

    private Manager manager;
    private User user;
    private static Datacrack instance;

    public Datacrack(User admin){
        setInstance(this);
        this.user = admin;
        this.manager = new Manager();
    }

    public Datacrack(User admin, boolean notInstance){
        if(!notInstance) {
            setInstance(this);
        }
        this.user = admin;
        this.manager = new Manager();
    }

    public static Datacrack getInstance() {
        return instance;
    }

    public static void setInstance(Datacrack instance) {
        Datacrack.instance = instance;
    }

    public Manager getManager(){
        return manager;
    }

    public User getUser(){
        return user;
    }

}
