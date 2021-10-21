import React, {useEffect} from 'react';
import {
  Text,
  ScrollView,
  View,
  StyleSheet,
  TouchableOpacity,
  NativeEventEmitter,
} from 'react-native';

import RNGateways from 'react-native-gateways';


export default function App() {
  
  const exemploJson = {
    amount: 10 * 100,
    installmentType: 1,
    installments: RNGateways.INSTALLMENT_TYPE_A_VISTA,
    type: RNGateways.PAYMENT_PIX,
    userReference: 'PAGAMENTO',
    printReceipt: false,
    gateway: 'pagseguro',
  };

  useEffect(() => {
    /* const eventEmitter = new NativeEventEmitter(RNGateways);
    eventListener = eventEmitter.addListener('eventPayments', event => {
      console.log(event); // "someValue"
    }); */
  }, []);

  useEffect(() => {
    return () => {
      /* eventListener.remove(); */
    };
  }, []);

  useEffect(() => {
    return () => {
      /* eventListener.remove(); */
    };
  }, []);


  useEffect(() => {
    console.log(RNGateways.PAYMENT_PIX)
  }, []);

  useEffect(() => {
    handleIdendification();
  }, []);

  async function handleActionModule() {
    RNGateways.show('tesre');
  }

  async function handleActionSerial() {
    RNGateways.getTerminalSerialNumber('pagseguro').then(
      result => {
        console.log(result);
      },
      error => {
        console.log(error);
      },
    );
  }

  async function handleActionVersion() {
    RNGateways.getLibVersion('pagseguro').then(
      result => {
        console.log(result);
      },
      error => {
        console.log(error);
      },
    );
  }

  async function handleIdendification() {
    RNGateways.setAppIdendification('pagseguro', 'appTeste', '0').then(
      result => {
        console.log(result);
      },
      error => {
        console.log(error);
      },
    );
  }

  async function handleCheckAuthentication() {
    RNGateways.checkAuthentication('pagseguro').then(
      result => {
        console.log(result);
      },
      error => {
        console.log(error);
      },
    );
  }
  async function handleCalculateInstallments() {
    RNGateways.calculateInstallments('pagseguro', '300000').then(
      result => {
        console.log(result);
      },
      error => {
        console.log(error);
      },
    );
  }

  async function handlereprint() {
    RNGateways.reprintCustomerReceipt('pagseguro').then(
      result => {
        console.log(result);
      },
      error => {
        console.log(error);
      },
    );
  }

  async function handlereprintStablishment() {
    RNGateways.reprintStablishmentReceipt('pagseguro').then(
      result => {
        console.log(result);
      },
      error => {
        console.log(error);
      },
    );
  }

  async function handleInitializeAndActivatePinpad() {
    RNGateways.initializeAndActivatePinpad('pagseguro', '403938').then(
      result => {
        console.log(result);
      },
      error => {
        console.log(error);
      },
    );
  }

  async function handlePayment() {
    let jsonStr = JSON.stringify(exemploJson);
    RNGateways.doPayment('pagseguro', jsonStr).then(
      result => {
        console.log(result);
      },
      error => {
        console.log(error);
      },
    );
  }

  async function handleCancelOperation() {
    RNGateways.cancelOperation('pagseguro').then(
      result => {
        console.log(result);
      },
      error => {
        console.log(error);
      },
    );
  }

  async function handleAbortNFC() {
    RNGateways.cancelOperation('pagseguro').then(
      result => {
        console.log(result);
      },
      error => {
        console.log(error);
      },
    );
  }

  async function handleAbortNFC() {
    RNGateways.cancelReadCard('pagseguro').then(
      result => {
        console.log(result);
      },
      error => {
        console.log(error);
      },
    );
  }

  return (
    <ScrollView style={styles.scrollView}>
      <View style={styles.container}>
        <TouchableOpacity
          style={styles.button}
          onPress={() => handleActionModule()}>
          <Text>Aperte aqui</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={styles.button}
          onPress={() => handleActionSerial()}>
          <Text>Serial</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={styles.button}
          onPress={() => handleActionVersion()}>
          <Text>Versão</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={styles.button}
          onPress={() => handleIdendification()}>
          <Text>Idendification</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={styles.button}
          onPress={() => handleCheckAuthentication()}>
          <Text>Verificar autenticação</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={styles.button}
          onPress={() => handleCalculateInstallments()}>
          <Text>Calcular parcelas</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={styles.button}
          onPress={() => handleInitializeAndActivatePinpad()}>
          <Text>Ativar Pinpad</Text>
        </TouchableOpacity>
        <TouchableOpacity style={styles.button} onPress={() => handlePayment()}>
          <Text>Pagamento</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={styles.button}
          onPress={() => handleCancelOperation()}>
          <Text>Cancelar Pagamento</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={styles.button}
          onPress={() => handleAbortNFC()}>
          <Text>Cancelar Leitura Cartão</Text>
        </TouchableOpacity>
        <TouchableOpacity style={styles.button} onPress={() => handlereprint()}>
          <Text>Reimpressão Cliente</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={styles.button}
          onPress={() => handlereprintStablishment()}>
          <Text>Reimpressão Estabelecimento</Text>
        </TouchableOpacity>
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  button: {
    alignItems: 'center',
    backgroundColor: '#DDDDDD',
    padding: 15,
  },
  scrollView: {
    marginHorizontal: 20,
  },
});
