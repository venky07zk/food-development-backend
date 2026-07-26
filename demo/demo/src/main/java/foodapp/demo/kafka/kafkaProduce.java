package foodapp.demo.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class kafkaProduce {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    public void send(String Message)
    {
        kafkaTemplate.send("food-topic", Message);
    }
}
