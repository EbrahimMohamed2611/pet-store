package eg.gov.iti.jets.petstore.security;

import eg.gov.iti.jets.petstore.security.model.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;


@Component
public class JwtUtil {
    private final static String SECRET = "secret";
    private static final String CLAIMS_SUBJECT = "subject";
    private static final String CLAIMS_CREATED = "createdDate";
    private static final String ROLES = "role";
    private static final String USER_ID = "id";
    private static final String FULL_NAME = "name";

    public String generateToken(UserDetails userDetails, Long userId) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        HashMap<String, Object> claims = new HashMap<>(0);
        claims.put(CLAIMS_SUBJECT, userDetails.getUsername());
        claims.put(CLAIMS_CREATED, new Date());
        claims.put(USER_ID, userId);
        claims.put(FULL_NAME, customUserDetails.getFirsName() + " " + customUserDetails.getLastName());
        claims.put(ROLES, userDetails.getAuthorities());
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(HashMap<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 2000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public String getUserNameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }


    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String userName = getUserNameFromToken(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
