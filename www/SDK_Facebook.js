var exec = require('cordova/exec');


var SDK_Facebook = {

	logEventForFacebook : function (name, params, success, error) {
		console.log('----------------Llamando a la funcion logEventForFacebook Facebook_SDK');	
		exec(success, error, 'SDK_Facebook', 'logEventForFacebook',[name, params]);
	   },
	getDeferredApplink : function (name, params, success, error) {
		console.log('----------------Llamando a la funcion getDeferredApplink Facebook_SDK');	
		exec(success, error, 'SDK_Facebook', 'getDeferredApplink',[arg0]);
   	},
	logViewContentEvent : function (arg0, success, error) {
		console.log('----------------Llamando a la funcion logViewContentEvent Facebook_SDK');	
    	exec(success, error, 'SDK_Facebook', 'logViewContentEvent', [arg0]);
   	},
  	logAdClickEvent : function (arg0, success, error) {
    	console.log('----------------Llamando a la funcion logAdClickEvent Facebook_SDK');	  
    	exec(success, error, 'SDK_Facebook', 'logAdClickEvent', [arg0]);
	},
	logOnButtonClickEvent : function (arg0, success, error) {
    	console.log('----------------Llamando a la funcion logOnButtonClickEvent Facebook_SDK');	  
    	exec(success, error, 'SDK_Facebook', 'logOnButtonClickEvent', [arg0]);
    }
        
};

cordova.addConstructor(function () {
  if (!window.plugins) {
    window.plugins = {};
  }

  window.cordova.plugins.SDK_Facebook = SDK_Facebook;
  return window.cordova.plugins.SDK_Facebook;
});
