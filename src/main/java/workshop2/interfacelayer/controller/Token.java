/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import java.security.Key;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hwkei
 */
public class Token {
    private static final Logger log = LoggerFactory.getLogger(Token.class);
    
    static String createJWT(String id, String issuer, String subject, long ttlMillis) {
 
        //The JWT signature algorithm to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //Sign the JWT with a secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("Geheim");
        
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Set the claims
        JwtBuilder builder = Jwts.builder().setId(id)
                                    .setIssuedAt(now)
                                    .setSubject(subject)
                                    .setIssuer(issuer)
                                    .signWith(signatureAlgorithm, signingKey);
        

        //if expiration is specified then set it
        if (ttlMillis > 0) {
        long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //Build the JWT and serialize it to a compact, URL-safe string
        return builder.compact();
    }
    
    //Validate and read the JWT
    static String parseJWT(String jwt) { 
        //This will throw an exception if it is not a signed JWS (as expected)        
        try {
            Claims claims = Jwts.parser()         
           .setSigningKey(DatatypeConverter.parseBase64Binary("Geheim"))
           .parseClaimsJws(jwt).getBody();
            log.debug("ID: " + claims.getId());
            log.debug("Subject: " + claims.getSubject());
            log.debug("Issuer: " + claims.getIssuer());
            log.debug("Expiration: " + claims.getExpiration());
            String userName = claims.getId();
            return userName;
        } catch (ExpiredJwtException | SignatureException e) {
            return null;
        }        
    }
}
