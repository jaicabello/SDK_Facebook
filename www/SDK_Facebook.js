var exec = require('cordova/exec');


var SDK_Facebook = {

	logEventForFacebook : function (name, params, success, error) {
		console.log('----------------Llamando a la funcion logEventForFacebook Facebook_SDK');	
		exec(success, error, 'SDK_Facebook', 'logEventForFacebook',[name, params]);
	   },
	getDeferredApplink : function (arg0, success, error) {
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
	,logViewContent : function (contentType, contentData, contentId, contentCurrency, contentPrice, s, f)
	{
	  console.log('----------------Llamando a la funcion logOnButtonClickEvent logViewContent');	
	  exec(s, f, 'SDK_Facebook', 'logViewContent', [contentType, contentData, contentId, contentCurrency, contentPrice])
	}
	,logEventSearch : function (contentType, contentData, contentId, searchQuery, searchSuccess, s, f)
	{
	  console.log('----------------Llamando a la funcion logOnButtonClickEvent logEventSearch');
	  exec(s, f, 'SDK_Facebook', 'logEventSearch', [contentType, contentData, contentId, searchQuery, searchSuccess])
	}
	,logEventProductCartAdd : function (contentType, contentData, contentId, contentCurrency, contentPrice, s, f)
	{
	  console.log('----------------Llamando a la funcion logOnButtonClickEvent logEventProductCartAdd');
	  exec(s, f, 'SDK_Facebook', 'logEventProductCartAdd', [contentType, contentData, contentId, contentCurrency, contentPrice])
	}
	,logEventProductCustomize : function (contentParam, contentValue, s, f)
	{
	  console.log('----------------Llamando a la funcion logOnButtonClickEvent logEventProductCustomize');
	  exec(s, f, 'SDK_Facebook', 'logEventProductCustomize', [contentParam, contentValue])
	}
	,logEventInitiateCheckout : function (contentType, contentData, contentId, contentCurrency, contentPrice, numItems, PaymentInfo, s, f)
	{
	  exec(s, f, 'SDK_Facebook', 'logEventInitiateCheckout', [contentType, contentData, contentId, contentCurrency, contentPrice, numItems, PaymentInfo])
	}
	,logEventProductPurchase : function (contentType, contentData, contentId, contentCurrency, contentPrice, s, f)
	{
	  exec(s, f, 'SDK_Facebook', 'logEventProductPurchase', [contentType, contentData, contentId, contentCurrency, contentPrice])
	}
};

cordova.addConstructor(function () {
  if (!window.plugins) {
    window.plugins = {};
  }

  window.cordova.plugins.SDK_Facebook = SDK_Facebook;
  return window.cordova.plugins.SDK_Facebook;
});
