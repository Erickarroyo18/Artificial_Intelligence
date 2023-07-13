from CadenaDeMarkov import *

'''
Ejemplos obtenidos de:
url = https://en.wikipedia.org/wiki/Examples_of_Markov_chains
- Stock Market
- Steady state of the weather
'''
def prueba1(n=10):
    print("Ejemplo: Stock Market")
    e = ["bull","bear","stagnant"]
    v = np.array([[0.33, 0.33, 0.34]])
    m = np.array([[0.9, 0.075, 0.025],
                  [0.15, 0.8, 0.05],
                  [0.25, 0.25, 0.5]])
    c = CadenaDeMarkov(e, v, m)
    print(c)
    sec = c.generar_secuencia_estados(n)
    print(f"Secuencia aleatoria de {n} estados:\n", sec)
    prob_sec = c.obtener_probabilidad_cadena(sec)
    print("Probabilidad de esta secuencia:\n", prob_sec)
    d_l = c.distribucion_limite()
    print("Distribucion limite:\n", d_l)

def prueba2(n=10):
    print("Ejemplo: Steady state of the weather")
    e = ["sunny","rainy"]
    v = np.array([[0.5, 0.5]])
    m = np.array([[0.9, 0.1],
                  [0.5, 0.5]])
    c = CadenaDeMarkov(e, v, m)
    print(c)
    sec = c.generar_secuencia_estados(n)
    print(f"Secuencia aleatoria de {n} estados:\n", sec)
    proba_sec = c.obtener_probabilidad_cadena(sec)
    print("Probabilidad de esta secuencia:\n", proba_sec)
    d_l = c.distribucion_limite()
    print("Distribucion limite:\n", d_l)

 

if __name__ == '__main__':
    prueba1()
    #prueba2()