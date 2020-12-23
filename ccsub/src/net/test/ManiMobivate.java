package net.test;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

public class ManiMobivate {

	public static void main(String[] args) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
		System.out.println("sdf:: "+sdf.format(new Timestamp(System.currentTimeMillis())));
		LocalDateTime localTime = LocalDateTime.now(ZoneId.of("GMT+02:00"));
		Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now(ZoneId.of("GMT+02:00")));
		
		System.out.println("localTime:: "+localTime);
		System.out.println("timestamp:: "+timestamp);
	}
}
