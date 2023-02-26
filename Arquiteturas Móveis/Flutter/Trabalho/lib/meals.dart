class Meals{
  final String? img;
  final String weekDay;
  final String soup;
  final String fish;
  final String meat;
  final String vegetarian;
  final String desert;
  bool update;

  Meals({
    required this.img,
    required this.weekDay,
    required this.soup,
    required this.fish,
    required this.meat,
    required this.vegetarian,
    required this.desert,
    required this.update,
  });


  @override
  String toString() {
    return "$img $weekDay $soup $fish $meat $vegetarian $desert $update";
  }

}