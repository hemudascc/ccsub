package net.indonesia.triyakom;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import net.util.HttpURLConnectionUtil;

public class Test {

	
	@PostConstruct
	public static void main(String arg[]){
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(PushResponse.class);
			 PushResponse pushResponse=(PushResponse)jaxbContext.createUnmarshaller().unmarshal(
						new ByteArrayInputStream("<push-response><tid>DB9107C1AEBD45628FA28E1AFEDA10EB</tid><status-id>3003</status-id></push-response>".
								getBytes(StandardCharsets.UTF_8.name())));
				
			 System.out.println("pushResponse:: "+pushResponse);
		} catch (Exception e) {
			
		}
	}
	
	
	
	
}
