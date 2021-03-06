package com.rngateways;

import android.content.Context;
import java.nio.charset.StandardCharsets;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import javax.annotation.Nullable;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPag;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagActivationData;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagAppIdentification;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagEventData;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagEventListener;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagInitializationResult;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagNFCResult;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagNearFieldCardData;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagPaymentData;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagPrintResult;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagTransactionResult;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagVoidData;

public class PagSeguro extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private PlugPagAppIdentification appIdentification;
    private PlugPag plugPag;
    private int countPrint = 0;
    private int countImages = 0;
    private String messageCard = null;
    private int countPassword = 0;
    private String getPassword = null;
    private PlugPagTransactionResult result = null;

    public PagSeguro(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNGateways";
    }


    /*M??TODO PARA PEGAR O SERIAL*/
    public void getTerminalSerialNumber(Promise promise) {
        try {
            String deviceSerial = (String) Build.class.getField("SERIAL").get(null);
            promise.resolve(deviceSerial);
        } catch (IllegalAccessException e) {
            promise.resolve(e);
        } catch (NoSuchFieldException e) {
            promise.reject(e);
        }

    }

    /*M??TODO PARA PEGAR A VERS??O DA BIBLIOTECA*/
    public void getLibVersion(Promise promise) {
        try {
            String version = plugPag.getLibVersion();
            promise.resolve(version);
        } catch (Exception e) {
            promise.reject(e);
        }
    }

    /*CRIA A IDENTIFICA????O DO APLICATIVO*/
    public void setAppIdendification(Context context, String name, String version, Promise promise) {
        try {
            appIdentification = new PlugPagAppIdentification(name, version);
            plugPag = new PlugPag(context, appIdentification);
            String status = "Identifica????o Ok!";
            promise.resolve(status);
        } catch (Exception e) {
            promise.reject(e);
        }
    }

    /*VERIFICAR AUTENTICA????O*/
    public void checkAuthentication(Promise promise) {
        try {
            boolean authenticated = plugPag.isAuthenticated();
            promise.resolve(authenticated);
        } catch (Exception e) {
            promise.reject(e);
        }
    }

    /*CALCULAR PARCELAS*/
    public void calculateInstallments(String value, Promise promise) {
        WritableArray array = Arguments.createArray();
        try {
            String[] installments = plugPag.calculateInstallments(value);
            for (String installment : installments) {
                array.pushString(installment);
            }
            promise.resolve(array);
        } catch (Exception e) {
            e.printStackTrace();
            promise.reject(e);
        }

    }

    /*REIMPRESS??O DA VIA DO ESTABELECIMENTO*/
    public void reprintStablishmentReceipt(Promise promise) {
        try {
            PlugPagPrintResult result = plugPag.reprintStablishmentReceipt();
            promise.resolve(result);
        } catch (Exception e) {
            promise.reject(e);
        }
    }

    /*REIMPRESS??O DA VIA DO CLIENTE*/
    public void reprintCustomerReceipt(Promise promise) {
        try {
            PlugPagPrintResult result = plugPag.reprintCustomerReceipt();
            promise.resolve(result);
        } catch (Exception e) {
            promise.reject(e);
        }
    }


    /*ATIVA TERMINAL*/
    public void initializeAndActivatePinpad(String activationCode, Promise promise) {
        final PlugPagActivationData activationData = new PlugPagActivationData(activationCode);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<PlugPagInitializationResult> callable = new Callable<PlugPagInitializationResult>() {
            @Override
            public PlugPagInitializationResult call() throws Exception {
                return plugPag.initializeAndActivatePinpad(activationData);
            }
        };

        Future<PlugPagInitializationResult> future = executor.submit(callable);
        executor.shutdown();

        try {
            PlugPagInitializationResult initResult = future.get();
            final WritableMap map = Arguments.createMap();
            map.putInt("retCode", initResult.getResult());
            promise.resolve(initResult);
        } catch (ExecutionException e) {
            promise.reject(e);
        } catch (InterruptedException e) {
            promise.reject(e);
        }
    }

    /*PAGAMENTO*/
    public String doPayment(String jsonStr, final Promise promise) {
        final PlugPagPaymentData paymentData = JsonParseUtils.getPlugPagPaymentDataFromJson(jsonStr);
        if (paymentData.getType() == plugPag.TYPE_PIX){
            return "PIX";
        }else{
            return "Cart??o";
        }
    }

    /*CANCELAR PAGAMENTO*/
    public String cancelOperation() {
        try {
            plugPag.abort();
            return "Pagamento Cancelado";
        } catch (Exception e) {
            return "Error !!! " + e.getMessage();
        }
    }

    /*CANCELAR LEITURA DO CART??O*/
    public String cancelReadCard() {
        try {
            PlugPagNFCResult result = plugPag.abortNFC();
            if (result.getResult() == -1) {
                return "N??o foi poss??vel cancelar a opera????o.";
            } else {
                return String.valueOf(result.getResult());
            }
        } catch (Exception e) {
            return "Error !!! " + e.getMessage();
        }
    }

    /*REEMBOLSO PAGAMENTO*/
    public String reversePayment(final String code, final String id, final Promise promise) {
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Runnable runnableTask = new Runnable() {
                @Override
                public void run() {
                    PlugPagTransactionResult transactionResult = plugPag.voidPayment(new PlugPagVoidData(code, id, true));
                    final WritableMap map = Arguments.createMap();
                    map.putInt("retCode", transactionResult.getResult());
                    map.putString("message", transactionResult.getMessage());
                    promise.resolve(map);
                }
            };
            executor.execute(runnableTask);
            executor.shutdown();
            return String.valueOf(result.getResult());
        } catch (Exception e) {
            return "Error !!! " + e.getMessage();
        }
    }

    /*M??TODO PARA LER ID DO CART??O*/
    public void readNFCCardClean(int slot, Promise promise) {
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Runnable runnableTask = new Runnable() {
                @Override
                public void run() {
                    PlugPagNearFieldCardData dataCard = new PlugPagNearFieldCardData();
                    dataCard.setStartSlot(slot);
                    dataCard.setEndSlot(slot);
                    PlugPagNFCResult result = plugPag.readFromNFCCard(dataCard);
                    String returnValue = new String(result.getSlots()[result.getStartSlot()].get("data"), StandardCharsets.UTF_8);

                    if (result.getResult() == -1) {
                        promise.resolve(null);
                    } else {
                        promise.resolve(returnValue);
                    }
                }
            };
            executor.execute(runnableTask);
            executor.shutdown();
        }catch (Exception e){
            promise.resolve(e);
        }
    }

    /*M??TODO PARA LER ID DO CART??O*/
    public void writeToNFCCardClean(int slot, String info, Promise promise) {
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Runnable runnableTask = new Runnable() {
                @Override
                public void run() {
                    byte[] bytes = info.getBytes();

                    PlugPagNearFieldCardData dataCard = new PlugPagNearFieldCardData();
                    dataCard.setStartSlot(slot);
                    dataCard.setEndSlot(slot);
                    dataCard.getSlots()[slot].put("data", bytes);

                    PlugPagNFCResult result = plugPag.writeToNFCCard(dataCard);
                    int returnResult = result.getResult();
                    promise.resolve(returnResult);
                }
            };
            executor.execute(runnableTask);
            executor.shutdown();
        }catch (Exception e){
            promise.resolve(e);
        }
    }


    private void sendEvent(ReactContext reactContext, String eventName, @Nullable boolean params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit("connectionEvent", params);
    }

    public void connection() {
        ConnectivityManager conn = (ConnectivityManager) reactContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = conn.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null ? activeNetwork.isConnectedOrConnecting() : false;
        sendEvent(reactContext, "connectionEvent", isConnected);
    }


}
