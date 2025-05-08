public struct GeofenceError: Codable {
    let code: String
    let message: String

    init(code: String, message: String) {
        self.code = code
        self.message = message
    }
}
