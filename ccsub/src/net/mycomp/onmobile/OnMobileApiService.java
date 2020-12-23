package net.mycomp.onmobile;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

 

public class OnMobileApiService {

	

	 

	//// 16 bytes IV   

	 

	       private static String decrypt(String encrypted, String seed, String iv) throws Exception {
	              SecretKeySpec skey  = new SecretKeySpec(seed.getBytes("utf-8"), "AES");
	              Cipher dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	              dcipher.init(Cipher.DECRYPT_MODE, skey, new IvParameterSpec(iv.getBytes()));
	              byte[] clearbyte = dcipher.doFinal(DatatypeConverter.parseHexBinary(encrypted));
	              return new String(clearbyte);
	       }

	 

	       public static void main(String[] args) throws Exception {

	              String key = "colle12345colle1"; // 128 bit key
	              String cipher = "B64CBBCDF4A324C872029DA6C0449F37";
	              String iv = "OFRna73m*aze01xY";
	              String decipher = decrypt(cipher, key, iv);
	              System.out.println("Decrypted Msisdn : "+decipher);
	              
	      }

	
	
}
