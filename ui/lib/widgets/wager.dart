import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:wager/constant.dart';
import 'package:wager/models/expect.dart';
import 'package:wager/models/wager.dart';
import 'login.dart';
import '../models/game.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class WagerPage extends StatefulWidget {
  const WagerPage({super.key, required this.game, required this.title});

  final Game game;
  final String title;

  @override
  State<WagerPage> createState() => WagerPageState();
}

class WagerPageState extends State<WagerPage> {
  late Wager wager = Wager(null, null, null, null);

  late WinLoseWagerExpect winloseexpect = WinLoseWagerExpect(null);
  late ScoreWagerExpect scoreexpect = ScoreWagerExpect(null, null);
  late ScoreDiffWagerExpect diffexpect = ScoreDiffWagerExpect(null);
  late TotalScoreWagerExpect totalexpect = TotalScoreWagerExpect(null);
  late List<BaseWagerExpect> expects = [
    winloseexpect,
    scoreexpect,
    diffexpect,
    totalexpect
  ];

  GlobalKey scaffoldkey = GlobalKey<ScaffoldState>();

  @override
  Widget build(BuildContext context) {
    return DefaultTabController(
        length: 4,
        child: Scaffold(
          key: scaffoldkey,
          appBar: AppBar(
            title: Text(widget.title),
            bottom: const TabBar(tabs: [
              Tab(
                text: "猜胜负",
                icon: Icon(Icons.sports_football),
              ),
              Tab(
                text: "猜比分",
                icon: Icon(Icons.sports_football),
              ),
              Tab(
                text: "净胜球",
                icon: Icon(Icons.sports_football),
              ),
              Tab(
                text: "总球数",
                icon: Icon(Icons.sports_football),
              ),
            ]),
          ),
          body: TabBarView(children: [
            buildWinLose(),
            buildScore(),
            buildDiff(),
            buildTotal(),
          ]),
        ));
  }

  Widget buildAmount() {
    return Padding(
        padding: const EdgeInsets.all(10),
        child: Row(children: [
          const Text("竞猜金额"),
          Expanded(
              child: TextField(
            keyboardType: TextInputType.number,
            onChanged: (value) {
              wager.amount = double.parse(value);
            },
            inputFormatters: [
              FilteringTextInputFormatter.allow(RegExp(r'[0-9]'))
            ],
          )),
        ]));
  }

  Widget buildSubmit() {
    return SizedBox(
        height: 85,
        width: 300,
        child: Padding(
            padding: const EdgeInsets.all(20),
            child: ElevatedButton(
              style: ButtonStyle(
                  shape: MaterialStateProperty.all(const StadiumBorder(
                      side: BorderSide(style: BorderStyle.none)))),
              onPressed: () {
                BuildContext context = scaffoldkey.currentContext!;
                int? tabindex = DefaultTabController.of(context)?.index;
                BaseWagerExpect expect = expects[tabindex!];
                bool validate = expect.validate();
                wager.game = widget.game.id;
                wager.rule = Rule.values[tabindex];
                wager.expect = expect;
                if (!validate || !wager.validate()) {
                  ScaffoldMessenger.of(context).showSnackBar(
                      const SnackBar(content: Text("提交数据不正确，请检查后重新提交")));
                  return;
                }
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
                    "Content-Type": "application/json",
                    "Authorization": token
                  };
                  var uri = Uri.http(Constant.baseurl, "wager");
                  Map<String, dynamic> body = wager.to();
                  var future =
                      http.post(uri, headers: headers, body: json.encode(body));
                  future.then((response) {
                    if (response.statusCode >= 300 ||
                        response.statusCode < 200) {
                      ScaffoldMessenger.of(context)
                          .showSnackBar(const SnackBar(content: Text("服务器异常")));
                      return;
                    }
                    String utf8body = utf8.decode(latin1.encode(response.body));
                    Map<String, dynamic> result = json.decode(utf8body);
                    if (result["code"] != 0) {
                      ScaffoldMessenger.of(context)
                          .showSnackBar(SnackBar(content: Text(result["msg"])));
                      return;
                    }
                    ScaffoldMessenger.of(context)
                        .showSnackBar(const SnackBar(content: Text("竞猜成功")));
                    //竞猜成功，返回上一页
                    Navigator.of(context).pop();
                  });
                });
              },
              child: Text("提交",
                  style: Theme.of(context).primaryTextTheme.headline5),
            )));
  }

  Widget buildTotal() {
    return Column(children: [
      Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          const Text("两队总进球数"),
          Expanded(
              child: TextField(
            keyboardType: TextInputType.number,
            onChanged: (value) {
              totalexpect.total = int.parse(value);
            },
            inputFormatters: [
              FilteringTextInputFormatter.allow(RegExp(r'[0-9]'))
            ],
          )),
        ],
      ),
      buildAmount(),
      buildSubmit()
    ]);
  }

  Widget buildDiff() {
    return Column(children: [
      Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Text("${widget.game.home.name}进球数减去${widget.game.guest.name}进球数等于："),
          Expanded(
              child: TextField(
            keyboardType: TextInputType.text,
            onChanged: (value) {
              diffexpect.diff = int.parse(value);
            },
            inputFormatters: [
              FilteringTextInputFormatter.allow(RegExp(r'^(-)?[1-9][0-9]*$'))
            ],
          )),
        ],
      ),
      buildAmount(),
      buildSubmit()
    ]);
  }

  Widget buildScore() {
    return Column(children: [
      Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Text(widget.game.home.name),
          Expanded(
              child: TextField(
            keyboardType: TextInputType.number,
            onChanged: (value) {
              scoreexpect.home = int.parse(value);
            },
            inputFormatters: [
              FilteringTextInputFormatter.allow(RegExp(r'[0-9]'))
            ],
          )),
          Text(widget.game.guest.name),
          Expanded(
              child: TextField(
            keyboardType: TextInputType.number,
            onChanged: (value) {
              scoreexpect.guest = int.parse(value);
            },
            inputFormatters: [
              FilteringTextInputFormatter.allow(RegExp(r'[0-9]'))
            ],
          )),
        ],
      ),
      buildAmount(),
      buildSubmit()
    ]);
  }

  Widget buildWinLose() {
    return Column(children: [
      Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Text("${widget.game.home.name}赢"),
          Radio(
            value: 1,
            groupValue: winloseexpect.winner,
            onChanged: ((value) {
              setState(() {
                winloseexpect.winner = value!;
              });
            }),
          ),
          Text("${widget.game.guest.name}赢"),
          Radio(
            value: -1,
            groupValue: winloseexpect.winner,
            onChanged: ((value) {
              setState(() {
                winloseexpect.winner = value!;
              });
            }),
          ),
          const Text("平局"),
          Radio(
            value: 0,
            groupValue: winloseexpect.winner,
            onChanged: ((value) {
              setState(() {
                winloseexpect.winner = value!;
              });
            }),
          ),
        ],
      ),
      buildAmount(),
      buildSubmit()
    ]);
  }
}
