package foodapp.demo.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
public class kafkaController {
     @Autowired
     private kafkaProduce kafkaproduce;

     @PostMapping("/send")
    public String send(@RequestParam String message)
     {
         kafkaproduce.send(message);
         return "message sent successfully";
     }
}
