[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-c66648af7eb3fe8bc4f294546bfd86ef473780cde1dea487d3c4ff354943c9ae.svg)](https://classroom.github.com/online_ide?assignment_repo_id=10648675&assignment_repo_type=AssignmentRepo)
# ia-proyecto
Máquina de inferencias

# Implementación
Se añadió una clase (`NodoBusqueda`) al archivo *strips.py*, con el propósito de representar una estructura adicional que nos permitierá almacenar la información necesaria para contruir el camino en reversa desde el estado meta al estado inical. Esta clase contiene el método `obtener_ruta` que recontruye el camino que llevó al estado meta en sentido contrario, i.e., a partir de sus padres.

A la clase `Problema` se le añadió el método `busqueda_amplitud` que recibe dos cosas, la verificación para imprimir el proceso de búsqueda *(printV)* y un entero *(max_iter)* que representa el número máximo de acciones que se aplicarán. Además, se realizaron ajustes a algunas funciones de la primer parte del proyecto, estos ajustes no modificaron el comportamiento principal, solo se adecuaron los tipos de parámetros.

En el archito `__init__.py` se encuentras las pruebas de la primer parte, y de la segunda. En estas se espera ingresar comandos por la terminal. La primer entrada le permite al usuario decidir que quiere ver la ejecución de la búsqueda *(printV)*. La segunda entrada es para designar el número de acciones que se aplicarán *(max_iter)*.

## Ejecución
*Nota:* Ejecutar comando desde directorio **scripts\pclasica**.
```
    python __init__.py
```

# ¿Es suficiente información?, ¿Cómo se comporta tu programa?
Cómo se puede observar la implentación, el código es rígido, lo que restringe a este mismo a solo poder operar con este lenguaje de *strips*. Además, cómo sabemos, en la búsqueda por amplitud se evalúan los estados siguientes sin conocer si el estado a evaluar es mejor o peor que el estado anterior. De ahí las adecuaciones de la primer parte del proyecto. Respecto a la primer pregunta, se considera que la implementación ofrece la información suficiente para resolver el problema. Sin embargo, es bastante tardado.
Ahora, respecto a la segunda pregunta, el programa dado un estado, procede a verificar que acciones son aplicables al mismo. Tras dicha verificación procede a aplicar dicha acción y a insertar ese nuevo estado a una lista *(estados)*, para después volver a trabajar con ese estado. Este proceso continua hasta llegar al límite establecido en *(max_iter)* o si es que se ha llegado al estado meta. 
En caso de llegar al estado meta, se le pregunta al usuario si desea imprimir el recorrido que lo llevó a la meta.