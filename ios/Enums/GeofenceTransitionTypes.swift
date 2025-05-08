import CoreLocation

enum GeofenceTransitionType {
    case enter
    case exit
    case dwell
    case unknown

    static func fromRegionState(_ state: CLRegionState) -> GeofenceTransitionType {
        switch state {
        case .inside:
            return .enter
        case .outside:
            return .exit
        case .unknown:
            return .unknown
        @unknown default:
            return .unknown
        }
    }
}
