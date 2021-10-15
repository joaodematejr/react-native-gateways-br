package com.rngateways;

import android.content.Context;
import android.os.Build;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;

import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPag;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagAppIdentification;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagPrintResult;
import br.com.uol.pagseguro.plugpagservice.wrapper.PlugPagPrinterListener;

public class PagSeguro {

    private PlugPagAppIdentification appIdentification;
    private PlugPag plugPag;

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
            status = "Ativado";
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
}
