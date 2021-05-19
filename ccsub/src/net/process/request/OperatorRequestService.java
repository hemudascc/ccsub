package net.process.request;


import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import net.bizao.BizaoService;
import net.indonesia.triyakom.TriyakomService;
import net.mycom.nxt.vas.NxtVasService;
import net.mycomp.actel.ActelService;
import net.mycomp.beecel.jordon.BCJordonService;
import net.mycomp.comviva.ooredo.oman.OoredooOmanService;
import net.mycomp.cornet.sudan.CornetService;
import net.mycomp.du.DUService;
import net.mycomp.etisalat.EtisalatService;
import net.mycomp.intarget.IntargetService;
import net.mycomp.ksa.KsaService;
import net.mycomp.mcarokiosk.hongkong.MKHongkongService;
import net.mycomp.mcarokiosk.malaysia.MKMalaysiaService;
import net.mycomp.messagecloud.MessageCloudService;
import net.mycomp.messagecloud.gateway.MCGService;
import net.mycomp.messagecloud.gateway.za.MCGZAService;
import net.mycomp.mobimind.MobimindService;
import net.mycomp.mobivate.MobivateService;
import net.mycomp.mt2.ksa.Mt2KSAService;
import net.mycomp.mt2.uae.Mt2UAEService;
import net.mycomp.mt2.zain.iraq.Mt2ZainIraqService;
import net.mycomp.onmobile.OnMobileService;
import net.mycomp.oredoo.kuwait.OredoKuwaitService;
import net.mycomp.tpay.TpayService;
import net.mycomp.veoo.VeooService;
import net.mycomp.wintel.bangladesh.WintelBDService;
import net.mycomp.worldplay.WorldplayService;
import net.persist.bean.SubscriberReg;
import net.process.bean.AdNetworkRequestBean;
import net.process.bean.DeactivationResponse;
import net.thialand.ThialandService;
import net.util.MConstants;

@Service("operatorRequestService")
public class OperatorRequestService implements IOperatorService{

	@Autowired
	@Qualifier("defaultOperatorService")
	private IOperatorService defaultOperatorService;
	
	@Autowired
	@Qualifier("nxtVasService")
	private NxtVasService nxtVasService;
	
	@Autowired
	@Qualifier("etisalatService")
	private EtisalatService etisalatService;
	
	@Autowired
	@Qualifier("veooService")
	private VeooService veooService;
	
	@Autowired
	@Qualifier("messageCloudService")
	private MessageCloudService messageCloudService;
	
	@Autowired
	@Qualifier("duService")
	private DUService duService;
	
	@Autowired
	@Qualifier("intargetService")
	private IntargetService intargetService;
	
	
	@Autowired
	@Qualifier("thialandService")
	private ThialandService thialandService;
	
	@Autowired
	@Qualifier("mcgService")
	private MCGService mcgService;
	
	@Autowired
	@Qualifier("oredoKuwaitService")
	private OredoKuwaitService oredoKuwaitService;
	
	@Autowired
	@Qualifier("mcgZAService")
	private MCGZAService mcgZAService;
	
	@Autowired
	@Qualifier("triyakomService")
	private TriyakomService triyakomService;
	
	@Autowired
	@Qualifier("mkMalaysiaService")
	private MKMalaysiaService mkMalaysiaService;
	
	
	@Autowired
	@Qualifier("bizaoService")
	private BizaoService bizaoService;
	
	@Autowired
	@Qualifier("ksaService")
	private KsaService ksaService;
	
	
	
	@Autowired
	@Qualifier("wintelBDService")
	private WintelBDService wintelBDService;
	
	@Autowired
	@Qualifier("onMobileService")
	private OnMobileService onMobileService;
	
	@Autowired
	@Qualifier("mobimindService")
	private MobimindService mobimindService;
	
	@Autowired
	@Qualifier("actelService")
	private ActelService actelService;
	
	@Autowired
	@Qualifier("worldplayService")
	private WorldplayService worldplayService;
	
	@Autowired
	@Qualifier("mt2KSAService")
	private Mt2KSAService mt2KSAService;
	
	@Autowired
	@Qualifier("mt2ZainIraqService")
	private Mt2ZainIraqService mt2ZainIraqService;
	
	
	@Autowired
	@Qualifier("mobivateService")
	private MobivateService mobivateService;
	
	@Autowired
	@Qualifier("mt2UAEService")
	private Mt2UAEService mt2UAEService;
	@Autowired
	@Qualifier("ooredooOmanService")
	private OoredooOmanService ooredooOmanService;
	@Autowired
	@Qualifier("tpayService") 
	private TpayService tpayService;
	@Autowired
	@Qualifier("mkHongkongService")
	private MKHongkongService mkHongkongService;
	
	@Autowired
	@Qualifier("bcJordonService")
	private BCJordonService bcJordonService;
	
	@Autowired
	@Qualifier("cornetService")
	private CornetService cornetService;
	 
