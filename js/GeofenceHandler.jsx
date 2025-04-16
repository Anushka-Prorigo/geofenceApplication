import React, { useEffect } from 'react';
import { DeviceEventEmitter, Alert, Text, View, NativeModules } from 'react-native';

const { GeofenceModule } = NativeModules;

const GeofenceHandler = ({ setTransitionMessage }) => {
    useEffect(() => {
        console.log('Registering GeofenceTransition listener...');
        const subscription = DeviceEventEmitter.addListener(
            'GeofenceTransition',
            (message) => {
                console.log('Received Transition:', message);
                if (message) {
                    setTransitionMessage(message);
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

    return null;
};

const addGeofence = (latitude, longitude, radius) => {
    GeofenceModule.addGeofence(latitude, longitude, radius, (response) => {
        console.log(response);
    });
};

addGeofence(18.565999, 73.775532, 1000);

export default GeofenceHandler;
