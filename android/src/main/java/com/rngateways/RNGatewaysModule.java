package com.rngateways;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.widget.Toast;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.HashMap;
import java.util.Map;

import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPag;

public class RNGatewaysModule extends ReactContextBaseJavaModule {

    PagSeguro pagSeguro = new PagSeguro(null);

    private final ReactApplicationContext reactContext;
    Context context = getReactApplicationContext();

    public RNGatewaysModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    private PackageInfo getPackageInfo() throws Exception {
        return getReactApplicationContext().getPackageManager().getPackageInfo(getReactApplicationContext().getPackageName(), 0);
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();

        constants.put("PAYMENT_CREDITO", PlugPag.TYPE_CREDITO);
        constants.put("PAYMENT_DEBITO", PlugPag.TYPE_DEBITO);
        constants.put("PAYMENT_VOUCHER", PlugPag.TYPE_VOUCHER);
        constants.put("PAYMENT_QRCODE", PlugPag.TYPE_QRCODE);
        constants.put("PAYMENT_PIX", PlugPag.TYPE_PIX);
        constants.put("PAYMENT_PREAUTO", PlugPag.TYPE_PREAUTO);

        constants.put("INSTALLMENT_TYPE_A_VISTA", PlugPag.INSTALLMENT_TYPE_A_VISTA);
        constants.put("INSTALLMENT_TYPE_PARC_VENDEDOR", PlugPag.INSTALLMENT_TYPE_PARC_VENDEDOR);
        constants.put("INSTALLMENT_TYPE_PARC_COMPRADOR", PlugPag.INSTALLMENT_TYPE_PARC_COMPRADOR);

        constants.put("OPERATION_ABORT", PlugPag.OPERATION_ABORT);
        constants.put("OPERATION_ABORTED", PlugPag.OPERATION_ABORTED);
        constants.put("OPERATION_ACTIVATE", PlugPag.OPERATION_ACTIVATE);
        constants.put("OPERATION_CALCULATE_INSTALLMENTS", PlugPag.OPERATION_CALCULATE_INSTALLMENTS);
        constants.put("OPERATION_CHECK_AUTHENTICATION", PlugPag.OPERATION_CHECK_AUTHENTICATION);
        constants.put("OPERATION_DEACTIVATE", PlugPag.OPERATION_DEACTIVATE);
        constants.put("OPERATION_GET_APPLICATION_CODE", PlugPag.OPERATION_GET_APPLICATION_CODE);
        constants.put("OPERATION_GET_LIB_VERSION", PlugPag.OPERATION_GET_LIB_VERSION);
        constants.put("OPERATION_GET_READER_INFOS", PlugPag.OPERATION_GET_READER_INFOS);
        constants.put("OPERATION_GET_USER_DATA", PlugPag.OPERATION_GET_USER_DATA);
        constants.put("OPERATION_HAS_CAPABILITY", PlugPag.OPERATION_HAS_CAPABILITY);
        constants.put("OPERATION_INVALIDATE_AUTHENTICATION", PlugPag.OPERATION_INVALIDATE_AUTHENTICATION);
        constants.put("OPERATION_NFC_ABORT", PlugPag.OPERATION_NFC_ABORT);
        constants.put("OPERATION_NFC_READ", PlugPag.OPERATION_NFC_READ);
        constants.put("OPERATION_NFC_WRITE", PlugPag.OPERATION_NFC_WRITE);
        constants.put("OPERATION_PAYMENT", PlugPag.OPERATION_PAYMENT);
        constants.put("OPERATION_PRINT", PlugPag.OPERATION_PRINT);
        constants.put("OPERATION_QUERY_LAST_APPROVED_TRANSACTION", PlugPag.OPERATION_QUERY_LAST_APPROVED_TRANSACTION);
        constants.put("OPERATION_REFUND", PlugPag.OPERATION_REFUND);
        constants.put("OPERATION_REPRINT_CUSTOMER_RECEIPT", PlugPag.OPERATION_REPRINT_CUSTOMER_RECEIPT);
        constants.put("ACTION_POST_OPERATION", PlugPag.ACTION_POST_OPERATION);
        constants.put("ACTION_PRE_OPERATION", PlugPag.ACTION_PRE_OPERATION);
        constants.put("ACTION_UPDATE", PlugPag.ACTION_UPDATE);
        constants.put("APN_SERVICE_CLASS_NAME", PlugPag.APN_SERVICE_CLASS_NAME);
        constants.put("APN_SERVICE_PACKAGE_NAME", PlugPag.APN_SERVICE_PACKAGE_NAME);
        constants.put("AUTHENTICATION_FAILED", PlugPag.AUTHENTICATION_FAILED);
        constants.put("COMMUNICATION_ERROR", PlugPag.COMMUNICATION_ERROR);
        constants.put("ERROR_CODE_OK", PlugPag.ERROR_CODE_OK);
        constants.put("MIN_PRINTER_STEPS", PlugPag.MIN_PRINTER_STEPS);
        constants.put("NFC_SERVICE_CLASS_NAME", PlugPag.NFC_SERVICE_CLASS_NAME);
        constants.put("NFC_SERVICE_PACKAGE_NAME", PlugPag.NFC_SERVICE_PACKAGE_NAME);
        constants.put("NO_PRINTER_DEVICE", PlugPag.NO_PRINTER_DEVICE);
        constants.put("NO_TRANSACTION_DATA", PlugPag.NO_TRANSACTION_DATA);
        constants.put("SERVICE_CLASS_NAME", PlugPag.SERVICE_CLASS_NAME);
        constants.put("SERVICE_PACKAGE_NAME", PlugPag.SERVICE_PACKAGE_NAME);
        constants.put("SMART_RECHARGE_SERVICE_CLASS_NAME", PlugPag.SMART_RECHARGE_SERVICE_CLASS_NAME);
        constants.put("SMART_RECHARGE_SERVICE_PACKAGE_NAME", PlugPag.SMART_RECHARGE_SERVICE_PACKAGE_NAME);

        constants.put("RET_OK", PlugPag.RET_OK);

        String appVersion;
        try {
            appVersion = getPackageInfo().versionName;
        } catch (Exception e) {
            appVersion = "unkown";
        }

        constants.put("appVersion", appVersion);
        return constants;

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
        switch (gateway) {
            case "PAGSEGURO":
                pagSeguro.getTerminalSerialNumber(promise);
                break;
            default:
                promise.resolve("Nenhum gateway encontrado!!!");
        }
    }

