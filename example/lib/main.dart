import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_easemob/flutter_easemob.dart';
import 'package:flutter_easemob/user_profile.dart';



void main() {

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
//    await FlutterEasemob.login("huc_10_nop_8666", "huc_10_nop_8666");
//    await FlutterEasemob.loadAllConversations();
//    var messages = await FlutterEasemob.getAllMessages("admin");
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
