# cordova-plugin-wepay

A cordova plugin, a JS version of Weixin SDK

# Feature

wepay


# Install


# Note: Install(Android) 
Inspired by 
Wechat needs to callback to "your.package.name.wxapi.WXEntryActivity" to handle response. Since the package name is determined when you install the packag.java, so we use hook to call android-install.js to do the work.
I found some older version of cordova(ionic 1.3.0) doesn't trigger this js, so if you found this file isn't copied, consider upgrade Cordova.


# Usage

## Check if wepay is installed
```Javascript
WxPay.isInstalled(function (installed) {
    alert("WxPay installed: " + (installed ? "Yes" : "No"));
}, function (reason) {
    alert("Failed: " + reason);
});
```

### wepay
```Javascript
   /**
     * pay 
     *
     * @example
     * <code>
     * WxPay.wxPay({
     *    		uuid:'',
	 *    		ip: ''
     * 		}, function () {
     *     		alert("Success");
     * 		}, function (reason) {
     *     		alert("Failed: " + reason);
     * 		});
     * </code>
     */ 
```

# FAQ


# TODO


# LICENSE

[MIT LICENSE](http://opensource.org/licenses/MIT)
