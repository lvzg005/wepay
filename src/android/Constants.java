package com.seadde.wepay.cordova;

public class Constants {
	
	
	public static final String WEPAY_APP_ID = "wxf2f565574a968187";
	/**
	 *  appid 公众账号
	 *	请同时修改 androidmanifest.xml里面，.PayActivityd里的属性<data android:scheme="wxb4ba3c02aa476ea1"/>为新设置的appid
	 */
	public static final String APP_ID = "wxf2f565574a968187";

	//商户号
	public static final String MCH_ID = "1233848001";

	//API密钥，在商户平台设置
	public static final String API_KEY = "412fde4e9c2e2bb619514ecea142e449";
	
	public static final String PRODUCT_INFO = "手机延保";
	
	public static final String NOTIFY_URL = "http://";
	
	//交易金额默认为人民币交易，接口中参数支付金额单位为【分】，参数值不能带小数。
	public static final String TOTAL_AMT = "100";
	
	

}
