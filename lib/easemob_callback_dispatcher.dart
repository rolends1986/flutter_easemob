import 'dart:ui';
import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';

void easemobCallbackDispatcher() {
  const MethodChannel _backgroundChannel =
      MethodChannel('vip.hsq168.plugin.flutter_easemob/background');
  WidgetsFlutterBinding.ensureInitialized();

  _backgroundChannel.setMethodCallHandler((MethodCall call) async {
    if (call.method == "do") {
      var map = call.arguments as Map<dynamic, dynamic>;
      if (map.containsKey("callback")) {
        var rawHandle = map["callback"] as int;
        var callback = PluginUtilities.getCallbackFromHandle(
            CallbackHandle.fromRawHandle(rawHandle));
        var arguments;
        if (map.containsKey("arguments")) arguments = map["arguments"];
        return callback(arguments);
      }
    }
    return null;
  });
}
