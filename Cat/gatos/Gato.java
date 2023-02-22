/*
 * No redistribuir.
 */
package gatos;

import java.util.LinkedList;

/**
 * Clase para representar un estado del juego del gato. Cada estado sabe cómo
 * generar a sus sucesores.
 *
 * @author Vero
 */
public class Gato {
    public static final int MARCA1 = 1; // Número usado en el tablero del gato para marcar al primer jugador.
    public static final int MARCA2 = 4; // Se usan int en lugar de short porque coincide con el tamaÃ±o de la palabra,
                                        // el código se ejecuta ligeramente más rápido.
    int[][] tablero = new int[3][3]; // Tablero del juego
    Gato padre; // Quién generó este estado.
    LinkedList<Gato> sucesores; // Posibles jugadas desde este estado.
    boolean jugador1 = false; // Jugador que tiró en este tablero.
    boolean hayGanador = false; // Indica si la última tirada produjo un ganador.
    int tiradas = 0; // Número de casillas ocupadas.

    /**
     * Constructor del estado inicial.
     */
    Gato() {
    }

    /**
     * Constructor que copia el tablero de otro gato y el número de tiradas
     */
    Gato(Gato g) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.tablero[i][j] = g.tablero[i][j];
            }
        }
        this.jugador1 = g.jugador1;
        this.tiradas = g.tiradas;
    }

    /**
     * Indica si este estado tiene sucesores expandidos.
     */
    int getNumHijos() {
        if (sucesores != null) {
            return sucesores.size();
        } else {
            return 0;
        }
    }

    /*
     * Función auxiliar.
     * Dada la última posición en la que se tiró y la marca del jugador
     * calcula si esta jugada produjo un ganador y actualiza el atributo
     * correspondiente.
     * 
     * Esta función debe ser lo más eficiente posible para que la generación del
     * arbol no sea demasiado lenta.
     */
    private void hayGanador(int ren, int col, int marca) {
        // Horizontal
        if (tablero[ren][(col + 1) % 3] == marca && tablero[ren][(col + 2) % 3] == marca) {
            hayGanador = true;
            return;
        }
        // Vertical
        if (tablero[(ren + 1) % 3][col] == marca && tablero[(ren + 2) % 3][col] == marca) {
            hayGanador = true;
            return;
        }
        // Diagonal
        if ((ren != 1 && col == 1) || (ren == 1 && col != 1)) {
            hayGanador = false; // No debiera ser necesaria.
            return; // No pueden hacer diagonal
        } // Centro y esquinas
        if (col == 1 && ren == 1) {
            // Diagonal \
            if (tablero[0][0] == marca && tablero[2][2] == marca) {
                hayGanador = true;
                return;
            }
            if (tablero[2][0] == marca && tablero[0][2] == marca) {
                hayGanador = true;
                return;
            }
        } else if (ren == col) {
            // Diagonal \
            if (tablero[(ren + 1) % 3][(col + 1) % 3] == marca && tablero[(ren + 2) % 3][(col + 2) % 3] == marca) {
                hayGanador = true;
                return;
            }
        } else {
            // Diagonal /
            if (tablero[(ren + 2) % 3][(col + 1) % 3] == marca && tablero[(ren + 1) % 3][(col + 2) % 3] == marca) {
                hayGanador = true;
                return;
            }
        }
    }

    /*
     * Función auxiliar.
     * Coloca la marca del jugador en turno para este estado en las coordenadas
     * indicadas.
     * Asume que la casilla está libre.
     * Coloca la marca correspondiente, verifica y asigna la variable si hay un
     * ganador.
     */
    private void tiraEn(int ren, int col) {
        tiradas++;
        int marca = (jugador1) ? MARCA1 : MARCA2;
        tablero[ren][col] = marca;
        hayGanador(ren, col, marca);
    }

    /**
     * ------- *** ------- *** -------
     * Este es el método que se deja como práctica.
     * ------- *** ------- *** -------
     * Crea la lista sucesores y
     * agrega a todos los estados que sujen de tiradas válidas. Se consideran
     * tiradas válidas a aquellas en una casilla libre. Además, se optimiza el
     * proceso no agregando estados con jugadas simetricas. Los estados nuevos
     * tendrán una tirada más y el jugador en turno será el jugador
     * contrario.
     */
    LinkedList<Gato> generaSucesores() {
        if (hayGanador || tiradas == 9) {
            return null; // Es un estado meta.
        }
        this.sucesores = filtrarS(sucesores(this));
        return sucesores;
    }

    /**
     * Metodo auxiliar que filtra los estados sucesores de estados simetricos
     * 
     * @param list LinkedList
     * @return LinkedList
     */
    public LinkedList<Gato> filtrarS(LinkedList<Gato> list) {
        LinkedList<Gato> filterList = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            Gato aux = list.get(i);
            if (filterList.isEmpty())
                filterList.add(aux);
            if(!exist(filterList, aux))
                filterList.add(aux);
        }
        return filterList;
    }

    /**
     * Metodo que genera todos los estados sucesores posibles al dado
     * 
     * @param g Gato
     * @return LinkedList
     */
    public LinkedList<Gato> sucesores(Gato g) {
        LinkedList<Gato> list = new LinkedList<>();
        Gato temp = null;
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                temp = new Gato(g);
                if (temp.tablero[i][j] == 0) {
                    temp.tiraEn(i, j);
                    temp.jugador1 = !g.jugador1;
                    list.add(temp);
                } else {
                    continue;
                }

            }
        }
        return list;
    }

    /**
     * Metodo que verifica si existe un estado sucesor simetrico al dado
     * 
     * @param list LinkedList
     * @param b    Gato
     * @return True si existe un estado simetrico al parametrizado
     */
    public boolean exist(LinkedList<Gato> list, Gato b) {
        if (list == null || list.isEmpty())
            return false;
        for (Gato i : list) {
            if (i.equals(b))
                return true;
        }
        return false;
    }

    // ------- *** ------- *** -------
    // Serie de funciones que revisan la equivalencia de estados considerando las
    // simetrías de un cuadrado.
    // ------- *** ------- *** -------
    // http://en.wikipedia.org/wiki/Examples_of_groups#The_symmetry_group_of_a_square_-_dihedral_group_of_order_8
    // ba es reflexion sobre / y ba3 reflexion sobre \.

    /**
     * Revisa si ambos gatos son exactamente el mismo.
     */
    boolean esIgual(Gato otro) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[i][j] != otro.tablero[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Metodo que intercambia temporalmente los elementos del tablero
     * 
     * @param f1 fila 1
     * @param c1 columna 1
     * @param f2 fila 2
     * @param c2 columna 2
     */
    public void swap(int f1, int c1, int f2, int c2) {
        int aux = this.tablero[f1][c1];
        this.tablero[f1][c1] = this.tablero[f2][c2];
        this.tablero[f2][c2] = aux;
    }

    /**
     * Al reflejar el gato sobre la diagonal \ son iguales (ie traspuesta)
     */
    boolean esSimetricoDiagonalInvertida(Gato otro) {
        Gato temp = new Gato(otro);
        temp.swap(1, 0, 0, 1);
        temp.swap(2, 0, 0, 2);
        temp.swap(1, 2, 2, 1);
        return this.esIgual(temp);
    }

    /**
     * Al reflejar el gato sobre la diagonal / son iguales (ie traspuesta)
     */
    boolean esSimetricoDiagonal(Gato otro) {
        Gato temp = new Gato(otro);
        temp.swap(0, 0, 2, 2);
        temp.swap(1, 0, 2, 1);
        temp.swap(0, 1, 1, 2);
        return this.esIgual(temp);
    }

    /**
     * Al reflejar el otro gato sobre la vertical son iguales
     */
    boolean esSimetricoVerticalmente(Gato otro) {
        Gato temp = new Gato(otro);
        temp.swap(0, 0, 0, 2);
        temp.swap(1, 0, 1, 2);
        temp.swap(2, 0, 2, 2);
        return this.esIgual(temp);
    }

    /**
     * Al reflejar el otro gato sobre la horizontal son iguales
     */
    boolean esSimetricoHorizontalmente(Gato otro) {
        Gato temp = new Gato(otro);
        temp.swap(0, 0, 2, 0);
        temp.swap(0, 1, 2, 1);
        temp.swap(0, 2, 2, 2);
        return this.esIgual(temp);
    }

    /**
     * Rota el otro tablero 90Â° en la dirección de las manecillas del reloj.
     */
    boolean esSimetrico90(Gato otro) {
        return this.esIgual(this.rotar90(otro));
    }

    /**
     * Metodo que rota los elementos de la matriz 90 grados hacia la derecha
     * 
     * @param g Gato
     * @return Gato
     */
    public Gato rotar90(Gato g) {
        Gato temp = new Gato();
        temp.tablero[0][0] = g.tablero[2][0];
        temp.tablero[0][1] = g.tablero[1][0];
        temp.tablero[0][2] = g.tablero[0][0];
        temp.tablero[1][0] = g.tablero[2][1];
        temp.tablero[1][1] = g.tablero[1][1];
        temp.tablero[1][2] = g.tablero[0][1];
        temp.tablero[2][0] = g.tablero[2][2];
        temp.tablero[2][1] = g.tablero[1][2];
        temp.tablero[2][2] = g.tablero[0][2];
        return temp;
    }

    /**
     * Metodo que rota los elementos de la matriz 180 grados hacia la derecha
     * 
     * @param g Gato
     * @return Gato
     */
    public Gato rotar180(Gato g) {
        Gato temp = new Gato();
        temp.tablero[0][0] = g.tablero[2][2];
        temp.tablero[0][1] = g.tablero[2][1];
        temp.tablero[0][2] = g.tablero[2][0];
        temp.tablero[1][0] = g.tablero[1][2];
        temp.tablero[1][1] = g.tablero[1][1];
        temp.tablero[1][2] = g.tablero[1][0];
        temp.tablero[2][0] = g.tablero[0][2];
        temp.tablero[2][1] = g.tablero[0][1];
        temp.tablero[2][2] = g.tablero[0][0];
        return temp;
    }

    /**
     * Metodo que rota los elementos de la matriz 270 grados hacia la derecha
     * 
     * @param g Gato
     * @return Gato
     */
    public Gato rotar270(Gato g) {
        Gato temp = new Gato();
        temp.tablero[0][0] = g.tablero[0][2];
        temp.tablero[0][1] = g.tablero[1][2];
        temp.tablero[0][2] = g.tablero[2][2];
        temp.tablero[1][0] = g.tablero[0][1];
        temp.tablero[1][1] = g.tablero[1][1];
        temp.tablero[1][2] = g.tablero[2][1];
        temp.tablero[2][0] = g.tablero[0][0];
        temp.tablero[2][1] = g.tablero[1][0];
        temp.tablero[2][2] = g.tablero[2][0];
        return temp;
    }

    /**
     * Rota el otro tablero 180Â° en la dirección de las manecillas del reloj.
     */
    boolean esSimetrico180(Gato otro) {
        return this.esIgual(this.rotar180(otro));
    }

    /**
     * Rota el otro tablero 270Â° en la dirección de las manecillas del reloj.
     */
    boolean esSimetrico270(Gato otro) {
        return this.esIgual(this.rotar270(otro));
    }

    /**
     * Indica si dos estados del juego del gato son iguales, considerando
     * simetrías, de este modo el problema se vuelve manejable.
     */
    @Override
    public boolean equals(Object o) {
        Gato otro = (Gato) o;
        if (esIgual(otro)) {
            return true;
        }
        if (esSimetricoDiagonalInvertida(otro)) {
            return true;
        }
        if (esSimetricoDiagonal(otro)) {
            return true;
        }
        if (esSimetricoVerticalmente(otro)) {
            return true;
        }
        if (esSimetricoHorizontalmente(otro)) {
            return true;
        }
        if (esSimetrico90(otro)) {
            return true;
        }
        if (esSimetrico180(otro)) {
            return true;
        }
        if (esSimetrico270(otro)) {
            return true; // No redujo el diámetro máximo al agregarlo
        }
        return false;
    }

    /**
     * Devuelve una representación con caracteres de este estado. Se puede usar
     * como auxiliar al probar segmentos del código.
     */
    @Override
    public String toString() {
        String gs = "";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                char simbolo = ' ';
                if (this.tablero[i][j] == MARCA1) {
                    simbolo = 'o';
                } else if (this.tablero[i][j] == MARCA2) {
                    simbolo = 'x';
                } else {
                    simbolo = '-';
                }
                gs += simbolo + " ";
            }
            gs += '\n';
        }
        return gs;
    }
}