from Factores import *
'''
    Prueba de reduccion para factor de imagen 7.10 del archivo de especificaciones
    variables es el conjunto de variables que se desea usar
    valor es el valor que se usa del conjunto de valores para 'variables'
'''
def test1(variables='Letras', valor='a'):
    # Variables
    letras = Variable("Letras",['a','b'])
    utiles = Variable("Utiles",['cuaderno','lapiz','goma'])
    numeros = Variable("Numeros",['2','8'])
    print("Factor original")
    f = Factor((letras,utiles,numeros),[1,2,3,4]*3)
    print(f)
    if variables == 'Letras':
        f = f.reduce(letras,valor)
    elif variables == 'Utiles':
        f = f.reduce(utiles,valor)
    elif variables == 'Numeros':
        f = f.reduce(numeros,valor)
    print("Factor Reducido a [%s] de variables [%s]" % (valor, variables))
    print(f)

'''
    Prueba de las cuatro funciones para factores de imagen 7.11 del archivo de especificaciones
    Nota: Se sigue la secuencia que muestra la imagen
'''
def test2():
    # Variables
    A = Variable("A", [1,2])
    B = Variable("B", [5,10,15])
    C = Variable("C", [1,2,3])
    # Creacion de factores f1 y f2
    f1 = Factor((A,), [1,2])
    print("Factor 1\n", f1)
    f2 = Factor((B,C), [5,10,15,10,20,30,15,30,45])
    print("Factor 2\n", f2)
    # Multiplicando f1 y f2 para obtener f3
    f3 = f1.multiply(f2)
    print("Multiplicando f1 y f2, se muestra f3\n", f3)
    # Normalizamos f3
    f3_n = f3.normalize()
    print("Normalizando el resultado de la multiplicacion (f3)\n", f3_n)
    # Reduciendo f3, reduciendo con variable A y valor 1
    f3_r = f3_n.reduce(A,1)
    print("Reduciendo a f3 por la variable A y el valor 1\n", f3_r)
    # Marginalizando f3, marginalizando la variable B
    f3_m = f3_n.marginalize(B)
    print("Marginalizando a f3 por la variable B\n", f3_m)

'''
    Prueba de las cuatro funciones para factores de imagen 7.1 del archivo de especificaciones
    Nota: Se sigue la secuencia de funciones que se muestran en las imagenes [7.1, 7.3 ... 7.9]
'''
def test3():
    # Variebles
    A = Variable("A", [0,1])
    B = Variable("B", [0,1])
    C = Variable("C", [0,1])
    # Factores
    f_A = Factor((A,), [.3,.7])
    f_B = Factor((B,), [.6,.4])
    f_C = Factor((C,), [.2,.8])
    
    # Imagen 7.1 factores originales
    #print("Factor A Original\n", f_A)
    #print("Factor B Original\n", f_B)
    #print("Factor C Original\n", f_C)
    
    # Imagen 7.3 (Multiplicacion) [f_AB = f_A*f_B, f_AC= f_A*f_C]
    f_AB = f_A.multiply(f_B)
    #print("Factor AB (Mult)\n", f_AB)
    f_AC = f_A.multiply(f_C)
    #print("Factor AC (Mult)\n", f_AC)

    # Imagen 7.4 (Multiplicacion) [f_ABC = f_AB*f_AC]
    f_ABC = f_AB.multiply(f_AC)
    #print("Factor ABC (Mult)\n", f_ABC)

    # Imagen 7.6 (Reduccion) [f_AB con variable A y valor 0]
    f_AB_r= f_AB.reduce(A, 0)
    #print("Factor AC_r (Reduccion)\n", f_AB_r)

    # Imagen 7.7 (Normalizacion)
    f_AB_rn = f_AB_r.normalize()
    #print("Factor AB_rn (Reducido y Normalizado)\n", f_AB_rn)

    # Imagen 7.9 (Marginalizacion) de factor f_AB por B
    f_AB_m = f_AB.marginalize(B)
    #print("Factor AB_m (Marginalizado)\n", f_AB_m)
'''
Funcion que abre y lee el archivo especificado (scripts/archivo.txt)
retorna una lista con las lineas del archivo
'''
def read(file):
    with open(file, 'r') as archivo:
        contenido = archivo.read()
    return contenido.splitlines()

'''
Funcion que construye una Variable dada una linea del archivo
Nota: Formato requerico Variable = nombre : [x1,x2]
'''
def readVar(linea):
    datos = linea.split(":")
    nombre = datos[0]
    nombre = nombre.strip()
    datos[1] = datos[1].replace("[", "")
    datos[1] = datos[1].replace("]", "")
    valores_posibles = datos[1].split(',')
    valores_posibles = list(map(lambda x: float(x), valores_posibles))
    return Variable(nombre, valores_posibles)
'''
Funcion que construye una Factor dada una linea del archivo
Nota: Formato requerico Factor = [x1,x2,.., xn] : [y1,y2,..,yn]
'''
def readFact(linea, vars):
    linea = linea.replace("[","")
    linea = linea.replace("]","")
    datos = linea.split(":")
    variables = datos[0].split(",")
    valores = datos[1].split(",")
    valores = list(map(lambda x: float(x), valores))
    varObj = []
    for v in variables:
        v = v.strip()
        var = getVar(v,vars)
        if var == None:
            return "ERROR: La variable %s no ha sido definida." % v
        else:
            varObj.append(var)
    tupla = tuple(varObj)
    return Factor(tupla,valores)
'''
Funcion que dado un nombre de variable una lista de variables, retorna el 
objeto correspondiente a 'n'
'''
def getVar(n, vars):
    for v in vars:
        if n == v.nombre:
            return v 
    return None   
'''
Funcion principal para leer el archivo y contruir a los objetos
'''
def create(file):
    lineas = read(file)
    variables, factores = [], []
    for linea in lineas:
        valida = True if (linea.find("=") != -1) else False
        if valida:
            tipo = linea.strip().split("=")[0]
            linea = linea.strip().split("=")[1]
            tipo = tipo.strip()
            if tipo=="Variable" :
                variables.append(readVar(linea))
            elif tipo=="Factor":
                factores.append(readFact(linea,variables))
    return variables, factores
'''
El archivo "archivo.txt" ya contiene algunos ejemplos similares a los del archivo "PruebaFactores.py"
'''
def test4():
    variables, factores = create("archivo.txt")
    for v in variables:
        print(v)
    for f in factores:
        print(f)