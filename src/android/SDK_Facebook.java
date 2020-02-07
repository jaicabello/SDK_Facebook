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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.FacebookSdk;
import com.facebook.CallbackManager;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.widget.ShareDialog;
import com.facebook.share.widget.GameRequestDialog;
import com.facebook.share.widget.MessageDialog;





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
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        FacebookSdk.sdkInitialize(cordova.getActivity().getApplicationContext());
        logger = AppEventsLogger.newLogger(cordova.getActivity().getApplicationContext());
        Log.i(TAG,"Comenzo la ejecuccion del metodo execute");
            if (action.equals("logViewContentEvent")) {
                try {
                    Log.i(TAG,"Se llama al action logViewContentEvent");
                    logger.logEvent(EVENT_NAME_VIEWED_CONTENT);
                    Log.i(TAG,"Fin del llamado al action logViewContentEvent");
                    return true;
                } catch (Exception e) {
                    //TODO: handle exception
                    Log.e(TAG,"ERROR al llamar el action logViewContentEvent");
                    callbackContext.error("Error ejecutando action: " + e);
                    return false;
                }
                
            }
            if(action.equals("logAdClickEvent")){
               try {
                    
                    Log.i(TAG,"Se llama al action logAdClickEvent");
                     Log.i(TAG,"args: "+args.getString(0));
                    logAdClickEvent(args.getString(0));
                    Log.i(TAG,"Fin del llamado al action logAdClickEvent");
                    PluginResult pluginResult = new PluginResult(PluginResult.Status.OK);
                    callbackContext.sendPluginResult(pluginResult);
                    Log.i(TAG,"Fin del llamado al action logAdClickEvent");
                    return true;
               } catch (Exception e) {
                    //TODO: handle exception
                    Log.e(TAG,"ERROR al llamar el action logAdClickEvent");
                    callbackContext.error("Error ejecutando action: " + e);
                    return false;
               }
            }
            
            if(action.equals("logOnButtonClickEvent")){
                try {
                     
                     Log.i(TAG,"Se llama al action logOnButtonClickEvent");
                      Log.i(TAG,"args: "+args.getString(0));
                     logAdClickEvent(args.getString(0));
                     Log.i(TAG,"Fin del llamado al action logOnButtonClickEvent");
                     PluginResult pluginResult = new PluginResult(PluginResult.Status.OK);
                     callbackContext.sendPluginResult(pluginResult);
                     Log.i(TAG,"Fin del llamado al action logOnButtonClickEvent");
                     return true;
                } catch (Exception e) {
                     //TODO: handle exception
                     Log.e(TAG,"ERROR al llamar el action logOnButtonClickEvent");
                     callbackContext.error("Error ejecutando action: " + e);
                     return false;
                }
             }
        callbackContext.error("No existe método: " + action);
        return false;
    }


    /**
     * This function assumes logger is an instance of AppEventsLogger and has been
     * created using AppEventsLogger.newLogger() call.
     */
    public void logViewContentEvent (String contentType, String contentData, String contentId, String currency, double price) {

        logger = AppEventsLogger.newLogger(cordova.getActivity().getApplicationContext());
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

        logger = AppEventsLogger.newLogger(cordova.getActivity().getApplicationContext());
        Bundle params = new Bundle();
        Log.i(TAG,"PARAMS adType: "+adType);
        params.putString(EVENT_PARAM_AD_TYPE, adType);
        logger.logEvent(EVENT_NAME_AD_CLICK, params);
    }

    /**
     * This function assumes logger is an instance of AppEventsLogger and has been
     * created using AppEventsLogger.newLogger() call.
    */

    /*Se marca cuando se hace click en un boton*/ 

    public void logOnButtonClickEvent (String nameButton) {

        logger = AppEventsLogger.newLogger(cordova.getActivity().getApplicationContext());
        Bundle params = new Bundle();
        Log.i(TAG,"nameButton: "+nameButton);
        params.putString("nameButton", nameButton);
        logger.logEvent("onButtonClick", params);
    }
}
