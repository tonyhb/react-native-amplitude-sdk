package nl.springtree.reactnativeamplitudesdk;

import com.amplitude.api.Amplitude;

import android.app.Activity;
import android.app.Application;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class AmplitudeSDKAndroid extends ReactContextBaseJavaModule {

	private Activity mActivity = null;
	private Application mApplication = null;

	public AmplitudeSDKAndroid(ReactApplicationContext reactContext, Application mApplication) {
		super(reactContext);
		this.mActivity = getCurrentActivity();
		this.mApplication = mApplication;
	}

	@Override
	public String getName() {
		return "AmplitudeSDKAndroid";
	}

	@ReactMethod
	public void initialize(String apiKey) {
		Amplitude.getInstance().trackSessionEvents(true);
		Amplitude.getInstance().initialize(getCurrentActivity(), apiKey).enableForegroundTracking(this.mApplication);
	}

	@ReactMethod
	public void setUserId(String id) {
		Amplitude.getInstance().setUserId(id);
	}

	@ReactMethod
	public void setUserProperties(ReadableMap properties) {
		try {
			JSONObject jProperties = convertReadableToJsonObject(properties);
			Amplitude.getInstance().setUserProperties(jProperties);
		} catch (JSONException e) {
			return;
		}
	}

	@ReactMethod
	public void logEvent(String identifier) {
		Amplitude.getInstance().logEvent(identifier);
	}

	@ReactMethod
	public void logEventWithProps(String identifier, ReadableMap properties) {

		try {
			JSONObject jProperties = convertReadableToJsonObject(properties);
			Amplitude.getInstance().logEvent(identifier, jProperties);
		} catch (JSONException e) {
			return;
		}

	}

	@ReactMethod
	public void logRevenue(String productIdentifier, int quantity, double amount) {
		Amplitude.getInstance().logRevenue(productIdentifier, quantity, amount);
	}

	public static JSONObject convertReadableToJsonObject(ReadableMap map) throws JSONException{
		JSONObject jsonObj = new JSONObject();
		ReadableMapKeySetIterator it = map.keySetIterator();

		while (it.hasNextKey()) {
			String key = it.nextKey();
			ReadableType type = map.getType(key);
			switch (type) {
				case Map:
					jsonObj.put(key, convertReadableToJsonObject(map.getMap(key)));
					break;
				case String:
					jsonObj.put(key, map.getString(key));
					break;
				case Number:
					jsonObj.put(key, map.getDouble(key));
					break;
				case Boolean:
					jsonObj.put(key, map.getBoolean(key));
					break;
				case Null:
					jsonObj.put(key, null);
					break;
			}
		}
		return jsonObj;
	}

}
