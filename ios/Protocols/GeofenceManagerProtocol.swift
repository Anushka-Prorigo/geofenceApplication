protocol GeofenceManagerProtocol {
    func startMonitoringGeofence(lat: Double, lon: Double, radius: Double)
    func stopMonitoringGeofence()
}
