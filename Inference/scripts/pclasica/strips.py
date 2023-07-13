import copy, random
from itertools import product

def clona(obj):
    """ Atajo para crear copias de objetos recursivamente. """
    return copy.deepcopy(obj)

def _lista_a_dict(l):
    """ Pone a todos los elementos de la lista en un diccionario cuya
    llave es el nombre del objeto.
    Solo sirve para listas de objetos con atributo 'nombre'.
    """
    d = {}
    for o in l:
        d[o.nombre] = o
    return d

# ------ Dominio -----

class Dominio:
    """ Clase para definir el dominio, o espacio de estados en el cual se plantearán problemas de planeacion. """
    def __init__(self, nombre, tipos, predicados, acciones):
        """
        Inicializa un dominio
        :param nombre:
        :param tipos:
        :param predicados:
        :param acciones:
        """
        self.nombre = nombre
        self.tipos = tipos
        self.predicados = predicados
        self.acciones = acciones

        self._predicados = _lista_a_dict(predicados)

    def __str__(self):
        dic = {'name':          self.nombre,
               'types':         "\n        ".join(self.tipos),
               'predicates':    "\n        ".join(str(p) for p in self.predicados),
               'actions':       "\n    ".join(str(a) for a in self.acciones)
               }
        return """
(define (domain {name})
    (:requirements :strips :typing)
    (:types
        {types}
    )
    (:predicates
        {predicates})
    )
    {actions}
)
        """.format(**dic)

    def declaracion(self, nombre):
        """ 
        Devuelve la declaracion del predicado con el nombre indicado.
        """
        return self._predicados[nombre]


class Objeto:
    """ Valor concreto para variables en el dominio. """
    def __init__(self, nombre, tipo):
        """
        Crea un objeto existente en el dominio para este problema.
        :param nombre: Símbolo del objeto
        :param tipo: tipo del objeto
        """
        self.nombre = nombre
        self.tipo = tipo

    def __str__(self):
        return "{} - {}".format(self.nombre, self.tipo)
    
    def __eq__(self, otro):
        return self.nombre==otro.nombre and self.tipo == otro.tipo


class Variable:
    """ Variable tipada. """
    def __init__(self, nombre, tipo, valor=None):
        """
        :param nombre: símbolo nombre de esta variable.  Los nombres de variables inician con ?
        :param tipo: tipo de la variable, debe estar registrado en la descripcion del dominio
        :param valor: objeto vinculado a esta variable, si es None la variable está libre
        """
        self.nombre = nombre
        self.tipo = tipo
        self._valor = valor

    @property
    def valor(self):
        return self._valor

    @valor.setter
    def valor(self, valor_nuevo):
        """
        Permite asignar None o un valor de tipo de esta variable.
        """
        if valor_nuevo and valor_nuevo.tipo != self.tipo:
            raise Exception(f"{valor_nuevo} no es de tipo {self.tipo}")
        self._valor = valor_nuevo

    @valor.deleter
    def valor(self):
        self._valor = None

    def __str__(self):
        if self.valor:
            return self.valor.nombre
        return "{} - {}".format(self.nombre, self.tipo)
    
    def __eq__(self, otro):
        return self.nombre == otro.nombre and self._valor == otro.valor
    
          
class Formula:
    """
    Predicado o formula generada.
    """
    pass


class Predicado(Formula):
    """ Representa un hecho. """
    def __init__(self, declaracion, variables):
        """
        Predicados para representar hechos.
        :param predicado: declaracion con los tipos de las variables.
        :param variables: lista de instancias de variables en la accion donde se usa este predicado.
        :param negativo: indica un predicado del tipo "no P", utilizable para especificar efectos o metas.
        """
        self.declaracion = declaracion
        self.variables = variables

    def __str__(self):
        pred = "({0} {1})".format(self.declaracion.nombre, " ".join(v.valor.nombre if v.valor else v.nombre for v in self.variables))
        return pred
    
    def __eq__(self, otro):
        if isinstance(otro, No):
            return False
        if not(self.declaracion == otro.declaracion):
            return False
        else:
            for i in range(0,len(self.variables)):
                if not(equalsObject(self.variables[i].valor,otro.variables[i].valor)):
                    return False
        return True


