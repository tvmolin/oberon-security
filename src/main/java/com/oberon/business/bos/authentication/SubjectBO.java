package com.oberon.business.bos.authentication;

import com.oberon.business.dtos.SubjectDTO;
import com.oberon.exceptions.SubjectAlreadyExistsException;
import com.oberon.exceptions.SubjectAlreadyLinkedToClientException;
import com.oberon.exceptions.SubjectNotAllowedAccessToClientException;
import com.oberon.exceptions.WrongCredentialsException;
import com.oberon.persistence.entities.authentication.Client;
import com.oberon.persistence.entities.authentication.Subject;
import com.oberon.persistence.entities.authentication.SubjectClient;
import com.oberon.persistence.entities.authentication.SubjectRole;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

@ApplicationScoped
public class SubjectBO {

    public Subject getSubject(Long subjectId) {
        return Subject.findById(subjectId);
    }

    public boolean isSubjectAuthenticated(Subject subject, String passwordTry) {

        try {

            byte[] salt = decodeBase64(subject.getSalt());
            String passwordTryHash = createPasswordHash(passwordTry, salt);

            if (!passwordTryHash.equals(subject.getPassword())) {
                throw new WrongCredentialsException();
            }

            return true;
        } catch (Exception e) {
            throw new WrongCredentialsException();
        }

    }

    public boolean isSubjectAllowedClient(Subject subject, Client client) {

        try {
            return SubjectClient.findBySubjectIdAndClientId(subject.getId(), client.getId()) != null;
        } catch (Exception e) {
            throw new SubjectNotAllowedAccessToClientException();
        }

    }

    @Transactional
    public Subject createSubject(SubjectDTO subjectDTO) throws Exception {

        if (Subject.findByUserName(subjectDTO.getUserName()) != null) {
            throw new SubjectAlreadyExistsException();
        }

        byte[] salt = createSalt();
        String passwordHash = createPasswordHash(subjectDTO.getPassword(), salt);

        Subject subject = new Subject();

        subject.setSalt(encodeBase64(salt));
        subject.setPassword(passwordHash);
        subject.setActive(false);
        subject.setEmail(subjectDTO.getEmail());
        subject.setUserName(subjectDTO.getUserName());
        subject.setAlternativeUserName(subjectDTO.getUserName());
        subject.setName(subjectDTO.getName());
        subject.persist();

        return subject;
    }

    @Transactional
    public void linkClientToSubject(Subject subject, Client client) throws SubjectAlreadyLinkedToClientException {

        if (SubjectClient.findBySubjectIdAndClientId(subject.getId(), client.getId()) != null) {
            throw new SubjectAlreadyLinkedToClientException();
        }

        SubjectClient subjectClient = new SubjectClient();

        //Link to the client
        subjectClient.setSubject(subject);
        subjectClient.setClient(client);
        subjectClient.persist();

        //Link to all the roles existing for the client
        client.getClientRoles().forEach(role -> {
            SubjectRole subjectRole = new SubjectRole();

            subjectRole.setRole(role);
            subjectRole.setSubject(subject);
            subjectRole.persist();
        });

    }

    private byte[] createSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        return salt;
    }

    private String createPasswordHash(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        return encodeBase64(factory.generateSecret(spec).getEncoded());
    }

    private String encodeBase64(byte[] array) {
        return new String(Base64.encodeBase64(array));
    }

    private byte[] decodeBase64(String encoded) {
        return Base64.decodeBase64(encoded);
    }
}
