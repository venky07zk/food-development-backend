package foodapp.demo.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
public class RedisController {
    @Autowired

    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/redis/save")
    public String save()
    {
        System.out.println("method called");
        redisTemplate.opsForValue().set("name","venky");
        return "saved";
    }

    @GetMapping("/redis/get")
    public Object get()
    {
        return redisTemplate.opsForValue().get("name");
    }
}
