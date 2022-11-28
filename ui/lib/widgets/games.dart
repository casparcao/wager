import 'package:flutter/material.dart';
import 'package:wager/widgets/records.dart';
import 'package:wager/widgets/wager.dart';
import '../models/game.dart';
import 'package:intl/intl.dart';
import 'package:wager/tools/data.dart';

class GamesScreen extends StatefulWidget {
  const GamesScreen({super.key, required this.title});

  final String title;

  @override
  State<GamesScreen> createState() => GamesState();
}

class GamesState extends State<GamesScreen> {
  late List<Game> games = [];

  @override
  void initState() {
    super.initState();
    load("games", {}, (List<dynamic> list) {
      setState(() {
        games = list.map((item) => Game.from(item)).toList();
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
        itemCount: games.length,
        itemBuilder: (BuildContext context, int index) {
          return buildItem(games[index]);
        },
      )),
    );
  }

  Widget buildItem(Game game) {
    DateFormat format = DateFormat("yyyy-MM-dd HH:mm:ss");
    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Padding(
            padding: const EdgeInsets.all(10),
            child: Text(format.format(game.date))),
        Padding(padding: const EdgeInsets.all(10), child: Text(game.title)),
        Padding(padding: const EdgeInsets.all(10), child: Text(game.home.name)),
        Padding(
            padding: const EdgeInsets.all(10),
            child: Image.network(
              game.home.icon,
              fit: BoxFit.contain,
              width: 40,
              height: 40,
            )),
        Padding(
          padding: const EdgeInsets.all(10),
          child: Text(game.over
              ? "${game.home.score} vs ${game.guest.score}"
              : "? vs ?"),
        ),
        Padding(
            padding: const EdgeInsets.all(10),
            child: Image.network(
              game.guest.icon,
              fit: BoxFit.contain,
              width: 40,
              height: 40,
            )),
        Padding(
            padding: const EdgeInsets.all(10), child: Text(game.guest.name)),
        TextButton(
            onPressed: () {
              Navigator.of(context).push(MaterialPageRoute(
                builder: (context) => WagerPage(
                  game: game,
                  title: '竞猜',
                ),
              ));
            },
            child: const Text("竞猜")),
        TextButton(
            onPressed: () {
              Navigator.of(context).push(MaterialPageRoute(
                builder: (context) => RecordScreen(game: game, title: "精彩记录"),
              ));
            },
            child: const Text("记录"))
      ],
    );
  }
}
