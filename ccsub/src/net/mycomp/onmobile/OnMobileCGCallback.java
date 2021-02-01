package net.mycomp.onmobile;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_onmobile_cg_callback")
public class OnMobileCGCallback implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//http://192.241.253.234/WayOfLife/om/home?
	//u=B64CBBCDF4A324C872029DA6C0449F37&status=ACTIVE&opr=Zain&omsource=onmobile&ommedium=onmobile
	//&omcampaign=onmobile&sid=YEKZYby0a5lnYMR2A5EN641cI5q76ZZZW9xv5pkBnzw
	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;
	@Column(name="msisdn")
	private String msisdn;
	@Column(name="on_mobile_status")
	private String onMobileStatus;
	@Column(name="operator_name")
	private String operatorName;
	@Column(name="token")
	private String token;
	@Column(name="token_id")
	private String tokenId;
	@Column(name = "query_str")
	private String queryStr;
	@Column(name = "create_time")
	private Timestamp createTime;
	@Column(name="status")
	private Boolean status;

	public OnMobileCGCallback(){}
	public OnMobileCGCallback(boolean status){
		this.status=status;
		this.createTime=new Timestamp(System.currentTimeMillis());
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getOnMobileStatus() {
		return onMobileStatus;
	}
	public void setOnMobileStatus(String onMobileStatus) {
		this.onMobileStatus = onMobileStatus;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
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
