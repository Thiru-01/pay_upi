import 'dart:typed_data';

class UpiAppData {
  final String packageName;
  final String applicationName;
  final Uint8List icon;

  UpiAppData(
      {required this.packageName,
      required this.applicationName,
      required this.icon});

  UpiAppData.ofNative(Map<dynamic, dynamic> nativeObject)
      : packageName = nativeObject["appName"] as String,
        applicationName = nativeObject["packageName"] as String,
        icon = Uint8List.fromList(nativeObject["icon"] as List<int>);
}
