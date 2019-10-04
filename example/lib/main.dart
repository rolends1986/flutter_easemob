import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_easemob/flutter_easemob.dart';
import 'package:flutter_easemob/user_profile.dart';

Future<dynamic> userProfileEvent(String userName) async {
  var profile = UserProfile()
    ..avater = "http://res.test.hsq168.co/upload/res/avater.webp"
    ..nickName = "打击已已尽"
    ..userName = userName;
  return {
    "userName": profile.userName,
    "nickName": profile.nickName,
    "avater": profile.avater
  };
}

void main() {
  //FlutterEasemob.enableHWPush();
  // FlutterEasemob.enableVivoPush();//发布成功后才能测试
  // FlutterEasemob.enableMeiZuPush(appId, appKey)
  //   MiPushClient.registerPush(registrar.context(), "2882303761518149924", "5291814984924")
  FlutterEasemob.enableMiPush("2882303761518149924", "5291814984924");
  FlutterEasemob.enableOppoPush(
      "46ebb270e61a4bba9c7d5a80affc8218", "cc74be925c484bc8a3b94c0a0ed6307f");
  FlutterEasemob.init("1150190903085951#huashangqing", "74175");
  FlutterEasemob.setDebugMode(true);
  FlutterEasemob.setUserProfileHandler(userProfileEvent);
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();

    login();
  }

  Future login() async {
    await FlutterEasemob.login("huc_10_nop_8666", "huc_10_nop_8666");
    await FlutterEasemob.loadAllConversations();
    var messages = await FlutterEasemob.getAllMessages("admin");
  }

  start() {
    FlutterEasemob.startServiceIM("_services");
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          children: <Widget>[
            FlatButton(
                child: Text("启动"),
                onPressed: () {
                  start();
                }),
          ],
        ),
      ),
    );
  }
}
