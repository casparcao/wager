import 'package:flutter/material.dart';
import 'package:wager/tools/data.dart';
import '../models/game.dart';
import 'package:intl/intl.dart';
import '../models/mine.dart';

class MineScreen extends StatefulWidget {
  const MineScreen({super.key, required this.title});

  final String title;

  @override
  State<MineScreen> createState() => MineState();
}

class MineState extends State<MineScreen> {
  late List<MineWager> wagers = [];

  @override
  void initState() {
    super.initState();
    load("wager/mine", {}, (List<dynamic> list) {
      setState(() {
        wagers = list.map((item) => MineWager.from(item)).toList();
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

  Widget buildItem(MineWager mine) {
    DateFormat format = DateFormat("yyyy-MM-dd HH:mm:ss");
    Game game = mine.game;
    return Container(
        decoration: const BoxDecoration(
            border:
                Border(bottom: BorderSide(width: 0.8, color: Colors.black))),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Padding(
                padding: const EdgeInsets.all(10),
                child: Text(format.format(mine.date))),
            Padding(
                padding: const EdgeInsets.all(10), child: Text(game.home.name)),
            Padding(
              padding: const EdgeInsets.all(10),
              child: Text(game.over
                  ? "${game.home.score} vs ${game.guest.score}"
                  : "? vs ?"),
            ),
            Padding(
                padding: const EdgeInsets.all(10),
                child: Text(game.guest.name)),
            Padding(
              padding: const EdgeInsets.all(10),
              child: Text(mine.rule.alias()),
            ),
            Padding(
              padding: const EdgeInsets.all(10),
              child: Text(mine.expect.desc()),
            ),
            Padding(
              padding: const EdgeInsets.all(10),
              child: Text("竞猜金额${mine.amount ?? 0}"),
            ),
            Padding(
              padding: const EdgeInsets.all(10),
              child: Text((mine.right ?? false) ? "竞猜正确" : "竞猜错误"),
            ),
            Padding(
              padding: const EdgeInsets.all(10),
              child: Text("竞猜赢得${mine.win ?? 0}"),
            )
          ],
        ));
  }
}
