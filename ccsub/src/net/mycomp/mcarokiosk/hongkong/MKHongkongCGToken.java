package net.mycomp.mcarokiosk.hongkong;

import net.util.MConstants;

public class MKHongkongCGToken {
	private int tokenId;
	private int campaignId;

	public MKHongkongCGToken(int tokenId, int campaignId) {
		this.tokenId = tokenId;
		this.campaignId = campaignId;
	}

	public MKHongkongCGToken() {
	}
	public String getCGToken() {
		return tokenId + MConstants.TOKEN_SEPERATOR + campaignId;
	}

	public MKHongkongCGToken(String str) {
		try {
			String token[] = str.split(MConstants.TOKEN_SEPERATOR);
			// this.time=Long.parseLong(token[0]);
			this.tokenId = Integer.parseInt(token[0]);
			this.campaignId = Integer.parseInt(token[1]);
		
		} catch (Exception ex) {
			campaignId = MConstants.DEFAULT_ADNETWORK_CAMPAIGN_ID;
			try {
			} catch (Exception e) {
			}

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
