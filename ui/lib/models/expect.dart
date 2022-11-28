import 'package:wager/models/wager.dart';

abstract class BaseWagerExpect {
  bool validate();
  bool compatible(Rule rule);
  Map<String, dynamic> to();
  BaseWagerExpect from(Map<String, dynamic> json);
  static List<BaseWagerExpect> expects = [
    WinLoseWagerExpect(null),
    ScoreWagerExpect(null, null),
    ScoreDiffWagerExpect(null),
    TotalScoreWagerExpect(null)
  ];
  static BaseWagerExpect select(Rule rule) {
    return expects[rule.index];
  }

  String desc();
}

class WinLoseWagerExpect extends BaseWagerExpect {
  //主队=1，客队=-1，平局=0
  late int? winner;
  WinLoseWagerExpect(this.winner);

  @override
  bool validate() {
    return winner != null;
  }

  @override
  bool compatible(Rule rule) {
    return Rule.winlose == rule;
  }

  @override
  Map<String, dynamic> to() {
    return {"winner": winner};
  }

  @override
  BaseWagerExpect from(Map<String, dynamic> json) {
    return WinLoseWagerExpect(json["winner"]);
  }

  @override
  String desc() {
    if (winner == -1) {
      return "猜测客队获胜";
    } else if (winner == 0) {
      return "猜测平局";
    } else {
      return "猜测主队获胜";
    }
  }
}

class ScoreWagerExpect extends BaseWagerExpect {
  late int? home;
  late int? guest;
  ScoreWagerExpect(this.home, this.guest);

  @override
  bool validate() {
    return home != null && guest != null;
  }

  @override
  bool compatible(Rule rule) {
    return Rule.score == rule;
  }

  @override
  Map<String, dynamic> to() {
    return {"home": home, "guest": guest};
  }

  @override
  BaseWagerExpect from(Map<String, dynamic> json) {
    return ScoreWagerExpect(json["home"], json["guest"]);
  }

  @override
  String desc() {
    return "猜测 $home : $guest";
  }
}

class ScoreDiffWagerExpect extends BaseWagerExpect {
  late int? diff;

  ScoreDiffWagerExpect(this.diff);

  @override
  bool validate() {
    return diff != null;
  }

  @override
  bool compatible(Rule rule) {
    return Rule.diff == rule;
  }

  @override
  Map<String, dynamic> to() {
    return {"diff": diff};
  }

  @override
  BaseWagerExpect from(Map<String, dynamic> json) {
    return ScoreDiffWagerExpect(json["diff"]);
  }

  @override
  String desc() {
    return "猜测净胜$diff球";
  }
}

class TotalScoreWagerExpect extends BaseWagerExpect {
  late int? total;

  TotalScoreWagerExpect(this.total);

  @override
  bool validate() {
    return total != null;
  }

  @override
  bool compatible(Rule rule) {
    return Rule.total == rule;
  }

  @override
  Map<String, dynamic> to() {
    return {"total": total};
  }

  @override
  BaseWagerExpect from(Map<String, dynamic> json) {
    return TotalScoreWagerExpect(json["total"]);
  }

  @override
  String desc() {
    return "猜测总共进$total球";
  }
}
