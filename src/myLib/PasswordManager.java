package myLib;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordManager {
    private static final int SALT_LENGTH = 32;
    private static final int ITERATIONS = 1000000;
    private static final int KEY_LENGTH = 256;

    private final SecureRandom secureRandom;
    private final SecretKeyFactory keyFactory;
    private final String algorithm;
    private final Map<String, UserCredentials> userDatabase;

    // Storage of User Credentials using an Inner Function
    private static class UserCredentials {
        private final byte[] salt;
        private final String passwordHash;

        public UserCredentials(byte[] salt, String passwordHash) {
            this.salt = salt;
            this.passwordHash = passwordHash;
        }

        public byte[] getSalt() {
            return salt;
        }

        public String getPasswordHash() {
            return passwordHash;
        }
    }

    public PasswordManager() {
        this.secureRandom = new SecureRandom();
        this.userDatabase = new HashMap<>();

        String[] algorithmsList = {
                "PBKDF2WithHmacSHA512",
                "PBKDF2WithHmacSHA384",
                "PBKDF2WithHmacSHA256",
                "PBKDF2WithHmacSHA1"
        };

        SecretKeyFactory factory = null;
        String selectedAlgorithm = null;

        for (String algo : algorithmsList) {
            try {
                factory = SecretKeyFactory.getInstance(algo);
                selectedAlgorithm = algo;
                break;
            } catch (NoSuchAlgorithmException e) {
                System.out.println("Algorithm " + algo + " not found");
            }
        }

        if (factory == null) {
            throw new RuntimeException(
                    "No PBKDF2 algorithm found. Please update Java!!!"
            );
        }

        this.keyFactory = factory;
        this.algorithm = selectedAlgorithm ;
    }

    public String getAlgorithm() {
        return this.algorithm;
    }

    public boolean registerUser(String username, String password) {
        if (userDatabase.containsKey(username)) {
            return false; // Username already exists
        }

        byte[] salt = generateSalt();
        String hashedPassword = hashPassword(password, salt);

        userDatabase.put(username, new UserCredentials(salt, hashedPassword));
        return true;
    }

    public boolean authenticateUser(String username, String password) {
        UserCredentials credentials = userDatabase.get(username);
        if (credentials == null) {
            return false; // User doesn't exist
        }

        String attemptHash = hashPassword(password, credentials.getSalt());
        return attemptHash.equals(credentials.getPasswordHash());
    }

    private byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);
        return salt;
    }

    private String hashPassword(String password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(
                    password.toCharArray(),
                    salt,
                    ITERATIONS,
                    KEY_LENGTH
            );

            byte[] hash = keyFactory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("Error while hashing password", e);
        }
    }

    public boolean userExists(String username) {
        return userDatabase.containsKey(username);
    }

    public boolean removeUser(String username) {
        return userDatabase.remove(username) != null;
    }

    public int getUserCount() {
        return userDatabase.size();
    }
}

