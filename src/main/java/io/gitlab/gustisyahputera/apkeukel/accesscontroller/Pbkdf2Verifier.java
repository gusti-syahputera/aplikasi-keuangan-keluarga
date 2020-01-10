package io.gitlab.gustisyahputera.apkeukel.accesscontroller;

import com.google.inject.Inject;
import de.rtner.security.auth.spi.SimplePBKDF2;


public class Pbkdf2Verifier implements Verifier {

    @Inject SimplePBKDF2 pbkdf2;

    public String deriveKey(String password) {
        return pbkdf2.deriveKeyFormatted(password);
    }

    public boolean verifyKey(String passkey, String password) {
        return pbkdf2.verifyKeyFormatted(passkey, password);
    }
}