class DeclaracionDePredicado:
    """ Representa un hecho. """
    def __init__(self, nombre, variables):
        """
        Declaracion de predicados para representar hechos.
        :param nombre:
        :param variables: lista de variables tipadas
        """
        self.nombre = nombre
        self.variables = variables
    
    def __eq__(self, otro):
        return self.nombre == otro.nombre 

    def __str__(self):
        pred = "({0} {1})".format(self.nombre, " ".join(str(v) for v in self.variables))
        return pred

    def _verifica_tipos(self, variables):
        """
        Las variables en la lista deben tener los mismos tipos, en el mismo orden, que este predicado.
        """
        for dec, var in zip(self.variables, variables):
            if (dec.tipo != var.tipo):
                raise Exception(f"Los tipos de las variables {dec} y {var} no coinciden")

    def __call__(self, *args):
        """
        Crea un predicado con las variables o valores indicados y verifica que sean del tipo
        correspondiente a esta declaracion.

        Cuando se usa dentro de una accion las variables deben ser las mismas instancias para todos
        los predicados dentro de la misma accion.
        """
        variables = []
        for var, arg in zip(self.variables, args):
            if isinstance(arg, Objeto):
                # print("instancia ", arg)
                temp_v = clona(var)
                temp_v.valor = arg
                variables.append(temp_v)
            elif isinstance(arg, Variable):
                # print("variable ", arg)
                variables.append(arg)
            else:
                print("Ni lo uno ni lo otro ", arg, " tipo ", type(arg))

        return Predicado(self, variables)


class No(Formula):
    """
    Negacion de un predicado.
    """
    def __init__(self, predicado):
        super().__init__()
        self.predicado = predicado

    def __str__(self):
        return "(not {0})".format(str(self.predicado))

    def __eq__(self, otro):
        if(isinstance(otro, No)):
            return self.predicado == otro.predicado
        else:
            return False


class Accion:
    """ Funcion de transicion con su accion correspondiente. """
    def __init__(self, nombre, parámetros, variables, precondiciones, efectos):
        """
        Inicializa definicion de la funcion de transicion para esta accion.
        :param nombre: nombre de la accion
        :param parámetros: lista de variables tipadas
        :param variables: lista de variables libres que pueden tomar su valor de cualquier objeto del domino siempre que
               sus valores satisfagan las restriciones de las precondiciones.
        :param precondiciones: lista de predicados con variables libres
        :param efectos: lista de predicados con variables libres
        """
        self.nombre = nombre
        self.parámetros = parámetros
        self.vars = variables
        self.precondiciones = precondiciones
        self.efectos = efectos

    def __str__(self):
        dic = {'name':      self.nombre,
               'params':    " ".join(str(p) for p in self.parámetros),   # Podrían reunirse 1o los de tipos iguales
               'prec':      " ".join(str(p) for p in self.precondiciones),
               'efec':      " ".join(str(p) for p in self.efectos)
               }

        if self.vars:
            dic['vars'] = "\n        :vars         ({})".format(" ".join(str(v) for v in self.vars))
        else:
            dic['vars'] = ""

        if len(self.precondiciones) >= 2:
            dic['prec'] = "(and " + dic['prec'] + ")"

        if len(self.efectos) >= 2:
            dic['efec'] = "(and " + dic['efec'] + ")"

        return """(:action {name}
        :parameters   ({params}) {vars}
        :precondition {prec}
        :effect       {efec}
    )""".format(**dic)


