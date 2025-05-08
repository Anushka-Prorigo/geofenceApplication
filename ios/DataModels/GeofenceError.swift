public struct GeofenceError: Codable {
  let code: Int
  let message: String
  init(code: Int, message: String) {
          self.code = code
          self.message = message
      }
}
