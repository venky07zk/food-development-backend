package foodapp.demo;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    private static final String SECRET="mysecretkeymysecretkeymysecretkey123456";
    private SecretKey getKey()
    {
        return Keys.hmacShaKeyFor(
                SECRET.getBytes()
        );
    }
    public boolean validateToken(String token)
    {
        Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token);
        return true;
    }
    public String extractUsername(String token)
    {
        return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload().getSubject();
    }
    public String generateToken(String username)
    {
        return Jwts.builder().subject(username).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis()+1000*60*60)).
        signWith(getKey()).compact();
    }
}
