
/**
 * @author Arroyo Erick
 * @version 1.0
 */
import java.util.ArrayList;

public class Poblacion {
    ArrayList<Individuo> poblacion = new ArrayList<>();
    ArrayList<Individuo> normalizados = new ArrayList<>();
    Individuo elite = new Individuo(0, this.dimension);
    int proporcionRC, dimension, fitnessOptimo;

    /**
     * Constructor de clase
     * 
     * @param n numero de individuos por generacion
     * @param d dimensiones [nxn]
     */
    public Poblacion(int n, int d) {
        this.dimension = d;
        this.fitnessOptimo = d*10;
        if (n != 0) {
            this.generarPoblacion(n);
        }
        // Se determina de forma aleatorio el porcentaje de recombinacion, como minimo
        // 1/6 de la dimension
        // como máximo 1/3
        int min = (int) (this.dimension * (1 * 1.0 / 6)), max = (int) (this.dimension * (1 * 1.0 / 3));
        this.proporcionRC = (int) ((Math.random() * (max + 1 - min)) + min);
    }

    /**
     * Metodo que genera los estados de los individuos
     * 
     * @param individuos int
     */
    public void generarPoblacion(int individuos) {
        for (int i = 0; i < individuos; i++) {
            Individuo temp = new Individuo(0, this.dimension);
            for (int j = 0; j < this.dimension; j++) {
                temp.estado[j] = (int) (Math.random() * this.dimension);
            }
            temp.fitness = this.aptitud(temp);
            this.poblacion.add(temp);
        }
    }

    /**
     * Metodo que determina la aptitud de un individuo
     * 
     * @param ind Individuo
     * @return int
     */
    public int aptitud(Individuo ind) {
        int sumFitness = 0;
        for (int i = 0; i < this.dimension; i++) {
            sumFitness += this.aptitudIndividuo(ind, ind.estado[i], i);
        }
        return Math.round((float) sumFitness / this.dimension);
    }

    /**
     * Metodo que determina si una reina ataca a otra a su derecha, partiendo de su
     * columna.
     * En este caso no consideramos los ataques en la misma columna, pues dada
     * nuestra implementacion
     * no generaremos a ningun individuo con esa caracteristica.
     * A su vez, solo consideramos los ataques posibles hacia la derecha, pues
     * evitamos los ataques reflexivos
     * 
     * @param ind     Individuo
     * @param fila    int
     * @param columna int
     * @return int
     */
    public int aptitudIndividuo(Individuo ind, int fila, int columna) {
        int aptitud = this.fitnessOptimo;
        if (this.diagonalAsc(ind, fila, columna)) {
            aptitud -= 10;
        }
        if (this.diagonalDes(ind, fila, columna)) {
            aptitud -= 10;
        }
        if (this.fila(ind, fila, columna)) {
            aptitud -= 10;
        }
        return aptitud;
    }

    /**
     * Metodo que verifica los ataques de una reina en sus movimientos diagonales
     * ascendentes.
     * 
     * @param ind     Individuo
     * @param fila    int
     * @param columna int
     * @return boolean
     */
    public boolean diagonalAsc(Individuo ind, int fila, int columna) {
        fila++;
        columna++;
        while (columna < this.dimension && fila < this.dimension) {
            if (this.hayReina(ind, fila, columna)) {
                return true;
            }
            columna++;
            fila++;
        }
        return false;
    }

    /**
     * Metodo que verifica los ataques de una reina en sus movimientos diagonales
     * descendentes.
     * 
     * @param ind     Individuo
     * @param fila    int
     * @param columna int
     * @return boolean
     */
    public boolean diagonalDes(Individuo ind, int fila, int columna) {
        fila--;
        columna++;
        while (columna < this.dimension && fila >= 0) {
            if (this.hayReina(ind, fila, columna)) {
                return true;
            }
            columna++;
            fila--;
        }
        return false;
    }

    /**
     * Metodo que verifica los ataques de una reina en sus movimientos sobre su
     * fila.
     * 
     * @param ind     Individuo
     * @param fila    int
     * @param columna int
     * @return boolean
     */
    public boolean fila(Individuo ind, int fila, int columna) {
        for (int i = columna + 1; i < this.dimension; i++) {
            if (ind.estado[i] == fila) {
                return true;
            }
        }
        return false;
    }

