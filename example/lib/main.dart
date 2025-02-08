import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:pay_upi/model/UpiAppData.dart';
import 'package:pay_upi/pay_upi.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  final _payUpiPlugin = PayUpi();

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: FutureBuilder(
            future: _payUpiPlugin.getInstalledUpiApplications(),
            builder: (context, snapshot) {
              if (snapshot.connectionState == ConnectionState.done) {
                if (snapshot.hasError) {
                  return Text('Error: ${snapshot.error}');
                } else {
                  return ListView.builder(
                    itemCount: snapshot.data!.length,
                    itemBuilder: (context, index) {
                      UpiAppData upiApp = snapshot.data![index];
                      return ListTile(
                        title: Text(upiApp.applicationName),
                        onTap: () async {
                          await _payUpiPlugin.initiateTransaction(
                              1.0,
                              upiApp.applicationName,
                              Uri.parse(
                                  'tez://upi/pay?pa=thirugaja2001@okaxis&pn=Thiru'));
                        },
                      );
                    },
                  );
                }
              } else {
                return CircularProgressIndicator();
              }
            },
          ),
        ),
      ),
    );
  }
}

// StreamBuilder(
//             stream: _payUpiPlugin.intentStream,
//             builder: (context, snapshot) {
//               return Text(snapshot.data ?? 'Intent was not triggered');
//             }
//           )
