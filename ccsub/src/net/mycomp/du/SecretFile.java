package net.mycomp.du;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.annotation.Resource;

import org.springframework.core.io.ClassPathResource;

public class SecretFile {

	public static PublicKey publicKey;
	public static PrivateKey privateKey;
	
	public SecretFile(String filename){
		try {
//			/readPublicKey(filename);		
		    readPrivateKey(filename);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	public  byte[] readFileBytes(String filename) throws IOException
	{
		URL resource;
		try {
			Class clazz = SecretFile.class;
		    File pathFile = new File(clazz.getResource(filename).getFile());
		    
//			/resource = Class.forName(this.getClass().getName()).getResource(filename);
			Path path = Paths.get("F:\\home\\keyFile.jks");
			 return Files.readAllBytes(path);        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	   
	}

	public  void readPublicKey(String filename) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException
	{
	    X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(readFileBytes(filename));
	    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	     publicKey= keyFactory.generatePublic(publicSpec);       
	}

	public  void readPrivateKey(String filename) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException
	{
	    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(readFileBytes(filename));
	    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	    privateKey= keyFactory.generatePrivate(keySpec);     
	}
	
}
