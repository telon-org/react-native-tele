/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import React, { useEffect, useRef, useState } from 'react';
import {
  SafeAreaView,
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  Platform,
  Alert,
} from 'react-native';
// @ts-ignore
import { Endpoint, Call } from './src';
import { check, request, PERMISSIONS, RESULTS } from 'react-native-permissions';

const requestPermissions = async () => {
  if (Platform.OS === 'android') {
    const permissions = [
      PERMISSIONS.ANDROID.READ_PHONE_STATE,
      PERMISSIONS.ANDROID.CALL_PHONE,
      PERMISSIONS.ANDROID.READ_CALL_LOG,
      PERMISSIONS.ANDROID.READ_CONTACTS,
    ];
    for (const perm of permissions) {
      const result = await check(perm);
      if (result !== RESULTS.GRANTED) {
        await request(perm);
      }
    }
  }
};

const App = () => {
  const [phoneNumber, setPhoneNumber] = useState('');
  const [currentCall, setCurrentCall] = useState<Call | null>(null);
  const [callState, setCallState] = useState<string>('IDLE');
  const [isInCall, setIsInCall] = useState(false);
  const endpointRef = useRef<Endpoint | null>(null);

  useEffect(() => {
    (async () => {
      await requestPermissions();
      const endpoint = new Endpoint();
      endpointRef.current = endpoint;
      try {
        await endpoint.start({ ReplaceDialer: false, Permissions: false });
        console.log('Telephony module started successfully');
      } catch (e) {
        Alert.alert('Error', 'Failed to start telephony module: ' + e);
      }
      endpoint.on('call_received', (call: Call) => {
        console.log('Call received:', call);
        setCurrentCall(call);
        setIsInCall(true);
        setCallState('INCOMING');
      });
      endpoint.on('call_changed', (call: Call) => {
        console.log('Call changed:', call.getState());
        setCurrentCall(call);
        setCallState(call.getState());
        if (call.getState() === 'PJSIP_INV_STATE_DISCONNECTED') {
          setIsInCall(false);
          setCurrentCall(null);
          setCallState('IDLE');
        }
      });
      endpoint.on('call_terminated', (_call: Call) => {
        console.log('Call terminated');
        setIsInCall(false);
        setCurrentCall(null);
        setCallState('IDLE');
      });
    })();
    return () => {
      // Clean up listeners if needed
    };
  }, []);

  const makeCall = async () => {
    if (!phoneNumber) {
      Alert.alert('Error', 'Please enter a phone number');
      return;
    }
    try {
      const call = await endpointRef.current?.makeCall(1, phoneNumber, {}, {});
      setCurrentCall(call || null);
      setIsInCall(true);
      setCallState('OUTGOING');
      console.log('Call initiated:', call);
    } catch (e) {
      Alert.alert('Error', 'Failed to make call: ' + e);
    }
  };

  const answerCall = async () => {
    if (currentCall && endpointRef.current) {
      try {
        await endpointRef.current.answerCall(currentCall);
        setCallState('CONNECTED');
        console.log('Call answered');
      } catch (e) {
        Alert.alert('Error', 'Failed to answer call: ' + e);
      }
    }
  };

  const hangupCall = async () => {
    if (currentCall && endpointRef.current) {
      try {
        await endpointRef.current.hangupCall(currentCall);
        setIsInCall(false);
        setCurrentCall(null);
        setCallState('IDLE');
        console.log('Call hung up');
      } catch (e) {
        Alert.alert('Error', 'Failed to hang up: ' + e);
      }
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.body}>
        <Text style={styles.title}>react-native-tele Example</Text>
        <Text style={styles.subtitle}>Version 2.0.1 - React Native 0.80</Text>
        
        <Text style={styles.label}>Phone Number:</Text>
        <TextInput
          style={styles.input}
          value={phoneNumber}
          onChangeText={setPhoneNumber}
          placeholder="Enter phone number"
          keyboardType="phone-pad"
        />
        
        <Text style={styles.label}>Call State: {callState}</Text>
        
        {!isInCall ? (
          <TouchableOpacity style={styles.button} onPress={makeCall}>
            <Text style={styles.buttonText}>Make Call</Text>
          </TouchableOpacity>
        ) : (
          <View style={styles.row}>
            {callState === 'INCOMING' && (
              <TouchableOpacity style={[styles.button, styles.answerButton]} onPress={answerCall}>
                <Text style={styles.buttonText}>Answer</Text>
              </TouchableOpacity>
            )}
            <TouchableOpacity style={[styles.button, styles.hangupButton]} onPress={hangupCall}>
              <Text style={styles.buttonText}>Hang Up</Text>
            </TouchableOpacity>
          </View>
        )}
        
        {currentCall && (
          <View style={styles.callInfo}>
            <Text style={styles.callInfoTitle}>Call Information:</Text>
            <Text>Remote: {currentCall.getRemoteNumber() || currentCall.getRemoteUri()}</Text>
            <Text>Direction: {currentCall["_direction"]}</Text>
            <Text>Duration: {currentCall.getFormattedTotalDuration()}</Text>
            <Text>State: {currentCall.getState()}</Text>
          </View>
        )}
      </View>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  body: {
    flex: 1,
    padding: 24,
    justifyContent: 'center',
  },
  title: {
    fontSize: 28,
    fontWeight: 'bold',
    marginBottom: 8,
    textAlign: 'center',
    color: '#333',
  },
  subtitle: {
    fontSize: 16,
    marginBottom: 32,
    textAlign: 'center',
    color: '#666',
  },
  label: {
    fontSize: 16,
    marginBottom: 8,
    fontWeight: '600',
    color: '#333',
  },
  input: {
    borderWidth: 1,
    borderColor: '#ddd',
    borderRadius: 8,
    padding: 16,
    marginBottom: 20,
    backgroundColor: '#fff',
    fontSize: 16,
  },
  button: {
    padding: 16,
    borderRadius: 8,
    alignItems: 'center',
    marginVertical: 8,
    marginRight: 8,
    flex: 1,
  },
  answerButton: {
    backgroundColor: '#4CAF50',
  },
  hangupButton: {
    backgroundColor: '#F44336',
  },
  buttonText: {
    color: '#fff',
    fontWeight: 'bold',
    fontSize: 16,
  },
  row: {
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  callInfo: {
    marginTop: 32,
    padding: 16,
    backgroundColor: '#e8f4fd',
    borderRadius: 8,
    borderLeftWidth: 4,
    borderLeftColor: '#2196F3',
  },
  callInfoTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 12,
    color: '#333',
  },
});

export default App;
