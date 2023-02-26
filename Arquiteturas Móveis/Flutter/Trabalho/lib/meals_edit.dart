
import 'dart:io';

import 'package:camera/camera.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:tp_am_2/meals.dart';
import 'package:http/http.dart' as http;
import 'dart:typed_data';
import 'dart:async';
import 'dart:convert';

import 'camera_page.dart';



const String _mealsUrl = 'http://10.0.2.2:8080/menu';
/*const String _mealsUrl = 'http://localhost:8080/menu';*/
/*const String _mealsUrl = 'http://192.168.1.87:8080/menu';*/
/*const String _mealsUrl = 'http://192.168.43.201:8080/menu';*/
/*const String _imageUrl = 'http://192.168.43.201:8080/images/';*/
const String _imageUrl = 'http://10.0.2.2:8080/images/';

class MealsEdit extends StatefulWidget {
  const MealsEdit({Key? key}) : super(key: key);

  static const String routeName = '/MealsEdit';

  @override
  State<MealsEdit> createState() => _MealsEditState();
}

class _MealsEditState extends State<MealsEdit> {

  late Meals meals = ModalRoute.of(context)!.settings.arguments as Meals;
  bool webFile = false;
  final TextEditingController _soup = TextEditingController();
  final TextEditingController _fish = TextEditingController();
  final TextEditingController _meat = TextEditingController();
  final TextEditingController _vegetarian = TextEditingController();
  final TextEditingController _desert = TextEditingController();
  String? img;
  late String soup;
  late String fish;
  late String meat;
  late String vegetarian;
  late String desert;
  bool original = false;
  XFile? image;
  bool takePic = false;

  @override
  void initState(){
    super.initState();
    Future.delayed(Duration.zero, () {
      initMeals();
      _soup.addListener(() {
        soup = _soup.value.text;
      });
      _fish.addListener(() {
        fish = _fish.value.text;
      });
      _meat.addListener(() {
        meat = _meat.value.text;
      });
      _vegetarian.addListener(() {
        vegetarian = _vegetarian.value.text;
      });
      _desert.addListener(() {
        desert = _desert.value.text;
      });
      takeCameras();
    });

    
  }

  @override
  void dispose(){
    _soup.dispose();
    _fish.dispose();
    _meat.dispose();
    _vegetarian.dispose();
    _desert.dispose();
    super.dispose();
  }

  Future<void> initMeals() async {
    setState(() {
      _soup.text = meals.soup.toString();
      soup = meals.soup.toString();
      _fish.text = meals.fish.toString();
      fish = meals.fish.toString();
      _meat.text = meals.meat.toString();
      meat= meals.meat.toString();
      _vegetarian.text = meals.vegetarian.toString();
      vegetarian = meals.vegetarian.toString();
      _desert.text = meals.desert.toString();
      desert = meals.desert.toString();
    });
  }

  Future<void> changeMeals() async {
    setState(() {
      soup = _soup.text;
      fish = _fish.text;
      meat = _meat.text;
      vegetarian = _vegetarian.text;
      desert = _desert.text;
    });
  }