	private IOperatorService findProcessRequest(int opId){
		
		IOperatorService ioperatorService=null;
		switch(opId){
		case MConstants.NXT_VAS_JORDAN_UMNAIH_OPERATOR:	
		case MConstants.NXT_VAS_JORDAN_ORANGE_OPERATOR:	
		case MConstants.NXT_VAS_EGYPT_ORANGE_OPERATOR:	
		case MConstants.NXT_VAS_EGYPT_VODAFONE_OPERATOR:	
		case MConstants.NXT_VAS_TUNISIA_ORANGE_OPERATOR:	
		case MConstants.NXT_VAS_KUWAIT_VIVA_OPERATOR:		
		{
			ioperatorService=nxtVasService;
			break;
		}
		case MConstants.ETISALAT_OPERATOR_ID:	
		{
			ioperatorService=etisalatService;
			break;
		}
		case MConstants.VEOO_HONDURAS_TIGO_OPERATOR_ID:	
		case MConstants.VEOO_NICARAGUA_CLARO_OPERATOR_ID:	
		case MConstants.VEOO_NICARAGUA_MOVISTAR_OPERATOR_ID:	
		case MConstants.VEOO_COSTA_RICA:		
		{
			ioperatorService=veooService;
			break;
		}
		case MConstants.UK_ORANGE_OPERATOR_ID:	
		case MConstants.UK_OS2_OPERATOR_ID:	
		case MConstants.UK_VODAFONE_OPERATOR_ID:	
		{
			ioperatorService=messageCloudService;
			break;
		}
		case MConstants.DU_OPERATOR_ID:	
		{
			ioperatorService=duService;
			break;
		}
		case MConstants.INTARGET_SAFARICOM_OPERATOR_ID:	
		{
			ioperatorService=intargetService;
			break;
		}
		case MConstants.MICROKIOSK_AIS_OPERATOR_ID:	
		case MConstants.MICROKIOSK_TRUEMOVE_OPERATOR_ID:	
		{
			ioperatorService=thialandService;
			break;
		}
		case MConstants.MESSAGE_CLOUD_GATWAY_CH:	
		{
			ioperatorService=mcgService;
			break;
		}
		
		case MConstants.OREDOO_KUWAIT_OPERATOR_ID:	
		{
			ioperatorService=oredoKuwaitService;
			break;
		}
		case MConstants.MESSAGE_CLOUD_GATWAY_ZA_OPERATOR:	
		{
			ioperatorService=mcgZAService;
			break;
		}
		case MConstants.ORANGE_CAMERON_OPERATOR_ID:
		case MConstants.ORANGE_IV_OPERATOR_ID:
		case MConstants.ORANGE_RDCONGO_OPERATOR_ID:
		case MConstants.ORANGE_SENEGAL_OPERATOR_ID:	
		{
			ioperatorService=bizaoService;
			break;
		}
		case MConstants.TRIAKOM_INDONESIA_IM3_OPERATOR_ID:
		case MConstants.TRIAKOM_INDONESIA_SAT_OPERATOR_ID:
		case MConstants.TRIAKOM_INDONESIA_XL_OPERATOR_ID:
		{
			ioperatorService=triyakomService;
			break;
		}
		case MConstants.MK_MALAYSIA_DIGI_OPERATOR_ID:
		case MConstants.MK_MALAYSIA_UMOBILE_OPERATOR_ID:
		{
			ioperatorService=mkMalaysiaService;
			break;
		}
		case MConstants.KSA_JAIN_OPERATOR_ID:
		case MConstants.KSA_STA_OPERATOR_ID:
		case MConstants.KSA_MOBILY_OPERATOR_ID:	
		{
			ioperatorService=ksaService;
			break;
		}		
		case MConstants.WINTEL_BD_GRAMEEPHONE_OPERATOR_ID:
		{
			ioperatorService=wintelBDService;
			break;
		}
		case MConstants.ONMOBILE_OPERATOR_ID:
		{
			ioperatorService=onMobileService;
			break;
		}
		case MConstants.MOBIMIND_OERATOR_BATELCO:
		case MConstants.MOBIMIND_OERATOR_KOREK:
		case MConstants.MOBIMIND_OERATOR_VIVA:
		case MConstants.MOBIMIND_EGYPT_VODAFONE_OPERATOR_ID:
		case MConstants.MOBIMIND_OMAN_OOREDOO_OPERATOR_ID:
		case MConstants.MOBIMIND_KUWAIT_STC_OPERATOR_ID:
		{
			ioperatorService=mobimindService;
			break;
		}
		case MConstants.ACTEL_OPERATOR_ETISALAT:
		case MConstants.ACTEL_OPERATOR_DU:
		case MConstants.ACTEL_OOREDOO_QATAR_OPERATOR_ID:
		case MConstants.ACTEL_VODAFONE_QATAR_OPERATOR_ID:
		{
			ioperatorService=actelService;
			break;
		}
		case MConstants.WORLD_PLAY_CELLC_OPERATOR_ID:
		case MConstants.WORLD_PLAY_MTN_OPERATOR_ID:
		case MConstants.WORLD_PLAY_VODACOM_OPERATOR_ID:
		{
			ioperatorService=worldplayService;
			break;
		}
		case MConstants.MT2_KSA_ZAIN_OPERATOR_ID:
		case MConstants.MT2_KSA_STC_OPERATOR_ID:
		{
			ioperatorService=mt2KSAService;
			break;
		}
		case MConstants.MT2_ZAIN_IRAQ_OPERATOR_ID:{
			ioperatorService=mt2ZainIraqService;
			break;
		}
		case MConstants.MOBIVATE_SOUTH_AFRICA_CELLC_OPERATOR_ID:
		case MConstants.MOBIVATE_SOUTH_AFRICA_MTN_OPERATOR_ID:	
		case MConstants.MOBIVATE_UK_VODAFONE_OPERATOR_ID:
		case MConstants.MOBIVATE_SOUTH_AFRICA_ZA_VODACOM_OPERATOR_ID:{
			ioperatorService=mobivateService;
			break;
		}
		case MConstants.MT2_UAE_ETISALAT_OPERATOR_ID:
		case MConstants.MT2_UAE_DU_OPERATOR_ID:
		case MConstants.MT2_JORDAN_OPERATOR_ID:{
			ioperatorService=mt2UAEService;
			break;
		}
		case MConstants.COMVIVA_OOREDOO_OMAN_OPERATOR_ID:{
			ioperatorService=ooredooOmanService;
			break;
		}
		case MConstants.TPAY_KSA_ZAIN_OPERATOR_ID:
		case MConstants.TPAY_EGYPT_WE_OPERATOR_ID:
		case MConstants.TPAY_EGYPT_ETISALAT_OPERATOR_ID:
		case MConstants.TPAY_EGYPT_VODAFONE_OPERATOR_ID:
		case MConstants.TPAY_EGYPT_ORANGE_OPERATOR_ID:
		{
			ioperatorService = tpayService;
			break;
		}
		case MConstants.MK_HONGKONG_HUTCHISON_OPERATOR_ID:
		case MConstants.MK_HONGKONG_SMARTONE_OPERATOR_ID:{
			ioperatorService=mkHongkongService;
			break;
		}	 
		case MConstants.BC_JORDON_ORANGE_OPERATOR_ID:
		case MConstants.BC_JORDON_MOBILY_OPERATOR_ID:
		case MConstants.BC_JORDON_UMNIAH_OPERATOR_ID:{
			ioperatorService=bcJordonService;
			break;
		}
		case MConstants.CORNET_SUDAN_ZAIN_OPERATOR_ID:{
			ioperatorService = cornetService;
			break;
		}
		default:	{
			ioperatorService=defaultOperatorService	;
		}
		}
		return ioperatorService;
	}
	

