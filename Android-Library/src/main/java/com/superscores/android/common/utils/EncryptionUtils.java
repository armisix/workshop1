package com.superscores.android.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtils {

    // Shared secret key
    public static final byte[] KEY_BYTES = new byte[] { 0x78, 0x52, 0x54, 0x23, 0x39, 0x34, 0x48, 0x25, 0x35, 0x21, 0x46, 0x24, 0x6d, 0x57,
            0x6a, 0x4c };

    public static final byte[] KEY_BYTES_2 = new byte[] { 0x24, 0x5e, 0x54, 0x23, 0x39, 0x57, 0x44, 0x25, 0x6a, 0x21, 0x46, 0x34, 0x6d,
            0x4c, 0x0f, 0x19 };

    // Initialization Vector - usually a random data, stored along with the shared secret, or transmitted along with a message.
    // Not all the ciphers require IV - we use IV in this particular sample
    public static final byte[] IV_BYTES = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d,
            0x0e, 0x0f };
    public static final String PADDING = "PKCS5Padding"; // "ISO10126Padding", "PKCS5Padding", "ZeroBytePadding"

    // public static Cipher getAESCBCEncryptor(byte[] keyBytes, byte[] IVBytes, String padding) throws Exception {
    // SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
    // IvParameterSpec ivSpec = new IvParameterSpec(IVBytes);
    // Cipher cipher = Cipher.getInstance("AES/CBC/" + padding);
    // cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
    // return cipher;
    // }
    //
    // public static Cipher getAESCBCDecryptor(byte[] keyBytes, byte[] IVBytes, String padding) throws Exception {
    // SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
    // IvParameterSpec ivSpec = new IvParameterSpec(IVBytes);
    // Cipher cipher = Cipher.getInstance("AES/CBC/" + padding);
    // cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
    // return cipher;
    // }

    public static Cipher getAESECBEncryptor(byte[] keyBytes, String padding) throws Exception {
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/" + padding);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher;
    }

    public static Cipher getAESECBDecryptor(byte[] keyBytes, String padding) throws Exception {
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/" + padding);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher;
    }

    public static byte[] encrypt(Cipher cipher, byte[] dataBytes) throws Exception {
        ByteArrayInputStream bIn = new ByteArrayInputStream(dataBytes);
        CipherInputStream cIn = new CipherInputStream(bIn, cipher);
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        int ch;
        while ((ch = cIn.read()) >= 0) {
            bOut.write(ch);
        }
        cIn.close();

        return bOut.toByteArray();
    }

    public static byte[] decrypt(Cipher cipher, byte[] dataBytes) throws Exception {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        CipherOutputStream cOut = new CipherOutputStream(bOut, cipher);
        cOut.write(dataBytes);
        cOut.flush();
        cOut.close();
        return bOut.toByteArray();
    }

    // public static byte[] encryptWithIV(byte[] keyBytes, byte[] ivBytes, String sPadding, byte[] messageBytes) throws Exception {
    // Cipher cipher = getAESCBCEncryptor(keyBytes, ivBytes, sPadding);
    // return encrypt(cipher, messageBytes);
    // }
    //
    // public static byte[] decryptWithIV(byte[] keyBytes, byte[] ivBytes, String sPadding, byte[] encryptedMessageBytes) throws Exception {
    // Cipher decipher = getAESCBCDecryptor(keyBytes, ivBytes, sPadding);
    // return decrypt(decipher, encryptedMessageBytes);
    // }

    public static byte[] encrypt(byte[] keyBytes, String sPadding, byte[] messageBytes) throws Exception {
        Cipher cipher = getAESECBEncryptor(keyBytes, sPadding);
        return encrypt(cipher, messageBytes);
    }

    public static byte[] decrypt(byte[] keyBytes, String sPadding, byte[] encryptedMessageBytes) throws Exception {
        Cipher decipher = getAESECBDecryptor(keyBytes, sPadding);
        return decrypt(decipher, encryptedMessageBytes);
    }

    public static void example() throws Exception {
        // String sDemoMesage = "This is a demo message from Java!";
        // byte[] demoMesageBytes = sDemoMesage.getBytes();
        //
        // byte[] demo1EncryptedBytes = demo1encrypt(demoKeyBytes, demoIVBytes, sPadding, demoMesageBytes);
        // byte[] demo1DecryptedBytes = demo1decrypt(demoKeyBytes, demoIVBytes, sPadding, demo1EncryptedBytes);
        //
        // byte[] demo2EncryptedBytes = demo2encrypt(demoKeyBytes, sPadding, demoMesageBytes);
        // byte[] demo2DecryptedBytes = demo2decrypt(demoKeyBytes, sPadding, demo2EncryptedBytes);
    }
}
