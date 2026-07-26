package foodapp.demo;

public class placeOrderDTO {
    private double customerlatitude;
    private double customerlongitude;
    private String deliveryInstructions;

    public double getCustomerlatitude() {
        return customerlatitude;
    }

    public void setCustomerlatitude(double customerlatitude) {
        this.customerlatitude = customerlatitude;
    }

    public double getCustomerlongitude() {
        return customerlongitude;
    }

    public void setCustomerlongitude(double customerlongitude) {
        this.customerlongitude = customerlongitude;
    }

    public String getDeliveryInstructions() {
        return deliveryInstructions;
    }

    public void setDeliveryInstructions(String deliveryInstructions) {
        this.deliveryInstructions = deliveryInstructions;
    }
}