class Problema:
    """ Definicion de un problema en un dominio particular. """
    def __init__(self, nombre, dominio, objetos, predicados, predicados_meta):
        """
        Problema de planeacion en una instancia del dominio.
        :param nombre: nombre del problema
        :param dominio: referencia al objeto con la descripcion genérica del dominio
        :param objetos: lista de objetos existentes en el dominio, con sus tipos
        :param predicados: lista de predicados con sus variables aterrizadas, indicando qué cosas son verdaderas en el
               estado inicial.  Todo aquello que no esté listado es falso.
        :param predicados_meta: lista de predicados con sus variables aterrizadas, indicando aquellas cosas que deben
               ser verdaderas al final.  Para indicar que algo debe ser falso, el predicado debe ser negativo.
        """
        self.nombre = nombre
        self.dominio = dominio # ref a objeto Dominio
        d_objetos = {}
        for objeto in objetos:
            if objeto.tipo not in d_objetos:
                d_objetos[objeto.tipo] = [objeto]
            else:
                d_objetos[objeto.tipo].append(objeto)
        self.d_objetos = d_objetos
        self.estado = predicados
        self.meta = predicados_meta

    def __str__(self):
        dic = {'name':          self.nombre,
               'domain_name':   self.dominio.nombre,
               'objects':       "\n      ".join(" ".join(o.nombre for o in self.d_objetos[tipo]) + " - " + tipo for tipo in self.d_objetos),
               'init':          "\n      ".join(str(p) for p in self.estado),
               'goal':          "\n      ".join(str(p) for p in self.meta)}
        if len(self.meta) >= 2:
            dic['goal'] = "(and " + dic['goal'] + ")"
        return """(define (problem {name}
    (:domain {domain_name})
    (:objects
      {objects})
    (:init
      {init})
    (:goal
      {goal})
)
        """.format(**dic)
        
    """
        BUSQUEDA POR AMPLITUD
        printV : Determina si se han de imprimir los pasos de la busqueda de tipo Bool
        max_iter: Determina el número de iteraciones (aplicación de acciones) máximas a aplicar.
    """
    def busqueda_amplitud(self, printV=False, max_iter=500):
        estado_inicial = self.estado
        estados = [estado_inicial]
        nodo = NodoBusqueda(estado_inicial, None, None, None)
        count = 0
        while count < max_iter:
            count+=1
            # Verificamos después de recorres todos los estados
            if len(estados) == 0:
                return False
            # Obtenemos un estado
            estado = estados.pop(0) 
            # Imprimir en consola.
            if printV:
                print("\nEstado Actual:")
                st = ""
                for p in estado:    
                    print(p)
            # Verificamos si se cumple el estado meta
            if satisfiesMeta(estado, self.meta):
                if printV:
                    print("\n\t¡Satisface la meta!")
                return nodo
            # Aplicamos cada accion posible
            for accion in self.dominio.acciones:
                # Verificamos si la accion es aplicable.
                posible_asignacion = applicableFunction(estado, accion)
                if posible_asignacion != False:
                    if printV:
                        print(f"Aplicando accion {accion.nombre} con asignacion:\n" )
                        for k in posible_asignacion:
                            print (k)
                    estado_nuevo = applyAction(estado, posible_asignacion, accion)
                    nodo = NodoBusqueda(estado_nuevo, nodo, accion, posible_asignacion)
                    estados.append(estado_nuevo) 
'''
    Clase auxiliar para modelar la busqueda
'''
class NodoBusqueda(object):
    """
        actual:  Estado actual.
        padre: NodoBusqueda padre del actual.
        accion: Accion mediante la que se llego al nodo ctual.
        asignacion: Asignacion mediante la que se llego al nodo actual.
    """
    def __init__(self, actual, padre, accion, asignacion):
        self.actual = actual
        self.padre = padre
        self.accion = accion
        self.asignacion = clona(asignacion)
        
    """
        Imprime todos los nodos padres (Nodos de Busqueda)    
    """
    def obtener_ruta(self):
        actual = self
        while actual != None: 
            print(actual.actual)
            if (actual.padre == None):
                print("\nSE LLEGO AL ESTADO INICIAL (NODO)")
            else:
                print(f"\n Se llego por accion {actual.accion.nombre}, con la asignacion:" )
                for a in actual.asignacion:
                    print (a)
            actual = actual.padre
            
