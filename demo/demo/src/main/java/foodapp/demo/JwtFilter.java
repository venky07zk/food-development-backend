package foodapp.demo;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtService jwtservice;
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().startsWith("/redis");
    }
   @Override
    protected void doFilterInternal( HttpServletRequest request,
                                HttpServletResponse response,
                                FilterChain filterChain)throws ServletException, IOException{
       System.out.println("Jwt Filter Called");
      String authHeader=request.getHeader("Authorization");
       if (authHeader != null && authHeader.startsWith("Bearer ")) {
           String token = authHeader.substring(7);

           try {
               if (jwtservice.validateToken(token)) {
                   String username = jwtservice.extractUsername(token);

                   UserDetails userDetails =
                           userDetailsService.loadUserByUsername(username);

                   UsernamePasswordAuthenticationToken authToken =
                           new UsernamePasswordAuthenticationToken(
                                   userDetails,
                                   null,
                                   userDetails.getAuthorities());

                   SecurityContextHolder.getContext().setAuthentication(authToken);

                   System.out.println("JWT authenticated: " + username);
               }
           } catch (Exception e) {
               System.out.println("JWT validation failed: " + e.getMessage());
               e.printStackTrace();
           }
       }

       filterChain.doFilter(request,response);
   }
}
