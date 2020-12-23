package net.mycomp.worldplay;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_worldplay_cgcallback")
public class WorldplayCGCallback implements Serializable{

	@Id
	@GeneratedValue
	private Integer id;
	@Column(name = "worldplay_service_id")
	private String worldPlayServiceId;
	
	@Column(name = "action")
	private String action;
	
	@Column(name = "mt")
	private String mt;
	@Column(name = "client")
	private String client;
	
	@Column(name = "operator")
	private String operator;
	
	@Column(name = "tel_no")
	private String telNo;
	
	@Column(name = "request_id")
	private String requestId;
	@Column(name = "sub_date")
	private String subDate;
	@Column(name = "ad_tracking")
	private String adTracking;
	@Column(name = "query_str")
	private String queryStr;
	@Column(name = "create_time")
	private Timestamp createTime;
	@Column(name = "status")
	private Boolean status;
	
	public WorldplayCGCallback(){}
	public WorldplayCGCallback(boolean status){
	this.status=status;
	this.createTime=new Timestamp(System.currentTimeMillis());
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

	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getWorldPlayServiceId() {
		return worldPlayServiceId;
	}
	public void setWorldPlayServiceId(String worldPlayServiceId) {
		this.worldPlayServiceId = worldPlayServiceId;
	}
	public String getQueryStr() {
		return queryStr;
	}
	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getMt() {
		return mt;
	}
	public void setMt(String mt) {
		this.mt = mt;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getSubDate() {
		return subDate;
	}
	public void setSubDate(String subDate) {
		this.subDate = subDate;
	}
	public String getAdTracking() {
		return adTracking;
	}
	public void setAdTracking(String adTracking) {
		this.adTracking = adTracking;
	}
	public String getTelNo() {
		return telNo;
	}
	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
}
