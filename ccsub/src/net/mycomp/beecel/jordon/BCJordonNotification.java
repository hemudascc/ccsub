package net.mycomp.beecel.jordon;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_bc_jordon_notification")
public class BCJordonNotification  implements Serializable{

	@GeneratedValue
	@Id
	private int id;
	@Column(name = "sid")
	private String sid;  
	@Column(name = "msisdn")
	private String msisdn;
	@Column(name = "notification_type")
	private String notificationtype;
	@Column(name = "token")
	private String token;
	@Column(name = "query_str")
	private String queryStr;
	@Column(name = "send_to_adnetwork")
	private Boolean sendToAdnetwork;
	@Column(name = "create_time")
	private Timestamp createTime;
	@Column(name="renew")
	private boolean renew;
	@Column(name="mo")
	private String mo;
	
	private boolean status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getNotificationtype() {
		return notificationtype;
	}

	public void setNotificationtype(String notificationtype) {
		this.notificationtype = notificationtype;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getQueryStr() {
		return queryStr;
	}

	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}

	public Boolean getSendToAdnetwork() {
		return sendToAdnetwork;
	}

	public void setSendToAdnetwork(Boolean sendToAdnetwork) {
		this.sendToAdnetwork = sendToAdnetwork;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	
	public boolean isRenew() {
		return renew;
	}

	public void setRenew(boolean renew) {
		this.renew = renew;
	}

	public String isMo() {
		return mo;
	}

	public void setMo(String mo) {
		this.mo = mo;
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

}
