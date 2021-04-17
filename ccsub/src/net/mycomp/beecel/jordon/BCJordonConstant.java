package net.mycomp.beecel.jordon;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

public interface BCJordonConstant {

	 static final Logger logger = Logger
				.getLogger(BCJordonConstant.class.getName());

	 public static AtomicInteger moMessageIdAtomicInteger=new AtomicInteger(0);
	 public static AtomicInteger mtMessageIdAtomicInteger=new AtomicInteger(0);
	 public static final DateFormat yyyyMMddHHmmssAccessToken=new SimpleDateFormat("yyyyMMddHHmmss");
		
	  public static final String PUB = "COLLECTCENT";
	  public static final String SUBSCRIBE = "Subscribe";
	  public static final String UNSUBSCRIBE = "Unsubscribe";
	  public static final String ACTIVATE = "Activate";
	  public static final String DEACTIVATE = "Deactivate";
	  public static final String RENEW = "Renew";
	  public static final String OPERATOR_ORANGE = "Orange";
	  public static final String CG_CALLBACK_CAHCHE_PREFIX = "CG_CALLBACK_CAHCHE_PREFIX";
	  public static final int ORANGECAMPAIGNID = 286;
	  public static final int UMNIAHCAMPAIGNID = 287;
	  public static final int ORANGESERVICEID = 122;
	  public static final int UMNIAHSERVICEID = 123;
	  public static final String CONTENT_MESSAGE = "Thanks for subscribing to KidoKingdom to access the content please click this <portalurl>";
	  public static final String CG_URL = "http://mobibees.com/index.php?r=view&t=<t>&cid=<cid>&pub=<pub>"; 
	  public static final String MT_URL = "http://api.beecell.com/index.php?r=api/SendMsg&msisdn=<msisdn>&shortCode=<shortcode>&msg=<msg>&opCode=<opcode>&countryCode=<countrycode>&lang=1&service=<service>"; 
	  public static Map<Integer,BCJordonConfig> mapIdToBCJordonConfig=new HashMap<Integer,BCJordonConfig>();
		 
	  public static final List<BCJordonConfig> listBCJordonConfig=new ArrayList<BCJordonConfig>();
	  public static Map<Integer,BCJordonConfig> mapServiceIdToBCJordonConfig=new HashMap<Integer,BCJordonConfig>();
	  
	  public static Timestamp getFormatUTC8Date(){			
			ZonedDateTime now = ZonedDateTime.now(ZoneOffset.of("+08:00"));		
			return Timestamp.valueOf(now.toLocalDateTime());
		}
}
