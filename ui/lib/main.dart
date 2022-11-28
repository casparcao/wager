import 'package:flutter/material.dart';
import 'widgets/nav.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        title: '竞猜世界杯',
        theme: ThemeData(
          primarySwatch: Colors.blue,
        ),
        home: const NavScreen(
          title: "首页",
        )
        // routes: {
        // '/': (context) => const LoginPage(
        // title: "登录",
        // )
        // },
        );
  }
}
