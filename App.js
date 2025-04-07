import React from 'react';
import { Text, View } from 'react-native';
import useGeofenceHandler from './js/GeofenceHandler';

const App = () => {
    const transitionMessage = useGeofenceHandler();

    return (
        <View>
            <Text>Geofence Transition Message: {transitionMessage}</Text>
        </View>
    );
};

export default App;
