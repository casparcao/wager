import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:wager/constant.dart';
import 'package:wager/widgets/nav.dart';
import 'register.dart';

class LoginPage extends StatefulWidget {
  const LoginPage({super.key, required this.title});

  final String title;

  @override
  State<LoginPage> createState() => LoginPageState();
}

class LoginPageState extends State<LoginPage> {
  late String username, password;
  //是否展示密码铭文
  bool showpasswd = false;
  Color eyecolor = Colors.grey;
  final GlobalKey formkey = GlobalKey<FormState>();

  @override
  void initState() {
    super.initState();
    SharedPreferences.getInstance().then((prefs) {
      String? token = prefs.getString("token");
      if (token?.isNotEmpty ?? false) {
        Navigator.of(context).push(MaterialPageRoute(
          builder: (context) => const NavScreen(title: "赛程列表"),
        ));
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
          child: Form(
        key: formkey,
        autovalidateMode: AutovalidateMode.onUserInteraction,
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            buildTitle(),
            buildUserName(context),
            buildPassword(context),
            const SizedBox(
              height: 20,
            ),
            buildSubmitBtn(context),
            buildRegister(context)
          ],
        ),
      )),
    );
  }

  Widget buildTitle() {
    return const Padding(
        padding: EdgeInsets.all(8),
        child: Text("竞猜世界杯", style: TextStyle(fontSize: 40)));
  }

  Widget buildUserName(BuildContext context) {
    return Padding(
        padding: const EdgeInsets.all(20),
        child: TextFormField(
          decoration: const InputDecoration(labelText: "请输入用户名"),
          validator: (value) {
            var regex = RegExp("[a-zA-Z0-9]{4,}");
            if (!regex.hasMatch(value!)) {
              return "用户名只包括英文字母及数字";
            }
            return null;
          },
          onSaved: (value) {
            username = value!;
          },
        ));
  }

  Widget buildPassword(BuildContext context) {
    return Padding(
        padding: const EdgeInsets.all(20),
        child: TextFormField(
          obscureText: showpasswd,
          onSaved: (value) => password = value!,
          validator: (value) {
            if (value!.isEmpty || value.length < 4) {
              return "密码最少6位数";
            }
            return null;
          },
          decoration: InputDecoration(
              labelText: "请输入密码",
              suffixIcon: IconButton(
                icon: Icon(
                  Icons.remove_red_eye,
                  color: eyecolor,
                ),
                onPressed: () {
                  setState(() {
                    showpasswd = !showpasswd;
                    eyecolor = (showpasswd
                        ? Colors.grey
                        : Theme.of(context).iconTheme.color!);
                  });
                },
              )),
        ));
  }

  Widget buildSubmitBtn(BuildContext context) {
    return SizedBox(
        height: 45,
        width: 300,
        child: ElevatedButton(
          style: ButtonStyle(
              shape: MaterialStateProperty.all(const StadiumBorder(
                  side: BorderSide(style: BorderStyle.none)))),
          onPressed: () {
            if ((formkey.currentState as FormState).validate()) {
              (formkey.currentState as FormState).save();
              Map<String, String> headers = {
                "Content-Type": "application/json"
              };
              Map<String, String> body = {
                "username": username,
                "password": password
              };
              var uri = Uri.http(Constant.baseurl, "api/token");
              var future =
                  http.post(uri, headers: headers, body: json.encode(body));
              future.then((response) {
                if (response.statusCode >= 300 || response.statusCode < 200) {
                  Fluttertoast.showToast(
                      msg: "服务器异常",
                      toastLength: Toast.LENGTH_LONG,
                      gravity: ToastGravity.CENTER,
                      timeInSecForIosWeb: 1,
                      backgroundColor: Colors.orange,
                      textColor: Colors.white,
                      fontSize: 16);
                  return;
                }
                Map<String, dynamic> result = json.decode(response.body);
                if (result["code"] != 0) {
                  ScaffoldMessenger.of(context)
                      .showSnackBar(SnackBar(content: Text(result["msg"])));
                  return;
                }
                //登录成功
                final fprefs = SharedPreferences.getInstance();
                fprefs.then((prefs) {
                  prefs.setString("token", result["data"]);
                  Navigator.of(context).push(MaterialPageRoute(
                    builder: (context) => const NavScreen(title: "赛程列表"),
                  ));
                });
              });
            }
          },
          child:
              Text("登录", style: Theme.of(context).primaryTextTheme.headline5),
        ));
  }

  Widget buildRegister(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(top: 8),
      child: Align(
        alignment: Alignment.centerRight,
        child: TextButton(
          onPressed: () {
            Navigator.of(context).push(MaterialPageRoute(
              builder: (context) => const RegisterPage(title: "注册"),
            ));
          },
          child: const Text(
            "注册",
            style: TextStyle(fontSize: 16, color: Colors.lightBlue),
          ),
        ),
      ),
    );
  }
}
