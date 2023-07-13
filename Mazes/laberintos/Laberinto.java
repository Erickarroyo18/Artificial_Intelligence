package laberintos;

import java.util.Stack;
import javax.swing.JOptionPane;
import processing.core.PApplet;

/**
 * Clase que crea un laberinto con Processing.
 * 
 * @author Sara
 * @author Baruch
 * 
 *         Comando para probar el programa
 *         mkdir classes
 *         javac -d ./classes -cp ".;lib/core.jar" .\laberintos\Laberinto.java
 *         java -cp ".;classes.;lib/core.jar" laberintos.Laberinto
 * 
 *         Para determinar las dimensiones del laberinto debe modificar
 *         directamente el valor de los atributos
 *         alto y ancho de la clase Laberinto.
 *         De esta manera podra generar distintos ejemplares de laberintos para
 *         sus dimensiones dadas
 */
public class Laberinto extends PApplet {
  int alto = 15; // Altura (en celdas) de la cuadricula.
  int ancho = 15; // Anchura (en celdas) de la cuadricula.
  int celda = 40; // Tamanio de cada celda cuadrada (en pixeles).
  ModeloLaberinto modelo; // El objeto que representa el modelo del laberinto.

  @Override
  public void setup() {
    frameRate(60);
    background(50);
    modelo = new ModeloLaberinto(ancho, alto, celda);
  }

  @Override
  public void settings() {
    size(ancho * celda, (alto * celda));
  }

  /**
   * Pintar el mundo del modelo.
   */
  @Override
  public void draw() {
    for (int i = 0; i < alto; i++) {
      for (int j = 0; j < ancho; j++) {
        fill(204, 204, 204);
        stroke(25, 25, 25);
        rect(j * modelo.tamanio, i * modelo.tamanio, modelo.tamanio, modelo.tamanio);

        if (!modelo.mundo[i][j].pared_1) {
          stroke(204, 204, 204);
          line(j * modelo.tamanio, i * modelo.tamanio, ((j + 1) * modelo.tamanio), i * modelo.tamanio);
        }
        if (!modelo.mundo[i][j].pared_2) {
          stroke(204, 204, 204);
          line((j * modelo.tamanio) + modelo.tamanio, i * modelo.tamanio, (j + 1) * modelo.tamanio,
              (((i + 1) * modelo.tamanio)));
        }
        if (!modelo.mundo[i][j].pared_3) {
          stroke(204, 204, 204);
          line(j * modelo.tamanio, (i * modelo.tamanio) + modelo.tamanio, ((j + 1) * modelo.tamanio),
              ((i + 1) * modelo.tamanio));
        }
        if (!modelo.mundo[i][j].pared_4) {
          stroke(204, 204, 204);
          line(j * modelo.tamanio, i * modelo.tamanio, j * modelo.tamanio, ((i + 1) * modelo.tamanio));
        }
      }
    }
  }

  /**
   * Clase que representa cada celda de la cuadricula.
   */
  class Celda {
    int celdaX;
    int celdaY;
    boolean pared_1;
    boolean pared_2;
    boolean pared_3;
    boolean pared_4;
    boolean estado;

    /**
     * Constructor de una celda.
     * 
     * @param celdaX Coordenada en x
     * @param celdaY Coordenada en y
     * @param estado Estado de la celda. true si no ha sido visitada, false en otro
     *               caso.
     */
    Celda(int celdaX, int celdaY, boolean estado) {
      this.celdaX = celdaX;
      this.celdaY = celdaY;
      this.estado = estado;
      this.pared_1 = true; // Booleano que representa la pared de arriba
      this.pared_2 = true; // Booleano que representa la pared de la derecha
      this.pared_3 = true; // Booleano que representa la pared de abajo
      this.pared_4 = true; // Booleano que representa la pared de la izquierda
    }

    @Override
    public String toString() {
      return "[" + celdaX + "," + celdaY + "]";
    }
  }

  /**
   * Clase que modela el laberinto, es decir, crea el mundo del laberinto.
   */
  class ModeloLaberinto {
    int ancho, alto; // Tamaño de celdas a lo largo y ancho de la cuadrícula.
    int tamanio; // Tamaño en pixeles de cada celda.
    Celda[][] mundo; // Mundo de celdas
    Celda inicial;

    /**
     * Constructor del modelo
     * 
     * @param ancho   Cantidad de celdas a lo ancho en la cuadricula.
     * @param alto    Cantidad de celdas a lo largo en la cuadricula.
     * @param tamanio Tamaño (en pixeles) de cada celda cuadrada que compone la
     *                cuadricula.
     */
    ModeloLaberinto(int ancho, int alto, int tamanio) {
      if (ancho < 2 || alto < 2) {
        JOptionPane.showMessageDialog(null, "Las dimensiones ingresadas no permiten generar un laberinto.");
        System.exit(0);
      } else {
        this.ancho = ancho;
        this.alto = alto;
        this.tamanio = tamanio;
        mundo = new Celda[alto][ancho];
        for (int i = 0; i < alto; i++) {
          for (int j = 0; j < ancho; j++) {
            mundo[i][j] = new Celda(i, j, true);
          }
        }
        this.inicial = this.mundo[random(0, alto - 1)][random(0, ancho - 1)];
        System.out.println("Celda inicial: " + inicial);
        Stack<Celda> stack = new Stack<>();
        this.backTrack(inicial, stack);
      }
    }

