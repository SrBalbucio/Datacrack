package balbucio.datacrack.client.exception;

public class DataPackLimitException extends Exception{

    private String pack;

    public DataPackLimitException(String message, String pack){
        super(message);
        this.pack = pack;
    }

    public String getDataPackName(){
        return pack;
    }
}
