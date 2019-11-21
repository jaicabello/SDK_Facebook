var exec = require('cordova/exec');


var SDK_Facebook = {
	logEventSDKFacebook : function (arg0, success, error) {
    exec(success, error, 'SDK_Facebook', 'logEventSDKFacebook', [arg0]);
    },
    logAdClickEvent : function (adType) {
        exec('SDK_Facebook', 'logAdClickEvent',adType);
    }
        
};

cordova.addConstructor(function () {
  if (!window.plugins) {
    window.plugins = {};
  }

  window.cordova.plugins.SDK_Facebook = SDK_Facebook;
  return window.cordova.plugins.SDK_Facebook;
});



