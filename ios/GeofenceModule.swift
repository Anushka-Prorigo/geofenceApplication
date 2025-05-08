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
      guard hasListeners else { return }
      print(" Module received geofence error: \(error)")
      
      sendEvent(withName: "onGeofenceError", body: [
        "error": error
      ])
    }
    
    override static func requiresMainQueueSetup() -> Bool {
      return true
    }
    
    override func supportedEvents() -> [String]! {
      return ["onGeofenceTransition"]
    }
    
    override func startObserving() {
      hasListeners = true
    }
    
    override func stopObserving() {
      hasListeners = false
    }
    
    @objc(startMonitoringGeofence:withLongitude:withRadius:)
    func startMonitoringGeofence(lat: Double, lon: Double, radius: Double) {
      geofenceManager.startMonitoringGeofence(lat: lat, lon: lon, radius: radius)
    }
  }

