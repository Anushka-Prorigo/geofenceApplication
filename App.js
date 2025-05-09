import React, { useState } from 'react';
import { View, Text, StyleSheet, Platform } from 'react-native';
import GeofenceManager from './js/GeofenceManager'; // iOS Geofence Manager
import GeofenceHandler from './js/GeofenceHandler'; // Android Geofence Handler

const App = () => {
  const [geofenceState, setGeofenceState] = useState('Waiting for geofence...');
  const GeofenceComponent = Platform.OS === 'ios' ? GeofenceManager : GeofenceHandler;

  return (
    <View style={styles.container}>
      <GeofenceComponent setGeofenceState={setGeofenceState} />

      <Text style={styles.title}>Geofencing ({Platform.OS.toUpperCase()})</Text>
      <Text style={styles.stateText}>{geofenceState}</Text>
    </View>
  );
};

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
