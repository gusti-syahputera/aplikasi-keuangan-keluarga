package io.gitlab.gustisyahputera.apkeukel.accesscontroller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.security.MessageDigest;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class MessageDigestVerifierTest {

    @Mock MessageDigest mockMd;

    @InjectMocks
    private MessageDigestVerifier messageDigestVerifier = new MessageDigestVerifier();

    private String password = "Spam Egg";
    private String passkey = "b76c87d12d9ac7dc832d956b62886bce";  // it's the md5 digest of password
    private byte[] passworddDigest = {-73, 108, -121, -47, 45, -102, -57, -36, -125, 45, -107, 107, 98, -120, 107, -50};

    private String wrongPassword = "Foo Bar";
    private byte[] wrongPassworddDigest = {9, 73, 40, -56, 14, -113, -19, 103, 60, -18, -55, 119, -31, 112, -26, 75};


    //region Setups
    //==========================================================================

    @Before
    public void mockSetUp() {
        when(mockMd.digest(password.getBytes())).thenReturn(passworddDigest);
        when(mockMd.digest(wrongPassword.getBytes())).thenReturn(wrongPassworddDigest);
    }
    //endregion


    //region General tests
    //==========================================================================

    @Test
    public void whenDeriveKey() {

        /* When */
        String retreivedKey = messageDigestVerifier.deriveKey(password);
        verify(mockMd).digest(password.getBytes());

        /* Then */
        Assert.assertEquals(passkey, retreivedKey);
    }

    @Test
    public void givenRightPassword_whenVerify_thenTrue() {

        /* When */
        boolean verified = messageDigestVerifier.verifyKey(passkey, password);
        verify(mockMd).digest(password.getBytes());

        /* Then */
        Assert.assertTrue(verified);
    }

    @Test
    public void givenWrongPassword_whenVerify_thenFalse() {

        /* When */
        boolean verified = messageDigestVerifier.verifyKey(passkey, wrongPassword);
        verify(mockMd).digest(wrongPassword.getBytes());

        /* Then */
        Assert.assertFalse(verified);
    }
    //endregion

}
