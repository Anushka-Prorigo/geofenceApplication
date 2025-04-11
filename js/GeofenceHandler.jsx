import React, { useState, useEffect } from 'react';
import { Alert, DeviceEventEmitter } from 'react-native';
import { NativeModules } from 'react-native';

const { GeofenceModule } = NativeModules;

const GeofenceHandler = () => {
    const [transitionMessage, setTransitionMessage] = useState('');
    const [isReady, setIsReady] = useState(false);

    useEffect(() => {
        const checkReactNativeContext = async () => {
            try {
                console.log('Checking if React Native context is ready...');
                // Simulate a delay to ensure React Native context is ready
                await new Promise((resolve) => setTimeout(resolve, 1000));
                setIsReady(true);
                console.log('React Native context is ready.');
            } catch (error) {
                console.error('Error checking React Native context:', error);
            }
        };

        checkReactNativeContext();
    }, []);

    useEffect(() => {
        if (!isReady) return;

        const addGeofence = () => {
            console.log('Adding Geofence...');
            GeofenceModule.addGeofence(
                18.565999,
                73.775532,
                100,
                (response) => {
                    console.log('Geofence Callback:', response);
                    console.log('Geofence Setup', response);
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
    }, [isReady]);

    return transitionMessage;
};

export default GeofenceHandler;
