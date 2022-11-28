import 'package:flutter/material.dart';
import 'package:wager/widgets/games.dart';
import 'package:wager/widgets/mine.dart';

class NavScreen extends StatefulWidget {
  const NavScreen({super.key, required this.title});

  final String title;

  @override
  State<NavScreen> createState() => NavState();
}

class NavState extends State<NavScreen> {
  int index = 0;
  static const pages = [GamesScreen(title: "赛程"), MineScreen(title: "我的")];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      // appBar: AppBar(
      // title: const Text("首页"),
      // ),
      body: Center(child: pages.elementAt(index)),
      bottomNavigationBar: BottomNavigationBar(
        currentIndex: index,
        items: const [
          BottomNavigationBarItem(
              icon: Icon(Icons.schedule),
              label: "赛程",
              backgroundColor: Colors.blue),
          BottomNavigationBarItem(
              icon: Icon(Icons.my_location),
              label: "我的",
              backgroundColor: Colors.green)
        ],
        onTap: (value) {
          change(value);
        },
      ),
    );
  }

  change(int value) {
    setState(() {
      index = value;
    });
  }
}
