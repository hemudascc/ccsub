package net.test;

import java.net.URLEncoder;
import java.util.Base64;

import org.mortbay.util.UrlEncoded;

public class MainOoredo {

	public static void main(String[] args) throws Exception{
	//String query=	new String(Base64.getDecoder().decode("YWRpZD00NSZjbXBpZD0xMzImdG9rZW49JTNDdG9rZW4lM0U%3d"));
	//System.out.print(query);
	//http://13.232.180.113/timwe/2/decryptpmsisdn.php?encmsisdn=FXRKZrIY+UNd4NzUSSbNlA==
		System.out.print(URLEncoder.encode("FXRKZrIY+UNd4NzUSSbNlA==", "utf-8"));
	}

}
