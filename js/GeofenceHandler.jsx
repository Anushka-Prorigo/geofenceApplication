import React, { useState, useEffect } from 'react';
import { Alert, DeviceEventEmitter } from 'react-native';
import { NativeModules } from 'react-native';

const { GeofenceModule } = NativeModules;

const GeofenceHandler = () => {
    const [transitionMessage, setTransitionMessage] = useState('');

    useEffect(() => {
        const addGeofence = () => {
            console.log('Adding Geofence...');
            GeofenceModule.addGeofence(
                18.565999,
                73.775532,
                100,
                (response) => {
                    console.log('Geofence Callback:', response);
                    Alert.alert('Geofence Setup', response);
                }
            );
        };

        addGeofence();

        const subscription = DeviceEventEmitter.addListener(
            'GeofenceTransition',
            (message) => {
                console.log('Received Transition:', message);
                setTransitionMessage(message);
                Alert.alert('Geofence Transition', message);
            }
        );

        return () => {
            subscription.remove();
        };
    }, []);

    return transitionMessage;
};

export default GeofenceHandler;
