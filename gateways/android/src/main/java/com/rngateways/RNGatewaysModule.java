package com.rngateways;

import android.content.Context;
import android.widget.Toast;

import com.facebook.react.bridge.Callback;
import android.os.Build;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class RNGatewaysModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RNGatewaysModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNGateways";
  }

  @ReactMethod
  public void show(String text) {
    Context context = getReactApplicationContext();
    Toast.makeText(context, text, Toast.LENGTH_LONG).show();
  }

  /*MÉTODO PARA PEGAR O SERIAL*/
  @ReactMethod
  public void getTerminalSerialNumber(Promise promise, String gateway) throws NoSuchFieldException, IllegalAccessException {
    String deviceSerial = (String) Build.class.getField("SERIAL").get(null);
    promise.resolve(deviceSerial);
  }

}
