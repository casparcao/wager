import 'dart:io';

class Constant {
  Map<String, String> envs = Platform.environment;
  String baseurl() {
    return envs["BASE_URL"] ?? "localhost:8080";
  }
}