# Predicados
p_sostiene = DeclaracionDePredicado('sostiene', [Variable('?k', 'grua'), Variable('?c','contenedor')])
p_libre = DeclaracionDePredicado('libre', [Variable('?k', 'grua')])
p_en = DeclaracionDePredicado('en',[Variable('?c','contenedor'), Variable('?p', 'pila')])
p_hasta_arriba = DeclaracionDePredicado('hasta_arriba',[Variable('?c','contenedor'), Variable('?p','pila')])
p_sobre = DeclaracionDePredicado('sobre',[Variable('?k1','contenedor'), Variable('?k2','contenedor')])

# --- Accion *Toma* ---
# parametros 
param_k = Variable('?k', 'grua')
param_c = Variable('?c', 'contenedor')
param_p = Variable('?p', 'pila')
#variable
otro = Variable('?otro', 'contenedor')
# Accion
a_toma = Accion('toma', [param_k,param_c,param_p],[otro]
                          , [p_libre(param_k),p_en(param_c,param_p),p_hasta_arriba(param_c,param_p), p_sobre(param_c,otro)],
                            [p_sostiene(param_k,param_c), p_hasta_arriba(otro,param_p), No(p_en(param_c,param_p)),
                             No(p_hasta_arriba(param_c,param_p)), No(p_sobre(param_c,otro)), No(p_libre(param_k))])
    
# --- Accion *Pon* ---
a_pon = Accion('pon', [param_k,param_c,param_p],[otro] 
                        , [p_sostiene(param_k,param_c), p_hasta_arriba(otro, param_p)]
                        , [p_en(param_c,param_p), p_hasta_arriba(param_c,param_p), p_sobre(param_c,otro),
                           No(p_hasta_arriba(otro, param_p)), No(p_sostiene(param_k,param_c)), p_libre(param_k)]); 
# Dominio
dominio = Dominio('platform-worker-robot',
                      ['contenedor','pila','grua'],
                      [p_sostiene,p_libre,p_en,p_hasta_arriba,p_sobre],
                      [a_pon,a_toma])

# Objetos
k1 = Objeto('k1', 'grua')
k2 = Objeto('k2', 'grua')
p1 = Objeto('p1', 'pila')
p2 = Objeto('p2', 'pila')
q1 = Objeto('q1', 'pila')
q2 = Objeto('q2', 'pila')
ca = Objeto('ca','contenedor')
cb = Objeto('cb','contenedor')
cc = Objeto('cc','contenedor')
cd = Objeto('cd','contenedor')
ce = Objeto('ce','contenedor')
cf = Objeto('cf','contenedor')
pallet = Objeto('pallet','contenedor')
    
# Estos son los los predicados que componen al estado 
# Predicados aterrizados init
pred_init = [p_en(ca,p1),p_en(cb,p1),p_en(cc,p1),p_en(cd,q1),p_en(ce,q1),p_en(cf,q1),
                 p_sobre(ca,pallet),p_sobre(cb,ca),p_sobre(cc,cb),p_sobre(cd,pallet),
                 p_sobre(ce,cd),p_sobre(cf,ce),p_hasta_arriba(cc,p1),p_hasta_arriba(cf,q1),
                 p_hasta_arriba(pallet,p2),p_hasta_arriba(pallet,q2),p_libre(k1),p_libre(k2)]
    
# Predicados aterrizados goal
pred_goal = [p_en(ca,p2),p_en(cb,q2),p_en(cc, p2),p_en(cd,q2),p_en(ce,q2),p_en(cf,q2)]

# Problema
problema = Problema('dwrpb1', dominio, [k1,k2,p1,p2,q1,q2,ca,cb,cc,cd,ce,cf,pallet], pred_init, pred_goal)
    
