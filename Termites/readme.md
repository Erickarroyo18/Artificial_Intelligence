[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-c66648af7eb3fe8bc4f294546bfd86ef473780cde1dea487d3c4ff354943c9ae.svg)](https://classroom.github.com/online_ide?assignment_repo_id=9970659&assignment_repo_type=AssignmentRepo)
# Introducción a Agentes.

Implementación de un agente biológico basado en el comportamiento de las termitas. Para mayores
instrucciones por favor lea el PDF anexo y revise el código.

Es el código base para la práctica correspondiente. Está prohibido compartir soluciones con compañeros o estudiantes fuera del curso.

## Ejecución
Para compilar y ejecutar este código desde la terminal se recomiendan los comandos siguientes:

```
mkdir classes
javac -d ./classes -cp lib/core.jar:. termitas/Termitas.java
java -cp ./classes:lib/core.jar termitas.Termitas
```
## Comandos alternativos
De no funcionar los comandos anteriores, como lo fue en mi caso, usar los siguientes comandos, estos fueron probados
en window 10 y jdk 17.0.5:
```
 mkdir classes 
 javac -d ./classes -cp ".;lib/core.jar" .\termitas\Termitas.java
 java -cp ".;classes.;lib/core.jar" termitas.Termitas
```
