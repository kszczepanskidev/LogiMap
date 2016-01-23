package hkp.logimap;

import android.util.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 * Created by krysztal on 04.01.16.
 */
public class Encryptor {
    private static final char[] PASSWORD = "poznanuniversityoftechnology2016".toCharArray();
    private static final byte[] SALT = {
            (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
            (byte) 0x13, (byte) 0xff, (byte) 0x02, (byte) 0x57,
    };
    private static final String algorithm = "PBEWithMD5AndDES";

    String encrypt(String data) throws GeneralSecurityException, UnsupportedEncodingException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));

        return Base64.encodeToString(cipher.doFinal(data.getBytes("UTF-8")), Base64.NO_WRAP);
    }

    String decrypt(String data) throws GeneralSecurityException, IOException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, 20));

        return new String(cipher.doFinal(Base64.decode(data, Base64.NO_WRAP)), "UTF-8");
    }
}