###############################
###### Listas auxiliares ######
###############################
list_gruas = [k1,k2]
list_cont = [ca,cb,cc,cd,ce,cf,pallet]
list_pilas = [p1,p2,q1,q2]
    
###############################
## Funcion de auxiliares ######
###############################
    
#compara a dos objetos
def equalsObject(ob1,ob2):
    return ob1 == ob2
        
#compara a dos Predicados 
def equalsPredicate(p1,p2):
    return p1==p2

#Genera todas las combinaciones posibles entre el conjunto de objetos sin repeticion de contenedores
# [0] = grua, [1] = pila, [2] =contenedor c, [3] = contenedor otro
# las listas son las auxiliares definidas arriba
def getCombWithNotRepetition():
    cleanList = []
    comb = list(product(list_gruas,list_pilas,list_cont,list_cont))
    for c in comb:
        if not(equalsObject(c[2],c[3])):
            cleanList.append(c)
    return cleanList

#Esta funcion genera todas las sustituciones de varibles en las precondicones de la accion Toma
# y retorna una lista que contiene todas las sustituciones de combinaciones en las pre de la accion Toma
def generarPreToma():
    combinations = getCombWithNotRepetition()
    sust = []
    for c in combinations:
        sust.append([p_libre(c[0]),p_en(c[2],c[1]),p_hasta_arriba(c[2],c[1]), p_sobre(c[2],c[3])])
    return sust

#Esta funcion genera todas las sustituciones de varibles en las precondicones de la accion Toma
# y retorna una lista que contiene todas las sustituciones de combinaciones en las pre de la accion Toma
def generarPrePon():
    combinations = getCombWithNotRepetition()
    sust = []
    for c in combinations:
        sust.append([p_sostiene(c[0],c[2]), p_hasta_arriba(c[3], c[1])])
    return sust

#funcion que comprueba si un predicado existe en el estado
#st es el estado init
#p es un predicado generado con alguna combinacion de objetos
def existPinS(st, p):
    for s in st:
        if equalsPredicate(s,p):
            return True
    return False
    
#Esta funcion determina si todos los elementos de una lista *assig* de predicados 
#existe en la lista *st*
def assigValid(st, assig):
    valid = True
    for a in assig:
        valid = valid and existPinS(st,a)
    return valid
    
#funcion que retorna la lista de asignaciones que satisfacen las predondiciones
#de la accion dada en [cs]
#st es el estado actual
#cs es e conjunto de asignaciones generadas por combinacion de valores
def getValidAssig(st, cs):
    valids = []
    for c in cs:
        if assigValid(st,c):
            valids.append(c)
    return valids
    
#Funcion que obtiene los objetos de la lista de precondiciones de Toma, i.e., [p,k,c,otro]
def getObjectsToma(assig):
    return [assig[0].variables[0],assig[1].variables[1],assig[2].variables[0], assig[3].variables[1]]
    
#Funcion que obtiene los objetos de la lista de precondiciones de Pon , i.e., [p,k,c,otro]
def getObjectsPon(assig):
    return [assig[0].variables[0],assig[1].variables[1],assig[0].variables[1],assig[1].variables[0]]
        
#Funcion que verifica si un objeto existe en una lista de objetos
def existObject(l,obj):
    for e in l:
        if equalsObject(e,obj):
            return True
    return False
    
#Funcion que genera la lista de efecto de accion Toma con la asignacion valida
def getEfectsToma(obs):
    k = obs[0]
    p = obs[1]
    c = obs[2]
    otro = obs[3]
    return [p_sostiene(k,c), p_hasta_arriba(otro,p), No(p_en(c,p)),
                         No(p_hasta_arriba(c,p)), No(p_sobre(c,otro)), No(p_libre(k))]
    
#funcion que genera la lista de efecto de accion Pon con la asignacion valida
def getEfectsPon(obs):
    k = obs[0]
    p = obs[1]
    c = obs[2]
    otro = obs[3]
    return [p_en(c,p), p_hasta_arriba(c,p), p_sobre(c,otro),
                       No(p_hasta_arriba(otro, p)), No(p_sostiene(k,c)), p_libre(k)]
    
