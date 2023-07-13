[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-718a45dd9cf7e7f842a935f5ebbe5719a5e09af4491e668f4dbf3b35d5cca122.svg)](https://classroom.github.com/online_ide?assignment_repo_id=11052643&assignment_repo_type=AssignmentRepo)
# Factores
Los factores en las redes Bayesianas son tablas de probabilidad condicional que representan la relación entre un conjunto de variables aleatorias en una red bayesiana. Estas tablas se utilizan para calcular la probabilidad conjunta de todas las variables de la red, teniendo en cuenta las relaciones de dependencia entre ellas.

# Implementación
La práctica esta desarrollada en lenguaje *python*, en el directorio '**scrips** enontrará tres archivos. En el archivo *Factores.py* se implementaron las clases **Variable** y **Factores**, así como las fucniones solicitadas:
*Reducción*, *Normalización*, *Marginalizacion* y *Multiplicación*, claramente sobre la clase *Factores*.
Dado que se trabaja sobre estructuras de datos como listas, tuplas y matrices, el uso de arreglos fue indiscriminado. Sin embargo, se hizo gran enfasis en cuidar los tipos de datos que reciben las funciones, así para evitar inconsistencias en las operaciones. Hacemos uso de al función *assert* de Python para verificar los tipos, pero como sabemos esta misma, no es recomendable para condiciones de gran importancia, por lo que puede ver en el código la combinación de estructuras de contron comunes y esta función.

Por un lado, el archivo *PruebaFactores.py* contiene una prueba por default. Este es el archivo *main* a ejecutar
en el apartado *if __name__ == '__main__':* puede comentar o decomentar las pruebas que desee.
**NOTA:** No fue posible probar la función *pLuviaInv_NoMayNoFin()*, pues aparecen errores de tipos que por motivos de tiempo no pude corregir. Por lo que, se recomienda usar las pruebas del archivo *Tests.py* para probar las funciones.

Por otro lado, el archivo *Tests.py* contiene tres funciones de prueba, todas basadas en el contenido del archivo de especificaciones dado. Para mayor impormación revise dicho documento, puede modificarlo a conveniencia procurando no caer en problemas similares a los de la función antes mencionada. Análogo a esto, puede decomentar o comentar las sentencias print del archivo parar visualizar el estado de los factores.

Para finalizar, se implemento la función de leer ficheros para construir objetos *Variable* y *Factor*. Dado que no se específico la forma de hacerlo, se asumió libre la implementación. Por tanto, para visualizar el conjunto de funciones que permiten esta característica del sistema, vaya al archivo *Tests.py*, tal que, este contiene la documentación necesaria para entender la implementación. Ponga especial atención al formato de declaración de objetos en el archivo *archivo.txt*, y en la lógica de la información en las declaraciones. Pues, como puede darse cuenta los objetos *Variable* y *Factor* contienen validaciones especiales para los atributos. De ahí que, el archivo *archivo.txt*, contiene un ejemplo correcto de texto para dichos objetos. Por lo tanto, cualquier error en la prueba de esta funcionalidad se le adjudica al usuario, pues esta funcionalidad ya ha sido probada.

# Ejecución
*Nota:* Ejecute el siguiente comando desde el directorio **scripts**
'''
python PruebaFactores.py
'''