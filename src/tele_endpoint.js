import React, { Component } from 'react'
import { NativeModules } from 'react-native'
import { EventEmitter } from 'events'

import Call from './call'

const STATE_CONNECTING = 9;
const STATE_RINGING = 2;
const STATE_DIALING = 1;
const STATE_ACTIVE = 4;
const STATE_DISCONNECTED = 7;
const STATE_DISCONNECTING = 10;

/*   
    STATE_DISCONNECTING 10
    STATE_HOLDING 3
    STATE_NEW 0
    STATE_PULLING_CALL 11
    STATE_SELECT_PHONE_ACCOUNT 8
*/


export default class TeleEndpoint extends EventEmitter {

  constructor() {
    console.log("TeleEndpoint.constructor");
    super();

    //this.call=new Call();
    
    this.currentCall=null;

    this.state = {
    };

    //TODO ADD
    DeviceEventEmitter.addListener('teleCallReceived', this._onCallReceived.bind(this));
    DeviceEventEmitter.addListener('teleCallChanged', this._onCallChanged.bind(this));
    DeviceEventEmitter.addListener('teleCallRemoved', this._onCallTerminated.bind(this));

    //TODO REMOVE
    //this.getCurrentCall();

  }

      /**
     * @fires Endpoint#call_received
     * @private
     * @param data {Object}
     */
    _onCallReceived(data) {
      /**
       * TODO
       *
       * @event Endpoint#call_received
       * @property {Call} call
       */
      this.emit("call_received", new Call(data));
  }

    /**
     * @fires Endpoint#call_changed
     * @private
     * @param data {Object}
     */
    _onCallChanged(data) {
      /**
       * TODO
       *
       * @event Endpoint#call_changed
       * @property {Call} call
       */
      this.emit("call_changed", new Call(data));
  }

  /**
   * @fires Endpoint#call_terminated
   * @private
   * @param data {Object}
   */
  _onCallTerminated(data) {
      /**
       * TODO
       *
       * @event Endpoint#call_terminated
       * @property {Call} call
       */
      this.emit("call_terminated", new Call(data));
  }


    /**
     * Returns a Promise that will be resolved once Tele module is initialized.
     * Do not call any function while library is not initialized.
     *
     * @returns {Promise}
     */
    start(configuration) {
      return new Promise(function(resolve, reject) {
          NativeModules.TeleModule.start(configuration, (successful, data) => {
              if (successful) {
                  let calls = [];

                  if (data.hasOwnProperty('calls')) {
                      for (let d of data['calls']) {
                          calls.push(new Call(d));
                      }
                  }

                  let extra = {};

                  for (let key in data) {
                      if (data.hasOwnProperty(key)  && key != "calls") {
                          extra[key] = data[key];
                      }
                  }

                  resolve({
                      calls,
                      ...extra
                  });
              } else {
                  reject(data);
              }
          });
      });
  }


      
      getCurrentCall=()=>{
        console.log("getCurrentCall()");
        NativeModules.TeleModule.getCurrentCall()
      };
          
      //outgoing
      declineCall=(call)=>{
        NativeModules.TeleModule.declineCall()};

      //incoming
      answerCall=(call)=>{ NativeModules.TeleModule.answerCall()};
      hangupCall=(call)=>{ NativeModules.TeleModule.hangupCall()};

      sendEnvelope=(str)=>{ NativeModules.TeleModule.sendEnvelope(str)};


      /*
      hangupCall(call) {
        // TODO: Add possibility to pass code and reason for hangup.
        return new Promise((resolve, reject) => {
            NativeModules.PjSipModule.hangupCall(call.getId(), (successful, data) => {
                if (successful) {
                    resolve(data);
                } else {
                    reject(data);
                }
            });
        });
    }*/


