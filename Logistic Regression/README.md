[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-718a45dd9cf7e7f842a935f5ebbe5719a5e09af4491e668f4dbf3b35d5cca122.svg)](https://classroom.github.com/online_ide?assignment_repo_id=10897864&assignment_repo_type=AssignmentRepo)
# Regresión Logística
La regresión logística es un modelo estadístico utilizado para predecir la probabilidad de ocurrencia de un evento binario. En otras palabras, la regresión logística se utiliza para modelar la relación entre una variable dependiente binaria y una o más variables independientes, y para predecir la probabilidad de que la variable dependiente sea *verdadera* dadas ciertas características de las variables independientes.

# Implementación (python)

Este programa consta de un solo archivo *.py* que contiene la clase especificada **LogisticRegression**. Esta, a su vez, contiene tres funciones principales: *predict_proba()* que por medio de la función sigmoide calcula la probabilidad de que una entrada pertenezca a la clase uno, *predict()* haciendo uso de su función auxliar *predict_input()* se encarga de clasificar las entradas dadas y la función *fit()* que se encarga de "entrenar" a nuestro modelos. Todas estas hace uso de la función *sigmoide()*.

Las primeras funciones mencionadas son bastante intuitivas, por tanto solo hagamos enfasis en la función *fit()*, como se menciona en la documentación, esta está basada en el *algoritmo 3*. Esta función pretende encontrar los valores de los parámetros del modelo que minimizan una función de costo *sigmoide*.

Como sabemos en una regresión logística, la máxima verosimilitud (MV) se utiliza para encontrar los valores de los coeficientes de regresión que maximizan la probabilidad de los datos observados. Este proceso implica maximizar la función de verosimilitud. Por otro lado, la minimización de la función objetivo se utiliza para encontrar los valores de los coeficientes de regresión que minimizan una función de costo, como la función de pérdida logística o la función de *entropía cruzada*.

Aunque la maximización de la función de verosimilitud y la minimización de la función de costo pueden parecer opuestos, ambos métodos conducen a los mismos valores de los parámetros del modelo en una regresión logística. En otras palabras, encontrar los valores de los parámetros que maximizan la MV es equivalente a encontrar los valores de los parámetros que minimizan la función de costo.

En conclusión, la MV y la minimización de la función objetivo son métodos diferentes pero equivalentes para encontrar los valores de los parámetros del modelo en una regresión logística.

Por tanto, a fines de sta implementación la función (*fit()*) retorna la minimización de la función de costo *sigmoide()*.

Por otro lado, en el archivo **main.py** se encuentran algunas funciones globales (ya documentadas), y como podemos darnos cuenta, algunas de estás permiten ser mofidicadas, como la función *getSets(n,m)*, donde n es el número de entradas para la clase cero y m el número para la clase uno. Estos valores pueden ser modificados sin prolemas para incrementar la cardinalidad del conjunto de entrenamiento. **Sugerimos leer la documentación antes de modificar**.

Por último, en la sección de *Prueba* se encuentra la variable **testSet** ahí puede añadir las entradas que quiera probar.

# Ejecución

Nota: Ejecute desde directorio *src*

```
python main.py

``` 

# Reporte (Comparación con Perceptrón)

Por un lado, observemos que el enfoque de ambas implementaciones cambia, puesto que, si recordamos la implementación del perceptrón, tenemos que este tiene como propósito "aprender" a resolver las compuertas *AND* y *OR* para tres variables. Y esta implementación únicamente para dos. Por lo tanto, tratar de compararlos se vuelve una tarea un poco más complicada.

Por otro lado, recordando la implementación del perceptron, su aprendizaje se basa en el iterativo cálculo del error respecto a una entrada y la salida esperada, de tal forma que este dicta si el reajuste de los pesos es necesario. De este modo, este termina una vez que a minimizado el error *(error=0)*, para todas su entradas. Notemos que, dicha implementación nos permite entrenarlo para ambas compuertas.

Ahora, la implementación de esta regresión comparte el concepto de reajuste de pesos. Pero no de la misma manera, pues la forma en la que esta pretende ajustarse es respecto a una función de coste. Es decir, dado que no cálculamos especificamente el error de la entrada con su respectica salida, sino que pretende minimizar el valor de la función de coste. En otras palabras, el reajuste iterativo de los pesos y de theta, se basa en encontrar los valores de los coeficientes *(W y theta)*  que maximicen la probabilidad de los datos de entrada.

En conslusión, ambas implementaciones permiten hacer análogías dependiendo de cual tomemos como punto de partida. Los reajustes iterativos satisfacen el propósito de aprendizaje necesario para este conjunto de problemas. Obviando, las diferencias respecto al discernimiento para cada recalculo.