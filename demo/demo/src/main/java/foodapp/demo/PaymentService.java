package foodapp.demo;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    @Value("${razorpay.key.id}")
    private String keyId;
    @Value("${razorpay.key.secret}")
    private String keysecret;
    @Autowired
    private cartRepository cartrepository;
    @Autowired
    private OrderRepository orderrepository;

    private RazorpayClient getClient() throws RazorpayException
    {
        return new RazorpayClient(keyId, keysecret);
    }
    public String createOrder(double amount) throws RazorpayException {
        RazorpayClient client = getClient();
        JSONObject options=new  JSONObject();
        options.put("amount",(int)amount*100);
        options.put("currency","INR");

        com.razorpay.Order razorpayOrder = client.orders.create(options);
        System.out.println(razorpayOrder);
        return razorpayOrder.get("id").toString();
    }
    public boolean verifypayment(paymentVerifyDTO dto) throws RazorpayException
    {
        System.out.println("OrderId = " + dto.getOrderId());
        System.out.println("OrderId Razorpay = " + dto.getRazorpayorderId());
        System.out.println("PaymentId = " + dto.getRazorpaypaymentId());
        System.out.println("Signature = " + dto.getRazorpaySignature());
        JSONObject options=new JSONObject();
        options.put("razorpay_order_id",dto.getRazorpayorderId());
        options.put("razorpay_payment_id",dto.getRazorpaypaymentId());
        options.put("razorpay_signature",dto.getRazorpaySignature());
        System.out.println(options.toString(2));
        System.out.println("Key Secret: " + keysecret);
        boolean verified= Utils.verifyPaymentSignature(options,keysecret);
        if(!verified)
        {
           return false;
        }
        Order order=orderrepository.findById(dto.getOrderId()).orElseThrow(()-> new RuntimeException("Order not found"));
        order.setPaymentStatus(PaymentStatus.PAID);
        order.setStatus(OrderStatus.PLACED);
        order.setRazorpayPaymentId(dto.getRazorpaypaymentId());
        orderrepository.save(order);
        Cart cart=cartrepository.findByUser(order.getUser());
        if(cart!=null)
        {
            cartrepository.delete(cart);
        }
        return true;
    }

}
