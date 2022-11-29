import 'package:flutter/material.dart';
import 'package:wager/tools/data.dart';
import '../models/game.dart';
import 'package:intl/intl.dart';
import '../models/records.dart';

class RecordScreen extends StatefulWidget {
  const RecordScreen({super.key, required this.game, required this.title});

  final Game game;
  final String title;

  @override
  State<RecordScreen> createState() => RecordState();
}

class RecordState extends State<RecordScreen> {
  late List<GameWager> wagers = [];

  @override
  void initState() {
    super.initState();
    load("api/wager/game", {"game": widget.game.id}, (List<dynamic> list) {
      setState(() {
        wagers = list.map((item) => GameWager.from(item)).toList();
      });
    }, context);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
          child: ListView.builder(
        itemCount: wagers.length,
        itemBuilder: (BuildContext context, int index) {
          return buildItem(wagers[index]);
        },
      )),
    );
  }

  Widget buildItem(GameWager w) {
    DateFormat format = DateFormat("yyyy-MM-dd HH:mm:ss");
    return Container(
        decoration: const BoxDecoration(
            border:
                Border(bottom: BorderSide(width: 0.8, color: Colors.black))),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Padding(
                padding: const EdgeInsets.all(10),
                child: Text(format.format(w.date))),
            Padding(padding: const EdgeInsets.all(10), child: Text(w.user)),
            Padding(
              padding: const EdgeInsets.all(10),
              child: Text(w.rule.alias()),
            ),
            Padding(
              padding: const EdgeInsets.all(10),
              child: Text(w.expect.desc()),
            ),
            Padding(
              padding: const EdgeInsets.all(10),
              child: Text("竞猜金额${w.amount ?? 0}"),
            ),
            Padding(
              padding: const EdgeInsets.all(10),
              child: Text((w.right ?? false) ? "竞猜正确" : "竞猜错误"),
            ),
            Padding(
              padding: const EdgeInsets.all(10),
              child: Text("竞猜赢得${w.win ?? 0}"),
            )
          ],
        ));
  }
}
