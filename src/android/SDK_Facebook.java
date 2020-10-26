package cordova.plugin.facebookplugin;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import java.math.BigDecimal;
import java.util.Currency;
import java.util.Iterator;

import com.facebook.FacebookSdk;
import com.facebook.CallbackManager;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.widget.ShareDialog;
import com.facebook.share.widget.GameRequestDialog;
import com.facebook.share.widget.MessageDialog;
import com.facebook.applinks.AppLinkData;






/**
 * This class echoes a string called from JavaScript.
 */
public class SDK_Facebook extends CordovaPlugin {

    /** Log this event when the user has viewed a form of content in the app. */
    public static final String EVENT_NAME_VIEWED_CONTENT = "fb_mobile_content_view";

    /**
     * Parameter key used to specify a generic content type/family for the logged event, e.g.
     * "music", "photo", "video".  Options to use will vary depending on the nature of the app.
     */
    public static final String EVENT_PARAM_CONTENT_TYPE = "fb_content_type";

    /**
     * Parameter key used to specify an ID for the specific piece of content being logged about.
     * This could be an EAN, article identifier, etc., depending on the nature of the app.
     */
    public static final String EVENT_PARAM_CONTENT_ID = "fb_content_id";

    public static final String EVENT_PARAM_CONTENT = "fb_content";

     /**
     * Parameter key used to specify currency used with logged event.  E.g. "USD", "EUR", "GBP". See
     * <a href="http://en.wikipedia.org/wiki/ISO_4217">ISO-4217</a>
     * for specific values.
     */
    public static final String EVENT_PARAM_CURRENCY = "fb_currency";
    
    // Constantes para log event adclick ( evento tipo banner)

    public static final String EVENT_NAME_AD_CLICK = "AdClick";
    public static final String EVENT_PARAM_AD_TYPE  = "ad_type";

    // constante para los logs 
    private static final String TAG = "SDK_Facebook";


