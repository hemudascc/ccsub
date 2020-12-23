package net.mycomp.comviva.ooredo.oman;

import java.io.Serializable;
import java.lang.reflect.Field;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ocsRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class OCSRequest implements Serializable{

	
	private static final long serialVersionUID = 1L;

	@XmlElement(name="serviceNode")
	
	private String serviceNode;
	@XmlElement(name="sequenceNo")
	
	private String sequenceNo;
	@XmlElement(name="callingParty")
	
	private String callingParty;

	@XmlElement(name="serviceId")	
	private String serviceId;
	
	@XmlElement(name="bearerId")
	
	private String bearerId;

	@XmlElement(name="planId")
	
	private String planId;
	
	@XmlElement(name="shortcode")
	
	private String shortcode;
	@XmlElement(name="keyword")
	
	private String keyword;
	@XmlElement(name="otp")
	
	private String otp;
	
	@XmlElement(name="contentName")
	private String contentName;
	
	@XmlElement(name="startTime")
	private String startTime;
	
	
	
	public OCSRequest(){
		
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

public String getServiceNode() {
	return serviceNode;
}

public void setServiceNode(String serviceNode) {
	this.serviceNode = serviceNode;
}

public String getSequenceNo() {
	return sequenceNo;
}

public void setSequenceNo(String sequenceNo) {
	this.sequenceNo = sequenceNo;
}

public String getCallingParty() {
	return callingParty;
}

public void setCallingParty(String callingParty) {
	this.callingParty = callingParty;
}

public String getServiceId() {
	return serviceId;
}

public void setServiceId(String serviceId) {
	this.serviceId = serviceId;
}

public String getBearerId() {
	return bearerId;
}

public void setBearerId(String bearerId) {
	this.bearerId = bearerId;
}

public String getPlanId() {
	return planId;
}

public void setPlanId(String planId) {
	this.planId = planId;
}

public String getShortcode() {
	return shortcode;
}

public void setShortcode(String shortcode) {
	this.shortcode = shortcode;
}

public String getKeyword() {
	return keyword;
}

public void setKeyword(String keyword) {
	this.keyword = keyword;
}

public String getOtp() {
	return otp;
}

public void setOtp(String otp) {
	this.otp = otp;
}

public String getContentName() {
	return contentName;
}

public void setContentName(String contentName) {
	this.contentName = contentName;
}

public String getStartTime() {
	return startTime;
}

public void setStartTime(String startTime) {
	this.startTime = startTime;
}

	
}
