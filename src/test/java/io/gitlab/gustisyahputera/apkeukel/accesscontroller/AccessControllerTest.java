package io.gitlab.gustisyahputera.apkeukel.accesscontroller;

import io.gitlab.gustisyahputera.apkeukel.entitymodel.FamilyMember;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class AccessControllerTest {

    @Mock private FamilyMember testMember = new FamilyMember();

    String password = "Spam Egg";
    String passkey = "b76c87d12d9ac7dc832d956b62886bce";  // it's the md5 digest of password


    @Test
    public void whenCreateAccessController() {

        AccessController newAccessController = new AccessController(testMember, password);
        newAccessController.verify();
    }

    @Test
    public void givenWrongPassword_whenIsVerified_thenFalse() {

        testMember.setPassKey(passkey + "wrong");

        /* Given */
        AccessController newAccessController = new AccessController(testMember, password);
        newAccessController.verify();

        /* When */
        Assert.assertFalse(newAccessController.isVerified());
    }

}
