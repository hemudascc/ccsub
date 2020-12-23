package net.test;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import net.util.MUtility;

public class TestDayDiff {

	public  static void main (String arg[]){
		LocalDateTime l1 =  LocalDateTime.now();     //Today
		LocalDateTime l2 = LocalDateTime.now();     //Plus 1 day
		l2=l2.plusDays(1);
		Timestamp ts1 = Timestamp.valueOf(l1);
		Timestamp ts2 = Timestamp.valueOf(l2);
		System.out.println("ts1:: "+ts1+" , ts2: "+ts2);
		System.out.println("daydiff:: "+MUtility.noOfDaysDiffrence(ts1, ts2));
	}
}
