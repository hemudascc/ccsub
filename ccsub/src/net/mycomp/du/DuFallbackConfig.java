package net.mycomp.du;

import java.lang.reflect.Field;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "tb_du_fallback_config")
public class DuFallbackConfig {

	@Id
	private Integer id;
	@Column(name = "du_config_id")
	private Integer duConfigId;
	@Column(name = "fallback_plan_id")
	private String fallBackPlanId;
	@Column(name = "fallback_price")
	private Double falbackPrice;
	@Column(name = "fallback_validity")
	private Integer fallBaclValidity;
	private Boolean status;
	
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
	public Integer getDuConfigId() {
		return duConfigId;
	}
	public void setDuConfigId(Integer duConfigId) {
		this.duConfigId = duConfigId;
	}
	public String getFallBackPlanId() {
		return fallBackPlanId;
	}
	public void setFallBackPlanId(String fallBackPlanId) {
		this.fallBackPlanId = fallBackPlanId;
	}
	public Double getFalbackPrice() {
		return falbackPrice;
	}
	public void setFalbackPrice(Double falbackPrice) {
		this.falbackPrice = falbackPrice;
	}
	public Integer getFallBaclValidity() {
		return fallBaclValidity;
	}
	public void setFallBaclValidity(Integer fallBaclValidity) {
		this.fallBaclValidity = fallBaclValidity;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	
}
