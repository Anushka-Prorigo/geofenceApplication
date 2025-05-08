import CoreLocation

class GeofenceManager: NSObject,CLLocationManagerDelegate,GeofenceManagerProtocol,GeofenceCallbackProtocol{
  var moduleCallback: GeofenceCallbackProtocol?
  var geofenceHandler: GeofenceHandler
  private let locationManager = CLLocationManager()
  private var monitoredRegion: CLCircularRegion?
  
  init(eventProcessor: GeofenceHandler) {
    self.geofenceHandler = eventProcessor
    super.init()
    self.geofenceHandler.callback = self
    locationManager.delegate = self
    locationManager.requestAlwaysAuthorization()
    locationManager.desiredAccuracy = kCLLocationAccuracyBest

  }

  func startMonitoringGeofence(lat: Double, lon: Double, radius: Double) {
    let authorizationStatus = CLLocationManager.authorizationStatus()
    guard authorizationStatus == .authorizedAlways || authorizationStatus == .authorizedWhenInUse else {
            print(" Permission Denied: Location access is not granted")
            return
        }
    let center = CLLocationCoordinate2D(latitude: lat, longitude: lon)
    let region = CLCircularRegion(center: center, radius: radius, identifier: "Geofence")
    region.notifyOnEntry = true
    region.notifyOnExit = true
    
    monitoredRegion = region
    locationManager.startMonitoring(for: region)
    locationManager.requestState(for: region)
    
    //for display determinenant state
    DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
      self.locationManager.requestState(for: region)
    }
    print("Geofence monitoring started for: \(region.identifier)")
  }
  func stopMonitoringGeofence() {
    return
    
  }
  
  //request geofence states, monitor locations, and manage region tracking
  func locationManager(_ manager: CLLocationManager, didDetermineState state: CLRegionState, for region: CLRegion) {
   geofenceHandler.processGeofenceEvent(state: state, locationManager: locationManager, region: region)
  }
  
  func onGeofenceResult(_ result: GeofenceResult) {
      print("Manager received geofence result: \(result)")

      guard let callback = moduleCallback else {
          print(" moduleCallback is nil Geofence result not forwarded.")
          return
      }

      print("Forwarding geofence result to module...")
      moduleCallback?.onGeofenceResult(result)
  }
  
  func onGeofenceError(_ error: GeofenceError) {
    print("Received geofence error: \(error)")
    moduleCallback?.onGeofenceError(error)
    return
  }
  
}
