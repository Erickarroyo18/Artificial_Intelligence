/*
 * Código utilizado para el curso de Inteligencia Artificial.
 * Se permite consultarlo para fines didácticos en forma personal,
 * pero no esta permitido transferirlo resuelto a estudiantes actuales o potenciales.
 */
package pacman.personajes.navegacion;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.LinkedList;
import pacman.escenario.Pasillo;
import pacman.personajes.Movimiento;

/**
 * Clase que modela a los nodos de nuestra gráfica de búsqueda.
 *
 * @author blackzafiro
 * @author baruch
 */
public class NodoBusqueda implements Comparable<NodoBusqueda> {

    private final static Logger LOGGER = Logger.getLogger("pacman.personajes.navegacion.NodoBusqueda");

    static {
        LOGGER.setLevel(Level.OFF);
    }

    private NodoBusqueda padre;     // Nodo que generó este nodo.
    private Movimiento accionPadre; // Acción que llevo al agente a este estado.
    private Estado estado;          // Referencia del estado al que se llego.
    private int gn;                 // costo de llegar a este nodo.

    /**
     * Nodo que considera al estado indicado.
     *
     * @param estado
     */
    public NodoBusqueda(Estado estado) {
        this.estado = estado;
    }

    /**
     * Devuelve el estado en este nodo.
     */
    public Estado estado() {
        return estado;
    }

    /**
     * Devuelve la acción que permite llegar a este nodo.
     *
     * @return
     */
    public Movimiento accionPadre() {
        return accionPadre;
    }

    /**
     * Devuelve al nodo búsqueda que generó a este nodo como sucesor.
     *
     * @return nodo antecesor en la solución.
     */
    public NodoBusqueda padre() {
        return padre;
    }

    public void setPadre(NodoBusqueda padre) {
        this.padre = padre;
    }

    /**
     * Devuelve la distancia de la mejor ruta conocida hasta el momento.
     *
     * @return Valor de la función g(n).
     */
    public int gn() {
        return gn;
    }

    /**
     * Metodo que asigna un valor al atributo gn
     *
     * @param value int
     */
    public void gn(int value) {
        this.gn = value;
    }

    /**
     * Función para obtener el estado de la función fn.
     *
     * @return Valor de la función f(n).
     */
    public int fn() {
        return gn + estado.hn();
    }

    /**
     * Obtenemos todos los sucesores posibles respecto al estado actual del
     * nodo.
     *
     * @return Lista con los sucesores al nodo actual.
     */
    public LinkedList<NodoBusqueda> getSucesores() {
        LinkedList<NodoBusqueda> sucesores = new LinkedList<>();
        //obtenemos el vecino superior
        Pasillo temp = this.estado.pasillo().obtenVecino(Movimiento.ARRIBA);
        if (temp != null) {
            NodoBusqueda n = generarNodo();
            n.accionPadre = Movimiento.ARRIBA;
            n.estado = this.estado.aplicaAccion(n.accionPadre);
            sucesores.add(n);
        }
        //obtenemos el vecino inferior
        temp = this.estado.pasillo().obtenVecino(Movimiento.ABAJO);
        if (temp != null) {
            NodoBusqueda n = generarNodo();
            n.accionPadre = Movimiento.ABAJO;
            n.estado = this.estado.aplicaAccion(n.accionPadre);
            sucesores.add(n);
        }
        //obtenemos el vecino derecho
        temp = this.estado.pasillo().obtenVecino(Movimiento.DERECHA);
        if (temp != null) {
            NodoBusqueda n = generarNodo();
            n.accionPadre = Movimiento.DERECHA;
            n.estado = this.estado.aplicaAccion(n.accionPadre);
            sucesores.add(n);
        }
        //obtenemos el vecino izquierda
        temp = this.estado.pasillo().obtenVecino(Movimiento.IZQUIERDA);
        if (temp != null) {
            NodoBusqueda n = generarNodo();
            n.accionPadre = Movimiento.IZQUIERDA;
            n.estado = this.estado.aplicaAccion(n.accionPadre);
            sucesores.add(n);
        }
        return sucesores;
    }

    /**
     * Metodo auxiliar que se encarga de inicializar los atributos de los nodos
     * vecinos generados
     *
     * @return NodoBusqueda
     */
    public NodoBusqueda generarNodo() {
        NodoBusqueda nb = new NodoBusqueda(null);
        nb.gn = this.gn + 10;
        nb.padre = this;
        return nb;
    }

    @Override
    public int compareTo(NodoBusqueda nb) {
        return fn() - nb.fn();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NodoBusqueda) || o == null) {
            return false;
        }
        NodoBusqueda otro = (NodoBusqueda) o;
        return estado.compare(otro.estado);
    }

    /**
     * Metodo que calcula la distacia a un nodoBusqueda
     *
     * @param n NodoBusqueda
     * @return int
     */
    public int distanciaA(NodoBusqueda n) {
        return (Math.abs(this.estado.pasillo().renglon() - n.estado.pasillo().renglon()) + Math.abs(this.estado.pasillo().columna() - n.estado.pasillo().columna())) * 10;
    }

    /**
     * Metodo que llama al metodo del mismo nombre para el atributo estado
     *
     * @param std Estado
     */
    public void calcularHeuristica(Estado std) {
        this.estado.calculaHeuristica(std);
    }

    @Override
    public String toString() {
        if (this.padre == null) {
            return String.format("[PADRE NULL][%d,%d], h(n)=%d | g(n)=%d | f(n)=%d", estado.pasillo().renglon(), estado.pasillo().columna(), estado.hn(), this.gn, this.fn());
        } else {
            return String.format("Padre [%d,%d], h(n)=%d | g(n)=%d | f(n)=%d", padre.estado.pasillo().renglon(), padre.estado.pasillo().columna(), padre.estado.hn(), padre.gn, padre.fn())
                    + String.format("[Nodo][%d,%d], h(n)=%d | g(n)=%d | f(n)=%d", estado.pasillo().renglon(), estado.pasillo().columna(), estado.hn(), this.gn, this.fn());
        }

    }
}
