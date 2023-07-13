[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-718a45dd9cf7e7f842a935f5ebbe5719a5e09af4491e668f4dbf3b35d5cca122.svg)](https://classroom.github.com/online_ide?assignment_repo_id=10816214&assignment_repo_type=AssignmentRepo)
# Perceptrón, unidad fundamental de las redes neuronales.
Conocer el funcionamiento de la unidad elemental con la cual se construyen las redes
neuronales: el perceptrón, y lograr implementar un perceptrón simple que aprenda las
operaciones AND y OR para tres variables.

# Funcionamiento
Este programa consiste en dos archivos desarrolladas en lenguaje *java* cuyo proposito es el antes mencionado.
El archivo de clase *Main.java* implementa los conjuntos de entrenamiento para el perceptron, así como el conjunto
de prueba. A su vez, esta permite interactuar limitadamente con el usuario. Por su lado, el archivo de clase *Perceptron.java* implementa la lógica de entrenamiento del mismo, en esta se encuentra la documentación necesaria
para comprender la implementación.

# Ejecución
Ejecutar desde directorio *src*
```
mkdir classes
javac -d ./classes Main.java
java -cp ".;classes." Main
```
# Comandos alternativos

```
javac Main.java
java Main
```
