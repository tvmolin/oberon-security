package com.oberon.business.bos.authentication;

import com.oberon.business.dtos.TokenDTO;
import com.oberon.exceptions.SubjectNotAllowedAccessToClientException;
import com.oberon.exceptions.WrongCredentialsException;
import com.oberon.persistence.entities.authentication.Client;
import com.oberon.persistence.entities.authentication.Subject;
import com.oberon.persistence.entities.authentication.SubjectRole;
import com.oberon.persistence.entities.authentication.Token;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claims;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class TokenBO {

    @ConfigProperty(name = "oberon.security.exp")
    private Long expirationTime;

    private SubjectBO subjectBO;

    public TokenBO(SubjectBO subjectBO) {
        this.subjectBO = subjectBO;
    }

    /**
     * Method to generate a JWT string that is signed by the privateKey.pem resource key.
     *
     * @return the JWT string
     * @throws Exception on parse failure
     */
    private String generateTokenString(JwtClaims jwtClaims) throws Exception {
        // Use the private key associated with the public key for a valid signature
        PrivateKey privateKey = readPrivateKey();

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(jwtClaims.toJson());
        jws.setKey(privateKey);
        jws.setKeyIdHeaderValue("/privateKey.pem");
        jws.setHeader("typ", "JWT");
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        return jws.getCompactSerialization();
    }

    private JwtClaims getClaims(Subject subject, String... audience) {
        JwtClaims claims = new JwtClaims();
        claims.setIssuer("Oberon-security");
        claims.setJwtId(UUID.randomUUID().toString());
        claims.setSubject(subject.getName());
        claims.setAudience(audience);
        claims.setStringListClaim("groups", getSubjectRolesStringList(subject));

        long currentTimeInSecs = currentTimeInSecs();
        long exp = currentTimeInSecs + expirationTime; //set expiration time in properties file

        claims.setIssuedAt(NumericDate.fromSeconds(currentTimeInSecs));
        claims.setClaim(Claims.auth_time.name(), NumericDate.fromSeconds(currentTimeInSecs));
        claims.setExpirationTime(NumericDate.fromSeconds(exp));

        return claims;
    }

    private List<String> getSubjectRolesStringList(Subject subject) {
        List<SubjectRole> subjectRoles = SubjectRole.findBySubjectId(subject.getId());
        return subjectRoles.stream().map(subjectRole -> subjectRole.getRole().getName()).collect(Collectors.toList());
    }

    /**
     * Read a PEM encoded private key from the classpath
     *
     * @return PrivateKey
     * @throws Exception on decode failure
     */
    private PrivateKey readPrivateKey() throws Exception {
        InputStream contentIS = TokenBO.class.getResourceAsStream("/privateKey.pem");
        byte[] tmp = new byte[4096];
        int length = contentIS.read(tmp);
        return decodePrivateKey(new String(tmp, 0, length, StandardCharsets.UTF_8));
    }

    /**
     * Decode a PEM encoded private key string to an RSA PrivateKey
     *
     * @param pemEncoded - PEM string for private key
     * @return PrivateKey
     * @throws Exception on decode failure
     */
    private PrivateKey decodePrivateKey(final String pemEncoded) throws Exception {
        byte[] encodedBytes = toEncodedBytes(pemEncoded);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(keySpec);
    }

    private byte[] toEncodedBytes(final String pemEncoded) {
        final String normalizedPem = removeBeginEnd(pemEncoded);
        return Base64.getDecoder().decode(normalizedPem);
    }

    private String removeBeginEnd(String pem) {
        pem = pem.replaceAll("-----BEGIN (.*)-----", "");
        pem = pem.replaceAll("-----END (.*)----", "");
        pem = pem.replaceAll("\r\n", "");
        pem = pem.replaceAll("\n", "");
        return pem.trim();
    }

    private int currentTimeInSecs() {
        long currentTimeMS = System.currentTimeMillis();
        return (int) (currentTimeMS / 1000);
    }

    @Transactional
    public Token createToken(TokenDTO tokenDto) throws Exception {
        Subject subject = Subject.findByUserName(tokenDto.getUserName());
        Client client = Client.findByName(tokenDto.getClient());

        if (!subjectBO.isSubjectAuthenticated(subject, tokenDto.getPassword())) {
            throw new WrongCredentialsException();
        }

        if (!subjectBO.isSubjectAllowedClient(subject, client)) {
            throw new SubjectNotAllowedAccessToClientException();
        }

        Token token = new Token();

        token.setClient(client);
        token.setSubject(subject);
        token.setTokenString(generateTokenString(getClaims(subject, tokenDto.getClient())));
        token.setExpiresIn(expirationTime);
        token.setType("password");
        token.persist();

        return token;
    }
}
