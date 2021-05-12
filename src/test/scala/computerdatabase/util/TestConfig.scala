package computerdatabase.util

trait TestConfig {
  final lazy val url = sys.env.apply("API_BASE_URL")
  final lazy val username = sys.env.get("API_USERNAME")
  final lazy val password = sys.env.get("API_PASSWORD")
}