#Funcion que determina que asignacion de valores satisface las precondicones
# de la accion toma
def tomaIsApplicable(state):
    validAssigToToma = getValidAssig(state,generarPreToma())
    if len(validAssigToToma)==0:
        return []
    else:
        return validAssigToToma
            
#Funcion que determina que asignacion de valores satisface las precondicones
# de la accion pon
def ponIsApplicable(state):
    validAssigToPon = getValidAssig(state, generarPrePon())
    if len(validAssigToPon)==0:
        return []
    else:
        return validAssigToPon
    
#Funcion auxliar que imprime una lista de listas de predicados
def printComb(c):
    for i in c:
        printL(i)
        print("------------------")

#Funcion auxiliar para imprimir lista de pred
def printL(l):
    for i in l:
        print(i)
            
###############################
## Funcion de punto tres ######
###############################
# Determina si una accion es aplicable y retorna la asignacion valida
# accion es de clase accion
def applicableFunction(state, accion):
    if accion.nombre == "pon":
        validAssigToPon = ponIsApplicable(state)
        if len(validAssigToPon)==0:
            return False
        else:
            
            return validAssigToPon[random.randint(0, len(validAssigToPon)-1)]
    elif accion.nombre == "toma":
        validAssigToToma = tomaIsApplicable(state)
        if len(validAssigToToma)==0:
            return False
        else: 
            return validAssigToToma[random.randint(0, len(validAssigToToma)-1)]
    
###############################
### Funcion de punto cuatro ###
###############################
#Funcion que aplica una accion a un estado
#st Estado actual
#assig asignacion valida para aplicar accion
#action String que indica que accion se toma
def applyAction(st, assig, action):
    objects = []
    obEfects = []
    stateCopy = clona(st)
    if action.nombre == 'toma':
        objects = getObjectsToma(assig)
        obEfects = getEfectsToma(objects)
    elif action.nombre == 'pon':
        objects = getObjectsPon(assig)
        obEfects = getEfectsPon(objects)
        
    for i in assig:
        for j in stateCopy:
            if equalsPredicate(i,j):
                stateCopy.remove(j)
    for obE in obEfects:
        for p in stateCopy:
            if isinstance(obE,No) and isinstance(p,No):
                if obE.predicado == p.predicado:
                    stateCopy.remove(p)
            elif isinstance(obE,No) and isinstance(p,Predicado):
                if obE.predicado == p:
                    stateCopy.remove(p)
            elif isinstance(obE,Predicado) and isinstance(p,No):
                if obE == p.predicado:
                    stateCopy.remove(p)
        stateCopy.append(obE)
    return stateCopy
    
###############################
### Funcion de punto cinco ####
############################### 
#Funcion que determina si un estado satisface las condiciones indicadas en el campo meta
def satisfiesMeta(st, meta):
    if len(st)<len(meta):
        return False
    else:
        for pred in meta:
            if not(existPinS(st,pred)):
                return False
    return True
    
#Funcion de prueba
def test(problem, action):
    init = problem.estado
    print("############### Estado inicial ###############")
    printL(init)
    print("..........................................")
    applicableFunction(problem,accion=a_pon)
    print("..........................................")
    toma = tomaIsApplicable(problem)
    pon = ponIsApplicable(problem)
    newSt = []
    if len(toma) > 0 and action=="toma":
        print("Las precondicones que se aplicaron son")
        printL(toma[0])
        newSt = applyAction(init,toma[0],a_toma)
        print("..........................................")
    elif len(pon) > 0 and action=="pon":
        print("Las precondicones que se aplicaron son")
        printL(pon[0])
        newSt = applyAction(init,pon[0],a_pon)
    else:
        print("ERROR: No se puede aplicar la accion ingresada")
    return newSt