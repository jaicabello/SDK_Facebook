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
        logger = AppEventsLogger.newLogger(cordova.getActivity().getApplicationContext());

            if (action.equals("logViewContentEvent")) {
                try {
                    logger.logEvent(AppEventsConstants.EVENT_NAME_VIEWED_CONTENT);
                    return true;
                } catch (Exception e) {
                    //TODO: handle exception
                    callbackContext.error("Error ejecutando action: " + e);
                    return false;
                }
                
            }
            if(action.equals("logAdClickEvent")){
               try {
                    logAdClickEvent(args[0]);
                    PluginResult pluginResult = new PluginResult(PluginResult.Status.OK);
                    callbackContext.sendPluginResult(pluginResult);
                    return true;
               } catch (Exception e) {
                    //TODO: handle exception
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
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, contentType);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, contentData);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, contentId);
        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, currency);
        logger.logEvent(AppEventsConstants.EVENT_NAME_VIEWED_CONTENT, price, params);
    }
    /**
     * This function assumes logger is an instance of AppEventsLogger and has been
     * created using AppEventsLogger.newLogger() call.
     */
    public void logAdClickEvent (String adType) {

        logger = AppEventsLogger.newLogger(cordova.getActivity().getApplicationContext());
        Bundle params = new Bundle();
        params.putString(AppEventsConstants.EVENT_PARAM_AD_TYPE, adType);
        logger.logEvent(AppEventsConstants::EVENT_NAME_AD_CLICK, params);
    }
}