  //async componentDidMount() {
  Rec = async (data) => {
    console.log("Rec!!!", data);

    if (data.action === 'TeleService') {
      console.log("->TeleService");
      if (data.extra1s === 'onCallAdded') {
        console.log("->onCallAdded");
        this.currentCall=new Call({remoteUri:data.extra3s,creationTime:data.extra1l});
        this.currentCall.state='PJSIP_INV_STATE_NULL';
        this.currentCall._state=this.currentCall.state;
      }

      if ((this.currentCall==null)&&((data.extra1s === 'getCurrentCall'))&&(data.extra1i!=0))
      {
        this.currentCall=new Call({remoteUri:data.extra3s,creationTime:data.extra1l});
      }

      if (this.currentCall!=null)
      if ((data.extra1s === 'getCurrentCall')||(data.extra1s === 'onStateChanged')||(data.extra1s === 'onCallAdded')) {
        if (data.extra1i === STATE_CONNECTING) {
          this.currentCall.state='PJSIP_INV_STATE_CALLING';
          this.currentCall._state=this.currentCall.state;
          this.currentCall.incoming=false;
        }
        if (data.extra1i === STATE_RINGING) {
          this.currentCall.state='PJSIP_INV_STATE_INCOMING';
          this.currentCall._state=this.currentCall.state;
          this.currentCall.incoming=true;
        }

        if (data.extra1i === STATE_DIALING) {
          this.currentCall.state='PJSIP_INV_STATE_EARLY';
          this.currentCall._state=this.currentCall.state;
        }
        if (data.extra1i === STATE_ACTIVE) {
          this.currentCall.connectTime=data.extra2l;
          this.currentCall.state='PJSIP_INV_STATE_CONFIRMED';
          this.currentCall._state=this.currentCall.state;
        }
        if (data.extra1i === STATE_DISCONNECTED) {
          this.currentCall.state='PJSIP_INV_STATE_DISCONNECTED';
          this.currentCall._state=this.currentCall.state;
          this._lastReason='PJSIP_SC_OK';
        }
        if (data.extra1i === STATE_DISCONNECTING) {
          //TODO
          this.currentCall.state='PJSIP_INV_STATE_DISCONNECTED';
          this.currentCall._state=this.currentCall.state;
          this._lastReason='PJSIP_SC_OK';
        }



      }

        if (data.extra1s === 'onCallAdded') {
          console.log("tEndpoint.emit.onCallAdded");
          this.emit("call_received", this.currentCall);
        }  
        
      if (data.extra1s === 'onCallRemoved') {
        console.log("tEndpoint.emit.onCallRemoved");
        this.currentCall=null;
        this.emit("call_terminated", this.currentCall);
      }

      if ((data.extra1s === 'onStateChanged')) {
        console.log("tEndpoint.emit.onStateChanged");
        this.emit("call_changed", this.currentCall);
      }
      if ((data.extra1s === 'getCurrentCall')) {
        console.log("tEndpoint.emit.getCurrentCall->call_changed");
        this.emit("call_changed", this.currentCall);
      }

    }

   }

}











    /*
    if (data.state === 'extra_state_out_dialing') {
        pjsip.progress();
    }
    
    if (data.state === 'extra_state_out_active') {
        pjsip.answer();
    }
 
    if (data.state === 'extra_state_out_disconnected') {
        pjsip.hangup();
    }
    */

  /*
    let call={
      getState: (()=>{return "state"}),
      getFormattedConnectDuration: (()=>{return "00:00"}),
      getRemoteName: (()=>{return "remotename"}),
      getRemoteNumber: (()=>{return "79000000000"}),
      getRemoteFormattedNumber: (()=>{return "+7900 000 0000"}),
      getId: (()=>{return 123}),
      isHeld: ()=>{return false},
      isMuted: ()=>{return false},
      isSpeaker: ()=>{return false}
    }
*/
/*
Outgoing BUSY

9->1 -> 4 -> 7
 LOG  Rec {"action": "CallService", "extra1i": 9, "extra1l": 1577437262895, "extra1s": "onCallAdded", "extra2i": 0, "extra2l": 0, "extra2s": null, "extra3s": "tel:89006367756"}
 LOG  Rec {"action": "new_outgoing_call"}
 LOG  Rec {"action": "CallService", "extra1i": 0, "extra1l": 0, "extra1s": "onDetailsChanged", "extra2i": 0, "extra2l": 0, "extra2s": null, "extra3s": "tel:89006367756"}
 LOG  Rec {"action": "CallService", "extra1i": 0, "extra1l": 0, "extra1s": "onDetailsChanged", "extra2i": 0, "extra2l": 0, "extra2s": null, "extra3s": "tel:89006367756"}
 LOG  Rec {"action": "CallService", "extra1i": 0, "extra1l": 0, "extra1s": "onDetailsChanged", "extra2i": 0, "extra2l": 0, "extra2s": null, "extra3s": "tel:89006367756"}
 LOG  Rec {"action": "CallService", "extra1i": 1, "extra1l": 0, "extra1s": "onStateChanged", "extra2i": 0, "extra2l": 0, "extra2s": "", "extra3s": ""}
 LOG  Rec {"action": "phone_state", "incoming_call": false, "state": "extra_state_offhook"}
 LOG  Rec {"action": "phone_state", "incoming_call": false, "state": "extra_state_offhook"}
 LOG  Rec {"action": "CallService", "extra1i": 0, "extra1l": 0, "extra1s": "onDetailsChanged", "extra2i": 0, "extra2l": 0, "extra2s": null, "extra3s": "tel:89006367756"}
 LOG  Rec {"action": "CallService", "extra1i": 7, "extra1l": 0, "extra1s": "onStateChanged", "extra2i": 0, "extra2l": 0, "extra2s": "", "extra3s": ""}
 LOG  Rec {"action": "CallService", "extra1i": 0, "extra1l": 0, "extra1s": "onDetailsChanged", "extra2i": 0, "extra2l": 0, "extra2s": null, "extra3s": "tel:89006367756"}
 LOG  Rec {"action": "CallService", "extra1i": 0, "extra1l": 0, "extra1s": "onCallDestroyed", "extra2i": 0, "extra2l": 0, "extra2s": "", "extra3s": ""}
 LOG  Rec {"action": "CallService", "extra1i": 0, "extra1l": 0, "extra1s": "onCallRemoved", "extra2i": 0, "extra2l": 0, "extra2s": "", "extra3s": ""}
 LOG  Rec {"action": "phone_state", "incoming_call": false, "state": "extra_state_idle"}
 LOG  Rec {"action": "phone_state", "incoming_call": false, "state": "extra_state_idle"}


//ICNOMING
G  Rec {"action": "CallService", "extra1i": 2, "extra1l": 1577437382193, "extra1s": "onCallAdded", "extra2i": 0, "extra2l": 0, "extra2s": null, "extra3s": "tel:%2B79219542499"}
 LOG  Rec {"action": "phone_state", "incoming_call": true, "number": null, "state": "extra_state_ringing"}
 LOG  Rec {"action": "phone_state", "incoming_call": true, "number": "+79219542499", "state": "extra_state_ringing"}



 LOG  Rec {"action": "CallService", "extra1i": 0, "extra1l": 0, "extra1s": "onDetailsChanged", "extra2i": 0, "extra2l": 0, "extra2s": null, "extra3s": "tel:%2B79219542499"}
 LOG  Rec {"action": "CallService", "extra1i": 0, "extra1l": 0, "extra1s": "onDetailsChanged", "extra2i": 0, "extra2l": 0, "extra2s": null, "extra3s": "tel:%2B79219542499"}
 LOG  Rec {"action": "CallService", "extra1i": 0, "extra1l": 0, "extra1s": "onDetailsChanged", "extra2i": 0, "extra2l": 0, "extra2s": null, "extra3s": "tel:%2B79219542499"}
 LOG  Rec {"action": "CallService", "extra1i": 7, "extra1l": 0, "extra1s": "onStateChanged", "extra2i": 0, "extra2l": 0, "extra2s": "", "extra3s": ""}
 LOG  Rec {"action": "CallService", "extra1i": 0, "extra1l": 0, "extra1s": "onDetailsChanged", "extra2i": 0, "extra2l": 0, "extra2s": null, "extra3s": "tel:%2B79219542499"}
 LOG  Rec {"action": "CallService", "extra1i": 0, "extra1l": 0, "extra1s": "onCallDestroyed", "extra2i": 0, "extra2l": 0, "extra2s": "", "extra3s": ""}
 LOG  Rec {"action": "CallService", "extra1i": 0, "extra1l": 0, "extra1s": "onCallRemoved", "extra2i": 0, "extra2l": 0, "extra2s": "", "extra3s": ""}
 LOG  Rec {"action": "phone_state", "incoming_call": false, "state": "extra_state_idle"}
 LOG  Rec {"action": "phone_state", "incoming_call": false, "state": "extra_state_idle"}


	displayName = details.handle.schemeSpecificPart
 */

 /*

    //FIX
    //const call = this.state.call
    let call = {
      getState: (() => { return "state" }),
      getFormattedConnectDuration: (() => { return "00:00" }),
      getRemoteName: (() => { return "remotename" }),
      getRemoteNumber: (() => { return "79000000000" }),
      getRemoteFormattedNumber: (() => { return "+7900 000 0000" }),
      getId: (() => { return 123 }),
      isHeld: () => { return false },
      isMuted: () => { return false },
      isSpeaker: () => { return false }
    }

    let calls = { c1: call };
    //





        let call = {
          state: "state",
          _remoteName: data.extra2s,
          _remoteNumber: data.extra3s,
          _remoteUri: data.extra3s,
          _constructionTime: data.extra1l,
          id: 123,
          held: false,
          muted: false,
          speaker: false,

          getState: (() => { return 5 }),
          getFormattedConnectDuration: (() => { return "00:00" }),
          getRemoteName: (() => { return this._remoteNumber }),
          getRemoteNumber: (() => { return this._remoteNumber }),
          getRemoteFormattedNumber: (() => { return this._remoteNumber }),
          getRemoteUri: (() => { return this._remoteNumber }),
          getId: (() => { return this.id }),
          isHeld: () => { return this.held },
          isMuted: () => { return this.muted },
          isSpeaker: () => { return this.speaker }
        };


        call._remoteNumber = call._remoteNumber;

        if (call._remoteUri) {
          match = call._remoteUri.match(/tel:([^@]+)/);
          if (match) {
            call._remoteNumber = match[1];
          }
        }


        call.getFormattedConnectDuration = (() => (new Date() - this._constructionTime));
        call.getRemoteName = (() => { return this._remoteName; });
        call.getRemoteNumber = (() => { return this._remoteNumber; });
        call.getRemoteFormattedNumber = (() => { return this._remoteNumber; });
*/
        //call._remoteName=data.extra2s;
        //call._remoteNumber=data.extra3s;
        //call._constructionTime=data.extra1l;