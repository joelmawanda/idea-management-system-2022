package com.flyhub.ideaMS.security;

import com.flyhub.ideaMS.dao.merchant.MerchantDetails;
import com.flyhub.ideaMS.dao.systemuser.SystemUserDetails;
import io.jsonwebtoken.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 *
 * <ol>
 * <li>iss - specifies the issuer who issued the JWT.</li>
 * <li>sub - specifies the subject of the JWT.</li>
 * <li>aud - specifies the audience for which the JWT is intended.</li>
 * <li>exp - the date when the JWT will expire.</li>
 * <li>nbf - the date before which the JWT is invalid.</li>
 * <li>iat - the time when the JWT was issued.</li>
 * <li>jti - the unique identifier of the JWT.</li>
 * </ol>
 *
 *
 * @author Benjamin E Ndugga
 */
@Component
public class JwtTokenUtil {

    private static final Logger log = Logger.getLogger(JwtTokenUtil.class.getName());

    private static final String JWT_SECRET = "thisshouldbealongsecretekeythatwillgeneratean";
    private static final String JWT_ISSUER = "flyhub.ug.com";

    public String generateAccessToken(MerchantDetails merchantDetails) {

        return Jwts.builder()
                .setIssuer(JWT_ISSUER)
                .setSubject(merchantDetails.getUsername())
                .claim("id", merchantDetails.getId())
                .claim("modules", merchantDetails.getModuleAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 1 week
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public String generateAccessToken(SystemUserDetails systemUserDetails) {

        return Jwts.builder()
                .setIssuer(JWT_ISSUER)
                .setSubject(systemUserDetails.getUsername())
                .claim("id", systemUserDetails.getId())
                .claim("issuperadmin", systemUserDetails.isSuperAdmin())
                .claim("modules", systemUserDetails.getModuleAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 1 week
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public boolean validate(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(JWT_SECRET)
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature - {}", ex.getCause());
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token - {}", ex.getCause());
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token - {}", ex.getCause());
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token - {}", ex.getCause());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty - {}", ex.getCause());
        }
        return false;
    }

    public String getUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();

        return claims.get("id", String.class);
    }
}
