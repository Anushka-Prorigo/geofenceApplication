import { useEffect } from 'react';
import { NativeModules, NativeEventEmitter } from 'react-native';

const { GeofenceModule } = NativeModules;
const geofenceEmitter = new NativeEventEmitter(GeofenceModule);

const GeofenceManager = ({ setGeofenceState }) => {
  useEffect(() => {
    // **Step 1: Subscribe to error events FIRST**
    const errorSubscription = geofenceEmitter.addListener(
      'onGeofenceTransitionError',
      (event) => {
        console.error("🛑 Geofence Error Event Received:", event);
        if (event.error) {
          setGeofenceState(`Error (${event.error.code}): ${event.error.message}`);
        } else {
          setGeofenceState('An unknown geofence error occurred.');
        }
      }
    );

    // **Step 2: Subscribe to transition events**
    const transitionSubscription = geofenceEmitter.addListener(
      'onGeofenceTransition',
      (event) => {
        console.log("✅ Received Transition Event:", event);
        setGeofenceState(
          `State: ${event.state}, Latitude: ${event.latitude}, Longitude: ${event.longitude}, Time: ${event.timestamp}`
        );
      }
    );

    // **Step 3: Only call startMonitoringGeofence AFTER subscriptions are set up**
    const latitude = 19.0176147;
    const longitude = 72.8561644;
    const radius = 100;

    GeofenceModule.startMonitoringGeofence(latitude, longitude, radius);

    return () => {
      transitionSubscription.remove();
      errorSubscription.remove();
    };
  }, []);

  return null;
};

export default GeofenceManager;
