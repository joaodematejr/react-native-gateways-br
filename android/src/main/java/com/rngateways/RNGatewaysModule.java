package com.rngateways;

import android.content.Context;
import android.widget.Toast;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class RNGatewaysModule extends ReactContextBaseJavaModule {

  PagSeguro pagSeguro = new PagSeguro();

  private final ReactApplicationContext reactContext;
  Context context = getReactApplicationContext();

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
  public void getTerminalSerialNumber(String gateway, Promise promise) {
    gateway = gateway.toUpperCase();
    switch (gateway){
      case "PAGSEGURO":
        promise.resolve(pagSeguro.getTerminalSerialNumber());
        break;
      default:
        promise.resolve("Nenhum gateway encontrado!!!");
    }
  }

  /*MÉTODO PARA PEGAR O SERIAL*/
  @ReactMethod
  public void getLibVersion(String gateway, Promise promise) {
    gateway = gateway.toUpperCase();
    switch (gateway){
      case "PAGSEGURO":
        promise.resolve(pagSeguro.getLibVersion());
        break;
      default:
        promise.resolve("Nenhum gateway encontrado!!!");
    }
  }

  /*CRIA A IDENTIFICAÇÃO DO APLICATIVO*/
  @ReactMethod
  public void setAppIdendification(String gateway, String name, String version, Promise promise) {
    gateway = gateway.toUpperCase();
    switch (gateway){
      case "PAGSEGURO":
        promise.resolve(pagSeguro.setAppIdendification(context, name, version));
        break;
      default:
        promise.resolve("Nenhum gateway encontrado!!!");
    }
  }

  /*VERIFICAR AUTENTICAÇÃO*/
  @ReactMethod
  public void checkAuthentication(String gateway, Promise promise) {
    gateway = gateway.toUpperCase();
    switch (gateway){
      case "PAGSEGURO":
        promise.resolve(pagSeguro.checkAuthentication(context));
        break;
      default:
        promise.resolve("Nenhum gateway encontrado!!!");
    }
  }

  /*CALCULAR PARCELAS*/
  @ReactMethod
  public void calculateInstallments(String gateway, String value, Promise promise) {
    gateway = gateway.toUpperCase();
    switch (gateway){
      case "PAGSEGURO":
        promise.resolve(pagSeguro.calculateInstallments(value));
        break;
      default:
        promise.resolve("Nenhum gateway encontrado!!!");
    }
  }

  /*REIMPRESSÃO DA VIA DO CLIENTE*/
  @ReactMethod
  public void reprintCustomerReceipt(String gateway, Promise promise) {
    gateway = gateway.toUpperCase();
    switch (gateway){
      case "PAGSEGURO":
        promise.resolve(pagSeguro.reprintCustomerReceipt());
        break;
      default:
        promise.resolve("Nenhum gateway encontrado!!!");
    }
  }

  /*REIMPRESSÃO DA VIA DO ESTABELECIMENTO*/
  @ReactMethod
  public void reprintStablishmentReceipt(String gateway, Promise promise) {
    gateway = gateway.toUpperCase();
    switch (gateway){
      case "PAGSEGURO":
        promise.resolve(pagSeguro.reprintStablishmentReceipt());
        break;
      default:
        promise.resolve("Nenhum gateway encontrado!!!");
    }
  }

}
