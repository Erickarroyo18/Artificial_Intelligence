from strips import NodoBusqueda,test, printL, satisfiesMeta, clona, pred_goal, pred_init
import strips 

#print("###### DOMINIO ######")
#print(strips.dominio)
#print("###### PROBLEMA ######")
problema = strips.problema
#print(problema)

###############################
#######     PRUEBAS     #######
###############################
#st1 = test(problema,"toma") 
#problema.estado = st1
#st2 = test(problema,"pon")
#problema.estado=st2
#st3 = test(problema,"toma")
#print("Estado final obtenido")
#printL(st3)
#print("Satisface el estado meta? :",satisfiesMeta(clona(pred_goal), pred_goal))
#print("Satisface el estado meta? :",satisfiesMeta(clona(pred_init), pred_goal))

###########################
##    Estado Inicial     ##
###########################
'''
    ______             ______
    |    | k1        k2|    |
    |                       |    
    |                       |
    |   |cc|      |cf|      |
    |   |cb|      |ce|      |  
    |___|ca|______|cd|______|
    |        pallet         |
    |-----------------------|
    |    p1   p2    q1   q2 |
    -------------------------
'''

print("\n \t Búsqueda en amplitud.\n")
print("ESTADO INICIAL")
for p in problema.estado:
    print(p)
print("\nMETA")
print(*problema.meta)

valid = input("\nIngresa 'T' si deseas ver el proceso de búsqueda. En otro caso, presiona 'Enter' para continuar  ")
it = input("Ingresa un numero (>0) si deseas definir un número maximo de acciones. En otro caso, presiona 'Enter' para continuar  ")
printt = True if valid == "T" else False
it = int(it) if it!="" else 200

resultado = problema.busqueda_amplitud(printt,it)

if (isinstance(resultado,NodoBusqueda)):
    print("\n\tSe llego al estado meta")
    print(resultado.actual)
    input("\nPresiona 'Enter' para ver cómo llegar del estado inicial a este estado. ")
    resultado.obtener_ruta()
else:
    print("\n\t== No fue posible llegar al estado meta ==")