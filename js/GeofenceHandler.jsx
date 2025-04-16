import React, { useEffect, useState } from 'react';
import { DeviceEventEmitter, Alert, StyleSheet, View } from 'react-native';
import MapView, { Circle, Marker } from 'react-native-maps';
import { NativeModules } from 'react-native';

const { GeofenceModule } = NativeModules;

const GeofenceHandler = ({ setTransitionMessage }) => {
    const [geofence, setGeofence] = useState({
        latitude: 18.565999, // Default geofence latitude
        longitude: 73.775532, // Default geofence longitude
        radius: 1000, // Default geofence radius in meters
    });

    useEffect(() => {
        console.log('Registering GeofenceTransition listener...');
        const subscription = DeviceEventEmitter.addListener(
            'GeofenceTransition',
            (message) => {
                console.log('Received Transition:', message);
                if (message) {
                    setTransitionMessage(message);
                    Alert.alert('Geofence Transition', message);
                }
            }
        );

        return () => {
            console.log('Removing GeofenceTransition listener...');
            subscription.remove();
        };
    }, []);

    return (
        <View style={styles.container}>
            <MapView
                style={styles.map}
                initialRegion={{
                    latitude: geofence.latitude,
                    longitude: geofence.longitude,
                    latitudeDelta: 0.05, // Zoom level
                    longitudeDelta: 0.05, // Zoom level
                }}
            >
                {/* Marker for the geofence center */}
                <Marker
                    coordinate={{
                        latitude: geofence.latitude,
                        longitude: geofence.longitude,
                    }}
                    title="Geofence Center"
                    description="This is the center of the geofence."
                />

                {/* Circle representing the geofence */}
                <Circle
                    center={{
                        latitude: geofence.latitude,
                        longitude: geofence.longitude,
                    }}
                    radius={geofence.radius} // Radius in meters
                    strokeColor="rgba(0, 150, 255, 0.8)" // Circle border color
                    fillColor="rgba(0, 150, 255, 0.2)" // Circle fill color
                />
            </MapView>
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
    },
    map: {
        flex: 1,
    },
});

export default GeofenceHandler;
