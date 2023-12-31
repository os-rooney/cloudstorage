package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {

    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public List<Credential> getCredentials(Integer userId) {
        return credentialMapper.getCredentials(userId);
    }

    public void saveCredential(Credential credential) {
        credentialMapper.save(credential);
    }

    public String createEncryptedKey() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }

    public String createEncryptedPassword(String password, String encodedKey) {
        return encryptionService.encryptValue(password, encodedKey);
    }

    public void deleteCredential(Integer credentialId, Integer userId) {
        credentialMapper.delete(credentialId, userId);
    }

    public void updateCredential(Credential credential) {
        credentialMapper.update(credential);
    }
}