    private CallbackManager callbackManager;
    private AppEventsLogger logger;
    private CallbackContext loginContext = null;
    private CallbackContext showDialogContext = null;
    private CallbackContext lastGraphContext = null;
    private String graphPath;
    private ShareDialog shareDialog;
    private GameRequestDialog gameRequestDialog;
    private MessageDialog messageDialog;


    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView){

        Log.i(TAG,"Comenzo la ejecuccion del metodo initialize");
        super.initialize(cordova, webView);
        FacebookSdk.sdkInitialize(cordova.getActivity().getApplicationContext());
        logger = AppEventsLogger.newLogger(cordova.getActivity().getApplicationContext());
        enableHybridAppEvents();

        FacebookSdk.setAutoInitEnabled(true);
        FacebookSdk.fullyInitialize();
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);
        FacebookSdk.setAutoLogAppEventsEnabled(true);
        FacebookSdk.setAdvertiserIDCollectionEnabled(true);
    }
    
    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);
        AppEventsLogger.activateApp(cordova.getActivity().getApplication());
    }

    @Override
    public void onPause(boolean multitasking) {
        super.onPause(multitasking);
        AppEventsLogger.deactivateApp(cordova.getActivity().getApplication());
    }



 

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        
        Log.i(TAG,"Comenzo la ejecuccion del metodo execute");
            if (action.equals("logViewContentEvent")) {
                try {
                    Log.i(TAG,"Se llama al action logViewContentEvent");
                    logger.logEvent(EVENT_NAME_VIEWED_CONTENT);
                    Log.i(TAG,"Fin del llamado al action logViewContentEvent");
                    return true;
                } catch (Exception e) {
                    Log.e(TAG,"ERROR al llamar el action Event");
                    callbackContext.error("Error ejecutando action: " + e);
                    return false;
                }
                
            }
            else if(action.equals("logAdClickEvent")){
                try {
                    Log.i(TAG,"Se llama al action logAdClickEvent");
                    Log.i(TAG,"args: "+args.getString(0));
                    logAdClickEvent(args.getString(0));
                    Log.i(TAG,"Fin del llamado al action logAdClickEvent");
                    PluginResult pluginResult = new PluginResult(PluginResult.Status.OK);
                    pluginResult.setKeepCallback(true);
                    callbackContext.sendPluginResult(pluginResult);
                    Log.i(TAG,"Fin del llamado al action logAdClickEvent");
                    return true;
                    }
                catch (Exception e) {
                    Log.e(TAG,"ERROR al llamar el action logAdClickEvent");
                    callbackContext.error("Error ejecutando action: " + e);
                    return false;
                }
            }else if(action.equals("logEventForFacebook")){
                
                Log.i(TAG,"Se llama al action logEventForFacebook");
                Log.d(TAG,"name event: "+args.getString(0));
                Log.d(TAG,"parameters: "+args.getJSONObject(1));
                try{
                    logEventForFacebook(args.getString(0), args.getJSONObject(1));
                }catch (JSONException e){
                    //e.printStackTrace();
                    Log.w(TAG, "error JSON", e);
                }
                Log.i(TAG,"Fin del llamado al action logEventForFacebook");
                return true;        
            
            }else if (action.equals("getDeferredApplink")) {
                Log.i(TAG,"Se llama al action executeGetDeferredApplink");
                executeGetDeferredApplink(args, callbackContext);
                Log.i(TAG,"Fin del llamado al action executeGetDeferredApplink");
                return true;
            } 
            else if (action.equals("logViewContent"))
            {
                Log.i(TAG,"Se llama al action logViewContent");
                Log.d( TAG, args.toString(4) );
    
                cordova.getThreadPool().execute(new Runnable()
                {
                    public void run() 
                    {
                        try
                        {
                            executeLogViewContent(args, callbackContext);
                        }
                        catch (JSONException e)
                        {
                            //e.printStackTrace();
                            Log.w(TAG, "error JSON", e);
                        }
                        callbackContext.success(); // Thread-safe.
                    }
                });
                Log.i(TAG,"Fin del llamado al action logViewContent");
                return true;
    
            }
            else if (action.equals("logEventSearch"))
            {
                Log.d( TAG, args.toString(4) );
    
                cordova.getThreadPool().execute(new Runnable()
                {
                    public void run()
                    {
                        try
                        {
                            executeEventSearch(args, callbackContext);
                        }
                        catch (JSONException e)
                        {
                            //e.printStackTrace();
                            Log.w(TAG, "error JSON", e);
                        }
                        callbackContext.success(); // Thread-safe.
                    }
                });
                
                return true;
    
            }
            else if (action.equals("logEventProductCartAdd"))
            {
                Log.d( TAG, args.toString(4) );
    
                cordova.getThreadPool().execute(new Runnable()
                {
                    public void run()
                    {
                        try
                        {
                            executeEventProductCartAdd(args, callbackContext);
                        }
                        catch (JSONException e)
                        {
                            //e.printStackTrace();
                            Log.w(TAG, "error JSON", e);
                        }
                        callbackContext.success(); // Thread-safe.
                    }
                });
                
                return true;
    
            }
            else if (action.equals("logEventProductCustomize"))
            {
                Log.d( TAG, args.toString(4) );
    
                cordova.getThreadPool().execute(new Runnable()
                {
                    public void run()
                    {
                        try
                        {
                            executeEventProductCustomize(args, callbackContext);
                        }
                        catch (JSONException e)
                        {
                            //e.printStackTrace();
                            Log.w(TAG, "error JSON", e);
                        }
                        callbackContext.success(); // Thread-safe.
                    }
                });
    
                return true;
    
            }
            else if (action.equals("logEventProductPurchase"))
            {
                Log.d( TAG, args.toString(4) );
    
                cordova.getThreadPool().execute(new Runnable()
                {
                    public void run()
                    {
                        try
                        {
                            executeEventProductPurchase(args, callbackContext);
                        }
                        catch (JSONException e)
                        {
                            //e.printStackTrace();
                            Log.w(TAG, "error JSON", e);
                        }
                        callbackContext.success(); // Thread-safe.
                    }
                });
    
                return true;
    
            }
            else if (action.equals("logEventInitiateCheckout"))
            {
                Log.d( TAG, args.toString(4) );
    
                cordova.getThreadPool().execute(new Runnable()
                {
                    public void run()
                    {
                        try
                        {
                            executeEventInitiateCheckout(args, callbackContext);
                        }
                        catch (JSONException e)
                        {
                            //e.printStackTrace();
                            Log.w(TAG, "error JSON", e);
                        }
                        callbackContext.success(); // Thread-safe.
                    }
                });
    
                return true;
    
            }
            else if (action.equals("logPurchase")) {
                /*
                 * While calls to logEvent can be made to register purchase events,
                 * there is a helper method that explicitly takes a currency indicator.
                 */
                if (args.length() != 2) {
                    callbackContext.error("Invalid arguments");
                    return true;
                }
                BigDecimal value = new BigDecimal(args.getString(0));
                String currency = args.getString(1);
                logger.logPurchase(value, Currency.getInstance(currency));
                callbackContext.success();
                return true;
    
            } 

        callbackContext.error("No existe método: " + action);
        return false;
    }



    //public void logViewContentEvent (String contentType, String contentData, String contentId, String currency, double price)
    private void executeLogViewContent(JSONArray args, CallbackContext callbackContext) throws JSONException 
    {
        if (args.length() == 0)
        {
            // Not enough parameters
            callbackContext.error("Invalid arguments");
            return;
        }

        String cntType = args.getString(0);
        String cntData = args.getString(1);
        String cntId   = args.getString(2);

        Bundle params = new Bundle();
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, cntType);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, cntData);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, cntId);

        if ( args.length() > 2 )
        {
            String cntCurr = args.getString(3);
            Double cntAmnt = args.getDouble(4);

            if ( null != cntCurr && null != cntAmnt ) {
                params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, cntCurr);

                logger.logEvent(AppEventsConstants.EVENT_NAME_VIEWED_CONTENT, cntAmnt, params);
            }
            else
            {
                logger.logEvent(AppEventsConstants.EVENT_NAME_VIEWED_CONTENT, params);
            }
        }
        else
        {
            logger.logEvent(AppEventsConstants.EVENT_NAME_VIEWED_CONTENT, params);
        }

        callbackContext.success();
    }

    private void executeEventSearch(JSONArray args, CallbackContext callbackContext) throws JSONException
    {
        String cntType = args.getString(0);
        String cntData = args.getString(1);
        String cntId   = args.getString(2);

        String cntSrch = args.getString(3);
        boolean cntSucc = Boolean.valueOf( args.getString(4) );

        Bundle params = new Bundle();
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, cntType);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, cntData);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, cntId);

        params.putString(AppEventsConstants.EVENT_PARAM_SEARCH_STRING, cntSrch);
        params.putInt(AppEventsConstants.EVENT_PARAM_SUCCESS, cntSucc ? 1 : 0);

        logger.logEvent(AppEventsConstants.EVENT_NAME_SEARCHED, params);
    }

    private void executeEventProductCartAdd(JSONArray args, CallbackContext callbackContext) throws JSONException
    {
        if (args.length() < 5)
        {
            // Not enough parameters
            callbackContext.error("Invalid arguments");
            return;
        }
        String cntType = args.getString(0);
        String cntData = args.getString(1);
        String cntId   = args.getString(2);
        String cntCurr = args.getString(3);
        Double cntAmnt = args.getDouble(4);

        Bundle params = new Bundle();
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, cntType);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, cntData);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, cntId);
        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, cntCurr);

        logger.logEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_CART, cntAmnt, params);

        callbackContext.success();
    }

    private void executeEventProductCustomize(JSONArray args, CallbackContext callbackContext) throws JSONException
    {
        if (args.length() == 0)
        {
            // Not enough parameters
            callbackContext.error("Invalid arguments");
            return;
        }

        String customParam = args.getString(0);
        String customValue = args.getString(1);

        Bundle params = new Bundle();
        params.putString(customParam, customValue );

        logger.logEvent(AppEventsConstants.EVENT_NAME_CUSTOMIZE_PRODUCT, params);

        callbackContext.success();
    }

    private void executeEventProductPurchase(JSONArray args, CallbackContext callbackContext) throws JSONException
    {
        if (args.length() == 0)
        {
            // Not enough parameters
            callbackContext.error("Invalid arguments");
            return;
        }

        String cntType = args.getString(0);
        String cntData = args.getString(1);
        String cntId   = args.getString(2);
        Currency cntCurr = Currency.getInstance(args.getString(3));
        BigDecimal cntAmnt = BigDecimal.valueOf(args.getDouble(4));

        Bundle params = new Bundle();
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, cntType);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, cntData);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, cntId);

        logger.logPurchase(cntAmnt, cntCurr, params);

        callbackContext.success();
    }

    private void executeEventInitiateCheckout(JSONArray args, CallbackContext callbackContext) throws JSONException
    {
        if (args.length() == 0)
        {
            // Not enough parameters
            callbackContext.error("Invalid arguments");
            return;
        }

        String cntType = args.getString(0);
        String cntData = args.getString(1);
        String cntId   = args.getString(2);
        String cntCurr = args.getString(3);
        Double cntAmnt = args.getDouble(4);
        Integer cntNumItms = args.getInt(5);
        Boolean cntPayInfo = Boolean.valueOf( args.getString(6) );

        Bundle params = new Bundle();
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, cntType);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, cntData);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, cntId);
        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, cntCurr);

        params.putInt   (AppEventsConstants.EVENT_PARAM_NUM_ITEMS , cntNumItms);
        params.putInt   (AppEventsConstants.EVENT_PARAM_PAYMENT_INFO_AVAILABLE , cntPayInfo ? 1 : 0 );

        logger.logEvent(AppEventsConstants.EVENT_NAME_INITIATED_CHECKOUT, cntAmnt, params);

        callbackContext.success();
    }


    /**
     * This function assumes logger is an instance of AppEventsLogger and has been
     * created using AppEventsLogger.newLogger() call.
     */
    public void logViewContentEvent (String contentType, String contentData, String contentId, String currency, double price) {

        Bundle params = new Bundle();
        params.putString(EVENT_PARAM_CONTENT_TYPE, contentType);
        params.putString(EVENT_PARAM_CONTENT, contentData);
        params.putString(EVENT_PARAM_CONTENT_ID, contentId);
        params.putString(EVENT_PARAM_CURRENCY, currency);
        logger.logEvent(EVENT_NAME_VIEWED_CONTENT, price, params);
    }
    /**
     * This function assumes logger is an instance of AppEventsLogger and has been
     * created using AppEventsLogger.newLogger() call.
     */

     /*
        Se registra un clic en un anuncio de una plataforma de terceros dentro de tu aplicación.
     */ 
    public void logAdClickEvent (String adType) {

        Bundle params = new Bundle();
        Log.i(TAG,"PARAMS adType: "+adType);
        params.putString(EVENT_PARAM_AD_TYPE, adType);
        logger.logEvent(EVENT_NAME_AD_CLICK, params);
    }


    /*Se marca un evento tipo firabase cuando se hace click en algun boton o imagen o seccion dentro de la app*/ 

    /**
     * This function assumes logger is an instance of AppEventsLogger and has been
     * created using AppEventsLogger.newLogger() call.
    */
    public void logEventForFacebook (String eventName, JSONObject parameters) throws JSONException{

        Bundle params = new Bundle();
        Log.i(TAG,"PARAMS eventName: "+eventName);
        Iterator<String> iter = parameters.keys();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            try{
                String value = parameters.getString(key);
                Log.i(TAG,"parameters key: "+key);
                Log.i(TAG,"parameters value: "+value);
                params.putString(key, value);    
            }catch (JSONException e) {
                Log.w(TAG, "El parametro no es string para la key" + key);
            }
        }
        logger.logEvent(eventName, params);
    }
    // DEEP LINKS DIFERIDOS  
    private void executeGetDeferredApplink(JSONArray args,
                                           final CallbackContext callbackContext) {
        Log.i(TAG,"DEEP LINKS DIFERIDOS EXECUTE");
        AppLinkData.fetchDeferredAppLinkData(cordova.getActivity().getApplicationContext(),
                new AppLinkData.CompletionHandler() {
                    @Override
                    public void onDeferredAppLinkDataFetched(
                            AppLinkData appLinkData) {
                        PluginResult pr;
                        if (appLinkData == null) {

                            pr = new PluginResult(PluginResult.Status.OK, "");
                        } else {                    
                            //Log.i("URL deeplink Diferido: "+(appLinkData.getTargetUri()).toString());
                            pr = new PluginResult(PluginResult.Status.OK, appLinkData.getTargetUri().toString());
                        }
                        pr.setKeepCallback(true);
                        callbackContext.sendPluginResult(pr);
                        return;
                    }
                });
    }
    
    private void enableHybridAppEvents() {
        try {
            Context appContext = cordova.getActivity().getApplicationContext();
            Resources res = appContext.getResources();
            int enableHybridAppEventsId = res.getIdentifier("fb_hybrid_app_events", "bool", appContext.getPackageName());
            boolean enableHybridAppEvents = enableHybridAppEventsId != 0 && res.getBoolean(enableHybridAppEventsId);
            if (enableHybridAppEvents) {
                AppEventsLogger.augmentWebView((WebView) this.webView.getView(), appContext);
                Log.d(TAG, "FB Hybrid app events are enabled");
            } else {
                Log.d(TAG, "FB Hybrid app events are not enabled");
            }
        } catch (Exception e) {
            Log.d(TAG, "FB Hybrid app events cannot be enabled");
        }
    }  
}
