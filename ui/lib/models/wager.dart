import 'dart:convert';

import 'expect.dart';

class Wager {
  late Rule? rule = Rule.winlose;
  late String? game;
  late double? amount;
  late BaseWagerExpect? expect;

  Wager(this.rule, this.game, this.amount, this.expect);

  bool validate() {
    return rule != null &&
        game != null &&
        amount != null &&
        expect != null &&
        expect!.compatible(rule!);
  }

  Map<String, dynamic> to() {
    return {
      "rule": rule!.str(),
      "game": game,
      "amount": amount,
      "expect": json.encode(expect!.to())
    };
  }
}

enum Rule {
  winlose,
  score,
  diff,
  total;

  static List<String> strs = ['WIN_LOSE', 'SCORE', 'SCORE_DIFF', 'TOTAL_SCORE'];
  static List<String> aliases = ['猜输赢', '猜比分', '猜测净胜球', '猜总进球数'];

  String str() {
    return strs[index];
  }

  String alias() {
    return aliases[index];
  }

  static Rule of(String str) {
    int index = strs.indexOf(str);
    return Rule.values[index];
  }
}
