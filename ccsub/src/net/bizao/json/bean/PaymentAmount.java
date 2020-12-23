package net.bizao.json.bean;

import java.lang.reflect.Field;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PaymentAmount {

	private ChargingInformation chargingInformation;
	private ChargingMetaData chargingMetaData;
	
	private String totalAmountCharged;
	public PaymentAmount(){}
	public PaymentAmount(String amount,String currency,String description,
			String onBehalfOf){
		chargingInformation=new ChargingInformation( amount, currency, description);
		chargingMetaData=new ChargingMetaData(onBehalfOf);
	}
	
public String toString() {
		
        Field[] fields = this.getClass().getDeclaredFields();
        String str = this.getClass().getName();
        try {
            for (Field field : fields) {
                str += field.getName() + "=" + field.get(this) + ",";
            }
        } catch (IllegalArgumentException ex) {
            System.out.println(ex);
        } catch (IllegalAccessException ex) {
            System.out.println(ex);
        }
        return str;
    }

	public ChargingInformation getChargingInformation() {
		return chargingInformation;
	}
	public void setChargingInformation(ChargingInformation chargingInformation) {
		this.chargingInformation = chargingInformation;
	}
	public ChargingMetaData getChargingMetaData() {
		return chargingMetaData;
	}
	public void setChargingMetaData(ChargingMetaData chargingMetaData) {
		this.chargingMetaData = chargingMetaData;
	}

	public String getTotalAmountCharged() {
		return totalAmountCharged;
	}

	public void setTotalAmountCharged(String totalAmountCharged) {
		this.totalAmountCharged = totalAmountCharged;
	}
	
	
	
}
