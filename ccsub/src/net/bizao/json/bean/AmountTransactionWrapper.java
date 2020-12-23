package net.bizao.json.bean;

import java.lang.reflect.Field;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AmountTransactionWrapper {

	private AmountTransaction amountTransaction;

	public AmountTransactionWrapper(){}
	public AmountTransactionWrapper(String amount,String currency,String description,
			String onBehalfOf,String referenceCode,
			String clientCorrelator){
		amountTransaction=new AmountTransaction( amount, currency, description,
				 onBehalfOf, referenceCode,
				 clientCorrelator);
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

	public AmountTransaction getAmountTransaction() {
		return amountTransaction;
	}

	public void setAmountTransaction(AmountTransaction amountTransaction) {
		this.amountTransaction = amountTransaction;
	}
}
