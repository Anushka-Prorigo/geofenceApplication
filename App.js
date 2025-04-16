import React, { useState } from 'react';
import { View, StyleSheet, Text } from 'react-native';
import GeofenceHandler from './js/GeofenceHandler';

const App = () => {
    const [transitionMessage, setTransitionMessage] = useState('');

    return (
        <View style={styles.container}>
            <Text style={styles.text}>Geofence Transition: {transitionMessage}</Text>
            <GeofenceHandler setTransitionMessage={setTransitionMessage} />
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
    },
    text: {
        fontSize: 16,
        textAlign: 'center',
        margin: 10,
    },
});

export default App;
