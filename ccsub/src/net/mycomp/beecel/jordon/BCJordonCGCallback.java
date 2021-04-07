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
@Table(name = "tb_bc_jordon_cg_callback")
public class BCJordonCGCallback implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private Integer id;
	@Column(name = "query_str")
	private String queryStr;
	private String tid;
	@Column(name = "cid")
	private String cid;
	private String msisdn;
	@Column(name = "redirect_to_url")
	private String redirectToUrl;
	@Column(name = "create_time")
	private Timestamp createTime;
	private Boolean status;
	
	public BCJordonCGCallback(){
	this.createTime=new Timestamp(System.currentTimeMillis());
	this.status=true;
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
	public String getQueryStr() {
		return queryStr;
	}
	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	
	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
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

	public String getRedirectToUrl() {
		return redirectToUrl;
	}

	public void setRedirectToUrl(String redirectToUrl) {
		this.redirectToUrl = redirectToUrl;
	}
}
