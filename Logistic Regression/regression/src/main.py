import numpy as np
import matplotlib.pyplot as plt
import random

class LogisticRegression:
  '''
  Funcion equivalente a la funcion logistica
  X es una entrada de la forma [x0,x1,x2]
  '''
  def sigmoide(self,X):
    nX = np.array(X)
    nW = np.array(self.w)
    z = np.sum(nX*nW)+self.theta
    sigmoi = np.frompyfunc(lambda z: 1/(1+np.exp(-z)),1,1)
    return sigmoi(z)

  '''
  Metodo constructor
  X es el conjunto de entrenamiento de la forma [[x1_1,x2_1,], [x1_2,x2_2],..., [x1_n,x2_n]]
  '''
  def __init__(self, X, T=100, alpha=0.1, theta=random.uniform(-0.5,0.5)):
    self.T = T
    self.alpha = alpha
    self.theta = theta
    self.X = np.hstack((np.ones((len(X), 1)), X))#Se añade el sesgo a todas las entradas
    self.w = np.random.uniform(low=-0.5, high=0.5, size=self.X.shape[1]) 
  
  '''
  Funcion que dado un arreglo unidimensional (X) retorna el valor de evaluar la funcion sigmoide,
  i.e., la probabilidad de que esa entrada pertenezca a la clase 1
  '''
  def predict_proba(self,X):
    return self.sigmoide(X)

  '''
  Funcion que determina a que clase pertenece la entrada X
  Se basa en la Clasificación con regresión logística.
  X es un arrego unidimensional
  '''
  def predic_input(self,X):
    return 1 if (self.sigmoide(X)>=0.5) else 0
    
  '''
  Funcion que clasifica el conjunto de entradas dadas
  x es un arreglo bidimensional (nx2)
  '''
  def predict(self, X):
    X = np.hstack((np.ones((len(X), 1)), X))
    for x in X:
      print(x[1:len(x)]," salida: ", self.predic_input(x))
  
  '''
  Funcion de entrebamiento (basada en el algoritmo 3)
  X es una matriz nx3, de la forma [[bias,x1,x2]...[]]
  Y es un arreglo unidimensional 
  '''
  def fit(self,X, Y, T, alpha):
    w = self.w
    for i in range(0,T):
      for x, y in zip(X,Y):#x es una lista [x0,x1,x2]
        for e in range(0,len(w)):
          f = self.sigmoide(x);
          w[e] = w[e]-(alpha*(f-y)*x[e])
          self.theta = self.theta - (alpha*(f-y))
    return w
  
##########################################
##          FUNCIONES GLOBALES          ##
##########################################
'''
Funcion para probar la implementacion
XS es el conjunto de entradas a probar
'''
def test(XS):
  sets = getSets(150,50)
  X = sets[0]
  Y = sets[3]
  T = 100
  alpha = 0.1
  theta = 0.2
  l = LogisticRegression(X,T,alpha,theta)
  X=l.X
  print("Pesos iniciales: ", l.w, ", theta= ", l.theta)
  l.predict(XS)
  print("Inicia el entrenamiento [alpha = ",alpha,"]")
  w = l.fit(X,Y,T,alpha)
  print("Pesos finales: ", l.w, ", theta= ", l.theta)
  l.predict(XS)
  draw_plot(sets[1],sets[2])

'''
Esta funcion genera el conjunto de datos para la clase cero
Nota: Solo genera elementos en {[0,0],[0,1],[1,0]}
'''
def generate_dataSetClass0(n):
    i = 0
    xs = []
    while i<n:
      x = 1 if (random.randint(0, 1) > 0.5) else 0
      y = 1 if (random.randint(0, 1) > 0.5) else 0
      if x==1 and y==1:
        i= i-1
      else:
        xs.append([x,y])
      i = i+1
    return xs
'''
Funcion que genera el conjunto de datos para la clase uno
'''
def generate_dataSetClass1(n):
  xs = np.ones(n)
  ys = np.ones(n)
  data = []
  for x, y in zip(xs, ys):
    data.append([x,y])
  return data

'''
Funcion que dado un set de datos les aplica un ruido a todos sus datos
'''
def apply_noise(st):
  noiseX = list(np.random.normal(0, 0.1, len(st)))
  noiseY = list(np.random.normal(0, 0.1, len(st)))
  for e,nX,nY in zip(st,noiseX,noiseY):
    e[0] = e[0] -nX
    e[1] = e[1] -nY
  return st

'''
Funcion que los conjuntos de datos utilizado
set0+set1 es el conjunto total de datos de entrenamiento
set0 es el conjunto de datos de la clase cero con ruido aplicado
set1 es el conjunto de datos de la clase uno con ruido aplicado
ceros+unos es el conjunto de salidas correspondientes a los datos de entrenamiento (set0+set1)
m y n pueden ser moficados sin problema
'''
def getSets(n,m):
  set0 = apply_noise(generate_dataSetClass0(n))
  set1 = apply_noise(generate_dataSetClass1(m))
  ceros = list(np.zeros(n))
  unos = list(np.ones(m))
  return set0 + set1, set0, set1, ceros+unos
'''
Funcion que dados los conjuntos de la clase cero y uno los grafica
'''
def draw_plot(dsC0, dsC1):
  xs0 = []
  ys0 = []
  for s in dsC0:
    xs0.append(s[0])
    ys0.append(s[1])
  xs1 = []
  ys1 = []
  for s in dsC1:
    xs1.append(s[0])
    ys1.append(s[1])
  fig, ax = plt.subplots()
  ax.scatter(xs0, ys0)
  ax.scatter(xs1, ys1)
  x=np.linspace(0.5, 2, num=10)
  y=np.linspace(2,0,num=10)
  plt.plot((0, 1.5), (1.5, 0))
  plt.show()

##############################
##          Prueba          ##
##############################
testSet = [[0,0],[0,1],[1,0],[1,1]]
test(testSet)