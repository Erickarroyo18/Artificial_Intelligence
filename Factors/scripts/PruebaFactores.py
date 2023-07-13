#!/usr/bin/python3
# -*- coding: utf-8 -*-
from Factores import *
from Tests import *

LF = Variable("LF",[0,1])
LI = Variable("LI",[0,1])
FIN = Variable("FIN",[0,1])
IT = Variable("IT",[0,1])
JA = Variable("JA",[0,1])
AA = Variable("AA",[0,1])
VP = Variable("VP",[0,1])
MA = Variable("MA",[0,1])
MP = Variable("MP",[0,1])
BA = Variable("BA",[0,1])

## Marginales
pLF = Factor((LF,),[0.78,0.22])
pLI = Factor((LI,),[0.78,0.22])
pFIN = Factor((FIN,),[0.78,0.22])

## Condicionales
pJA_LI = Factor((JA,LI),[0.1, 0.6, 0.9, 0.4])
pAA_FIN = Factor((AA,FIN),[0.6,0.2,0.4,0.8])
pMP_MA = Factor((MP,MA),[0.2,0.7,0.8,0.3])

pMA_JAAA = Factor((MA,JA,AA),[0.5,0.15,0.05,0.95,0.5,0.85,0.95,0.05])
pVP_AABA = Factor((VP,AA,BA),[0.3,0.6,0.1,0,0.7,0.4,0.9,1])
'''
    Por motivos de tiempo, no se pudieron corregir los errores en esta prueba
    por lo que puede probar con el archivo "Tests.py". O tratar de solucinar 
    los errores de tipos
'''
def pLuviaInv_NoMayNoFin():
    print("P(li|¬mp,¬fin)")
    Prob = pMA_JAAA.multiply(pMP_MA.reduce(MP,0)).marginalize(MA)
    print(Prob, type(Prob))
    #Prob = Prob.multiply(pAA_FIN.reduce(FIN,1))#.marginalize(AA)
    #print(Prob)
    #Prob = Prob.multiply(pJA_LI).marginalize(JA).multiply(pFIN.reduce(FIN,0)).multiply(pLI)
    #print(Prob)
    #Prob = Prob.normalize()
    #print(Prob)
    #Prob = Prob.reduce(LI,1)
    #print("Resultado = ", Prob)
    
if __name__ == '__main__':
    pLuviaInv_NoMayNoFin()
    # Puede modificar los tests para imprimir más informacion
    #test1()
    #test2()
    #test3
    #test4()