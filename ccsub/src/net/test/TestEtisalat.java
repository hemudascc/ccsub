package net.test;



import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import javax.xml.bind.JAXBContext;

import net.mycomp.etisalat.EtisalatChargingCallback;
import net.mycomp.etisalat.EtisalatConstant;
import net.util.HttpURLConnectionUtil;
import net.util.MConstants;

public class TestEtisalat {


	
	public static void main(String[] args) throws Exception{
		HttpURLConnectionUtil httpURLConnectionUtil=new HttpURLConnectionUtil();
		JAXBContext jaxbContext = JAXBContext.newInstance(EtisalatChargingCallback.class);
		  String content = new String(Files.readAllBytes(Paths.get("C:\\Users\\mobitize\\Desktop\\temp\\08082018\\etisalat.txt")));
		 EtisalatChargingCallback etisalatChargingCallback=EtisalatConstant.parseXMLToJava(jaxbContext, content);
		
		 System.out.println(etisalatChargingCallback.toString());
		//System.out.println(SwaziConstants.parseDataSyncTime("20130723082551"));
		// int index=MConstants.random.nextInt(1);
		// System.out.println("index:: "+index);
		// 8xQOGSVh2oitvBOiBErLZKLZTTrab4CkFaxW5imzyy0=
		//System.out.println("index:: "+AESUtil.decrypt("cZ1hiRo6zLK1BTByC9iycNhMOsr9HzxedS4kW+iy8ME="));
				 
				 
				 
		 
	}
}
