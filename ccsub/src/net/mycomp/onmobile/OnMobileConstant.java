package net.mycomp.onmobile;

import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class OnMobileConstant {

	public static Map<Integer,OnMobileServiceConfig> mapServiceIdToOnMobileServiceConfig
	=new HashMap<Integer,OnMobileServiceConfig>();
	
	public static Map<String,OnMobileServiceConfig> mapSrvkeyToOnMobileServiceConfig
	=new HashMap<String,OnMobileServiceConfig>();
	
	private static final String encryptionKey = "colle12345colle1";
	private static final String initialVector = "OFRna73m*aze01xY";
	
	public static String decrypt(String encryptedMsisdn)
			throws Exception {
		SecretKeySpec skey = new SecretKeySpec(encryptionKey.getBytes("utf-8"), "AES");
		Cipher dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		dcipher.init(Cipher.DECRYPT_MODE, skey, new IvParameterSpec(initialVector.getBytes()));
		byte[] clearbyte = dcipher.doFinal(DatatypeConverter
				.parseHexBinary(encryptedMsisdn));
		return new String(clearbyte);
	}
	public static void main(String[] args) throws Exception {
		String encryptedMsisdn = "B64CBBCDF4A324C872029DA6C0449F37";
		String msisdn = OnMobileConstant.decrypt(encryptedMsisdn);
		System.out.println("Decrypted Msisdn : "+msisdn);
	}
	
}
