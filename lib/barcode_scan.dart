import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class BarcodeScanner {
  static const CameraAccessDenied = 'PERMISSION_NOT_GRANTED';
  static const UserCanceled = 'USER_CANCELED';
  static const MethodChannel _channel =
      const MethodChannel('com.apptreesoftware.barcode_scan');
  static Future<String> scan({
    @required String flashOnTxt,
    @required String flashOffTxt,
    @required String cancelTxt,
  }) async => await _channel.invokeMethod('scan',{
    "flashOnTxt":flashOffTxt,
    "flashOffTxt":flashOffTxt,
    "cancelTxt":cancelTxt
  });
}
