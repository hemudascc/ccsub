package net.mycomp.actel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_actel_qatar_blocked_msisdn")
public class ActelQatarBlockList {
	
	@Id
	@GeneratedValue
	@Column(name="id")
	private Integer id;
	@Column(name="msisdn")
	private String msisdn;
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
	@Override
	public String toString() {
		return "ActelQatarBlockList [id=" + id + ", msisdn=" + msisdn + "]";
	}
	
	

}
