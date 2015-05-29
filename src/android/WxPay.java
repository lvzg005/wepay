package com.seadde.wepay.cordova;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;


import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin; 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import android.util.Log;
import android.util.Xml;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WxPay extends CordovaPlugin {
	
	private static final String TAG = "SDK_WxPay";
	
	public static final String WXAPPID_PROPERTY_KEY = "wechatappid";
	public static final String APPID_PROPERTY_KEY = "appid";
	public static final String MCHID_PROPERTY_KEY = "mchid";
	public static final String PRODUCT_PROPERTY_KEY = "product";
	public static final String ERROR_WX_NOT_INSTALLED = "尚未安装微信客户端";
	public static final String ERR_USER_CANCEL = "ERR_USER_CANCEL";
	public static final String ERR_COMM = "ERR_COMM";
	public static final String ERR_UNKNOWN = "ERR_UNKNOWN";
	
	public static IWXAPI wxAPI;
	public static CallbackContext currentCallbackContext;

	@Override
	public boolean execute(String action, JSONArray args,CallbackContext callbackContext) throws JSONException {
		
		if (action.equals("wxPay")) {
			// weixin pay
			return wxPay(args, callbackContext);
		} else if(action.equals("isWXAppInstalled")){
			
			return isInstalled(callbackContext);
		}
		
		return super.execute(action, args, callbackContext);
	}
	
	protected IWXAPI getWXAPI() {
		if (wxAPI == null) {
			String appId = Constants.WEPAY_APP_ID;//preferences.getString(WXAPPID_PROPERTY_KEY, "");
			wxAPI = WXAPIFactory.createWXAPI(webView.getContext(), appId, true);
		}
		return wxAPI;
	}
	
	/**
	 * wxPay
	 * @param args
	 * @param callbackContext
	 * @return
	 * @throws JSONException 
	 */
	protected boolean wxPay(JSONArray args,CallbackContext callbackContext) throws JSONException {
		//是否安装微信客户端
		if (!getWXAPI().isWXAppInstalled()) {
			callbackContext.error(ERROR_WX_NOT_INSTALLED);
			return false;
		}
		try {
			getWXAPI().registerApp(Constants.WEPAY_APP_ID);
		} catch (Exception e) {
			callbackContext.error(e.getMessage());
			return false;
		}
		
		/*
		final JSONObject params = args.getJSONObject(0);
		if (params == null) {
			callbackContext.error("params is null.");
			return false;
		}
		String ipAddress = params.getString("ipAddress");
		//同一订单接口
		//Map<String,String> resultUnifiedorder = unifiedorder(ipAddress);
		String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
		
		String entity = "";//genProductArgs(ipAddress);
		if (entity == null) {
			callbackContext.error("genProductArgs is null.");
			return false;
		}
		byte[] buf = Util.httpPost(url, entity);

		String content = new String(buf);
		
		if (content == null) {
			callbackContext.error("content is null.");
			return false;
		}
		Map<String,String> resultUnifiedorder = decodeXml(content);
		if (resultUnifiedorder == null) {
			callbackContext.error("resultUnifiedorder is null.");
			return false;
		}
		*/
		//PayReq req = new PayReq();
		//构造请求参数
		//genPayReq(req,resultUnifiedorder);
		
		//调用支付api
		//sendPayReq(req);
		
		
		callbackContext.success("true");
		
		currentCallbackContext = callbackContext;
		
		return true;
	}
	
	private Map<String,String>  unifiedorder(String ipAddress) {

		String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
		
		String entity = genProductArgs(ipAddress);

		byte[] buf = Util.httpPost(url, entity);

		String content = new String(buf);
		
		Map<String,String> xml=decodeXml(content);

		return xml;
	}
	
	private void genPayReq(PayReq req,Map<String,String> resultUnifiedorder) {

		req.appId = Constants.APP_ID;
		req.partnerId = Constants.MCH_ID;
		req.prepayId = resultUnifiedorder.get("prepay_id");
		req.packageValue = "Sign=WXPay";
		req.nonceStr = genNonceStr();
		req.timeStamp = String.valueOf(genTimeStamp());

		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

		req.sign = genPackageSign(signParams);

		

	}
	private void sendPayReq(PayReq req) {
		getWXAPI().registerApp(Constants.APP_ID);
		getWXAPI().sendReq(req);
	}
	
	private String genProductArgs(String ipAddress) {
		try {
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams.add(new BasicNameValuePair("appid", Constants.APP_ID));
			packageParams.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
			packageParams.add(new BasicNameValuePair("body", Constants.PRODUCT_INFO));
			packageParams.add(new BasicNameValuePair("nonce_str", genNonceStr()));
			packageParams.add(new BasicNameValuePair("notify_url", Constants.APP_ID));
			packageParams.add(new BasicNameValuePair("out_trade_no",genOutTradNo()));
			packageParams.add(new BasicNameValuePair("spbill_create_ip",ipAddress));
			packageParams.add(new BasicNameValuePair("total_fee", Constants.TOTAL_AMT));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));

			String sign = genPackageSign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));

		   String xmlstring =toXml(packageParams);

			return xmlstring;
		} catch (Exception e) {
			
			return null;
		}
	}
	
	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}
	
	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}
	
	private String genOutTradNo() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}
	
	/**
	 * from XML to Map
	 * @param content
	 * @return
	 */
	private Map<String,String> decodeXml(String content) {
		try {
			Map<String, String> xml = new HashMap<String, String>();
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {

				String nodeName=parser.getName();
				switch (event) {
					case XmlPullParser.START_DOCUMENT:

						break;
					case XmlPullParser.START_TAG:

						if("xml".equals(nodeName)==false){
							//
							xml.put(nodeName,parser.nextText());
						}
						break;
					case XmlPullParser.END_TAG:
						break;
				}
				event = parser.next();
			}

			return xml;
		} catch (Exception e) {
			
		}
		return null;

	}
	
	private String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<"+params.get(i).getName()+">");


			sb.append(params.get(i).getValue());
			sb.append("</"+params.get(i).getName()+">");
		}
		sb.append("</xml>");

		return sb.toString();
	}
	
	/**
	 * 生成签名
	 */
	private String genPackageSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY);
		

		String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		return packageSign;
	}
	
	protected boolean isInstalled(CallbackContext callbackContext){
		final IWXAPI api = getWXAPI();
		api.registerApp(Constants.WEPAY_APP_ID);
		
		if (!api.isWXAppInstalled()) {
			callbackContext.error(ERROR_WX_NOT_INSTALLED);
			return false;
		}else{
			callbackContext.success("true");
		}
		currentCallbackContext = callbackContext;
		return true;
	}
	
}
