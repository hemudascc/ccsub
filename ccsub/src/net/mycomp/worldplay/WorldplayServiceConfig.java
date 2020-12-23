package net.mycomp.worldplay;

import java.lang.reflect.Field;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_worldplay_service_config")
public class WorldplayServiceConfig {

	@Id
	private Integer id;
	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name="service_name")
	private String serviceName;
	
	@Column(name="worldplay_operator_name")
	private String worldplayOperatorName;
	
	@Column(name = "worldplay_service_id")
	private String worldplayServiceId;
	
	@Column(name = "worldplay_service_name")
	private String worldplayServiceName;
	@Column(name="client")
	private String client;
	
	@Column(name = "price_point")
	private Double pricePoint;
	

	@Column(name = "validity")
	private Integer validity;
	
	@Column(name="cg_url")
	private String cgUrl;
	@Column(name="lp_img")
	private String lpImg;
	@Column(name="portal_url")
	private String portalUrl;
	
	@Column(name="term_condition_page")
	private String termConditionPage;
	
	private Boolean status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getWorldplayServiceId() {
		return worldplayServiceId;
	}

	public void setWorldplayServiceId(String worldplayServiceId) {
		this.worldplayServiceId = worldplayServiceId;
	}

	public String getWorldplayServiceName() {
		return worldplayServiceName;
	}

	public void setWorldplayServiceName(String worldplayServiceName) {
		this.worldplayServiceName = worldplayServiceName;
	}

	public Double getPricePoint() {
		return pricePoint;
	}

	public void setPricePoint(Double pricePoint) {
		this.pricePoint = pricePoint;
	}

	public Integer getValidity() {
		return validity;
	}

	public void setValidity(Integer validity) {
		this.validity = validity;
	}

	public String getCgUrl() {
		return cgUrl;
	}

	public void setCgUrl(String cgUrl) {
		this.cgUrl = cgUrl;
	}

	public String getPortalUrl() {
		return portalUrl;
	}

	public void setPortalUrl(String portalUrl) {
		this.portalUrl = portalUrl;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getWorldplayOperatorName() {
		return worldplayOperatorName;
	}

	public void setWorldplayOperatorName(String worldplayOperatorName) {
		this.worldplayOperatorName = worldplayOperatorName;
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

public String getClient() {
	return client;
}

public void setClient(String client) {
	this.client = client;
}

public String getLpImg() {
	return lpImg;
}

public void setLpImg(String lpImg) {
	this.lpImg = lpImg;
}

public String getTermConditionPage() {
	return termConditionPage;
}

public void setTermConditionPage(String termConditionPage) {
	this.termConditionPage = termConditionPage;
}

public String getServiceName() {
	return serviceName;
}

public void setServiceName(String serviceName) {
	this.serviceName = serviceName;
}

	
}
