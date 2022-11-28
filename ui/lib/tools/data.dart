import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:wager/constant.dart';
import 'package:wager/widgets/login.dart';
import 'package:http/http.dart' as http;

void load(String path, Map<String, dynamic> params, Function callback,
    BuildContext context) {
  SharedPreferences.getInstance().then((prefs) {
    String? token = prefs.getString("token");
    if (token == null || token.isEmpty) {
      //没有token
      //重新登录
      Navigator.of(context).push(MaterialPageRoute(
        builder: (context) => const LoginPage(
          title: '登录',
        ),
      ));
      return;
    }
    Map<String, String> headers = {
      "Authorization": "Bearer $token",
      "Content-Type": "application/json;charset=utf-8"
    };
    Uri uri = Uri.http(Constant.baseurl, path, params);
    http.get(uri, headers: headers).then((response) {
      if (response.statusCode >= 300 || response.statusCode < 200) {
        ScaffoldMessenger.of(context)
            .showSnackBar(const SnackBar(content: Text("服务器异常")));
      }
      String utf8body = utf8.decode(latin1.encode(response.body));
      Map<String, dynamic> result = json.decode(utf8body);
      if (result["code"] != 0) {
        ScaffoldMessenger.of(context)
            .showSnackBar(SnackBar(content: Text(result["msg"])));
        return;
      }
      List<dynamic> list = result["data"] ?? [];
      callback(list);
    });
  });
}
