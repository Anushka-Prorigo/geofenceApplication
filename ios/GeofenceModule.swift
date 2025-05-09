import Foundation
import React
  
  @objc(GeofenceModule)
  class GeofenceModule: RCTEventEmitter, GeofenceCallbackProtocol {
    
    private var hasListeners = false
    private var geofenceHandler: GeofenceHandler
    private var geofenceManager: GeofenceManager
    
    override init() {
      let eventProcessor = GeofenceHandler()
      
      // Pass eventProcessor to geofenceManager
      self.geofenceManager = GeofenceManager(eventProcessor: eventProcessor)
      self.geofenceHandler = eventProcessor
      super.init()
      
      // Set module callback in manager
      geofenceManager.moduleCallback = self
    }
    func onGeofenceResult(_ result: GeofenceResult) {
      guard hasListeners else { return }
      print("Module received geofence result: \(result)")
      
      // Send event to React Native
      sendEvent(withName: "onGeofenceTransition", body: [
        "latitude": result.latitude,
        "longitude": result.longitude,
        "timestamp": result.timestamp,
        "state": result.state
      ])
    }

    func onGeofenceError(_ error: GeofenceError) {
        guard hasListeners else {
            print("No listeners available for geofence error.")
            return
        }

        print("Emitting Geofence Error Event to React Native:", error)

        sendEvent(withName: "onGeofenceTransitionError", body: [
            "error": [
                "code": error.code,
                "message": error.message
            ]
        ])
    }

    
    override static func requiresMainQueueSetup() -> Bool {
      return false
    }
    
    override func supportedEvents() -> [String]! {
      return ["onGeofenceTransition","onGeofenceTransitionError"]
    }
    
    override func startObserving() {
      hasListeners = true
      print("GeofenceModule: Start observing events!")
    }
    
    override func stopObserving() {
      hasListeners = false
    }
    
    @objc(startMonitoringGeofence:withLongitude:withRadius:)
    func startMonitoringGeofence(lat: Double, lon: Double, radius: Double) {
      geofenceManager.startMonitoringGeofence(lat: lat, lon: lon, radius: radius)
    }
  }

