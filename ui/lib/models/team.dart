class Team {
  final String name;
  final String icon;
  final int score;

  const Team({required this.name, required this.icon, required this.score});

  factory Team.from(Map<String, dynamic> json) {
    return Team(name: json["name"], icon: json["icon"], score: json["score"]);
  }
}
