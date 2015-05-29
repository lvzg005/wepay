var exec = require('cordova/exec');

module.exports = {
    
    isInstalled: function (onSuccess, onError) {
        exec(onSuccess, onError, "WxPay", "isWXAppInstalled", []);
    },

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
    wxPay: function(message, onSuccess, onError) {
        exec(onSuccess, onError, "WxPay", "wxPay", [message]);
    }
};
