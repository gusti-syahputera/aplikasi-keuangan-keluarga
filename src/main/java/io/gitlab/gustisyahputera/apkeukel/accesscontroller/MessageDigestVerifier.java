package io.gitlab.gustisyahputera.apkeukel.accesscontroller;

import com.google.inject.Inject;

import java.security.MessageDigest;
import java.util.stream.IntStream;


public class MessageDigestVerifier implements Verifier {

    @Inject MessageDigest md;

    public boolean verifyKey(String passkey, String password) {
        String derivedKey = deriveKey(password);
        return passkey.equals(derivedKey);
    }

    public String deriveKey(String password) {

        /* Get digest of password */
        byte[] digest = md.digest(password.getBytes());

        /* Encode digest bytes as hex string */
        return IntStream
                .range(0, digest.length)
                .mapToObj(i -> String.format("%02x", digest[i]))
                .reduce((a, b) -> a + b)
                .get();
    }
}