  Future<void> update() async{

    showDialog(context: context, builder: (context){
      return Center(child: CircularProgressIndicator(color: Color(0xFF894E4E),));
    });
    setState(() {

    });
    try{
      debugPrint("takePic" + takePic.toString());
      var m;
      if(webFile){
        http.Response response2 = await http.get(Uri.parse(meals.img.toString())).timeout(const Duration(seconds: 3));
        final bytes2 = response2.bodyBytes;
        m = base64.encode(Uint8List.view(bytes2.buffer));
      }else if(takePic) {
        final path = image!.path;
        final bytes = await File(path).readAsBytes();
        var buffer = bytes.buffer;
        m = base64.encode(Uint8List.view(buffer));
      }else{
        String imageFile = meals.img.toString();
        ByteData bytes = await rootBundle.load(imageFile);
        var buffer = bytes.buffer;
        m = base64.encode(Uint8List.view(buffer));
      }

      String day = weekDayFun(meals.weekDay);
      Map<String,dynamic> jsonMap = {
        'img' : m,
        'weekDay' : day,
        'soup' : soup,
        'fish' : fish,
        'meat' : meat,
        'vegetarian' : vegetarian,
        'desert' : desert
      };

      var body = json.encode(jsonMap);
      http.Response response = await http.post(
        Uri.parse(_mealsUrl),
        headers: {"Content-Type": "application/json; charset=UTF-8"},
        body: body
      ).timeout(const Duration(seconds: 3));
      original = false;
    }catch(ex) {
      debugPrint('Something went wrong2: $ex');
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
        content: Text("Server is down"),
      ));
    }finally{
      Navigator.of(context).pop(); //pop do CircularProgressIndicator
      Navigator.of(context).pop();
    }
  }

  void updateOriginal(){
    soup = "";
    fish = "";
    meat = "";
    vegetarian = "";
    desert = "";
    original = true;
    update();
  }

  String weekDayFun(String day){
    switch (day) {
      case "Segunda-feira":{
        return "MONDAY";
      }
      case "Terça-feira":{
        return "TUESDAY";
      }
      case "Quarta-feira":{
        return "WEDNESDAY";
      }
      case "Quinta-feira":{
        return "THURSDAY";
      }
      case "Sexta-feira":{
        return "FRIDAY";
      }
    }
    return "";
  }


  late List<CameraDescription> _cameras;
  late CameraDescription firstCamera;

  Future<void> takeCameras() async {
    WidgetsFlutterBinding.ensureInitialized();

    _cameras = await availableCameras();

    firstCamera = _cameras.first;
  }

  @override
  Widget build(BuildContext context) {

    return Scaffold(
      appBar: AppBar(
        title: Text("Editar ementa"),
      ),
      body: Center(
        child:
            SafeArea(
              child: SingleChildScrollView(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    const SizedBox(height: 10,),
                    Text(meals.weekDay,
                      textAlign: TextAlign.center,
                      style: const TextStyle(
                        decoration: TextDecoration.underline,
                        fontSize: 30,
                        fontWeight: FontWeight.bold,
                        color: Color(0xFF894E4E),
                      ),
                    ),
                    const SizedBox(height: 10,),
                    Center(
                      child: Stack(
                        children: [
                          CircleAvatar(
                            radius: 50,
                            backgroundImage: image == null ? buildImage(meals.img!).image : FileImage(File(image!.path)),
                          ),
                          Positioned(
                              bottom: -5.0,
                              right: 0.0,
                              child: InkWell(
                                onTap: () {
                                    Navigator.push(context,CupertinoPageRoute(builder: (context) => CameraApp(camera: firstCamera)))
                                        .then((value) => setState((){
                                          image = value;
                                          takePic = true;
                                          webFile = false;
                                          print(takePic.toString() + " " + webFile.toString());
                                        }));
                                },
                                child: const Icon(
                                  Icons.camera_alt,
                                  color: Color(0xFF894E4E),
                                  size: 28.0,
                                ),

                              ),
                          )
                        ],
                      ),
                    ),

                    const SizedBox(height: 20,),
                    const Text("Sopa: ",style: TextStyle(fontWeight: FontWeight.bold,
                      fontSize: 20,),),
                    SizedBox(height: 10,),
                    Padding(
                      padding: const EdgeInsets.fromLTRB(10, 0, 10, 0),
                      child: TextFormField(
                        decoration: const InputDecoration(
                          labelText: 'Sopa',
                          hintText: 'Introduza a sopa',
                          border: OutlineInputBorder(),
                        ),
                        controller: _soup,
                        onChanged: (value) => changeMeals(),
                      ),
                    ),

                    const SizedBox(height: 10,),
                    const Text("Prato de peixe: ",style: TextStyle(fontWeight: FontWeight.bold,
                      fontSize: 20,),),
                    SizedBox(height: 20,),
                    Padding(
                      padding: const EdgeInsets.fromLTRB(10, 0, 10, 0),
                      child: TextFormField(
                        decoration: const InputDecoration(
                          labelText: 'Prato de peixe',
                          hintText: 'Introduza o prato de peixe',
                          border: OutlineInputBorder(),
                        ),
                        controller: _fish,
                        onChanged: (value) => changeMeals(),
                      ),
                    ),

                    const SizedBox(height: 10,),
                    const Text("Prato de carne: ",style: TextStyle(fontWeight: FontWeight.bold,
                      fontSize: 20,),),
                    SizedBox(height: 20,),
                    Padding(
                      padding: const EdgeInsets.fromLTRB(10, 0, 10, 0),
                      child: TextFormField(
                        decoration: const InputDecoration(
                          labelText: 'Prato de carne',
                          hintText: 'Introduza o prato de carne',
                          border: OutlineInputBorder(),
                        ),
                        controller: _meat,
                        onChanged: (value) => changeMeals(),
                      ),
                    ),

                    const SizedBox(height: 10,),
                    const Text("Prato vegetariano: ",style: TextStyle(fontWeight: FontWeight.bold,
                      fontSize: 20,),),
                    SizedBox(height: 20,),
                    Padding(
                      padding: const EdgeInsets.fromLTRB(10, 0, 10, 0),
                      child: TextFormField(
                        decoration: const InputDecoration(
                          labelText: 'Prato vegetariano',
                          hintText: 'Introduza o prato vegetariano',
                          border: OutlineInputBorder(),
                        ),
                        controller: _vegetarian,
                        onChanged: (value) => changeMeals(),
                      ),
                    ),

                    const SizedBox(height: 20,),
                    const Text("Sobremesa: ",style: TextStyle(fontWeight: FontWeight.bold,
                      fontSize: 20,),),
                    SizedBox(height: 10,),
                    Padding(
                      padding: const EdgeInsets.fromLTRB(10, 0, 10, 0),
                      child: TextFormField(
                        decoration: const InputDecoration(
                          labelText: 'Sobremesa',
                          hintText: 'Introduza a sobremesa',
                          border: OutlineInputBorder(),
                        ),
                        controller: _desert,
                        onChanged: (value) => changeMeals(),
                      ),
                    ),
                    Row(
                      children: [
                        Expanded(
                          child: Padding(
                            padding: const EdgeInsets.fromLTRB(10, 10, 10, 50),
                            child: ElevatedButton(
                                onPressed: update,
                                style: ButtonStyle(backgroundColor: MaterialStateProperty.all(const Color(0xFF894E4E))),
                                child: const Text("Atualizar ementa")
                            ),
                          ),
                        ),
                        Expanded(
                          child: Padding(
                            padding: const EdgeInsets.fromLTRB(10, 10, 10, 50),
                            child: ElevatedButton(
                              onPressed: updateOriginal,
                              style: ButtonStyle(backgroundColor: MaterialStateProperty.all(const Color(0xFF894E4E))),
                              child: const Text("Ementa original"),
                            ),
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ),
          ),
      );
  }


  Image buildImage(String imageUrl) {
    if (imageUrl.contains("images/logo.png")) {
      return Image.asset(imageUrl);
    }
    webFile = true;
    return Image.network(imageUrl);
  }

}
