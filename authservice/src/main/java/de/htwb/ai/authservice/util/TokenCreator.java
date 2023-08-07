package de.htwb.ai.authservice.util;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;

// https://developer.okta.com/blog/2018/10/31/jwts-with-java
public class TokenCreator {
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("this_is_the_key");
    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

    public TokenCreator() {
    }

    public String create(String userid) {
        JwtBuilder builder = Jwts.builder().setSubject(userid).signWith(signatureAlgorithm, signingKey);
        return builder.compact();
    }
}
