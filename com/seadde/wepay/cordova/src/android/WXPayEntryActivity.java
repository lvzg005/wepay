package to.be.replaced.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.seadde.wepay.cordova.WxPay;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{

	 @Override
	 public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WxPay.wxAPI.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        WxPay.wxAPI.handleIntent(intent, this);
    }
	    
	@Override
	public void onReq(BaseReq arg0) {
		finish();
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.i(WXPayEntryActivity.class.getName(), resp.toString());
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			WxPay.currentCallbackContext.success();
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			WxPay.currentCallbackContext.error(WxPay.ERR_USER_CANCEL);
			break;
		case BaseResp.ErrCode.ERR_COMM:
			WxPay.currentCallbackContext.error(WxPay.ERR_COMM);
			break;
		default:
			WxPay.currentCallbackContext.error(WxPay.ERR_UNKNOWN);
			break;
		}
		finish();
	}

}
