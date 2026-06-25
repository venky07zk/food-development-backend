package foodapp.demo;

public class FoodNotFound  extends RuntimeException{
    public FoodNotFound(String message)
    {
        super(message);
    }
}
