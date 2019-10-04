import 'dart:async';
import 'dart:ui';
import 'package:flutter/services.dart';
import './easemob_callback_dispatcher.dart';

typedef Future<dynamic> UserProfileHandler(String userName);
typedef Future<dynamic> NotifyClickHandler(String json);

class FlutterEasemob {
  static const MethodChannel _channel =
  const MethodChannel('vip.hsq168.plugin.flutter_easemob');

  static Future enableVivoPush() async {
    await _channel.invokeMethod('enableVivoPush');
  }

  static Future enableMeiZuPush(String appId, String appKey) async {
    await _channel
        .invokeMethod('enableMeiZuPush', {"appId": appId, "appKey": appKey});
  }

  static Future enableMiPush(String appId, String appKey) async {
    await _channel
        .invokeMethod('enableMiPush', {"appId": appId, "appKey": appKey});
  }

  static Future enableOppoPush(String appKey, String appSecret) async {
    await _channel.invokeMethod(
        'enableMiPush', {"appKey": appKey, "appSecret": appSecret});
  }

  static Future enableHWPush() async {
    await _channel.invokeMethod('enableHWPush');
  }

  static Future init(String appKey, String tenantId) async {
    var callback = PluginUtilities.getCallbackHandle(easemobCallbackDispatcher)
        .toRawHandle();

    await _channel.invokeMethod(
        'init', {"appKey": appKey, "tenantId": tenantId, "callback": callback});
  }

  static Future setDebugMode(bool debug) async {
    await _channel.invokeMethod('setDebugMode', debug);
  }

  static Future login(String userName, String password) async {
    await _channel
        .invokeMethod('login', {"userName": userName, "password": password});
  }

  static Future loadAllConversations() async {
    await _channel.invokeMethod('loadAllConversations');
  }

  static Future<int> getUnreadMsgCount(String userName) async {
    return await _channel
        .invokeMethod('getUnreadMsgCount', {"userName": userName});
  }

  static Future startServiceIM(String serviceNumber) async {
    return await _channel
        .invokeMethod('startServiceIM', {"serviceNumber": serviceNumber});
  }

  static Future getAllMessages(String userName) async {
    var messages =
    await _channel.invokeMethod('getAllMessages', {"userName": userName});
    return messages;
  }

  ///*************  handlers        *************/

  static setHandler(String name, Function handler) async {
//    var callbackHandle = PluginUtilities.getCallbackHandle(handler)
//        .toRawHandle()
//        .toString();
//    await _channel
//        .invokeMethod('setHandler', {"name": name, "callback": callbackHandle});
  }

  static void setUserProfileHandler(UserProfileHandler userProfileHandler) {
    setHandler("getUserInfo", userProfileHandler);
  }

  static void setNotifyClickHandler(NotifyClickHandler notifyClickHandler) {
    setHandler("notifyClick", notifyClickHandler);
  }
}
