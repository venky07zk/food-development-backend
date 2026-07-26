package foodapp.demo;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class smsService {
    @Value("${twilio.account.sid}")
    private String accountSid;
    @Value("${twilio.auth.token}")
    private String authToken;
    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    public void initialize()
    {
        Twilio.init(accountSid, authToken);
    }
    public void sendOtp(String phoneNumber,String otp)
    {
        initialize();
        Message.creator(new PhoneNumber(phoneNumber),new PhoneNumber(twilioPhoneNumber),"your OTP is :"+otp).create();
    }
    public void callOtp(String phoneNumber) throws Exception
    {
       Twilio.init(accountSid, authToken);
       Call.creator(new PhoneNumber(phoneNumber),new PhoneNumber(twilioPhoneNumber),new URI("http://demo.twilio.com/docs/voice.xml")).create();
    }


}
