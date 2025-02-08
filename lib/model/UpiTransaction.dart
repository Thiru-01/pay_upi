import 'dart:math';

class UpiTransactionResponse {
  UpiTransactionResponse.ofNative({required Map<dynamic, dynamic> response});

  UpiTransactionResponse();
}

class UpiTransactionRequest {
  final double amount;
  final String appPackageName;

  late final String pa;
  late final String pc;

  UpiTransactionRequest(
      {required this.amount,
      required this.appPackageName,
      required Uri qrRequest}) {
    pa = qrRequest.queryParameters["pa"]!;
    pc = qrRequest.queryParameters["pn"]!;
  }

  Map<String, dynamic> toMap() {
    return {
      'am': amount.toString(),
      'pa': pa,
      'pn': pc,
      "app": appPackageName,
      'tr': _generateTxnRef(),
      'tid': _generateTxnId(),
    };
  }

  String _generateTxnId([int length = 20]) {
    const characters =
        'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    final random = Random();
    return List.generate(
            length, (index) => characters[random.nextInt(characters.length)])
        .join();
  }

  String _generateTxnRef({String prefix = 'TXN', int randomLength = 6}) {
    final now = DateTime.now();
    final timestamp = '${now.year}'
        '${now.month.toString().padLeft(2, '0')}'
        '${now.day.toString().padLeft(2, '0')}'
        '${now.hour.toString().padLeft(2, '0')}'
        '${now.minute.toString().padLeft(2, '0')}';

    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
    final random = Random();
    final randomPart = List.generate(randomLength,
        (index) => characters[random.nextInt(characters.length)]).join();

    return '$prefix$timestamp$randomPart';
  }
}
