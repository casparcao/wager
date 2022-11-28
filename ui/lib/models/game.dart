import 'package:wager/models/team.dart';

class Game {
  final String id;
  final Team home;
  final Team guest;
  final DateTime date;
  final String title;
  final bool clear;
  final bool over;

  const Game(
      {required this.id,
      required this.home,
      required this.guest,
      required this.date,
      required this.title,
      required this.clear,
      required this.over});

  factory Game.from(Map<String, dynamic> json) {
    return Game(
        id: json["id"].toString(),
        home: Team.from(json["home"]),
        guest: Team.from(json["guest"]),
        date: DateTime.parse(json["date"]),
        title: json["title"],
        clear: json["clear"],
        over: json["over"]);
  }
}
