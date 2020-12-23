package net.mycomp.mt2.ksa;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_mt2_ksa_notification")
public class Mt2KSANotification implements Serializable{
	// ID=517958967&MO=f04900012256365&MT=791113&DATA=R,430,1&Date=2020-02-0808:16:41&OP=Zain
	@Id
	@GeneratedValue
	private Integer id;
	@Column(name = "my_action")
	private String myAction;
	
	@Column(name="token")
	private String token;
	
	@Column(name="mt2_service_id")
	private String mt2ServiceId;
	
	@Column(name="mt2_action")
	private String mt2Action;
	@Column(name="mt2_period")
	private String mt2Period;
	
	@Column(name = "notification_id")
	private String notificationId;
	private String mo;
	private String mt;
	private String data;
	@Column(name = "notifcation_date")
	private String notifcationDate;
	private String op;
	@Column(name = "query_str")
	private String queryStr;
	@Column(name = "create_time")
	private Timestamp createTime;
	private Boolean status;

	public Mt2KSANotification(){}
	public Mt2KSANotification(boolean status){
		this.status=status;
		this.createTime=new Timestamp(System.currentTimeMillis());
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMyAction() {
		return myAction;
	}

	public void setMyAction(String myAction) {
		this.myAction = myAction;
	}

	public String getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}

	public String getMo() {
		return mo;
	}

	public void setMo(String mo) {
		this.mo = mo;
	}

	public String getMt() {
		return mt;
	}

	public void setMt(String mt) {
		this.mt = mt;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getNotifcationDate() {
		return notifcationDate;
	}

	public void setNotifcationDate(String notifcationDate) {
		this.notifcationDate = notifcationDate;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
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
	public String getQueryStr() {
		return queryStr;
	}
	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}
	public String getMt2ServiceId() {
		return mt2ServiceId;
	}
	public void setMt2ServiceId(String mt2ServiceId) {
		this.mt2ServiceId = mt2ServiceId;
	}
	public String getMt2Action() {
		return mt2Action;
	}
	public void setMt2Action(String mt2Action) {
		this.mt2Action = mt2Action;
	}
	public String getMt2Period() {
		return mt2Period;
	}
	public void setMt2Period(String mt2Period) {
		this.mt2Period = mt2Period;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
