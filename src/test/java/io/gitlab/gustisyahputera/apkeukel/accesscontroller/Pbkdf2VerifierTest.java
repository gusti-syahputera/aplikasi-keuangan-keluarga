package io.gitlab.gustisyahputera.apkeukel.accesscontroller;

import de.rtner.security.auth.spi.SimplePBKDF2;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class Pbkdf2VerifierTest {

    @Mock SimplePBKDF2 mockPbkdf2 = new SimplePBKDF2();

    @InjectMocks
    private Pbkdf2Verifier pbkdf2verifier = new Pbkdf2Verifier();

    private String password = "Spam Egg";
    private String passkey = "2096BC0DCE927792:1000:7902E30239A61B498829AE9D79170E0F895D21B1";
    private String wrongPassword = "Foo Bar";


    //region Setups
    //==========================================================================

    @Before
    public void mockSetUp() {
        when(mockPbkdf2.deriveKeyFormatted(password)).thenReturn(passkey);
        when(mockPbkdf2.verifyKeyFormatted(passkey, password)).thenReturn(true);
        when(mockPbkdf2.verifyKeyFormatted(passkey, wrongPassword)).thenReturn(false);
    }
    //endregion


    //region General tests
    //==========================================================================

    @Test
    public void whenDeriveKey() {

        /* When */
        String retreivedKey = pbkdf2verifier.deriveKey(password);
        verify(mockPbkdf2).deriveKeyFormatted(password);

        /* Then */
        Assert.assertEquals(passkey, retreivedKey);
    }


    @Test
    public void givenRightPassword_whenVerify_thenTrue() {

        /* When */
        boolean verified = pbkdf2verifier.verifyKey(passkey, password);
        verify(mockPbkdf2).verifyKeyFormatted(passkey, password);

        /* Then */
        Assert.assertTrue(verified);
    }

    @Test
    public void givenWrongPassword_whenVerify_thenTrue() {

        /* When */
        boolean verified = pbkdf2verifier.verifyKey(passkey, wrongPassword);
        verify(mockPbkdf2).verifyKeyFormatted(passkey, wrongPassword);

        /* Then */
        Assert.assertFalse(verified);
    }
    //endregion
}
