package balbucio.datacrack.client.exception;

import balbucio.datacrack.client.user.User;

public class UserInsufficientPermissionException extends Exception{

    private User user;

    public UserInsufficientPermissionException(String message, User user){
        super(message);
        this.user = user;
    }

    public User getUser(){
        return user;
    }
}
