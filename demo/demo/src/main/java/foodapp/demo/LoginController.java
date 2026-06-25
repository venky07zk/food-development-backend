package foodapp.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    AuthenticationService authService;
    @Autowired
    ServiceLayer s;
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody registerDTO r)
    {
        return s.register(r);
    }
    @PostMapping("/login")
    public String login(
            @RequestBody LoginRequest request)
    {
        return authService.authenticate(
                request.getUsername(),
                request.getPassword()
        );


    }
}
