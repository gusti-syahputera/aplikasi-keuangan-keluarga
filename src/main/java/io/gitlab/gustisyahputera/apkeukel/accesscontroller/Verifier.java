package io.gitlab.gustisyahputera.apkeukel.accesscontroller;


public interface Verifier {

    String deriveKey(String password);
    boolean verifyKey(String passkey, String password);
}
