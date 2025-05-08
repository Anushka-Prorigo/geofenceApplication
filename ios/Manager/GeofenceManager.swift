import Foundation
import CoreLocation
import MapKit
import UIKit

class GeofenceManager: NSObject{
    private let locationManager = CLLocationManager()
    private let geofenceUIManager : GeofenceUIManager
    private let geofenceService : GeofenceService

    private var entryTime: Date?
    private var dwellTimer: Timer?
    
    var radius : Double?
    init(mapView: MKMapView? ,viewController: UIViewController) {
        self.geofenceService = GeofenceService()
        self.geofenceUIManager = GeofenceUIManager(mapView: mapView, viewController: viewController)
    }
    
    func startGeofence(config: GeolocationConfig) {
            guard let lat = config.targetLocation.latitude,
                  let lon = config.targetLocation.longitude,
                  let radius = config.targetLocation.radiusInMeters else { return }
            print("Started geofence at \(lat), \(lon) with radius \(radius)")
            let center = CLLocationCoordinate2D(latitude: lat, longitude: lon)
            let region = CLCircularRegion(center: center, radius: radius, identifier: "Geofence")
            region.notifyOnEntry = true
            region.notifyOnExit = true

            geofenceUIManager.drawGeofenceRegion(center: center, radius: radius)
            geofenceService.startMonitoring(region: region)
        }
    
}

