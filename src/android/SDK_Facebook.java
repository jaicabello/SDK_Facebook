package cordova.plugin.facebookplugin;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

/**
 * This class echoes a string called from JavaScript.
 */
public class SDK_Facebook extends CordovaPlugin {

    @Override
    public boolean logEventSDKFacebook(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        AppEventsLogger logger = AppEventsLogger.newLogger(this.cordova.getActivity());
        //event name that we want to track
         if (action.equals("logEvent")) {
             logger.logEvent(AppEventsConstants.EVENT_NAME_VIEWED_CONTENT);
             return true;
         }
         return false;
    }


}