    /*MÉTODO PARA PEGAR O SERIAL*/
    @ReactMethod
    public void getLibVersion(String gateway, Promise promise) {
        gateway = gateway.toUpperCase();
        switch (gateway) {
            case "PAGSEGURO":
                pagSeguro.getLibVersion(promise);
                break;
            default:
                promise.resolve("Nenhum gateway encontrado!!!");
        }
    }

    /*CRIA A IDENTIFICAÇÃO DO APLICATIVO*/
    @ReactMethod
    public void setAppIdendification(String gateway, String name, String version, Promise promise) {
        gateway = gateway.toUpperCase();
        switch (gateway) {
            case "PAGSEGURO":
                pagSeguro.setAppIdendification(context, name, version, promise);
                break;
            default:
                promise.resolve("Nenhum gateway encontrado!!!");
        }
    }

    /*VERIFICAR AUTENTICAÇÃO*/
    @ReactMethod
    public void checkAuthentication(String gateway, Promise promise) {
        gateway = gateway.toUpperCase();
        switch (gateway) {
            case "PAGSEGURO":
                pagSeguro.checkAuthentication(promise);
                break;
            default:
                promise.resolve("Nenhum gateway encontrado!!!");
        }
    }

    /*CALCULAR PARCELAS*/
    @ReactMethod
    public void calculateInstallments(String gateway, String value, Promise promise) {
        gateway = gateway.toUpperCase();
        switch (gateway) {
            case "PAGSEGURO":
                pagSeguro.calculateInstallments(value, promise);
                break;
            default:
                promise.resolve("Nenhum gateway encontrado!!!");
        }
    }

