import React, { useState } from 'react';
import { Text, View ,StyleSheet} from 'react-native';
import GeofenceHandler from './js/GeofenceHandler';

const App = () => {
    const [transitionMessage, setTransitionMessage] = useState('');

    console.log('Rendering App with transitionMessage:', transitionMessage);

    return (
        <View style={styles.container}>
            <Text style={styles.text}>Geofence Transition Message: {transitionMessage}</Text>
            <GeofenceHandler setTransitionMessage={setTransitionMessage} />
        </View>
    );
};
const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: 'white',
    },
    text: {
        fontSize: 18,
        color: 'black',
        textAlign: 'center',
    },
});

export default App;
