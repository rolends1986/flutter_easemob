import 'dart:async';
import 'dart:ui';
import 'package:flutter/services.dart';
import './easemob_callback_dispatcher.dart';

typedef Future<dynamic> UserProfileHandler(String userName);
typedef Future<dynamic> NotifyClickHandler(String json);

class FlutterEasemob {
  static const MethodChannel _channel =
  const MethodChannel('vip.hsq168.plugin.flutter_easemob');

  static Future startServiceIM(String serviceNumber) async {
    return await _channel
        .invokeMethod('startServiceIM', {"serviceNumber": serviceNumber});
  }


}
