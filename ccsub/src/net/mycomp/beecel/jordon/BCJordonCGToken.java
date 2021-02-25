package net.mycomp.beecel.jordon;

import net.util.MConstants;

public class BCJordonCGToken {


	private int tokenId;
	private int campaignId;
	
	public BCJordonCGToken(int tokenId,int campaignId){
		//this.time=time;
		this.tokenId=tokenId;
		this.campaignId=campaignId;
	}
	
	public String getCGToken(){
		 return   tokenId + MConstants.TOKEN_SEPERATOR + 
				campaignId;
	}
	
	public BCJordonCGToken(String str){
		try{
			String token[]=str.split(MConstants.TOKEN_SEPERATOR);
			//this.time=Long.parseLong(token[0]);
			this.tokenId=Integer.parseInt(token[0]);
			this.campaignId=Integer.parseInt(token[1]);			
		}catch(Exception ex){
			campaignId=MConstants.DEFAULT_ADNETWORK_CAMPAIGN_ID;
			
			
		}
	}

	public int getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(int campaignId) {
		this.campaignId = campaignId;
	}

	public int getTokenId() {
		return tokenId;
	}

	public void setTokenId(int tokenId) {
		this.tokenId = tokenId;
	}
}
