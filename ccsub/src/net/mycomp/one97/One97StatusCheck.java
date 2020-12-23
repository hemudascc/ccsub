package net.mycomp.one97;

import java.lang.reflect.Field;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_one97_status_check")
public class One97StatusCheck {

	@Id
	@GeneratedValue
	private Integer id;
	private String msisdn;
	@Column(name = "query_str")
	private String queryStr;
	@Column(name = "status_check_url")
	private String statusCheckUrl;
	@Column(name = "status_check_resp")
	private String statusCheckResp;
	
	@Column(name = "response_to_caller")
	private String responseToCaller;
	
	@Column(name = "create_time")
	private Timestamp createTime;
	private Boolean status;
	
	public One97StatusCheck(){}
	
	public One97StatusCheck(boolean status){
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
	public String getQueryStr() {
		return queryStr;
	}
	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}
	public String getStatusCheckUrl() {
		return statusCheckUrl;
	}
	public void setStatusCheckUrl(String statusCheckUrl) {
		this.statusCheckUrl = statusCheckUrl;
	}
	public String getStatusCheckResp() {
		return statusCheckResp;
	}
	public void setStatusCheckResp(String statusCheckResp) {
		this.statusCheckResp = statusCheckResp;
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

	public String getResponseToCaller() {
		return responseToCaller;
	}

	public void setResponseToCaller(String responseToCaller) {
		this.responseToCaller = responseToCaller;
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
