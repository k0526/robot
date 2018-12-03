package com.playcrab.core.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
  
public class AES { 
  
 

	private static String ivParameter = "0000000000000000";

	public static byte[] encrypt(byte[] sSrcBytes, String aesKey) throws Exception {
		byte[] raw = aesKey.getBytes("utf-8");
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes("utf-8"));
		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		int paddingSize = 16;
		int mod16 = sSrcBytes.length % 16;
		byte[] afterPaddingData;
		if (mod16 != 0) {
			paddingSize = 16 - mod16;
		}
		byte[] padding = new byte[paddingSize];
		for (int i = 0; i < paddingSize; i++) {
			padding[i] = (byte) paddingSize;
		}
		afterPaddingData = contactBytes(sSrcBytes, padding);

		byte[] rangIv = ivParameter.getBytes("utf-8");
		byte[] enced = cipher.doFinal(afterPaddingData);
		byte[] finalData = contactBytes(rangIv, enced);
		return finalData;
	}

	public static String decrypt(String context,String aesKey) {
		try {
			byte[] decodeBase64 = Base64.decodeBase64(context);
			return new String(decrypt(decodeBase64, aesKey));
		} catch (Exception e) {
			return null;
		}
	}
	
	public static byte[] decrypt(byte[] sSrcBytes, String aesKey) {
		try {
			byte[] raw = aesKey.getBytes();
			byte[] randiv = catBytes(sSrcBytes, 0, 16);
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			IvParameterSpec iv = new IvParameterSpec(randiv);
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			int len = sSrcBytes.length;
			byte[] catBytes = catBytes(sSrcBytes, 16, len - 16);
			byte[] decrypt = cipher.doFinal(catBytes);
			int paddingLen = decrypt[decrypt.length - 1];
			return catBytes(decrypt, 0, decrypt.length - paddingLen);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] contactBytes(byte[] buf1, byte[] buf2) {
		int buf1Len = buf1.length;
		int buf2Len = buf2.length;
		if (buf1Len == 0) {
			return buf2;
		}
		if (buf2Len == 0) {
			return buf1;
		}
		byte[] finalData = new byte[buf1Len + buf2Len];
		System.arraycopy(buf1, 0, finalData, 0, buf1Len);
		System.arraycopy(buf2, 0, finalData, buf1Len, buf2Len);
		return finalData;
	}

	public static byte[] catBytes(byte[] buf, int start, int len) {
		byte[] bs = new byte[len];
		for (int i = start; i < start + len; i++) {
			bs[i - start] = buf[i];
		}
		return bs;
	}
	
	public static void main(String[] args) throws Exception {
		String key="461769fef78448dd8c8513b9f4b61d4b";
//		String key="ilo24wEFS*^^*2Ewilo24wEFS*^^*2Ew";
		String context="AjAwMDAwMDAwMDAwMDAwMDCjJE5nYriYNrh4a2QCXXVzd/XfUp2i3VYUA9Bsld6wLTUKztYlmIMSs+5ne3qmxaQ82A57qw8DjmbPiorrdSA43a554BmQLdEzxZxtSWuWHmPR+pc9sZK2+7HnSsJINj0HnyhlWq18wTjFfGdjAY/B";
		byte[] decodeBase64 = Base64.decodeBase64(context);
		byte[] catBytes = catBytes(decodeBase64, 1, decodeBase64.length-1);
		byte[] decrypt = decrypt(catBytes, key);
		String string = new String(decrypt);
		
	}
}