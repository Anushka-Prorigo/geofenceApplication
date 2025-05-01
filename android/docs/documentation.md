### Constants

### Geofence Error Codes

This section defines error codes related to geofencing operations. These constants represent specific issues that may occur during geofencing operations:

- **`ERROR_LOCATION_PERMISSION_DENIED (1001)`**: Indicates that the location permission has been denied by the user.
- **`ERROR_GEOFENCE_NOT_AVAILABLE (1002)`**: Indicates that the geofencing service is not available on the device.
- **`ERROR_GEOFENCE_LIMIT_EXCEEDED (1003)`**: Indicates that the maximum number of geofences has been exceeded.
- **`ERROR_LOCATION_UNAVAILABLE (1004)`**: Indicates that the device's location is currently unavailable.
- **`ERROR_UNKNOWN (1099)`**: Represents an unknown error related to geofencing operations.

This class is final and cannot be instantiated. It is designed to prevent instantiation by making the constructor private.

---

### Enums

### Geofence Transition Type
Enum representing the types of geofence transitions.

This enum provides three types of geofence transitions:
- **ENTER**: Indicates that the user has entered the geofence area.
- **EXIT**: Indicates that the user has exited the geofence area.
- **DWELL**: Indicates that the user is dwelling within the geofence area.

The `fromInt(int)` method maps integer transition values from the `com.google.android.gms.location.Geofence` API to the corresponding `GeofenceTransitionType` enum values.

### Geofence Type

Represents the types of geofences that can be defined.

- **`CIRCLE`**: Represents a circular geofence defined by a center point and a radius.
- **`POLYGON`**: Represents a polygonal geofence defined by a series of connected points.

---

### POJO Classes

### Geofence Config Class

 The `GeofenceConfig` class serves as the base class for all types of geofence configurations. It provides common properties and methods that are shared across different geofence types. This class is abstract, meaning it cannot be instantiated directly and must be extended by subclasses that define specific geofence behaviors.


 #### Fields
 - `private String geofenceId`: A unique identifier for the geofence.
 - `private int accuracyThreshold`: The accuracy threshold for the geofence in meters.
 - `private int dwellStateCoolOffPeriod`: The cool-off period for dwell state transitions in milliseconds.

 #### `abstract GeofenceType getGeofenceType()`
 - **Returns**: The type of the geofence as defined by the `GeofenceType` enum.
 - **Description**: Abstract method that must be implemented by subclasses to define the specific type of geofence.

### CircularGeofenceConfig Class

The `CircularGeofenceConfig` class represents the configuration for a circular geofence. It extends the `GeofenceConfig` class and provides additional properties specific to circular geofences, such as latitude, longitude, and radius.

#### Constructor

#### `CircularGeofenceConfig(double latitude, double longitude, float radius, String geofenceId, int accuracyThreshold, int dwellStateCoolOffPeriod)`
Creates a new instance of the `CircularGeofenceConfig` class.

 #### Parameters:
- `latitude` (`double`): The latitude of the center of the circular geofence.
- `longitude` (`double`): The longitude of the center of the circular geofence.
- `radius` (`float`): The radius of the circular geofence, in meters.
- `geofenceId` (`String`): A unique identifier for the geofence.
- `accuracyThreshold` (`int`): The accuracy threshold for geofence detection.
- `dwellStateCoolOffPeriod` (`int`): The cool-off period for dwell state transitions, in milliseconds.

#### `GeofenceType getGeofenceType()`
Overrides the `getGeofenceType` method in the `GeofenceConfig` class to return the type of the geofence as `GeofenceType.CIRCLE`.

#### Inheritance
The `CircularGeofenceConfig` class inherits from the `GeofenceConfig` class.

#### Geofence Type
This class is specifically designed for circular geofences and always returns `GeofenceType.CIRCLE` when the `getGeofenceType` method is called.
 
### Geofence Result

Represents the result of a geofence event, encapsulating details about the transition type, geofence configuration, and the timestamp of the event.

**Fields:**
- `transitionType` (`GeofenceTransitionType`): The type of geofence transition (e.g., ENTER, EXIT, DWELL).
- `circularGeofenceConfig` (`CircularGeofenceConfig`): The configuration details of the circular geofence associated with this result.
- `timestamp` (`long`): The timestamp of the geofence event in milliseconds since epoch.


