import React from 'react';
import {Text, View, StyleSheet, TouchableOpacity} from 'react-native';

import RNGateways from 'react-native-gateways';

const exemploJson = {
  amount: 10 * 100,
  installmentType: 1,
  installments: 1,
  type: 1,
  userReference: 'PAGAMENTO',
  printReceipt: false,
  gateway: 'pagseguro',
};

export default function App() {
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

  return (
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
    </View>
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
});
