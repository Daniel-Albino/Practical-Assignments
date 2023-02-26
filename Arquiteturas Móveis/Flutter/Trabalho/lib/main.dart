import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import 'package:tp_am_2/meals_edit.dart';
import 'dart:convert';
import 'dart:io';

import 'meals.dart';

const String _mealsUrl = 'http://10.0.2.2:8080/menu';
/*const String _mealsUrl = 'http://localhost:8080/menu';*/
/*const String _mealsUrl = 'http://192.168.1.87:8080/menu';*/
/*const String _mealsUrl = 'http://192.168.43.201:8080/menu';*/
/*const String _imageUrl = 'http://192.168.1.87:8080/images/';*/
/*const String _imageUrl = 'http://192.168.43.201:8080/images/';*/
const String _imageUrl = 'http://10.0.2.2:8080/images/';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Cantina ISEC',
      theme: ThemeData(
          appBarTheme: const AppBarTheme(
        color: Color(0xFF894E4E),
      )),
      /*home: const MyHomePage(title: 'Cantina ISEC'),*/
      initialRoute: MyHomePage.routeName,
      routes: {
        MyHomePage.routeName: (context) => const MyHomePage(title: 'Cantina ISEC'),
        MealsEdit.routeName: (context) => const MealsEdit(),
      },
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  static const String routeName = '/';

  final String title;



  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> with TickerProviderStateMixin{

  late AnimationController controller;
  late Animation<double> animation;

  Map<String, Meals> _Meals = {};
  bool _fetchingData = false;

  @override
  void initState() {
    super.initState();
    initializeMapMeals();
    initMeals();
    controller = AnimationController(duration: Duration(seconds: 3), vsync: this);
    animation = CurvedAnimation(parent: controller, curve: Curves.easeIn);

    controller.repeat();
  }

  @override
  void dispose() {
    controller.dispose();
    super.dispose();
  }

  void initializeMapMeals(){
    int d = DateTime.now().weekday;
    List<String> weekDayList = [];
    switch(d){
      case 1 : {
        weekDayList.add("MONDAY");
        weekDayList.add("TUESDAY");
        weekDayList.add("WEDNESDAY");
        weekDayList.add("THURSDAY");
        weekDayList.add("FRIDAY");
      }break;
      case 2 : {
        weekDayList.add("TUESDAY");
        weekDayList.add("WEDNESDAY");
        weekDayList.add("THURSDAY");
        weekDayList.add("FRIDAY");
        weekDayList.add("MONDAY");
      }break;
      case 3 : {
        weekDayList.add("WEDNESDAY");
        weekDayList.add("THURSDAY");
        weekDayList.add("FRIDAY");
        weekDayList.add("MONDAY");
        weekDayList.add("TUESDAY");
      }break;
      case 4 : {
        weekDayList.add("THURSDAY");
        weekDayList.add("FRIDAY");
        weekDayList.add("MONDAY");
        weekDayList.add("TUESDAY");
        weekDayList.add("WEDNESDAY");
      }break;
      case 5 : {
        weekDayList.add("FRIDAY");
        weekDayList.add("MONDAY");
        weekDayList.add("TUESDAY");
        weekDayList.add("WEDNESDAY");
        weekDayList.add("THURSDAY");
      }break;
      default:{
        weekDayList.add("MONDAY");
        weekDayList.add("TUESDAY");
        weekDayList.add("WEDNESDAY");
        weekDayList.add("THURSDAY");
        weekDayList.add("FRIDAY");
      }break;
    }


    for (var i = 0; i < 5; i++) {
      String week = weekDayFun(weekDayList[i].toString());
      if(week != "") {
        Meals meals = Meals(
            img: "images/logo.png",
            weekDay: week,
            soup: "Sem dados",
            fish: "Sem dados",
            meat: "Sem dados",
            vegetarian: "Sem dados",
            desert: "Sem dados",
            update: false);
        _Meals.putIfAbsent(weekDayList[i], () => meals);
      }
    }

  }

  Future<void> initMeals() async {
    var prefs = await SharedPreferences.getInstance();
    setState(() {
      if(prefs.getStringList('MONDAY') == null || prefs.getStringList('TUESDAY') == null
          || prefs.getStringList('WEDNESDAY') == null || prefs.getStringList('THURSDAY') == null|| prefs.getStringList('FRIDAY') == null) {
        _fetchMeals();
      }else {
        _Meals.update("MONDAY", (value) => getValuesMeals("MONDAY",prefs.getStringList('MONDAY')));
        _Meals.update("TUESDAY", (value) => getValuesMeals("TUESDAY",prefs.getStringList('TUESDAY')));
        _Meals.update("WEDNESDAY", (value) => getValuesMeals("WEDNESDAY",prefs.getStringList('WEDNESDAY')));
        _Meals.update("THURSDAY", (value) => getValuesMeals("THURSDAY",prefs.getStringList('THURSDAY')));
        _Meals.update("FRIDAY", (value) => getValuesMeals("FRIDAY",prefs.getStringList('FRIDAY')));
        _fetchingData = true;
      }
    });
  }

  Meals getValuesMeals(String day,List<String>? mealsList){
    bool update;
    if(mealsList![7] == "true") {
      update = true;
    } else {
      update = false;
    }
    Meals meals = Meals(
        img: mealsList![0],
        weekDay: mealsList[1],
        soup: mealsList[2],
        fish: mealsList[3],
        meat: mealsList[4],
        vegetarian: mealsList[5],
        desert: mealsList[6],
        update: update,
    );
    return meals;
  }

  Future<void> changeMeals(Map<String,Meals> meals) async {
    setState(() {
      _Meals = meals;
    });
    var prefs = await SharedPreferences.getInstance();
    List<String> mealsList = [];
    for (var i = 0; i < 5; i++) {
      switch(i){
        case 0 : {
          mealsList = returnValuesMeals("MONDAY");
          await prefs.setStringList('MONDAY', mealsList);
          mealsList = [];
        }break;
        case 1 : {
          mealsList = returnValuesMeals("TUESDAY");
          await prefs.setStringList('TUESDAY', mealsList);
          mealsList = [];
        }break;
        case 2 : {
          mealsList = returnValuesMeals("WEDNESDAY");
          await prefs.setStringList('WEDNESDAY', mealsList);
          mealsList = [];
        }break;
        case 3 : {
          mealsList = returnValuesMeals("THURSDAY");
          await prefs.setStringList('THURSDAY', mealsList);
          mealsList = [];
        }break;
        case 4 : {
          mealsList = returnValuesMeals("FRIDAY");
          await prefs.setStringList('FRIDAY', mealsList);
          mealsList = [];
        }break;
      }
    }
  }

  List<String> returnValuesMeals(String day){
    List<String> mealsList = [];
    mealsList.add(_Meals[day]!.img!);
    mealsList.add(_Meals[day]!.weekDay);
    mealsList.add(_Meals[day]!.soup);
    mealsList.add(_Meals[day]!.fish);
    mealsList.add(_Meals[day]!.meat);
    mealsList.add(_Meals[day]!.vegetarian);
    mealsList.add(_Meals[day]!.desert);
    mealsList.add(_Meals[day]!.update.toString());
    return mealsList;
  }



  Future<void> _fetchMeals() async {

    try {
      showDialog(context: context, builder: (context){
        return const Center(child: CircularProgressIndicator(color: Color(0xFF894E4E),));
      });
      setState(() => _fetchingData = true);
      http.Response response = await http.get(Uri.parse(_mealsUrl),
        headers: {'Content-type' : 'application/json; charset=UTF-8'},).timeout(const Duration(seconds: 3));
      final decoded = const Utf8Decoder(allowMalformed: true).convert(response.bodyBytes);
      if (response.statusCode == HttpStatus.ok) {
        Map<String, dynamic> data = jsonDecode(decoded);
        getMeals("MONDAY", data);
        getMeals("TUESDAY", data);
        getMeals("WEDNESDAY", data);
        getMeals("THURSDAY", data);
        getMeals("FRIDAY", data);
        changeMeals(_Meals);
      }

    } catch (ex) {
      debugPrint('Something went wrong: $ex ');
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
        content: Text("Server is down"),
      ));
      _fetchingData = true;
    }
    setState(() => {});
    Navigator.of(context).pop();
  }

  String weekDayFun(String day){
    switch (day) {
      case "MONDAY":{
        return "Segunda-feira";
      }
      case "TUESDAY":{
        return "Terça-feira";
      }
      case "WEDNESDAY":{
        return "Quarta-feira";
      }
      case "THURSDAY":{
        return "Quinta-feira";
      }
      case "FRIDAY":{
          return "Sexta-feira";
      }
    }
    return "";
  }

  void getMeals(String day, Map<String, dynamic> data) {
    Map<String, dynamic> aux1 = data[day];
    Map<String, dynamic> aux2 = {};
    bool update = false;
    try {
      aux2 = aux1['update'];
      update = true;
    } catch (ex) {
      aux2 = aux1['original'];
    }
    String weekDay = weekDayFun(day);

    if (weekDay == "") return;

    String image = "";
    if(aux2['img'] == null) {
      image = "images/logo.png";
    } else {
      image = _imageUrl + aux2['img'].toString();
    }


    Meals meals = Meals(
      img: image.toString(),
      weekDay: weekDay,
      soup: aux2['soup'],
      fish: aux2['fish'],
      meat: aux2['meat'],
      vegetarian: aux2['vegetarian'],
      desert: aux2['desert'],
      update: update,
    );


    _Meals.update(day, (value) => meals);
  }


  @override
  Widget build(BuildContext context) {

    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
          child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Padding(
              padding: const EdgeInsets.fromLTRB(5.0,10.0,10.0,10.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Text(
                    'Ementa',
                    style: TextStyle(
                      fontSize: 20,
                    ),
                    textAlign: TextAlign.left,
                  ),
                  TextButton.icon(
                    onPressed: _fetchMeals,
                    icon: RotationTransition(
                      turns: animation,
                      child: const Icon(
                        Icons.refresh,
                        size: 24.0,
                        color: Color(0xFF894E4E),
                      ),
                    ),
                    label: const Text('Atualizar',style: TextStyle(color: Color(0xFF894E4E)),),
                  ),
                ],
              ),
          ),
          if (!_fetchingData)
            const CircularProgressIndicator(color: Color(0xFF894E4E),),
          if (_fetchingData)
            Expanded(
              child: ListView.separated(

                itemCount: _Meals.length,
                separatorBuilder: (_, __) => const Divider(thickness: 2.0),
                itemBuilder: (BuildContext context, int index) {
                  String key = _Meals.keys.elementAt(index);
                  return Column(
                    children: <Widget>[
                      ListTile(
                        leading: CircleAvatar(
                          radius: 28,
                          backgroundImage: buildImage(_Meals[key]!.img!).image,
                        ),
                        title: Padding(
                          padding: const EdgeInsets.only(bottom: 3.0),
                          child: Text("${_Meals[key]?.weekDay}",
                                style: const TextStyle(
                                decoration: TextDecoration.underline,
                                fontSize: 18,
                                fontWeight: FontWeight.bold,
                                color: Color(0xFF894E4E),
                              )),
                        ),
                        subtitle: buildListView(key,index),
                        onTap: () => {
                          Navigator.pushNamed(
                                context,
                                MealsEdit.routeName,
                                arguments: _Meals.values.elementAt(index),
                              ).then((value) => setState((){_fetchMeals();}))
                        },
                      ),
                      const Divider(
                        height: 2.0,
                      ),
                    ],
                  );
                },
              ),
            ),
        ],
      )),
    );
  }

  Image buildImage(String image) {
    if (image.contains("images/logo.png")) {
      return Image.asset(image);
    }
    return Image.network(image);
  }


  Widget buildListView(String key,int index){
    Color color;
    Color textColor = Colors.black;
    if(_Meals[key]!.update) {
      color = const Color(0xFF894E4E);
      textColor =  const Color.fromRGBO(255, 255, 255, 1.0);
    } else {
      color = const Color.fromRGBO(220, 220, 220, 1.0);
      textColor =  const Color.fromRGBO(0, 0, 0, 1.0);
    }

    return Column(
        children: [
          const SizedBox(
            height: 8,
          ),
          Card(
              color: color,
              child: Column(
                children: [
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text(
                        "Sopa",
                        style: TextStyle(
                          color: textColor,
                          fontWeight: FontWeight.bold,
                          fontSize: 15,
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(
                    height: 4,
                  ),
                  Row(
                    children: [
                      Expanded(
                          child: Align(
                            alignment: Alignment.center,
                            child: Text("${_Meals[key]?.soup}",style: TextStyle(color: textColor),),
                          )),
                    ],
                  ),
                ],
              ),
            ),

          const SizedBox(
            height: 8,
          ),
          Card(
            color: color,
            child: Column(
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text(
                      "Prato de peixe",
                      style: TextStyle(
                        fontWeight: FontWeight.bold,
                        fontSize: 15,
                        color: textColor,
                      ),
                    ),
                  ],
                ),
                const SizedBox(
                  height: 4,
                ),
                Row(
                  children: [
                    Expanded(
                        child: Align(
                          alignment: Alignment.center,
                          child: Text("${_Meals[key]?.fish}",style: TextStyle(color: textColor),),
                        )),
                  ],
                ),
              ],
            ),
          ),
          const SizedBox(
            height: 8,
          ),
          Card(
            color: color,
            child: Column(
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text(
                      "Prato de carne",
                      style: TextStyle(
                        fontWeight: FontWeight.bold,
                        fontSize: 15,
                        color: textColor,
                      ),
                    ),
                  ],
                ),
                const SizedBox(
                  height: 4,
                ),
                Row(
                  children: [
                    Expanded(
                        child: Align(
                          alignment: Alignment.center,
                          child: Text("${_Meals[key]?.meat}",style: TextStyle(color: textColor),),
                        )),
                  ],
                ),
              ],
            ),
          ),
          const SizedBox(
            height: 8,
          ),
          Card(
            color: color,
            child: Column(
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text(
                      "Prato vegetariano",
                      style: TextStyle(
                        fontWeight: FontWeight.bold,
                        fontSize: 15,
                        color: textColor,
                      ),
                    ),
                  ],
                ),
                const SizedBox(
                  height: 4,
                ),
                Row(
                  children: [
                    Expanded(
                        child: Align(
                          alignment: Alignment.center,
                          child:
                          Text("${_Meals[key]?.vegetarian}",style: TextStyle(color: textColor),),
                        )),
                  ],
                ),
              ],
            ),
          ),
          const SizedBox(
            height: 8,
          ),
          Card(
            color: color,
            child: Column(
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text(
                      "Sobremesa",
                      style: TextStyle(
                        fontWeight: FontWeight.bold,
                        fontSize: 15,
                        color: textColor,
                      ),
                    ),
                  ],
                ),
                const SizedBox(
                  height: 4,
                ),
                Row(
                  children: [
                    Expanded(
                        child: Align(
                          alignment: Alignment.center,
                          child: Text("${_Meals[key]?.desert}" ,
                            style: TextStyle(color: textColor),),
                        )),
                  ],
                ),
              ],
            ),
          ),
        ],
    );
  }

}
