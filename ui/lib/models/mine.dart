import 'package:wager/models/expect.dart';
import 'package:wager/models/game.dart';

import 'wager.dart';

class MineWager {
  final Game game;
  final double? amount;
  final Rule rule;
  final BaseWagerExpect expect;
  final DateTime date;
  final bool? right;
  final double? win;

  const MineWager(
      {required this.game,
      required this.amount,
      required this.rule,
      required this.expect,
      required this.date,
      required this.right,
      required this.win});

  factory MineWager.from(Map<String, dynamic> json) {
    String strule = json["rule"];
    Rule rule = Rule.of(strule);
    BaseWagerExpect e = BaseWagerExpect.select(rule);
    return MineWager(
        game: Game.from(json["game"]),
        amount: json["amount"],
        rule: rule,
        expect: e.from(json["expect"]),
        date: DateTime.parse(json["date"]),
        right: json["right"],
        win: json["win"]);
  }
}
