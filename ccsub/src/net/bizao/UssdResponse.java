package net.bizao;

import net.util.MConstants;

public class UssdResponse {

	private boolean success;
	private String  action="ERROR";
	private double amountCharged;
	private String info;
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public double getAmountCharged() {
		return amountCharged;
	}
	public void setAmountCharged(double amountCharged) {
		this.amountCharged = amountCharged;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
}
