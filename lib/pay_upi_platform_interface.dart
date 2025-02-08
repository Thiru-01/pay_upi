import 'package:pay_upi/model/UpiTransaction.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'pay_upi_method_channel.dart';

abstract class PayUpiPlatform extends PlatformInterface {
  /// Constructs a PayUpiPlatform.
  PayUpiPlatform() : super(token: _token);

  static final Object _token = Object();

  static PayUpiPlatform _instance = MethodChannelPayUpi();

  /// The default instance of [PayUpiPlatform] to use.
  ///
  /// Defaults to [MethodChannelPayUpi].
  static PayUpiPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [PayUpiPlatform] when
  /// they register themselves.
  static set instance(PayUpiPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Stream<dynamic> intentStream();

  Future<List<Object?>> getInstalledUpiApplications(List<String> upiAppsPkgNames);

  Future<UpiTransactionResponse> initiateTransaction(UpiTransactionRequest request);
}
