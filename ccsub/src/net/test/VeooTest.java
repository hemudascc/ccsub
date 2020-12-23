package net.test;

import java.nio.file.Files;
import java.nio.file.Paths;

import net.mycomp.veoo.VeooClickFlowUrlResponse;
import net.util.JsonMapper;

public class VeooTest {

	public static void main(String arg[]) throws Exception{
		 String content = new String(Files.readAllBytes(Paths.get("C:\\Users\\mobitize\\Desktop\\collectcent\\Veoo\\sample_veoo_tn_subscriber.txt")));
		 VeooClickFlowUrlResponse cgSubscribersResponse=JsonMapper.getJsonToObject(content, VeooClickFlowUrlResponse.class);
		 System.out.println(cgSubscribersResponse);
	}
}
