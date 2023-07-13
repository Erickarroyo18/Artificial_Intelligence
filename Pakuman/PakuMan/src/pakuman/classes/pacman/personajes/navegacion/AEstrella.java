/*
 * Código utilizado para el curso de Inteligencia Artificial.
 * Se permite consultarlo para fines didácticos en forma personal,
 * pero no esta permitido transferirlo resuelto a estudiantes actuales o potenciales.
 */
 /*
NOTA:
Se realizó el cambio de un atributo, la listaCerrada paso de ser una HashMap a un ArrayList
de este modo se facilito la consttuccion del trayecto, pues si recordamos la tabla hash almacenaba
<Estado, Estado> y por la implementación sabemos que el estado no nos permite construir el trayecto.
Sin embargo, el tipo NodoBusqueda con sus atributos <padre> y <accionPadre> si nos lo permite. 
Por lo tanto, opte por este cambio, además considero que no afecta en nada al resto de la implementacion
pues el programa, en genera, funciona correctamente. Además el tipo NodoBusqueda nos permite almacenar 
un Estado.
 */
package pacman.personajes.navegacion;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.LinkedList;
import java.util.PriorityQueue;
import javafx.scene.paint.Color;
import pacman.personajes.Movimiento;

/**
 * Clase donde se define en algoritmo de A* para que se use en el fantasma.
 *
 * @author baruch
 * @author blackzafiro
 */
public class AEstrella extends Algoritmo {

    private final static Logger LOGGER = Logger.getLogger("pacman.personajes.navegacion.AEstrella");

    static {
        LOGGER.setLevel(Level.FINE);
    }

    private PriorityQueue<NodoBusqueda> listaAbierta;   // Cola de prioridad de donde obtendremos los nodos
    // sobre los que se realizará el algoritmo.
    private ArrayList<NodoBusqueda> listaCerrada;       // Se modifico este atributo por conveniencias de la implementación
    private Estado estadoFinal;                         // Casilla donde se encuentra pacman.
    private boolean terminado;                          // Define si nuestro algoritmo ha terminado.
    private NodoBusqueda nodoSolucion;                  // Nodo a partir del cual se define la solución,
    // porque ya se encontró la mejor rutal al estado meta.

    /**
     * Inicializador del algoritmo. Se debe mandar llamar cada vez que cambien
     * el estado incial y el estado final.
     *
     * @param estadoInicial Pasillo donde se encuentra el fantasma.
     * @param estadoFinal Pasillo donde se encuentra pacman.
     */
    private void inicializa(Estado estadoInicial, Estado estadoFinal) {
        this.estadoFinal = estadoFinal;
        this.terminado = false;
        this.nodoSolucion = null;
        this.listaAbierta = new PriorityQueue<>();
        this.listaCerrada = new ArrayList<>();
        estadoInicial.calculaHeuristica(estadoFinal);
        NodoBusqueda inicial = new NodoBusqueda(estadoInicial);
        inicial.calcularHeuristica(estadoFinal);
        inicial.gn(0);
        this.listaAbierta.offer(inicial);
    }

    /**
     * Función que realiza un paso en la ejecución del algoritmo. Se encarga de
     * indicarle al fantasma como se tiene que desplazar
     */
    private void expandeNodoSiguiente() {
        while (!this.listaAbierta.isEmpty()) {
            NodoBusqueda actual = this.menorLA();
            if (actual.equals(new NodoBusqueda(estadoFinal))) {
                this.listaCerrada.add(actual);
                break;
            }
            this.listaCerrada.add(actual);
            LinkedList<NodoBusqueda> vecinos = actual.getSucesores();
            for (NodoBusqueda n : vecinos) {
                n.calcularHeuristica(estadoFinal);
                if (this.containsLC(n.estado())) {
                    continue;
                }
                int costo = actual.gn() + actual.distanciaA(n);
                if (this.containsLA(n) && costo < n.gn()) {
                    this.listaAbierta.remove(this.getLA(n));
                }
                if (this.containsLC(n.estado()) && costo < n.gn()) {
                    this.listaCerrada.remove(this.getLC(n));
                }
                if (!(this.containsLA(n) && this.containsLC(estadoFinal))) {
                    n.gn(costo);
                    this.listaAbierta.add(n);
                }
            }

        }
        //Se manda a llamar el metodo pintaTrayectoria
        this.nodoSolucion = this.listaCerrada.get(this.listaCerrada.size() - 1);
        this.pintaTrayectoria(Color.BLUE);
    }

