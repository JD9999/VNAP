package VNAP;

import java.math.BigInteger;
import java.security.MessageDigest;

public class Hash {

    private String hashedPassword = new String();
    private MessageDigest md;

    public Hash(String password) {
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes(), 0, password.length());
            hashedPassword = new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getHashedPassword() {
        return hashedPassword;
    }
}
