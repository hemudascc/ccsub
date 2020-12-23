/*
 * package net.mycomp.mt2.zain.iraq;
 * 
 * import java.io.Serializable; import java.lang.reflect.Field; import
 * java.sql.Timestamp;
 * 
 * import javax.persistence.Entity; import javax.persistence.GeneratedValue;
 * import javax.persistence.Table; import javax.persistence.Id; import
 * javax.persistence.Column;
 * 
 * @Entity
 * 
 * @Table(name = "tb_mt2_zain_iraq_notification") public class
 * Mt2ZainIraqNotificationbk implements Serializable{
 * 
 * //http://{SP Host full
 * //address}?TransId={0}&msisdn={1}&actionType={2}&serviceId={3}&date={4}&
 * requestid={5 //}&sc={6}&key={7}
 * 
 * @Id
 * 
 * @GeneratedValue private Integer id;
 * 
 * @Column(name = "my_action") private String myAction;
 * 
 * @Column(name = "trans_id") private String transId; private String msisdn;
 * 
 * @Column(name = "action_type") private String actionType;
 * 
 * @Column(name = "service_id") private String serviceId; private String date;
 * private String requestid; private String sc;
 * 
 * @Column(name = "noti_key") private String key;
 * 
 * @Column(name = "query_str") private String queryStr;
 * 
 * @Column(name = "create_time") private Timestamp createTime;
 * 
 * @Column(name = "send_to_adnetwork") private Boolean sendToAdnetwork;
 * 
 * @Column(name = "token") private String token; private Boolean status;
 * 
 * public Mt2ZainIraqNotificationbk(){} public Mt2ZainIraqNotificationbk(boolean
 * status){ this.status=status; this.createTime=new
 * Timestamp(System.currentTimeMillis()); this.sendToAdnetwork=false; } public
 * Integer getId() { return id; } public void setId(Integer id) { this.id = id;
 * } public String getTransId() { return transId; } public void
 * setTransId(String transId) { this.transId = transId; } public String
 * getMsisdn() { return msisdn; } public void setMsisdn(String msisdn) {
 * this.msisdn = msisdn; } public String getActionType() { return actionType; }
 * public void setActionType(String actionType) { this.actionType = actionType;
 * } public String getServiceId() { return serviceId; } public void
 * setServiceId(String serviceId) { this.serviceId = serviceId; } public String
 * getDate() { return date; } public void setDate(String date) { this.date =
 * date; } public String getRequestid() { return requestid; } public void
 * setRequestid(String requestid) { this.requestid = requestid; } public String
 * getSc() { return sc; } public void setSc(String sc) { this.sc = sc; } public
 * String getKey() { return key; } public void setKey(String key) { this.key =
 * key; } public String getQueryStr() { return queryStr; } public void
 * setQueryStr(String queryStr) { this.queryStr = queryStr; } public Timestamp
 * getCreateTime() { return createTime; } public void setCreateTime(Timestamp
 * createTime) { this.createTime = createTime; } public Boolean getStatus() {
 * return status; } public void setStatus(Boolean status) { this.status =
 * status; } public String getMyAction() { return myAction; } public void
 * setMyAction(String myAction) { this.myAction = myAction; } public Boolean
 * getSendToAdnetwork() { return sendToAdnetwork; } public void
 * setSendToAdnetwork(Boolean sendToAdnetwork) { this.sendToAdnetwork =
 * sendToAdnetwork; } public String getToken() { return token; } public void
 * setToken(String token) { this.token = token; }
 * 
 * public String toString() {
 * 
 * Field[] fields = this.getClass().getDeclaredFields(); String str =
 * this.getClass().getName(); try { for (Field field : fields) { str +=
 * field.getName() + "=" + field.get(this) + ","; } } catch
 * (IllegalArgumentException ex) { System.out.println(ex); } catch
 * (IllegalAccessException ex) { System.out.println(ex); } return str; }
 * 
 * }
 */