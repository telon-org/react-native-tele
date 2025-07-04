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
import { Endpoint, Call } from '../';
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
      } catch (e) {
        Alert.alert('Error', 'Failed to start telephony module: ' + e);
      }
      endpoint.on('call_received', (call: Call) => {
        setCurrentCall(call);
        setIsInCall(true);
        setCallState('INCOMING');
      });
      endpoint.on('call_changed', (call: Call) => {
        setCurrentCall(call);
        setCallState(call.getState());
        if (call.getState() === 'PJSIP_INV_STATE_DISCONNECTED') {
          setIsInCall(false);
          setCurrentCall(null);
          setCallState('IDLE');
        }
      });
      endpoint.on('call_terminated', (_call: Call) => {
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
    } catch (e) {
      Alert.alert('Error', 'Failed to make call: ' + e);
    }
  };

  const answerCall = async () => {
    if (currentCall && endpointRef.current) {
      try {
        await endpointRef.current.answerCall(currentCall);
        setCallState('CONNECTED');
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
      } catch (e) {
        Alert.alert('Error', 'Failed to hang up: ' + e);
      }
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.body}>
        <Text style={styles.title}>react-native-tele Example</Text>
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
            <Text style={styles.buttonText}>Call</Text>
          </TouchableOpacity>
        ) : (
          <View style={styles.row}>
            {callState === 'INCOMING' && (
              <TouchableOpacity style={styles.button} onPress={answerCall}>
                <Text style={styles.buttonText}>Answer</Text>
              </TouchableOpacity>
            )}
            <TouchableOpacity style={styles.button} onPress={hangupCall}>
              <Text style={styles.buttonText}>Hang Up</Text>
            </TouchableOpacity>
          </View>
        )}
        {currentCall && (
          <View style={styles.callInfo}>
            <Text>Remote: {currentCall.getRemoteNumber() || currentCall.getRemoteUri()}</Text>
            <Text>Direction: {currentCall["_direction"]}</Text>
            <Text>Duration: {currentCall.getFormattedTotalDuration()}</Text>
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
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 24,
    textAlign: 'center',
  },
  label: {
    fontSize: 16,
    marginBottom: 8,
  },
  input: {
    borderWidth: 1,
    borderColor: '#ccc',
    borderRadius: 6,
    padding: 12,
    marginBottom: 16,
    backgroundColor: '#fff',
  },
  button: {
    backgroundColor: '#007AFF',
    padding: 16,
    borderRadius: 6,
    alignItems: 'center',
    marginVertical: 8,
    marginRight: 8,
    flex: 1,
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
    marginTop: 24,
    padding: 12,
    backgroundColor: '#e6e6e6',
    borderRadius: 6,
  },
});

export default App;
