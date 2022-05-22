/*
 * Copyright (c) 2022.
 * Datacrack é desenvolvido e mantido por SrBalbucio.
 * Não revenda e/ou distribua como seu.
 */

package balbucio.datacrack.client.exception;

import balbucio.datacrack.client.user.User;

public class InvalidCredentialException extends Exception {

    private User user;
    public InvalidCredentialException(String message, User user){
        super(message);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
