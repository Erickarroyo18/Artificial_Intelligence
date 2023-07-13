import numpy as np
import random 

class CadenaDeMarkov(object):
    
    '''
    Constructor de clase
    estados_posibles de tipo list es la lista con los nombres de los estados posibles
	vector de tipo numpy.ndarray es el Vector con la probabilidad de iniciar en cada uno de los estados posibles.
	matriz de tipo numpy.ndarray es la Matriz con la probabilidad de transitar de cada estado hacia los demás.
    '''
    def __init__(self, estados_posibles, vector, matriz):
        #Verificamos el tipo de los parametros
        if (type(estados_posibles)==list) and (type(vector)==np.ndarray) and(type(matriz)==np.ndarray):
            # Verificamos las dimensiones de vector y matriz
            if (len(estados_posibles) == vector.size) and (len(estados_posibles)**2 == matriz.size):
                # Verificamos las sumas de las probabilidades
                if (sum(vector[0])==1):
                    for f in matriz:
                        assert sum(f) == 1
                    self.estados_posibles = estados_posibles
                    self.probabilidad_inicial = vector
                    self.matriz_probabilidades = matriz
         
    """
    Metodo para generar una secuencia de estados a partir del modelo de Markov iniciado.
    n de tipo int es el numero de elementos que tendrá la secuencia.
    seed de tipo int es  opcional y representa la semilla para la generación de numeros aleatorios.
    """
    def generar_secuencia_estados(self, n, seed=None):
        # Verificamos el tipo de los parametros
        if type(n)==int:
            if n == 0:
                return []
            else:
                secuencia = []
                fila = self.get_indice_distribucion(self.probabilidad_inicial[0],seed)
                # Obtenemos el indice del siguiente estado
                secuencia.append(self.estados_posibles[fila])
                # Decrementamos a n en 1
                n -= 1
                # Construimos la secuencia a partir de la probabilidad actual
                actual = secuencia[0]
                while n>0:
                    fila = self.get_indice_distribucion(self.matriz_probabilidades[fila],seed)
                    actual = self.estados_posibles[fila]
                    secuencia.append(actual)
                    n -= 1
            return secuencia
        else:
            return None
    
    """
    Metodo que dada una distribucion de probabilidad retorna el 
    indice de la probabilidad.
    distribucion es de tipo numpy.ndarray y representa la distribucion de proba.
    seed es de tipo int y es la semilla de generacion.
	"""
    def get_indice_distribucion(self, distribucion, seed=None):
        # Verificamos el tipo de los parametros
        if (type(distribucion) == np.ndarray) and (seed == None or type(seed) == int):
            # Insertamos la semilla si seed no es None
            if seed != None:
                random.seed(seed)
            r = random.random()
            suma = 0
            # Recorremos la distribucion para obtener el indice
            for i,v in enumerate(distribucion):
                suma += v
                if r<suma:
                    return i
        return -1

    '''
    Metodo que dado un estado valido, retorna el indice en el que se encuentra 
    dentro del atributo estados_posibles.
    '''
    def obtener_indice_estado(self, estado):
        # Verificamos que el estado exista en los estados posibles
        if estado in self.estados_posibles:
            for i,e in enumerate(self.estados_posibles):
                if estado == e:
                    return i
    
    '''
    Metodo que dada una cadena de estados retorna la probabilidad de dicha cadena.
    cadena es de tipo list y representa la secuencia de estados.
    '''
    def obtener_probabilidad_cadena(self, cadena):
        # Verificamos el tipo del parametro y su contenido
        if type(cadena) == list and cadena != []:
            # Esta variable acumulara la proba
            proba = 1.0
            for i,e in enumerate(cadena):
                if i==0:
                    columna = self.obtener_indice_estado(e)
                    proba *= self.probabilidad_inicial[0][columna]
                proba *= self.matriz_probabilidades[self.obtener_indice_estado(cadena[i-1])][self.obtener_indice_estado(e)]
            return proba
        
    '''
    Metodo que estima las probabilidades de cada estado.
    '''
    def distribucion_limite(self):
        proba_T = np.transpose(self.matriz_probabilidades)
        # Obtenemos los valores y vectores usando eig de numpy
        eig = np.linalg.eig(proba_T)
        eig_valores = eig[0]   # Accedemos a los valores
        eig_vectores = np.transpose(eig[1]) # Accedemos a los vectores y calculamos su transpuesta
        lim = -1 # Almacenara la distribución limite
        # Buscamos el vector tal que su valor es 1, y lo normalizamos
        for i,v in enumerate(eig_valores):
            if np.isclose(1,v):
                lim = eig_vectores[i] / sum(eig_vectores[i])
        return lim
    
    '''
    Metodo que imprime el estado de un objeto d clase
    '''
    def __str__(self):
        s = "--------------------------------------\n"
        s += "---------- ESTADO DE CADENA ----------\n"
        s += "\t Estados posibles:\n"
        s += str(self.estados_posibles) + "\n"
        s += "\t Probabilidad de iniciar en cada estado:\n"
        s += str(self.probabilidad_inicial) + "\n"
        s += "\t Matriz de probabilidades:\n"
        s += str(self.matriz_probabilidades) + "\n"
        s += "--------------------------------------"
        return s