	@Override
	public boolean checkBlocking(AdNetworkRequestBean adNetworkRequestBean) {
		return findProcessRequest(adNetworkRequestBean.getOpId()).checkBlocking(adNetworkRequestBean);
	}

	@Override
	public boolean processBilling(ModelAndView modelAndView,AdNetworkRequestBean adNetworkRequestBean) {
		return findProcessRequest(adNetworkRequestBean.getOpId()).processBilling(modelAndView,adNetworkRequestBean);
	}

	@Override
	public boolean isSubscribed(AdNetworkRequestBean adNetworkRequestBean) {
		return findProcessRequest(adNetworkRequestBean.getOpId()).isSubscribed(adNetworkRequestBean);
	}
	
	@Override
	public DeactivationResponse deactivation(ModelAndView modelAndView,
			SubscriberReg subscriberReg) {
		return findProcessRequest(subscriberReg.getOperatorId()).deactivation(modelAndView, subscriberReg);
	}


//	@Override
//	public IOtp sendOtp(ModelAndView modelAndView, String msisdn,Integer operatorId,Integer serviceId) {
//		return findProcessRequest(operatorId).sendOtp( modelAndView,  msisdn, operatorId, serviceId);
//	}


	@Override
	public SubscriberReg searchSubscriber(Integer operatorId,String msisdn, Integer serviceId,Integer productId) {
		return findProcessRequest(operatorId).searchSubscriber(operatorId, msisdn, serviceId,productId);
	}	
	
	@Override
	public boolean checkSub(Integer productId,Integer operatorId,String msisdn) {
		return findProcessRequest(operatorId).checkSub(productId,operatorId, msisdn);
	}


	@Override
	public boolean sendOtp(ModelAndView modelAndView,
			AdNetworkRequestBean adNetworkRequestBean) {
		return findProcessRequest(adNetworkRequestBean.getOpId()).sendOtp( modelAndView,
				 adNetworkRequestBean);
	}


	@Override
	public boolean validateOtp(ModelAndView modelAndView, 
			AdNetworkRequestBean adNetworkRequestBean) {
		return findProcessRequest(adNetworkRequestBean.getOpId()).validateOtp( modelAndView,
				 adNetworkRequestBean);
	}


	@Override
	public Timestamp getTimeByOperator(Integer opId) {
		return findProcessRequest(opId).getTimeByOperator(
				 opId);
	}	
	
	
}
