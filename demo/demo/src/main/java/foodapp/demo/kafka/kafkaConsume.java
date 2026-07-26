package foodapp.demo.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class kafkaConsume {
    @KafkaListener(topics="food-topic",groupId="food-group")
    public void consume(String message)
    {
        System.out.println("received: "+message);
    }
}
