package net.test;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Hex;

public class Mt2UAEMain {

	
	public static void main2(String arg[]){
		
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		String data="SENDOTP1.2WAP91122S9EN11,22430029715689672342.201.311223323KK!NGD0M";
		String enc=getSha1Hash(data);
		System.out.println("enc::: "+enc);
		String expected="990d12055725d8ac9cad9485cfca89257f94d0509bcddd8a779f960f4f41f22a";
		System.out.println("expected ::: "+expected);
		System.out.println("equal ::: "+expected.equals(enc));
	}
	
	 public static byte[] getSHA(String input) throws NoSuchAlgorithmException 
	    {  
	        // Static getInstance method is called with hashing SHA  
	        MessageDigest md = MessageDigest.getInstance("SHA-256");  
	  
	        // digest() method called  
	        // to calculate message digest of an input  
	        // and return array of byte 
	        return md.digest(input.getBytes(StandardCharsets.UTF_8));  
	    } 
	    
	    public static String toHexString(byte[] hash) 
	    { 
	        // Convert byte array into signum representation  
	        BigInteger number = new BigInteger(1, hash);  
	  
	        // Convert message digest into hex value  
	        StringBuilder hexString = new StringBuilder(number.toString(16));  
	  
	        // Pad with leading zeros 
	        while (hexString.length() < 32)  
	        {  
	            hexString.insert(0, '0');  
	        }  
	  
	        return hexString.toString();  
	    } 
	    
	public static String getSha1Hash(String data){
	try{
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(data.getBytes("UTF-8"));
		return Hex.encodeHexString(md.digest());
	}catch(Exception ex){
		System.out.println("getSha1Hash "+ex);
	}
	return null;
	}

}
