[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-c66648af7eb3fe8bc4f294546bfd86ef473780cde1dea487d3c4ff354943c9ae.svg)](https://classroom.github.com/online_ide?assignment_repo_id=10426455&assignment_repo_type=AssignmentRepo)
# Recocido Simulado
 Implementación de un algoritmo que simula un estado de temperaturas variables donde la temperatura de un estado influye en la probabilidad de toma de decisiones en cada paso. Además se trata de un algoritmo estocástico, pues la decisión de pasar a un esta *mejor/peor* puede depender de determinar cierta probabilidad.

Material auxiliar para la práctica de laboratorio de IA

Dentro de la carpeta ```data``` se incluye un ejemplo de archivo .tps, en caso de que el sitio de internet no esté disponible al realizar la práctica.
# Ejecución
Nota: Ejecute los comando desde la carpeta principal, el archivo *.tsp* debe estar en el directorio *data*.
```
javac recocido\Main.java
java recocido.Main "*nombre del archivo [.tsp]*"
```
La representación de la solución en esta implementación es un arreglo unidimensional, cuya longitud es igual al número de ciudades leídas por la clase DatosPAV más uno, pues la ciudad de partida queda fijdada en la última posición. Este arreglo almacena los indentificadores de cada una de las ciudades sin repetición.
Dado que la lectura del archivo es secuencial, la generación de una solución es aleatoria. Entonces, para la contrucción de una nueva solución a partir de una *actual* se determinan dos subarreglos, de dimensión igual a cinco, de forma aleatoria, para después determinar de forma pseudoaleatoria el índice del subarreglo que será intercambiado por el otro índice cálculado. 

*Anexo un ejemplo de la salida del programa*
```
15, 6, 3, 24, 25, 27, 29, 30, 14, 9, 4, 1, 0, 5, 7, 8, 12, 2, 13, 34, 37, 32, 31, 20, 22, 23, 16, 18, 17, 10, 26, 21, 19, 28, 36, 35, 33, 11, 15
Distacia: 1010823.75km
```
