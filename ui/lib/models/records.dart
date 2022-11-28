import 'package:wager/models/expect.dart';

import 'wager.dart';

class GameWager {
  final String user;
  final double? amount;
  final Rule rule;
  final BaseWagerExpect expect;
  final DateTime date;
  final bool? right;
  final double? win;

  const GameWager(
      {required this.user,
      required this.amount,
      required this.rule,
      required this.expect,
      required this.date,
      required this.right,
      required this.win});

  factory GameWager.from(Map<String, dynamic> json) {
    String strule = json["rule"];
    Rule rule = Rule.of(strule);
    BaseWagerExpect e = BaseWagerExpect.select(rule);
    return GameWager(
        user: json["user"],
        amount: json["amount"],
        rule: rule,
        expect: e.from(json["expect"]),
        date: DateTime.parse(json["date"]),
        right: json["right"],
        win: json["win"]);
  }
}
