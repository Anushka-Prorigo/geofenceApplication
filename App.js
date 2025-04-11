import React from 'react';
import { Text, View } from 'react-native';
import GeofenceHandler from './js/GeofenceHandler';

const App = () => {
    const transitionMessage = GeofenceHandler();

    console.log('Rendering App with transitionMessage:', transitionMessage);

    return (
        <View>
            <Text>Geofence Transition Message: {transitionMessage}</Text>
        </View>
    );
};

export default App;
