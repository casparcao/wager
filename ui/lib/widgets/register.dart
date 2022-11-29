import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:wager/constant.dart';
import 'package:wager/widgets/nav.dart';
import 'login.dart';

class RegisterPage extends StatefulWidget {
  const RegisterPage({super.key, required this.title});

  final String title;

  @override
  State<RegisterPage> createState() => RegisterPageState();
}

class RegisterPageState extends State<RegisterPage> {
  late String username, password, repassword;
  //是否展示密码铭文
  bool showpasswd = false;
  Color eyecolor = Colors.grey;
  final GlobalKey formkey = GlobalKey<FormState>();
  final GlobalKey passkey = GlobalKey<FormFieldState>();

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
            buildRepassword(context),
            const SizedBox(
              height: 20,
            ),
            buildSubmitBtn(context),
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
          key: passkey,
          obscureText: showpasswd,
          onSaved: (value) => password = value!,
          validator: (value) {
            if (value!.isEmpty || value.length < 4) {
              return "密码最少4位数";
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

  Widget buildRepassword(BuildContext context) {
    return Padding(
        padding: const EdgeInsets.all(20),
        child: TextFormField(
          obscureText: showpasswd,
          onSaved: (value) => repassword = value!,
          validator: (value) {
            if (value!.isEmpty || value.length < 4) {
              return "密码最少4位数";
            }
            if (value != (passkey.currentState as FormFieldState).value) {
              return "两次密码不一致";
            }
            return null;
          },
          decoration: InputDecoration(
              labelText: "请再次输入密码",
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
              var uri = Uri.http(Constant().baseurl(), "api/sign/up");
              var future =
                  http.post(uri, headers: headers, body: json.encode(body));
              future.then((response) {
                if (response.statusCode >= 300 || response.statusCode < 200) {
                  ScaffoldMessenger.of(context)
                      .showSnackBar(const SnackBar(content: Text("服务器异常")));
                  return;
                }
                Map<String, dynamic> result = json.decode(response.body);
                if (result["code"] != 0) {
                  ScaffoldMessenger.of(context)
                      .showSnackBar(SnackBar(content: Text(result["msg"])));
                  return;
                }
                ScaffoldMessenger.of(context)
                    .showSnackBar(const SnackBar(content: Text("注册成功，请前去登录")));
                //注册成功，跳转登录页
                Navigator.of(context).push(MaterialPageRoute(
                  builder: (context) => const LoginPage(title: "登录"),
                ));
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
            Navigator.pop(context);
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
