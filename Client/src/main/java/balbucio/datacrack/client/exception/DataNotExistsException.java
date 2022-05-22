package balbucio.datacrack.client.exception;

public class DataNotExistsException extends Exception{

    private String keyData;

    public DataNotExistsException(String message, String keyData){
        super(message);
        this.keyData = keyData;
    }

    public String getKeyData(){
        return keyData;
    }
}