- **Parameters:**
  - `transitionType`: The type of geofence transition.
  - `circularGeofenceConfig`: The configuration details of the circular geofence.
  - `timestamp`: The timestamp of the geofence event in milliseconds since epoch.


### Geofence Error
Represents an error related to a geolocation operation. This class encapsulates the error code and the corresponding error message.

---

### Interfaces

### Geofence Callback Listener

The `GeofenceCallbackListener` interface defines a callback mechanism for handling geofence-related events, such as transitions and errors.

#### Methods

##### `onGeofenceTransition(GeofenceResult result)`
This method is invoked when a geofence transition occurs.

- **Parameters:**
    - `result` (`GeofenceResult`): Contains details about the geofence transition, such as the type of transition and the geofence involved.

##### `onGeofenceError(GeofenceError error)`
This method is invoked when an error related to geofencing occurs.

- **Parameters:**
    - `error` (`GeofenceError`): Contains details about the geofence error, including the error code and message.

---

#### GeofencePackage Class
The `GeofencePackage` class is a custom implementation of the `ReactPackage` interface, which is used to register native modules and view managers with a React Native application.

This class is responsible for exposing the `GeofenceModule` to the React Native JavaScript runtime, enabling the use of geofencing functionality in React Native applications.

**Methods:**
- `createNativeModules(ReactApplicationContext reactContext)`: 
    - Returns a list of native modules to be registered with the React Native runtime.
    - Adds an instance of the `GeofenceModule` to the list of native modules.
- `createViewManagers(ReactApplicationContext reactContext)`:
    - Returns a list of view managers to be registered with the React Native runtime.
    - In this implementation, it returns an empty list as no custom view managers are used.

**Usage:**
To use the `GeofencePackage` in a React Native application, register it in the `MainApplication` class of your Android project:

```java
@Override
protected List<ReactPackage> getPackages() {
        return Arrays.<ReactPackage>asList(
                new MainReactPackage(),
                new GeofencePackage() // Register the GeofencePackage
        );
}
```

This ensures that the `GeofenceModule` is available for use in the JavaScript code of your React Native application.

---

### GeofenceModule 

#### Overview
The `GeofenceModule` is a React Native module that provides geofencing capabilities for Android applications. It allows developers to add geofences, handle geofence transitions, and communicate events between the native Android layer and the React Native JavaScript layer.

#### Class: `GeofenceModule`

#### Constructor
- **`GeofenceModule(ReactApplicationContext reactContext)`**
    - Initializes the `GeofenceModule` with the provided React application context.
    - Sets up the `GeofenceHandler` and `GeofenceBroadcastReceiver`.

#### Methods

#### `getName()`
- **Returns:** `String`
    - The name of the module, which is `GeofenceModule`.

#### `addGeofence(CircularGeofenceConfig configData)`
- Adds a geofence with the specified configuration.
- **Parameters:**
    - `configData` (`GeoLocationConfig`):Contains the latitude, longitude, radius, accuracy, geofence id and dwellStateCooloffPeriod for the geofence.
- **Usage:**
    - Call this method to add a geofence without a callback.

#### `addGeofence(CircularGeofenceConfig configData, Callback callback)`
- Adds a geofence with the specified configuration and provides a callback for success or error.
- **Parameters:**
    - `configData` (`CircularGeofenceConfig`): Contains the latitude, longitude, radius, accuracy, geofence id and dwellStateCooloffPeriod for the geofence.
    - `callback` (`Callback`): A callback function to handle success or error messages.
- **Usage:**
    - Call this method to add a geofence and receive feedback via the callback.

#### `onGeofenceTransition(GeofenceResult result)`
- Handles geofence transition events and sends them to the React Native JavaScript layer.
- **Parameters:**
    - `result` (`GeofenceResult`):Contains the timestamp, transitionState and CircularGeofenceConfig data for the geofence.
- **Usage:**
    - This method is triggered automatically when a geofence transition occurs.


#### `sendEventToReactNative(Context context, String eventName, String message)`
- Sends an event from the native Android layer to the React Native JavaScript layer.
- **Parameters:**
    - `context` (`Context`): The application context.
    - `eventName` (`String`): The name of the event to emit.
    - `message` (`String`): The message to send with the event.
- **Usage:**
    - This method is used internally to communicate geofence events to React Native.


