package net.test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

import net.util.JsonMapper;

public class InappTest {

	public static void main2(String[] args) {
		String json="{\"status\":true,\"trxId\":1021974989,\"error\":\"\"}";
		 Map map= JsonMapper.getJsonToObject(json, Map.class);
		 System.out.println("sendPin:::::::: "+map+" ,is true:: "+Objects.toString(map.get("status")).equals("true"));
		
	}
	
	public static void main(String[] args) throws Exception{
		 String content = new String(Files.readAllBytes(
				 Paths.get("C:\\Users\\mobitize\\Desktop\\temp\\07092019\\a.json")));
					 Map map= JsonMapper.getJsonToObject(content, Map.class);
		 System.out.println("sendPin:::::::: "+map+(
				 map!=null&&Objects.toString(((Map)map.get("mbSubApiResponseTO"))
						 .get("responseStatusCode")).equalsIgnoreCase("1")));


		
	}

}
