package net.bizao;

import java.lang.reflect.Field;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Transient;

import org.apache.commons.net.util.SubnetUtils;

@Entity
@Table(name = "tb_bizao_ip_pool")
public class BizaoIpPool {
	
@Id
private Integer id;
private String ip;
@Column(name = "bizao_op_id")
private Integer bizaoOpId;
private Boolean status;

@Transient
private SubnetUtils subnetUtils;
@Transient
private String info="pool";

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
public String getIp() {
	return ip;
}
public void setIp(String ip) {
	this.ip = ip;
}
public Integer getBizaoOpId() {
	return bizaoOpId;
}
public void setBizaoOpId(Integer bizaoOpId) {
	this.bizaoOpId = bizaoOpId;
}
public Boolean getStatus() {
	return status;
}
public void setStatus(Boolean status) {
	this.status = status;
}
public SubnetUtils getSubnetUtils() {
	return subnetUtils;
}
public void setSubnetUtils(SubnetUtils subnetUtils) {
	this.subnetUtils = subnetUtils;
}
public String getInfo() {
	return info;
}
public void setInfo(String info) {
	this.info = info;
}
}
