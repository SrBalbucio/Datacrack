/*
 * Copyright (c) 2022.
 * Datacrack é desenvolvido e mantido por SrBalbucio.
 * Não revenda e/ou distribua como seu.
 */

package balbucio.datacrack.client;

import balbucio.datacrack.client.options.Organization;
import balbucio.datacrack.client.user.User;

public class Datacrack {

    private Manager manager;
    private User user;
    private static Datacrack instance;

    private Organization orgazationOption = Organization.FIRST;

    public Datacrack(User admin){
        setInstance(this);
        this.user = admin;
        this.manager = new Manager();
    }

    public static Datacrack getInstance() {
        return instance;
    }

    public static void setInstance(Datacrack instance) {
        Datacrack.instance = instance;
    }

    public Manager getSocketManager(){
        return manager;
    }

    public User getUser(){
        return user;
    }

    public void setOrganizationOption(Organization organization){
        this.orgazationOption = organization;
    }

    public Organization getOrgazationOption() {
        return orgazationOption;
    }

}