    /*REIMPRESSÃO DA VIA DO CLIENTE*/
    @ReactMethod
    public void reprintCustomerReceipt(String gateway, Promise promise) {
        gateway = gateway.toUpperCase();
        switch (gateway) {
            case "PAGSEGURO":
                pagSeguro.reprintCustomerReceipt(promise);
                break;
            default:
                promise.resolve("Nenhum gateway encontrado!!!");
        }
    }

    /*REIMPRESSÃO DA VIA DO ESTABELECIMENTO*/
    @ReactMethod
    public void reprintStablishmentReceipt(String gateway, Promise promise) {
        gateway = gateway.toUpperCase();
        switch (gateway) {
            case "PAGSEGURO":
                pagSeguro.reprintStablishmentReceipt(promise);
                break;
            default:
                promise.resolve("Nenhum gateway encontrado!!!");
        }
    }

    /*ATIVA TERMINAL*/
    @ReactMethod
    public void initializeAndActivatePinpad(String gateway, String code, Promise promise) {
        gateway = gateway.toUpperCase();
        if (!code.isEmpty()) {
            switch (gateway) {
                case "PAGSEGURO":
                    pagSeguro.initializeAndActivatePinpad(code, promise);
                    break;
                default:
                    promise.resolve("Nenhum gateway encontrado!!!");
            }
        }
    }

    /*PAGAMENTO*/
    @ReactMethod
    public void doPayment(String gateway, String jsonStr, Promise promise) {
        gateway = gateway.toUpperCase();
        if (!jsonStr.isEmpty()) {
            switch (gateway) {
                case "PAGSEGURO":
                    promise.resolve(pagSeguro.doPayment(jsonStr, promise));
                    break;
                default:
                    promise.resolve("Nenhum gateway encontrado!!!");
            }
        }
    }

    /*CANCELAR PAGAMENTO*/
    @ReactMethod
    public void cancelOperation(String gateway, Promise promise) {
        gateway = gateway.toUpperCase();
        switch (gateway) {
            case "PAGSEGURO":
                promise.resolve(pagSeguro.cancelOperation());
                break;
            default:
                promise.resolve("Nenhum gateway encontrado!!!");
        }
    }

    /*CANCELAR LEITURA DO CARTÃO*/
    @ReactMethod
    public void cancelReadCard(String gateway, Promise promise) {
        gateway = gateway.toUpperCase();
        switch (gateway) {
            case "PAGSEGURO":
                promise.resolve(pagSeguro.cancelReadCard());
                break;
            default:
                promise.resolve("Nenhum gateway encontrado!!!");
        }
    }

    /*REEMBOLSAR PAGAMENTO*/
    @ReactMethod
    public void reversePayment(String gateway, final String code, final String id, final Promise promise) {
        gateway = gateway.toUpperCase();
        switch (gateway) {
            case "PAGSEGURO":
                promise.resolve(pagSeguro.reversePayment(code, id, promise));
                break;
            default:
                promise.resolve("Nenhum gateway encontrado!!!");
        }
    }

    /*MÉTODO PARA LER ID DO CARTÃO*/
    @ReactMethod
    public void readNFCCardClean(String gateway, final int slot, final Promise promise) {
        gateway = gateway.toUpperCase();
        switch (gateway) {
            case "PAGSEGURO":
                pagSeguro.readNFCCardClean(slot, promise);
                break;
            default:
                promise.resolve("Nenhum gateway encontrado!!!");
        }
    }

    /*MÉTODO PARA ESCREVER ID DO CARTÃO*/
    @ReactMethod
    public void writeToNFCCardClean(String gateway, int slot, String info, Promise promise) {
        gateway = gateway.toUpperCase();
        switch (gateway) {
            case "PAGSEGURO":
                pagSeguro.writeToNFCCardClean(slot, info, promise);
                break;
            default:
                promise.resolve("Nenhum gateway encontrado!!!");
        }
    }

}
