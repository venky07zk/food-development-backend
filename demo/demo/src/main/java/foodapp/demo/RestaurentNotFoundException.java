package foodapp.demo;

public class RestaurentNotFoundException extends RuntimeException{
    public RestaurentNotFoundException(String message){
        super(message);
    }
}
