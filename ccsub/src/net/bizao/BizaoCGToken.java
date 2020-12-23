package net.bizao;

import net.process.bean.CGToken;
import net.util.MConstants;

public class BizaoCGToken {

	
	private long time;
	private int tokenId;
	private int configId;
	private int adnetworkId;
	private  int campaignId;

	public BizaoCGToken(long time,int tokenId,int campaignId,int adnetworkId,Integer configId){
		//this.id=id;
		this.time=time;
		this.tokenId=tokenId;
		this.campaignId=campaignId;	
		this.adnetworkId=adnetworkId;	
		this.configId=configId;
		
	}
	
	public BizaoCGToken(String str){
		try{
			String s[]=str.split(MConstants.TOKEN_SEPERATOR);
			
			this.time=Long.parseLong(s[0]);
			this.tokenId=Integer.parseInt(s[1]);
			this.campaignId=Integer.parseInt(s[2]);
			this.adnetworkId=Integer.parseInt(s[3]);
			this.configId=Integer.parseInt(s[4]);
						
		}catch(Exception ex){
			
		}
	}

	public String getBizaoCGToken(){
		///long time,int tokenId,int campaignId,int adnetworkId,Integer configId
		 return time + MConstants.TOKEN_SEPERATOR + tokenId+MConstants.TOKEN_SEPERATOR+campaignId+
				 MConstants.TOKEN_SEPERATOR+adnetworkId+ MConstants.TOKEN_SEPERATOR+
				configId;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getTokenId() {
		return tokenId;
	}

	public void setTokenId(int tokenId) {
		this.tokenId = tokenId;
	}

	public int getConfigId() {
		return configId;
	}

	public void setConfigId(int configId) {
		this.configId = configId;
	}

	public int getAdnetworkId() {
		return adnetworkId;
	}

	public void setAdnetworkId(int adnetworkId) {
		this.adnetworkId = adnetworkId;
	}

	public int getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(int campaignId) {
		this.campaignId = campaignId;
	}
	
	
	

}
