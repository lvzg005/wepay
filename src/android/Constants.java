package com.seadde.wepay.cordova;

public class Constants {
	
	
	public static final String WEPAY_APP_ID = "wxfbe18a53c8235ade";
	/**
	 *  appid 公众账号- 暂时不用
	 *	请同时修改 androidmanifest.xml里面，.PayActivityd里的属性<data android:scheme="wxb4ba3c02aa476ea1"/>为新设置的appid
	 */
	public static final String APP_ID = "wxfbe18a53c8235ade";

	//商户号
	public static final String MCH_ID = "1245640902";

	//API密钥，在商户平台设置
	public static final String API_KEY = "FgsBuiOnSjq8kygV1NSH4hRBZzz7mvcv";
	
	public static final String PRODUCT_INFO = "yanbao-test";
	public static final String PRODUCT_DETAIL = "yanbaoguarantee-test";
	
	public static final String NOTIFY_URL = "http://112.124.69.93/wepay/notify.html";
	
	//交易金额默认为人民币交易，接口中参数支付金额单位为【分】，参数值不能带小数。
	public static final String TOTAL_AMT = "1";
	
	

}
