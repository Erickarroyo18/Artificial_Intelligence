#!/usr/bin/python3
# -*- coding: utf-8 -*-
import itertools
import sys
# Clase Variable
class Variable(object):
    '''
    Constructor
    nombre de tipo 'str' representa el nombre de la variable
    valores_posibles de tipo 'list' contiene los valores que puede tomar la variable
    '''
    def __init__(self, nombre, valores_posibles):
        # Verificamos el tipo de los parámetros
        assert (type(nombre) == str) and (type(valores_posibles) == list), "Error: Tipos inconsistentes de parametros[Clase Variable]"
        self.nombre = nombre
        self.valores_posibles = valores_posibles
        
    # Metodo que imprime el estado del objeto
    def __str__(self):
        return f"Nombre: {self.nombre} | Valores posibles: {self.valores_posibles}" 

# Clase Factor
class Factor(object):
    '''
    Constructor
    alcance de tipo 'tuple' representa el conjunto de variables del factor
    valores de tipo 'list'  almacena los valores de cada renglón
    '''
    def __init__(self, alcance, valores):
       # Verificamos el tipo de los parámetros
        assert (type(alcance) == tuple) and (type(valores) == list),"Error: Tipos inconsistentes de parametros[Clase Factor]"

        # Calculamos las dimensiones de 'valores'
        renglones = 1
        for a in alcance:
            renglones *= len(a.valores_posibles)
            
        # Verificamos que las dimensiones 
        if renglones != len(valores):
            sys.exit()

        self.alcance = alcance
        self.valores = valores
        self._generar_tabla_de_valores()#  generamos la tabla del contenido del factor
    
    '''
    Funcion de normalizacion 
    '''
    def normalize(self):
        # Obtenemos la suma de todos los valores de las asignaciones
        suma = sum(self.valores)
        # Dividimos cada valor entre la suma
        nuevos_valores = map(lambda x : x/suma, self.valores)
        return Factor(self.alcance, list(nuevos_valores)) 
    
    '''
    Función de multiplicacion de factores (tambien se considera el caso de multiplicar por un escalar)
    factor es el factor con el que se multipicara, puede ser un factor o un escalar
    '''
    def multiply(self, factor):
        # Verificamo el tipo de 'factor', i.e., es un factor o un escalar
        tipo = type(factor)
        assert (tipo == Factor) or (tipo == int) or (tipo == float),"Error: Tipo inconsistente de 'factor'" 

        # Si el parametro es un escalar, el factor resultado tiene el
        # mismo alcance, y sus valores se multiplcan por el escalar.
        if tipo == int or tipo == float:
            nv = list(map(lambda v : v*factor, self.valores))
            return Factor(self.alcance, nv) # Retornamos el Factor

        #En caso de que 'factor' sea un objeto de clase Factor
        # El alcance del factor resultante es la unión de los alcances de los operandos (self, factor).
        nuevo_alcance = self.alcance
        for a in factor.alcance:
            if a not in nuevo_alcance: # aplicamos la union
                nuevo_alcance += (a,)

        # Creamos el factor resultado cuyo alcance es 'nuevo_alcance'
        renglones = 1
        for nv in nuevo_alcance:
            renglones *= len(nv.valores_posibles)
        factor_resultado = Factor(nuevo_alcance, [None]*renglones)# Usamos NONE para inicializar los renglones
        nuevos_valores = [] # Almacenara los nuevos valores para 'factor_resultado'

        # Por cada asignacion posible para 'factor_resultado' creamos un diccionario ({Variables: valores})
        # para este nuevo factor
        for r in factor_resultado.tabla_de_valores:
            dic = {}
            for i,v in enumerate(nuevo_alcance):
                dic[v] = r[i]
            # Obtenemos los indices para cada factor
            i = self._obtener_indice(dic)
            j = factor._obtener_indice(dic)
            # Multiplicamos los elementos de los indices obtenidos y agregamos a 'nuevos_valores'
            nuevos_valores.append(self.valores[i]*factor.valores[j])

        # Verificamos que los 'nuevos_valores' sean validos para 'factor_resultante'
        if factor_resultado._verifica_valores(nuevos_valores):
            factor_resultado.valores = nuevos_valores
        # En caso contrario no actualizamos el atributo del factor
        return factor_resultado

    '''
    Funcion que recuccion de un factor al contexto de 'variable'
    variable de tipo Variable, determina el contexto
    valor es es valor asociado a 'variable'
    '''
    def reduce(self, variable, valor):
        # Verificamos el tipo de dato del atributo variable
        assert (type(variable) == Variable), "Error: Tipo de dato inconsistente de 'variable'"
        # Veridicamos que los parametros existan en el alcance 
        if not(variable in self.alcance and valor in variable.valores_posibles): # la segunda condicion puede ser externa
            sys.exit()

        # Si se reduce la unica variable, devolvemos el valor del factor que corresponde al valor indicado de la variable.
        if len(self.alcance) == 1:
            return self.valores[self._obtener_indice({variable : valor})] # pasamos los parametros como diccionario

        # Eliminado a 'variable' del alcance y definimos uno nuevo
        nuevo_alcance = tuple(x for x in self.alcance if x != variable)
        nuevos_valores = [] # Almacenará los nuevos valores del factor

        for tv in self.tabla_de_valores:
            dic = {} # Creamos un diccionario por cada asignacion
            for i,v in enumerate(self.alcance):
                dic[v] = tv[i]
            # Verificamos si el contexto es valido
            if dic[variable] == valor:
                nuevos_valores.append(self.valores[self._obtener_indice(dic)])
                
        return Factor(nuevo_alcance, nuevos_valores) # Retornamos el nuevo factor

    '''
    Funcion de marginalizacion
    variable de tipo Variable, es la variable a marginalizar del factor
    '''
    def marginalize(self, variable):
        # Veridicamos el tipo y exixtencia de 'variable'
        if not ((type(variable) == Variable) and (variable in self.alcance)):
            sys.exit()

        # Si se marginaliza la unica variable, devolvemos la suma de los renglones
        if len(self.alcance) == 1:
            return sum(self.valores)

        # Eliminado a 'variable' del alcance y definimos uno nuevo
        nuevo_alcance = tuple(x for x in self.alcance if x != variable)

        # Creamos el 'factor_resultado' con este nuevo alcance
        renglones = 1
        for v in nuevo_alcance:
            renglones *= len(v.valores_posibles)
        factor_resultado = Factor(nuevo_alcance, [None]*renglones)# NONE para inicializar los campos

        # Inicializamos los valors en cero, este contendra las sumas
        nuevos_valores = [0] * renglones 

        # Por cada asignación posible en el factor ORIGINAL...
        for r in self.tabla_de_valores:
            dicc = {}# Creamos un diccionario por cada asignacion
            for i,v in enumerate(self.alcance):
                dicc[v] = r[i]
            # Obtenemos los indices de los factores
            i = self._obtener_indice(dicc) 
            j = factor_resultado._obtener_indice(dicc)
            nuevos_valores[j] += self.valores[i] # Aplicamos la suma

        # Verificamos que los 'nuevos_valores' sean validos para 'factor_resultante'
        if factor_resultado._verifica_valores(nuevos_valores):
           factor_resultado.valores = nuevos_valores
        # Sino no actualizamos los valores
        return factor_resultado

    '''
    Metodo para imprimir el estado del factor
    '''
    def __str__(self):
        state = f"\t ## Variables del factor ##\n"
        for a in self.alcance:
            state += f"- {str(a)} \n"
        state += f"\t ## Factor ##\n"
        # Obtenemos el nombre de la variable
        state += "\t  Variable/s=[%s]\n" % ", ".join(map(lambda x : x.nombre, self.alcance)) # Usamos join para no imprimir direccion en memoria
        # concatenamos las posibles asignaciones, y su proba
        for i,asignación in enumerate(self.tabla_de_valores):
            state += "Indice= [%s], Asignacion/es=[%s], Probablilidad=[%s]\n" % (i, asignación, self.valores[i])
        return state
    
    #######################################################
    ##               FUNCIONES AUXILIARES                ##
    #######################################################
    '''
    Funcion auxiliar que genera en la matriz de las asignaciones 
    para las variables en el alcance dl factor
    '''
    def _generar_tabla_de_valores(self):
        # Obtenemos la matriz de valores para cada variable
        matriz_valores = map(lambda x : x.valores_posibles, self.alcance)
        # Obtenemos todas las combinaciones de la matriz y la pasamos a una lista
        self.tabla_de_valores = list(itertools.product(*matriz_valores))
    
    '''
    Función que obtiene el indice en la tabla de valores que corresponda a 'asignacion', 
    implementa el poliniomio de direccionamiento.
    asignacion es un diccionario de la forma {Variables:Valores}
    '''
    def _obtener_indice(self, asignacion):
        # Verificamos el tipo del parámetro
        assert (type(asignacion) == dict),"Error: Tipos inconsistentes en metodo '_obtener_indice()'"

        # Verificamos el tipo de las llaves del diccionario y que los valores 
        # relacionados existan en los 'valores_posbles' de la variable 'k' 
        for k,v in asignacion.items():
            if type(k) != Variable or v not in k.valores_posibles:# En caso de ser falso, rompemos toda la ejecución
                sys.exit()
                
        # Verificamos que 'asignacion' contenga las variables del fator.
        asig = () # es una tupla
        for a in self.alcance:
            assert a in asignacion
            asig += (asignacion[a],) # Llenamos la tupla con una asignacion ordenada

        indice = 0 # Almacenara el indice que corresponde a 'asigancion'
        # Aplicamos el polinomio de direccionamiento al alcance para obtener las variables del factor
        for i,v in enumerate(self.alcance): # Accedemos a los indices del alcance
            posicion = v.valores_posibles.index(asignacion[v])
            mult = 1
            for a in self.alcance[i+1:]:# Iteramos a partir del indice i
                mult *= len(a.valores_posibles)
            indice += (posicion * mult) # Ajustamos el indice

        # Comprobamos que se devuelva lo correcto (debug).
        # Verificamos que 'asig' exista en la tabla de valores del factor
        if asig in list(self.tabla_de_valores):
            if list(self.tabla_de_valores).index(asig) == indice:
                return indice # Retornamos el indice de a asignacion        
        
        return -1 # retornamos en caso de no encontrarse el indice de 'asig'
        
    '''
    Funcion que verifica si el tipo y dimensiones de 'valores'
    son adecuados asignarlos al atributo del factor
    '''
    def _verifica_valores(self, valores):
        if(type(valores) == list):
            renglones = 1
            # Verificamos las dimensiones
            for v in self.alcance:
                renglones *= len(v.valores_posibles)
            if renglones == len(valores):
                return True
        
        return False