import { AppRegistry } from 'react-native';
import App from './App';
import { name as appName } from './app.json';

console.log("Before registering App component");
AppRegistry.registerComponent(appName, () => App);
console.log("After registering App component");
