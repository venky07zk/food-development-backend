package foodapp.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService    implements UserDetailsService {
    @Autowired
    private UserRepository userrepo;
    @Override
    public UserDetails loadUserByUsername(String username)
    {
       foodapp.demo.User user=userrepo.findByUsername(username);
       System.out.println("usern ame : "+user.getUsername());
       System.out.println("password : "+user.getPassword());
       if(user==null)
       {
           throw new UsernameNotFoundException("user not Found");
       }
       return org.springframework.security.core.userdetails.User.builder().username(user.getUsername()).password(user.getPassword())
               .roles(user.getRole()).build();
    }
}
