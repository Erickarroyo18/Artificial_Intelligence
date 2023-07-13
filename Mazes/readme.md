[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-c66648af7eb3fe8bc4f294546bfd86ef473780cde1dea487d3c4ff354943c9ae.svg)](https://classroom.github.com/online_ide?assignment_repo_id=10336045&assignment_repo_type=AssignmentRepo)
## Generación de Laberintos (BackTrack)

Implementación de un generador de laberintos en processing y java aplicando el principio de *BackTracking*.

## Interacción
Para poder interactuar correctamente con el programa se recomiendan los comandos del apartado de *Ejecución*.
Así mismo, para poder realizarle pruebas particulares, es decir, sí quiere determinar las dimensiones de su laberinto
asegúrese de modificar los atributos *(largo* y *ancho)* de la clase principal *(Laberinto.java)*. En caso contrario, al ejecutar los comandos posteriores se cargar por default un ejemplar de dimenciones *15x15*.
## Ejecución
``` 
 mkdir classes
 javac -d ./classes -cp ".;lib/core.jar" .\laberintos\Laberinto.java
 java -cp ".;classes.;lib/core.jar" laberintos.Laberinto
 ```

