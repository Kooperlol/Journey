package me.kooper.fbla.util;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Getter @Setter
public class HashUtil {

    private String encryptedValue, originalValue;

    public HashUtil(String input) throws NoSuchAlgorithmException {
        setOriginalValue(input);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] messageDigest = md.digest(input.getBytes());
        BigInteger bigInt = new BigInteger(1, messageDigest);
        setEncryptedValue(bigInt.toString(16));
    }

}
