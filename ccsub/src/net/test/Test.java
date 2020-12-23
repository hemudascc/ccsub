package net.test;

import java.security.MessageDigest;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicIntegerArray;

import javax.xml.bind.DatatypeConverter;

import net.persist.bean.AdnetworkOperatorConfig;
import net.persist.bean.AdnetworkToken;
import net.util.MUtility;

public class Test {

	private static String calculateMD5(Integer id){
		
		 try { 
			 String token="c0llectcent"+"@"+"co!!ectcent"+"@"+id;
	            // Static getInstance method is called with hashing MD5 
	            MessageDigest md = MessageDigest.getInstance("MD5"); 
	  
	            // digest() method is called to calculate message digest 
	            //  of an input digest() return array of byte 
	            byte[] messageDigest = md.digest(token.getBytes()); 
	            String myHash = DatatypeConverter
	            	      .printHexBinary(messageDigest);
	            return myHash;
	           
	        }catch(Exception ex){
	        	ex.printStackTrace();
	        } 
		 return null;
	}
	
	public static void main2(String[] args) {
		String str="SirenBox: Descarga las mejores fotos, videos y tips de bellezas de modelos aqui http://mob.ccd2c.com/SirenBox/VN/home?msisdn=50495365664";
		System.out.println(str.length());
		System.out.println((Objects.toString(null))==null);
		String s="pin_sent|ay7FXicnG8sURfxjJvqtmYg6Q%2BZSAchSr3DmG3ytQxs%3D";
		String arr[]=s.split("\\|");
		System.out.println(arr[1]);
		Integer mnc=MUtility.toInt("03",0);
		System.out.println("mnc "+mnc);

	
	}
	
	
	
	public static AtomicIntegerArray atomicActSkipNumber=new AtomicIntegerArray(3);
	
	public  static boolean   isSendActMoreThanZeroPricePointAdnetworkCallBack(
			) {		
		boolean isSendToAdnetwork=!(atomicActSkipNumber.
				 getAndUpdate(2, n->n>=atomicActSkipNumber.get(1)?1:n+1)
				 <=atomicActSkipNumber.get(0));
		return isSendToAdnetwork;
	}

	public static void main22(String arg[]){
		
			int gcd=MUtility.gcd(0, 100);
			atomicActSkipNumber.set(0, 0/gcd);
			atomicActSkipNumber.set(1, 100/gcd);
			atomicActSkipNumber.set(2,1);
			System.out.println("send to adnetwork "+isSendActMoreThanZeroPricePointAdnetworkCallBack());
	}
	
	
	public static void main(String arg[]){
		
	
		System.out.println("send to adnetwork "+calculateMD5(7));
}
	
}
 