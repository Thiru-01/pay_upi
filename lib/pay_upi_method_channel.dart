import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:pay_upi/model/UpiTransaction.dart';

import 'pay_upi_platform_interface.dart';

/// An implementation of [PayUpiPlatform] that uses method channels.
class MethodChannelPayUpi extends PayUpiPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('pay_upi');

  @override
  Future<List<Object?>> getInstalledUpiApplications(
      List<String> upiAppsPkgNames) async {
    return await methodChannel.invokeMethod('getUpiApps', upiAppsPkgNames);
  }

  @override
  Future<UpiTransactionResponse> initiateTransaction(
      UpiTransactionRequest request) async {
    Object? response = await methodChannel.invokeMethod(
        'initiateTransaction', request.toMap());
    if (response != null && response is Map) {
      return UpiTransactionResponse.ofNative(response: response);
    }
    throw Exception("Transaction failed");
  }

  @override
  Stream intentStream() {
    final eventChannel = const EventChannel('pay_upi_event');
    return eventChannel.receiveBroadcastStream();
  }
}
