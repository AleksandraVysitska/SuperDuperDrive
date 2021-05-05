package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialsService {

    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialsService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public List<Credentials> getAll() {
        List<Credentials> credentials = credentialMapper.findAll();

        if (credentials == null) {
            return new ArrayList<>();
        }

        for (Credentials credential : credentials) {
            credential.setDecryptedPassword(decryptPassword(credential.getPassword(),
                    credential.getKey()));
        }

        return credentials;
    }

    public List<Credentials> getAllByUserId(Long userId) {
        List<Credentials> credentials = credentialMapper.findByUserId(userId);

        if (credentials == null) {
            return new ArrayList<>();
        }

        for (Credentials credential : credentials) {
            credential.setDecryptedPassword(decryptPassword(credential.getPassword(),
                    credential.getKey()));
        }

        return credentials;
    }

    public Credentials getById(Long id) {
        Credentials credential = credentialMapper.findById(id);

        if (credential != null) {
            credential.setDecryptedPassword(decryptPassword(credential.getPassword(),
                    credential.getKey()));
        }

        return credential;
    }

    public boolean create(Credentials credential, Long userId) {
        Integer result = credentialMapper.create(encryptPassword(credential), userId);

        return result > 0;
    }

    public boolean update(Credentials credential, Long userId) {
        Integer result = credentialMapper.update(encryptPassword(credential), userId);

        return result > 0;
    }

    public boolean delete(Long id, Long userId) {
        Integer result = credentialMapper.delete(id, userId);

        return result > 0;
    }

    private Credentials encryptPassword(Credentials credential) {
        if (credential.getKey() == null) {
            SecureRandom random = new SecureRandom();
            byte[] key = new byte[16];
            random.nextBytes(key);
            credential.setKey(Base64.getEncoder().encodeToString(key));
        }

        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), credential.getKey());

        credential.setPassword(encryptedPassword);

        return credential;
    }

    private String decryptPassword(String password, String key) {
        return encryptionService.decryptValue(password, key);
    }
}
