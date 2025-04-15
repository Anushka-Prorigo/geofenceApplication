import React, { useEffect } from 'react';
import { DeviceEventEmitter, Alert, Text, View } from 'react-native';

const GeofenceHandler = ({ setTransitionMessage }) => {
    useEffect(() => {
        console.log('Registering GeofenceTransition listener...');
        const subscription = DeviceEventEmitter.addListener(
            'GeofenceTransition',
            (message) => {
                console.log('Received Transition:', message);
                if (message) {
                    setTransitionMessage(message); // Pass the message to App.js
                    Alert.alert('Geofence Transition', message);
                } else {
                    console.error('Received empty or undefined transition message.');
                }
            }
        );

        return () => {
            console.log('Removing GeofenceTransition listener...');
            subscription.remove();
        };
    }, []);

    return null; // No UI is rendered by GeofenceHandler
};

export default GeofenceHandler;
