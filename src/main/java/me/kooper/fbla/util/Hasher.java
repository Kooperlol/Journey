package me.kooper.fbla.util;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {

    @Getter @Setter
    private String encryptedValue;
    @Getter @Setter
    private String originalValue;

    public Hasher(String input) throws NoSuchAlgorithmException {
        setOriginalValue(input);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] messageDigest = md.digest(input.getBytes());
        BigInteger bigInt = new BigInteger(1, messageDigest);
        setEncryptedValue(bigInt.toString(16));
    }

}
