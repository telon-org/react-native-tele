

# react-native-tele

[![npm version](https://img.shields.io/npm/v/react-native-tele.svg)](https://www.npmjs.com/package/react-native-tele)
[![license](https://img.shields.io/npm/l/react-native-tele.svg)](https://github.com/telefon-one/react-native-tele/blob/master/LICENSE)

A React Native module for telecommunication functionality based on Android's InCall and android.telecom Java APIs. This module enables you to build Android applications that can communicate with calls, such as custom dialers.

## Features

- ✅ **Android Support**: Full support for Android platform
- ✅ **Audio Call Events**: Complete audio call event handling and actions
- ✅ **Background Processing**: Continues working when app goes to background
- ✅ **React Native 0.60+**: Compatible with modern React Native versions (AndroidX support)
- ✅ **Custom Dialer**: Can replace the default Android dialer
- ✅ **Call Management**: Make, answer, hangup, hold, mute, and manage calls
- ✅ **Event System**: Comprehensive event system for call state changes

## Table of Contents

- [Installation](#installation)
- [Quick Start](#quick-start)
- [API Reference](#api-reference)
- [Examples](#examples)
- [Configuration](#configuration)
- [Events](#events)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)

## Installation

### Prerequisites

- React Native 0.60 or higher
- Android SDK
- Android device or emulator

### Step 1: Install the package

```bash
npm install react-native-tele
# or
yarn add react-native-tele
```

### Step 2: Android Setup

Follow the detailed Android installation guide: [Android Installation Guide](docs/installation_android.md)

### Step 3: Permissions

Add the following permissions to your `android/app/src/main/AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.CALL_PHONE" />
<uses-permission android:name="android.permission.READ_CALL_LOG" />
<uses-permission android:name="android.permission.MODIFY_PHONE_STATE" />
<uses-permission android:name="android.permission.READ_CONTACTS" />
```

## Building the Android Library (.aar)

To build the Android library (AAR) for use in other projects or for distribution:

1. Open a terminal and navigate to the root of the repository.
2. Run the following command:

```bash
cd android && ./gradlew bundleReleaseAar
```

3. After a successful build, the generated `.aar` file will be located at:

```
android/app/build/outputs/aar/app-release.aar
```

You can use this file as a standard Android library in your projects.

## Quick Start

### Basic Setup

```javascript
import { Endpoint } from 'react-native-tele';

class MyApp extends Component {
  async componentDidMount() {
    // Initialize the telecommunication module
    const endpoint = new Endpoint();
    
    try {
      // Start the module and get current state
      const state = await endpoint.start({
        ReplaceDialer: false,  // Set to true to replace default dialer
        Permissions: false     // Set to true to request permissions automatically
      });
      
      const { calls, settings } = state;
      console.log('Current calls:', calls);
      console.log('Settings:', settings);
      
      // Subscribe to call events
      this.setupEventListeners(endpoint);
      
    } catch (error) {
      console.error('Failed to start tele module:', error);
    }
  }
  
  setupEventListeners(endpoint) {
    endpoint.on("call_received", (call) => {
      console.log("Incoming call from:", call.getRemoteNumber());
    });
    
    endpoint.on("call_changed", (call) => {
      console.log("Call state changed:", call.getState());
    });
    
    endpoint.on("call_terminated", (call) => {
      console.log("Call ended:", call.getRemoteNumber());
    });
  }
}
```

### Making a Call

```javascript
async makeCall(phoneNumber) {
  try {
    const call = await this.endpoint.makeCall(
      1,                    // SIM slot (1 or 2)
      phoneNumber,          // Phone number to call
      {},                   // Call settings (optional)
      {}                    // Message data (optional)
    );
    
    console.log("Call initiated with ID:", call.getId());
    
    // Listen for this specific call
    this.endpoint.on("call_changed", (updatedCall) => {
      if (call.getId() === updatedCall.getId()) {
        console.log("Our call state:", updatedCall.getState());
      }
    });
    
  } catch (error) {
    console.error("Failed to make call:", error);
  }
}
```

### Answering a Call

```javascript
async answerCall(call) {
  try {
    await this.endpoint.answerCall(call);
    console.log("Call answered successfully");
  } catch (error) {
    console.error("Failed to answer call:", error);
  }
}
```

## API Reference

### Endpoint Class

The main class for managing telecommunication functionality.

#### Methods

##### `start(configuration)`
Initializes the telecommunication module.

**Parameters:**
- `configuration` (Object):
  - `ReplaceDialer` (Boolean): Set to true to replace default dialer
  - `Permissions` (Boolean): Set to true to request permissions automatically

**Returns:** Promise that resolves with current state including calls and settings

##### `makeCall(sim, destination, callSettings, msgData)`
Makes an outgoing call.

**Parameters:**
- `sim` (Number): SIM slot (1 or 2)
- `destination` (String): Phone number to call
- `callSettings` (Object): Call configuration (optional)
- `msgData` (Object): Additional message data (optional)

**Returns:** Promise that resolves with Call instance

##### `answerCall(call)`
Answers an incoming call.

**Parameters:**
- `call` (Call): Call instance to answer

**Returns:** Promise

##### `hangupCall(call)`
Hangs up a call.

**Parameters:**
- `call` (Call): Call instance to hangup

**Returns:** Promise

##### `declineCall(call)`
Declines an incoming call (sends 603 response).

**Parameters:**
- `call` (Call): Call instance to decline

**Returns:** Promise

##### `holdCall(call)`
Puts a call on hold.

**Parameters:**
- `call` (Call): Call instance to hold

**Returns:** Promise

##### `unholdCall(call)`
Takes a call off hold.

**Parameters:**
- `call` (Call): Call instance to unhold

**Returns:** Promise

##### `muteCall(call)`
Mutes a call.

**Parameters:**
- `call` (Call): Call instance to mute

**Returns:** Promise

##### `unMuteCall(call)`
Unmutes a call.

**Parameters:**
- `call` (Call): Call instance to unmute

**Returns:** Promise

##### `useSpeaker(call)`
Enables speaker mode for a call.

**Parameters:**
- `call` (Call): Call instance

**Returns:** Promise

##### `useEarpiece(call)`
Enables earpiece mode for a call.

**Parameters:**
- `call` (Call): Call instance

**Returns:** Promise

### Call Class

Represents a call instance with various properties and methods.

#### Properties

- `id` (Number): Unique call identifier
- `state` (String): Current call state
- `direction` (String): Call direction (incoming/outgoing)
- `remoteNumber` (String): Remote party's phone number
- `remoteName` (String): Remote party's name (if available)

#### Methods

##### `getId()`
Returns the call's unique identifier.

**Returns:** Number

##### `getState()`
Returns the current call state.

**Returns:** String

##### `getRemoteNumber()`
Returns the remote party's phone number.

**Returns:** String

##### `getRemoteName()`
Returns the remote party's name.

**Returns:** String

##### `getTotalDuration()`
Returns the total call duration in seconds.

**Returns:** Number

##### `getConnectDuration()`
Returns the connected call duration in seconds.

**Returns:** Number

##### `isHeld()`
Checks if the call is on hold.

**Returns:** Boolean

##### `isMuted()`
Checks if the call is muted.

**Returns:** Boolean

##### `isSpeaker()`
Checks if speaker mode is enabled.

**Returns:** Boolean

## Events

The Endpoint class extends EventEmitter and provides the following events:

### `call_received`
Emitted when an incoming call is received.

**Callback:** `(call: Call) => void`

### `call_changed`
Emitted when a call's state changes.

**Callback:** `(call: Call) => void`

### `call_terminated`
Emitted when a call ends.

**Callback:** `(call: Call) => void`

### `call_screen_locked` (Android only)
Emitted when the screen is locked during a call.

**Callback:** `(lock: Boolean) => void`

### `connectivity_changed`
Emitted when connectivity status changes.

**Callback:** `(available: Boolean) => void`

## Examples

### Complete Dialer App Example

```javascript
import React, { Component } from 'react';
import { View, Text, TouchableOpacity, TextInput } from 'react-native';
import { Endpoint } from 'react-native-tele';

class DialerApp extends Component {
  state = {
    phoneNumber: '',
    currentCall: null,
    isInCall: false
  };

  async componentDidMount() {
    this.endpoint = new Endpoint();
    
    try {
      await this.endpoint.start({ ReplaceDialer: true });
      this.setupEventListeners();
    } catch (error) {
      console.error('Failed to initialize:', error);
    }
  }

  setupEventListeners() {
    this.endpoint.on("call_received", (call) => {
      this.setState({ 
        currentCall: call, 
        isInCall: true,
        phoneNumber: call.getRemoteNumber() 
      });
    });

    this.endpoint.on("call_changed", (call) => {
      if (call.getState() === "PJSIP_INV_STATE_DISCONNECTED") {
        this.setState({ currentCall: null, isInCall: false });
      }
    });

    this.endpoint.on("call_terminated", (call) => {
      this.setState({ currentCall: null, isInCall: false });
    });
  }

  async makeCall() {
    if (!this.state.phoneNumber) return;
    
    try {
      const call = await this.endpoint.makeCall(1, this.state.phoneNumber);
      this.setState({ currentCall: call, isInCall: true });
    } catch (error) {
      console.error('Call failed:', error);
    }
  }

  async answerCall() {
    if (!this.state.currentCall) return;
    
    try {
      await this.endpoint.answerCall(this.state.currentCall);
    } catch (error) {
      console.error('Answer failed:', error);
    }
  }

  async hangupCall() {
    if (!this.state.currentCall) return;
    
    try {
      await this.endpoint.hangupCall(this.state.currentCall);
    } catch (error) {
      console.error('Hangup failed:', error);
    }
  }

  render() {
    return (
      <View style={{ flex: 1, padding: 20 }}>
        <TextInput
          value={this.state.phoneNumber}
          onChangeText={(text) => this.setState({ phoneNumber: text })}
          placeholder="Enter phone number"
          keyboardType="phone-pad"
          style={{ borderWidth: 1, padding: 10, marginBottom: 20 }}
        />
        
        {!this.state.isInCall ? (
          <TouchableOpacity onPress={this.makeCall}>
            <Text>Call</Text>
          </TouchableOpacity>
        ) : (
          <View>
            <TouchableOpacity onPress={this.answerCall}>
              <Text>Answer</Text>
            </TouchableOpacity>
            <TouchableOpacity onPress={this.hangupCall}>
              <Text>Hangup</Text>
            </TouchableOpacity>
          </View>
        )}
      </View>
    );
  }
}
```

## Configuration

### Replace Default Dialer

To replace the default Android dialer:

```javascript
const state = await endpoint.start({ ReplaceDialer: true });
```

This will prompt the user to set your app as the default dialer.

### Automatic Permissions

To automatically request required permissions:

```javascript
const state = await endpoint.start({ Permissions: true });
```

## Troubleshooting

### Common Issues

1. **NativeModule.TeleModule is null**
   - Rebuild and re-run the app
   - Check that the library was linked correctly
   - For unit testing, mock the native module

2. **Permission Denied**
   - Ensure all required permissions are added to AndroidManifest.xml
   - Request permissions at runtime if needed

3. **Call not working in background**
   - The module continues working in background automatically
   - Check that your app has proper background permissions

### Debug Mode

Enable debug logging by checking the console output. The module provides detailed logs for troubleshooting.

## Contributing

We welcome contributions! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

### Development Setup

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the ISC License - see the [LICENSE](LICENSE) file for details.

## Support

- **Issues**: [GitHub Issues](https://github.com/telefon-one/react-native-tele/issues)
- **Documentation**: [GitHub Wiki](https://github.com/telefon-one/react-native-tele/wiki)
- **Examples**: [Examples Directory](https://github.com/telefon-one/react-native-tele/tree/master/examples)

## Related Projects

- [react-native-replace-dialer](https://github.com/telefon-one/react-native-replace-dialer) - Replace default Android dialer
- [react-native-sip2](https://github.com/telefon-one/react-native-sip2) - SIP communication module

---

**Note**: This module is currently Android-only. iOS support is planned for future releases.






FROM LEGACY installation.md:

# Android installation

## Step 1

TODO: REPLACE TO INCALL SERVICE!!!

Add permissions & service to `android/app/src/main/AndroidManifest.xml`

```xml
<uses-feature android:name="android.hardware.camera" />
<uses-feature android:name="android.hardware.camera.autofocus"/>

<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.RECORD_AUDIO"/>
<uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.WAKE_LOCK"/>
<uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS"/>
<uses-permission android:name="android.permission.CALL_PHONE"/>
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
```

```xml
<application>
    ...
    <service
        android:name=""
        android:enabled="true"
        android:exported="true" />
    ...
</application>
```

## Step 2
```bash
react-native link
```

## Additional step: Ability to answer incoming call without Lock Screen

In `android/app/src/main/java/com/xxx/MainActivity.java`

```java
import android.view.Window;
import android.view.WindowManager;
import android.os.Bundle;
...
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window w = getWindow();
        w.setFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED,
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
        );
    }
```

## If Android targetSdk >= 23

If your Android targetSdk is 23 or above you should grant `android.permission.RECORD_AUDIO` at runtime before making/receiving an audio call.

To check and request Android permissions, please check out [react-native-android-permissions](https://github.com/lucasferreira/react-native-android-permissions).
