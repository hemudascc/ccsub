package net.mycomp.veoo;

public class VeooCGToken {
	
	private int tokenId;
	
	
	public VeooCGToken(int tokenId){
	
		this.tokenId=tokenId;
		
	}
	
	public String getCGToken(){
		 return  String.valueOf(tokenId);
	}
	
	public VeooCGToken(String str){
		try{
			
			this.tokenId=Integer.parseInt(str);
						
		}catch(Exception ex){
			
	
			
		}
	}

	
	public int getTokenId() {
		return tokenId;
	}

	public void setTokenId(int tokenId) {
		this.tokenId = tokenId;
	}
}