    /**
     * Metodo que dados la fila y a columna determina si esa casilla del tablero
     * esta ocupada por una reina
     * 
     * @param ind     Individuo
     * @param fila    int
     * @param columna int
     * @return boolean
     */
    public boolean hayReina(Individuo ind, int fila, int columna) {
        return (ind.estado[columna] == fila) ? true : false;
    }

    /**
     * Metodo que combina dos individuos para generar a otro
     * 
     * @param ind1 Individuo
     * @param ind2 Individuo Individuo
     * @return
     */
    public Individuo recombinacion(Individuo ind1, Individuo ind2) {
        Individuo hijo = new Individuo(0, this.dimension);
        for (int i = 0; i < this.dimension; i++) {
            if (i < this.proporcionRC) {
                hijo.estado[i] = ind1.estado[i];
            } else {
                hijo.estado[i] = ind2.estado[i];
            }
        }
        return hijo;
    }

    /**
     * Metodo que retorna a los individuos con mayor aptitud
     * 
     * @param n int
     * @return ArrayList<Individuo>
     */
    public ArrayList<Individuo> elitismo(int n) {
        ArrayList<Individuo> elites = new ArrayList<>();
        for (Individuo i : sort(this.poblacion)) {
            elites.add(i);
        }
        return elites;
    }

    /**
     * Metodo que ordena los individuos de la poblacion con respecto a su aptitud
     * 
     * @param arr ArrayList<Individuo>
     * @return ArrayList<Individuo>
     */
    ArrayList<Individuo> sort(ArrayList<Individuo> arr) {
        int n = arr.size();
        for (int i = 0; i < n - 1; i++) {
            int min_idx = i;
            for (int j = i + 1; j < n; j++)
                if (arr.get(j).fitness > arr.get(min_idx).fitness)
                    min_idx = j;
            Individuo temp = arr.get(min_idx);
            arr.set(min_idx, arr.get(i));
            arr.set(i, temp);
        }
        return arr;
    }

    /**
     * Metodo que determina a aptitud global de la poblacion
     * 
     * @return int
     */
    public int getFitness() {
        int fitness = 0;
        for (int i = 0; i < this.poblacion.size(); i++) {
            fitness += this.poblacion.get(i).fitness;
        }
        return fitness;
    }

    /**
     * Metodo que asigna a cada individuo de una poblacion su aptitud
     */
    public void asignarAptitud() {
        for (Individuo i : this.poblacion) {
            i.fitness = this.aptitud(i);
        }
    }

    /**
     * Metodo que determina la propocionalidad de un individuo para ser seleccionado
     * 
     * @return Individuo[]
     */
    public ArrayList<Individuo> normalizar() {
        ArrayList<Individuo> arr = sort(this.poblacion);
        this.elite = arr.get(0);
        ArrayList<Individuo> proporciones = new ArrayList<>();
        int fit = this.getFitness(), cont = 0, index = 0, tope = 0, i = 0;
        while (cont < arr.size()) {
            Individuo j = arr.get(cont);
            double proporcion = (double) (j.fitness * 1.0 / fit) * 100;
            tope += (int) proporcion;
            for (i = index; i < tope; i++) {
                proporciones.add(j);
            }
            index = tope;
            cont++;
        }
        return proporciones;
    }

    /**
     * Metodo de selección por ruleta
     * 
     * @return Individuo
     */
    public Individuo seleccionRuleta() {
        return this.normalizados.get((int) Math.random() * (normalizados.size() - 1));
    }

    /**
     * Metodo que asigna el arreglo de la poblacion normalizado al atributo
     */
    public void cargarNormalizacion() {
        this.normalizados = this.normalizar();
    }

    /**
     * Metodo que determuna si se ha generado un individuo optimo
     * 
     * @return boolean
     */

    public boolean optimoEncontrado() {
        for (Individuo i : this.poblacion) {
            if (i.fitness == this.fitnessOptimo)
                return true;
        }
        return false;
    }

    /**
     * Metodo que retorna al individuo optimo en caso de existir
     * 
     * @return Individuo
     */
    public Individuo getOptimo() {
        for (Individuo i : this.poblacion) {
            if (i.fitness == this.fitnessOptimo)
                return i;
        }
        return null;
    }
}