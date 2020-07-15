package Exceptions;

public class PlayerRegException extends Exception {
    private static final long serialVersionUID = 1L;

    public PlayerRegException(String errMessage){
        super(errMessage);
    }
}
