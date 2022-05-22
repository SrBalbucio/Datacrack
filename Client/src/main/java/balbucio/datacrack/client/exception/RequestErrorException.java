package balbucio.datacrack.client.exception;

import balbucio.datacrack.client.socket.SocketInstance;

public class RequestErrorException extends Exception{

    private SocketInstance.GetterAction gaction = null;
    private SocketInstance.SetterAction saction = null;
    private String jsonArguments;

    public RequestErrorException(String message, SocketInstance.GetterAction action, String arguments){
        super(message);
        this.gaction = action;
        this.jsonArguments = arguments;
    }
    public RequestErrorException(String message, SocketInstance.SetterAction action, String arguments){
        super(message);
        this.saction = action;
        this.jsonArguments = arguments;
    }

    public SocketInstance.GetterAction getGetterAction(){
        return gaction;
    }
    public SocketInstance.SetterAction getSetterAction(){
        return saction;
    }

    public String getJsonArguments(){
        return jsonArguments;
    }
}
