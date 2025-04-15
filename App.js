import React, { useState } from 'react';
import { Text, View } from 'react-native';
import GeofenceHandler from './js/GeofenceHandler';

const App = () => {
    const [transitionMessage, setTransitionMessage] = useState('');

    console.log('Rendering App with transitionMessage:', transitionMessage);

    return (
        <View>
            <Text>Geofence Transition Message: {transitionMessage}</Text>
            <GeofenceHandler setTransitionMessage={setTransitionMessage} />
        </View>
    );
};

export default App;
