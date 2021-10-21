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


    /*MÉTODO PARA PEGAR O SERIAL*/
    public String getTerminalSerialNumber() {
        String deviceSerial = null;
        try {
            deviceSerial = (String) Build.class.getField("SERIAL").get(null);
        } catch (IllegalAccessException e) {
            return "Error !!! " + e.getMessage();
        } catch (NoSuchFieldException e) {
            return "Error !!! " + e.getMessage();
        }
        return deviceSerial;
    }

    /*MÉTODO PARA PEGAR A VERSÃO DA BIBLIOTECA*/
    public String getLibVersion() {
        String version = null;
        try {
            version = plugPag.getLibVersion();
        } catch (Exception e) {
            return "Error !!! " + e.getMessage();
        }
        return version;
    }

    /*CRIA A IDENTIFICAÇÃO DO APLICATIVO*/
    public String setAppIdendification(Context context, String name, String version) {
        String status = null;
        try {
            appIdentification = new PlugPagAppIdentification(name, version);
            plugPag = new PlugPag(context, appIdentification);
            status = "Identificação Ok!";
        } catch (Exception e) {
            return "Error !!! " + e.getMessage();
        }
        return status;
    }

    /*VERIFICAR AUTENTICAÇÃO*/
    public Boolean checkAuthentication(Context context) {
        Boolean status = null;
        try {
            boolean authenticated = plugPag.isAuthenticated();
            status = authenticated;
        } catch (Exception e) {
            return Boolean.valueOf("Error !!! " + e.getMessage());
        }
        return status;
    }

    /*CALCULAR PARCELAS*/
    public WritableArray calculateInstallments(String value) {
        WritableArray array = Arguments.createArray();
        try {
            String[] installments = plugPag.calculateInstallments(value);
            for (String installment : installments) {
                array.pushString(installment);
            }
            return array;
        } catch (Exception e) {
            e.printStackTrace();
            array.pushString(String.valueOf(e));
            return array;
        }

    }

    /*REIMPRESSÃO DA VIA DO ESTABELECIMENTO*/
    public String reprintStablishmentReceipt() {
        try {
            PlugPagPrintResult result = plugPag.reprintStablishmentReceipt();
            return result.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error !!! " + e.getMessage();
        }
    }

    /*REIMPRESSÃO DA VIA DO CLIENTE*/
    public String reprintCustomerReceipt() {
        try {
            PlugPagPrintResult result = plugPag.reprintCustomerReceipt();
            return result.getMessage();
        } catch (Exception e) {
            return "Error !!! " + e.getMessage();
        }
    }


    /*ATIVA TERMINAL*/
    public String initializeAndActivatePinpad(String activationCode, Context context) {
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
            return String.valueOf(initResult.getResult());
        } catch (ExecutionException e) {
            return "Error !!! " + e.getMessage();
        } catch (InterruptedException e) {
            return "Error !!! " + e.getMessage();
        }
    }

    /*PAGAMENTO*/
    public String doPayment(String jsonStr, final Promise promise) {
        final PlugPagPaymentData paymentData = JsonParseUtils.getPlugPagPaymentDataFromJson(jsonStr);
        if (paymentData.getType() == plugPag.TYPE_PIX){
            return "PIX";
        }else{
            return "Cartão";
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

    /*CANCELAR LEITURA DO CARTÃO*/
    public String cancelReadCard() {
        try {
            PlugPagNFCResult result = plugPag.abortNFC();
            if (result.getResult() == -1) {
                return "Não foi possível cancelar a operação.";
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

    /*MÉTODO PARA LER ID DO CARTÃO*/
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

    /*MÉTODO PARA LER ID DO CARTÃO*/
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
