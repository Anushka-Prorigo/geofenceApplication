import { useEffect } from 'react';
import { Alert, DeviceEventEmitter, NativeModules } from 'react-native';

const { GeofenceModule } = NativeModules;

const GeofenceManager = ({ setTransitionMessage }) => {
  useEffect(() => {
    console.log('Registering GeofenceTransition listener...');
    Alert.alert('Geofence Transition', 'Listener registered successfully');
    
    const subscription = DeviceEventEmitter.addListener('GeofenceTransition', (event) => {
      console.log('Geofence Transition Event Received:', event);
    
      if (event && event.message) {
        setTransitionMessage(event.message);
        Alert.alert('Geofence Transition', event.message);
      } else {
        console.error('Received empty or undefined transition message.');
      }
    });
  
    return () => {
      console.log('Removing GeofenceTransition listener...');
      if (subscription) subscription.remove();
    };
  }, []);

  return null;
};

const addGeofence = (latitude, longitude, radius) => {
  GeofenceModule.addGeofence(latitude, longitude, radius)
    .then(response => {
      console.log('Geofence Added Successfully:', response);
      Alert.alert('Geofence Added', `Geofence added at ${latitude}, ${longitude} with radius ${radius}`,response);
    })
    .catch(error => {
      console.error('Geofence Error:', error);
      Alert.alert('Geofence Error', `Error adding geofence: ${error.message}`);
    });
};

addGeofence(19.0176147, 72.8561644, 100.0);

export default GeofenceManager;
