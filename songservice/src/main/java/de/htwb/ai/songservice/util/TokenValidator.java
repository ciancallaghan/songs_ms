package de.htwb.ai.songservice.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;

// https://developer.okta.com/blog/2018/10/31/jwts-with-java
public class TokenValidator {
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("this_is_the_key");
    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

    public TokenValidator() {

    }

    public String validate(String token) {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary("this_is_the_key"))
                .parseClaimsJws(token).getBody().getSubject();
    }
}
