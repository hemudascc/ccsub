package net.mycomp.comviva.ooredo.oman;

import java.lang.reflect.Field;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//@XmlRootElement(name="ocsResponse")
@XmlRootElement(name="ocsResponse")
@XmlAccessorType(XmlAccessType.NONE)
public class OCSResponse {

	// <?xml version="1.0" encoding="UTF-8"?><OcsRequest>
	//<code>1000</code><inError>false</inError><requestId />
	//<message>Successfully Processed</message></OcsRequest>

	@XmlElement(name="code")
	private String code;
	
	@XmlElement(name="errorCode")
	private String errorCode;
	
	
	
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



public String getErrorCode() {
	return errorCode;
}



public void setErrorCode(String errorCode) {
	this.errorCode = errorCode;
}



public String getCode() {
	return code;
}



public void setCode(String code) {
	this.code = code;
}


}
