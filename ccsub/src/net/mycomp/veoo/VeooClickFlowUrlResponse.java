package net.mycomp.veoo;

import java.lang.reflect.Field;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonAutoDetect(fieldVisibility=Visibility.ANY)
public class VeooClickFlowUrlResponse {

	
	@JsonProperty("status")
	private String status;
	@JsonProperty("result")
	private String result;
	@JsonProperty("type")
	private String type;
	@JsonProperty("special")
	private VeooClickFlowUrlSpecial special;
	
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

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public VeooClickFlowUrlSpecial getSpecial() {
		return special;
	}
	public void setSpecial(VeooClickFlowUrlSpecial special) {
		this.special = special;
	}

}