    /**
     * Se puede llamar cuando se haya encontrado la solución para obtener el
     * plan desde el nodo inicial hasta la meta.
     *
     * @return secuencia de movimientos que llevan del estado inicial a la meta.
     */
    private LinkedList<Movimiento> generaTrayectoria() {
        LinkedList<Movimiento> trayecto = new LinkedList<>();
        int size = this.listaCerrada.size();
        NodoBusqueda actual = this.listaCerrada.get(size - 1);
        while (actual.padre() != null) {
            trayecto.addFirst(actual.accionPadre());
            actual = actual.padre();
        }
        return trayecto;
    }

    /**
     * Pinta las celdas desde el nodo solución hasta el nodo inicial
     */
    private void pintaTrayectoria(Color color) {
        if (nodoSolucion == null) {
            return;
        }
        NodoBusqueda temp = nodoSolucion.padre();
        while (temp.padre() != null) {
            temp.estado().pintaCelda(color);
            temp = temp.padre();
        }
    }

    /**
     * Función que ejecuta A* para determinar la mejor ruta desde el fantasma,
     * cuya posición se encuetra dentro de <code>estadoInicial</code>, hasta
     * Pacman, que se encuentra en <code>estadoFinal</code>.
     *
     * @return Una lista con la secuencia de movimientos que Sombra debe
     * ejecutar para llegar hasta PacMan.
     */
    @Override
    public LinkedList<Movimiento> resuelveAlgoritmo(Estado estadoInicial, Estado estadoFinal) {
        if (!estadoInicial.compare(estadoFinal)) {
            this.inicializa(estadoInicial, estadoFinal);
            this.expandeNodoSiguiente();
            return this.generaTrayectoria();
        } else {
            System.exit(0);
        }
        return this.generaTrayectoria();
    }

    /**
     * Metodo retorna el nodo con menor f(n) de la lista abierta
     *
     * @return NodoBusqueda
     */
    public NodoBusqueda menorLA() {
        if (this.listaAbierta.size() == 1) {
            return this.listaAbierta.poll();
        }
        NodoBusqueda menor = this.listaAbierta.peek();
        for (NodoBusqueda i : this.listaAbierta) {
            if (menor.fn() > i.fn()) {
                menor = i;
            }
        }
        this.listaAbierta.remove(menor);
        return menor;
    }

    /**
     * Retorna el nodo semejante al parametrizado que esta contenido en la lista
     * abierta
     *
     * @param v NodoBusqueda
     * @return NodoBusqueda
     */
    public NodoBusqueda getLA(NodoBusqueda v) {
        for (NodoBusqueda i : this.listaAbierta) {
            if (i.equals(v)) {
                this.listaAbierta.remove(i);
                return i;
            }
        }
        return null;
    }

    /**
     * Retorna el nodo semejante al parametrizado que esta contenido en la lista
     * cerrada
     *
     * @param v NodoBusqueda
     * @return NodoBusqueda
     */
    public NodoBusqueda getLC(NodoBusqueda v) {
        for (NodoBusqueda i : this.listaCerrada) {
            if (i.equals(v)) {
                this.listaCerrada.remove(i);
                return i;
            }
        }
        return null;
    }

    /**
     * Metodo que determina si std ya existe en la lista cerrada
     *
     * @param std Estado
     * @return boolean
     */
    public boolean containsLC(Estado std) {
        for (NodoBusqueda nb : this.listaCerrada) {
            if (nb.equals(new NodoBusqueda(std))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Metodo que determina si n ya existe en la lista abierta
     *
     * @param std NodoBusqueda
     * @return boolean
     */
    public boolean containsLA(NodoBusqueda n) {
        for (NodoBusqueda nb : this.listaAbierta) {
            if (nb.equals(n)) {
                return true;
            }
        }
        return false;
    }

}
