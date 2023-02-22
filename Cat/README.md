[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-c66648af7eb3fe8bc4f294546bfd86ef473780cde1dea487d3c4ff354943c9ae.svg)](https://classroom.github.com/online_ide?assignment_repo_id=10157397&assignment_repo_type=AssignmentRepo)
# Estados y espacio de búsqueda.

Generación de los posibles estados del juego de gato.

## Ejecución
Para compilar y ejecutar este código desde la terminal, se recomiendan los comandos siguientes:

```
javac -d ./classes -cp lib/core.jar:. gatos/*.java
java -cp ./classes:lib/core.jar gatos.Gatos
```
## Comandos alternativos
De no funcionar los comandos anteriores, como lo fue en mi caso, usar los siguientes comandos, estos fueron probados
en window 10 y jdk 17.0.5:

```
javac -d ./classes -cp ".;lib/core.jar" .\gatos\Gatos.java
java -cp ".;classes.;lib/core.jar" gatos.Gatos
``` 
