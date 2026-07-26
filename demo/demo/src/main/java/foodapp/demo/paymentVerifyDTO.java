package foodapp.demo;

public class paymentVerifyDTO {
    private int orderId;
    private String razorpaypaymentId;
    private String razorpayorderId;
    private String razorpaySignature;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getRazorpaypaymentId() {
        return razorpaypaymentId;
    }

    public void setRazorpaypaymentId(String razorpaypaymentId) {
        this.razorpaypaymentId = razorpaypaymentId;
    }

    public String getRazorpayorderId() {
        return razorpayorderId;
    }

    public void setRazorpayorderId(String razorpayorderId) {
        this.razorpayorderId = razorpayorderId;
    }

    public String getRazorpaySignature() {
        return razorpaySignature;
    }

    public void setRazorpaySignature(String razorpaySignature) {
        this.razorpaySignature = razorpaySignature;
    }
}
