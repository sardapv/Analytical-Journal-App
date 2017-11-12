package Diary;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

/**
 * Created by pranav on 01/11/17.
 */
public class LoginModelTest {
    @Test
    public void login() throws Exception {
        LoginModel loginModel = new LoginModel();
        String userid = "thor";
        String password = "odin";
        assertEquals(false, loginModel.login("",""));
        assertEquals(false, loginModel.login("",password));
        assertEquals(false, loginModel.login("thor", ""));
        assertEquals(true,loginModel.login("anuja","anuja"));
        //assertEquals(true,loginModel.login("minigeek","minigeek"));
        assertEquals(true,loginModel.login("thor","odin"));
        assertEquals(false,loginModel.login("anuja","anuje")); // FAIL condition
    }
}