    /**
     * Metodo que simula el backTrack para resolver el problema
     * 
     * @param c  Celda
     * @param cs Stack
     */
    public void backTrack(Celda c, Stack<Celda> cs) {
      c.estado = false;
      visited(c);
      cs.add(c);
      while (!cs.isEmpty()) {
        Celda top = getCell(cs.pop());
        visited(top);
        if (this.notVisitedAdj(getCell(top))) {
          cs.add(top);
          Celda adj = this.getAdjCelda(top);
          int dir = this.getDir(top, adj);
          this.dropWall(top, adj, dir);
          cs.add(adj);
        }
      }
    }

    /**
     * Metodo que dadas dos celdas determina la pared que comparted
     * o el la direccion que une a la celda actual con la adyacente
     * 
     * @param act Celda
     * @param ad  Celda
     * @return int
     */
    public int getDir(Celda act, Celda ad) {
      int x1 = act.celdaX, y1 = act.celdaY, x2 = ad.celdaX, y2 = ad.celdaY;
      if (x1 == x2 && y1 < y2) {// Misma fila hacia enfrente
        return 2;
      } else if (x1 == x2 && y2 < y1) {// Misma fila hacia atras
        return 4;
      } else if (y1 == y2 && x1 < x2) {
        return 3;
      } else if (y1 == y2 && x2 < x1) {
        return 1;
      }
      return -1;
    }

    /**
     * Metodo que retorna la casilla del mundo correspondiente a las coordenadas de
     * la celda dada
     * 
     * @param c Celda
     * @return Celda
     */
    public Celda getCell(Celda c) {
      return this.mundo[c.celdaX][c.celdaY];
    }

    /**
     * Metodo que se encarga de eliminar la pared entre dos celdas, dada la
     * direccion act->ad
     * 
     * @param act Celda
     * @param ad  Celda
     * @param dir int
     */
    public void dropWall(Celda act, Celda ad, int dir) {
      int x1 = act.celdaX, y1 = act.celdaY, x2 = ad.celdaX, y2 = ad.celdaY;
      switch (dir) {
        case 1:
          this.mundo[x1][y1].pared_1 = false;
          this.mundo[x2][y2].pared_3 = false;
          break;
        case 2:
          this.mundo[x1][y1].pared_2 = false;
          this.mundo[x2][y2].pared_4 = false;
          break;
        case 3:
          this.mundo[x1][y1].pared_3 = false;
          this.mundo[x2][y2].pared_1 = false;
          break;
        case 4:
          this.mundo[x1][y1].pared_4 = false;
          this.mundo[x2][y2].pared_2 = false;
          break;
      }
    }

    /**
     * Metodo que marca como visitada una celda del mundo
     * 
     * @param c Celda
     */
    public void visited(Celda c) {
      this.mundo[c.celdaX][c.celdaY].estado = false;
    }

    /**
     * Metodo qued determina si las coordenadas de una celda son validas,
     * i.e., no sales de los limites
     * 
     * @param x int
     * @param y int
     * @return boolean
     */
    public boolean validCoordinates(int x, int y) {
      return (x < 0 || x > (alto - 1) || y < 0 || y > ancho - 1) ? false : true;
    }

    /**
     * Metodo que determina si la celda dada tiene celdas adyacentes que NO hayan
     * sido visitadas
     * 
     * @param act Celda
     * @return boolean
     */
    public boolean notVisitedAdj(Celda act) {
      int x = act.celdaX, y = act.celdaY;
      if (x == 0 && y == 0) {
        return this.mundo[x + 1][y].estado || this.mundo[x][y + 1].estado;
      } else if (x == alto - 1 && y == ancho - 1) {
        return this.mundo[x - 1][y].estado || this.mundo[x][y - 1].estado;
      } else if (x == 0 && y == ancho - 1) {
        return this.mundo[x + 1][y].estado || this.mundo[x][y - 1].estado;
      } else if (x == alto - 1 && y == 0) {
        return this.mundo[x - 1][y].estado || this.mundo[x][y + 1].estado;
      } else if (x == 0) {
        return this.mundo[x][y - 1].estado || this.mundo[x][y + 1].estado || this.mundo[x + 1][y].estado;
      } else if (x == alto - 1) {
        return this.mundo[x][y - 1].estado || this.mundo[x][y + 1].estado || this.mundo[x - 1][y].estado;
      } else if (y == 0) {
        return this.mundo[x - 1][y].estado || this.mundo[x + 1][y].estado || this.mundo[x][y + 1].estado;
      } else if (y == ancho - 1) {
        return this.mundo[x - 1][y].estado || this.mundo[x + 1][y].estado || this.mundo[x][y - 1].estado;
      } else {
        return this.mundo[x][y + 1].estado || this.mundo[x][y - 1].estado || this.mundo[x + 1][y].estado
            || this.mundo[x - 1][y].estado;
      }
    }

    /**
     * Metodo que retorna, una vez verificado que la celda dada tiene adyacentes NO
     * visitados, una celda aleatoria adyacente
     * 
     * @param act Celda
     * @return Celda
     */
    public Celda getAdjCelda(Celda act) {
      int x = act.celdaX, y = act.celdaY, dir = random(1, 4);
      switch (dir) {
        case 1:
          x = x - 1;
          break;
        case 2:
          y = y + 1;
          break;
        case 3:
          x = x + 1;
          break;
        case 4:
          y = y - 1;
          break;
      }
      if (validCoordinates(x, y)) {
        if (getCell(new Celda(x, y, false)).estado) {
          return getCell(new Celda(x, y, true));
        } else {
          return getAdjCelda(act);
        }
      } else {
        return getAdjCelda(act);
      }
    }

    /**
     * Metodo que genera un numero random que representa la dirección elegida por la
     * cela actual
     * 
     * @return int
     */
    public int random(int min, int max) {
      return (int) (Math.random() * (max + 1 - min)) + min;
    }
  }// Llave de Modelo

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    PApplet.main(new String[] { "laberintos.Laberinto" });
  }
}