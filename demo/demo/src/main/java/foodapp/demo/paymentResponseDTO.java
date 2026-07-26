package foodapp.demo;

public class paymentResponseDTO {
    private int orderid;;
    private String razorpayorderid;
    private double amount;
    private String currency;
    private double distance;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public String getRazorpayorderid() {
        return razorpayorderid;
    }

    public void setRazorpayorderid(String razorpayorderid) {
        this.razorpayorderid = razorpayorderid;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
