package gilir.backendfinalpro.security;



import gilir.backendfinalpro.error.BadRequestException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTProvider {
    //read values from application.properties:

    @Value("${gilir.backendfinalpro.secret}")
    private String secret;

    @Value("${gilir.backendfinalpro.expires}")
    private Long expires;

    private static Key mSecretKey;

    @PostConstruct
    private void init() {
        mSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateToken(String username) {
        var currentDate = new Date();
        //future date:
        var expiresDate = new Date(currentDate.getTime() + expires);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expiresDate)
                .signWith(mSecretKey)
                .compact();
    }

    public boolean validateToken(String jwt) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(mSecretKey)
                    .build()
                    .parseClaimsJws(jwt);
        } catch (ExpiredJwtException e) {
            throw new BadRequestException("Token", "Expired");
        } catch (MalformedJwtException e) {
            throw new BadRequestException("Token", "Invalid");
        } catch (JwtException e) {
            throw new BadRequestException("Token", "Exception: " + e.getMessage());
        }

        return true;
    }

    public String getUsernameFromToken(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(mSecretKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody().getSubject();
    }

}
