protocol GeofenceEventProcessingProtocol {
    func processGeofenceEvent(state: CLRegionState, locationManager: CLLocationManager, region: CLRegion?)
}
