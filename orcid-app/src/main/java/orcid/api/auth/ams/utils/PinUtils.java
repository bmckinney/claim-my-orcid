package orcid.api.auth.ams.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility methods for testing AWS/Encrypted HUL cookies
 */
public final class PinUtils {

    private PinUtils() {

    }

    // key is deterministic yet untraceable to HUID
    // generate key and store it for later
    public static String makeNetid(String huid, String secret)
    {
        String source = huid + secret;
        try
        {
            MessageDigest d = MessageDigest.getInstance("MD5");
            return bytesToHex(d.digest(source.getBytes()));
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new IllegalStateException("Configuration problem, should not get this error:"+e);
        }
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'}; // lowercase for DASH
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
