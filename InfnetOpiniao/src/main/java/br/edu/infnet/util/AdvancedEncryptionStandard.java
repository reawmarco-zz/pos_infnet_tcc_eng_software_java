package br.edu.infnet.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
 
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import br.edu.infnet.exception.AdvancedEncryptionStandardException;

public class AdvancedEncryptionStandard {
	
	private static SecretKeySpec SECRET_KEY;
    private static byte[] KEY;
    private static String UTF_8 = "UTF-8";
    private static String SHA_1 = "SHA-1";
    private static String AES = "AES";
 
    private static void setKey(String myKey) throws AdvancedEncryptionStandardException {
        MessageDigest sha = null;
        try {
        	KEY = myKey.getBytes(UTF_8);
            sha = MessageDigest.getInstance(SHA_1);
            KEY = sha.digest(KEY);
            KEY = Arrays.copyOf(KEY, 16);
            SECRET_KEY = new SecretKeySpec(KEY, AES);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
        	e.printStackTrace();
            throw new AdvancedEncryptionStandardException(e);
        }
    }
 
    public static String encrypt(String strToEncrypt, String secret) throws AdvancedEncryptionStandardException {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY);
            String result = Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(UTF_8)));
            return URLEncoder.encode(result, UTF_8);
        } catch (Exception e) {
        	e.printStackTrace();
            throw new AdvancedEncryptionStandardException(e.getMessage());
        }
    }
 
    public static String decrypt(String strToDecrypt, String secret) throws AdvancedEncryptionStandardException {
        try {
            setKey(secret);
            strToDecrypt = URLDecoder.decode(strToDecrypt, UTF_8); 
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
        	e.printStackTrace();
            throw new AdvancedEncryptionStandardException(e.getMessage());
        }
    }
}

