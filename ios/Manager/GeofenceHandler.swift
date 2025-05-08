import CoreLocation

class GeofenceHandler: GeofenceEventProcessingProtocol {
    var callback: GeofenceCallbackProtocol?
    private var dwellTimer: Timer?

    func processGeofenceEvent(state: CLRegionState, locationManager: CLLocationManager, region: CLRegion?) {
        guard let monitoredRegion = region, monitoredRegion.identifier == region?.identifier else { return }

        let transitionType = GeofenceTransitionType.fromRegionState(state)
        print(" Geofence transition detected: \(transitionType)")

        guard let currentLocation = locationManager.location else { return }
        
        let result = GeofenceResult(
            latitude: currentLocation.coordinate.latitude,
            longitude: currentLocation.coordinate.longitude,
            timestamp: DateFormatter.localizedString(from: Date(), dateStyle: .short, timeStyle: .short),
            state: "\(transitionType)"
        )

        // Handle dwell event separately if entering
        if transitionType == .enter {
            startDwellTimer(currentLocation)
        }
      
      //GeofenceHandler forwards the event to GeofenceManager
      callback?.onGeofenceResult(result)
    }

    private func startDwellTimer(_ currentLocation: CLLocation) {
      dwellTimer = Timer.scheduledTimer(withTimeInterval: 10, repeats: false) { [self] _ in
            let dwellResult = GeofenceResult(
                latitude: currentLocation.coordinate.latitude,
                longitude: currentLocation.coordinate.longitude,
                timestamp: DateFormatter.localizedString(from: Date(), dateStyle: .short, timeStyle: .short),
                state: "dwell"
            )
            print(" Dwelling in geofence, passing dwell event")
           callback?.onGeofenceResult(dwellResult)
        }
    }
}
