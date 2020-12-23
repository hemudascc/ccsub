package net.bizao.json.bean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonInclude(Include.NON_NULL)
@JsonRootName("challenge")
public class Challenge {
	private String method;
	private String country;
	private String service;
	private String partnerId;
	@JsonProperty(value="inputs")
	private List<Inputs> inputs;
	@JsonProperty(value="result")
	private List<Inputs> result;
	
	public Challenge(){
		inputs=new ArrayList<Inputs>();
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

	public void addInputs(String type,String value){
		inputs.add(new Inputs(type, value));
	}
public static class Inputs{
	
	private String type ;
	private String value ;
	public Inputs(){}
	public Inputs(String type,String value){
		this.type=type;
		this.value=value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}

public String getMethod() {
	return method;
}

public void setMethod(String method) {
	this.method = method;
}

public String getCountry() {
	return country;
}

public void setCountry(String country) {
	this.country = country;
}

public String getService() {
	return service;
}

public void setService(String service) {
	this.service = service;
}

public String getPartnerId() {
	return partnerId;
}

public void setPartnerId(String partnerId) {
	this.partnerId = partnerId;
}

public List<Inputs> getInputs() {
	return inputs;
}

public void setInputs(List<Inputs> inputs) {
	this.inputs = inputs;
}
public List<Inputs> getResult() {
	return result;
}
public void setResult(List<Inputs> result) {
	this.result = result;
}
}
