import React, { useEffect, useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  NativeModules,
  NativeEventEmitter,
  Platform,
} from 'react-native';

const { GeofenceModule } = NativeModules;
const geofenceEmitter = new NativeEventEmitter(GeofenceModule);

 const App = () =>{
  const [geofenceState, setGeofenceState] = useState('Waiting for geofence...');

  useEffect(() => {
    GeofenceModule.startMonitoringGeofence(19.0176147, 72.8561644, 100);

    const subscription = geofenceEmitter.addListener('onGeofenceTransition', (event) => {
      console.log("🧐 Received Transition Event:", event);
      setGeofenceState(`State: ${event.state}, Latitude: ${event.latitude}, Longitude: ${event.longitude}, Time: ${event.timestamp}`);     });

    return () => {
      subscription.remove();
    };
  }, []);

  return (
    <View style={styles.container}>
      <Text style={styles.title}>iOS Geofencing</Text>
      <Text style={styles.stateText}>{geofenceState}</Text>
    </View>
  );
 }
const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#eef6ff',
    justifyContent: 'center',
    alignItems: 'center',
  },
  title: {
    fontSize: 22,
    fontWeight: 'bold',
    marginBottom: 20,
  },
  stateText: {
    fontSize: 18,
    color: '#333',
  },
});
export default App;
