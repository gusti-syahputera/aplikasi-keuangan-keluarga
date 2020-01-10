package io.gitlab.gustisyahputera.apkeukel.accesscontroller;

import com.google.inject.Inject;

import java.security.MessageDigest;
import java.util.stream.IntStream;


public class Md5Verifier implements Verifier {

    @Inject MessageDigest md5;

    public boolean verifyKey(String passkey, String password) {
        String derivedKey = deriveKey(password);
        return passkey.equals(derivedKey);
    }

    public String deriveKey(String password) {

        /* Get digest */
        byte[] digest = md5.digest(password.getBytes());

        /* Encode digest bytes to hex string */
        return IntStream
                .range(0, digest.length)
                .mapToObj(i -> String.format("%02x", digest[i]))
                .reduce((a, b) -> a + b)
                .get();
    }
}
