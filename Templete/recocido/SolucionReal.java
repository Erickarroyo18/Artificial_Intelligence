package recocido;

/**
 * @author Erick Arroyo
 * @version 1.0
 *          Clase hija de Solucion, implementa sus metodos abstractos y define
 *          otros auxiliares
 */

public class SolucionReal extends Solucion {

    private float valor;
    private int[] id_ciudades;

    /**
     * Constructor principal de la clase, recibe el numero de ciudades leidas
     * para así inicializar la representacion de la solucion, tal que, esta es un
     * arreglo de enteros comprendidos entre 1 y el id maximo de la ciudad, dado
     * por el archivo tsp
     * 
     * @param numCiudades int
     */
    public SolucionReal(int numCiudades) {
        super();
        this.id_ciudades = new int[numCiudades + 1];
        generarAleatoria();
    }

    /**
     * Constructos que copia la solución de otra
     * 
     * @param act
     */
    public SolucionReal(SolucionReal act) {
        this.id_ciudades = new int[act.id_ciudades.length];
        for (int i = 0; i < this.id_ciudades.length; i++)
            this.id_ciudades[i] = act.id_ciudades[i];
    }

    @Override
    public SolucionReal siguienteSolucion() {
        SolucionReal neighbor = new SolucionReal(this);
        int mid = neighbor.id_ciudades.length / 2;
        int r = 0, fstInd = 0, sndInd = 0, cont = 0;
        int limit = (this.id_ciudades.length / 10) * 2;
        while (cont < limit) {
            r = random(1, mid);
            fstInd = determinarIndice(r, r + 5);
            r = random(mid, neighbor.id_ciudades.length - 2);
            sndInd = determinarIndice(r - 5, r);
            neighbor.swap(fstInd, sndInd);
            cont++;
        }

        return neighbor;
    }

    @Override
    public float evaluar(DatosPAV datos) {
        this.valor = (float) (this.funcionObjetivo(datos));
        return valor;
    }

    @Override
    public String toString() {
        String elements = "";
        for (int e : this.id_ciudades) {
            elements += e + ", ";
        }
        return elements;
    }

    /**
     * Metodo que genera una solución aleatoria
     */
    public void generarAleatoria() {
        int size = this.id_ciudades.length;
        for (int i = 0; i < size - 1; i++) {
            this.id_ciudades[i] = i;
        }
        for (int j = 0; j < size - 1; j++) {
            swap(random(0, size - 2), random(0, size - 2));
        }
        this.id_ciudades[size - 1] = this.id_ciudades[0];
    }

    /**
     * Metodo swap
     * 
     * @param i indice i
     * @param j indice j
     */
    public void swap(int i, int j) {
        int aux = this.id_ciudades[i];
        this.id_ciudades[i] = this.id_ciudades[j];
        this.id_ciudades[j] = aux;
    }

    /**
     * Metodo que genera un numero random entre dos valores
     * 
     * @param min random min
     * @param max random max
     * @return int
     */
    public int random(int min, int max) {
        return (int) (Math.random() * (max + 1 - min)) + min;
    }

    /**
     * Funcion que dados dos indices tal que ind_final-ind_inicio=5,
     * de esta forma determinamos de forma aleatoria cual de los
     * indices serviran para realizar un intercambio en nuestro
     * atributo id_ciudades.
     * 
     * @param ind_inicio int
     * @param ind_final  int
     * @return int
     */
    public int determinarIndice(int ind_inicio, int ind_final) {
        double incremento = (double) 0.2;
        double inter = incremento, last_inter = 0.0;
        double r = (double) random(0, 100) / 100;
        int cont = ind_inicio;
        while (cont <= ind_final) {
            if (r >= last_inter && r < inter) {
                return cont;
            }
            cont++;
            last_inter = inter;
            inter += incremento;
        }
        return -1;
    }

    /**
     * Funcion que determina la distacia entre dos pares de coordenadas
     * 
     * @param datos informacion de las ciudades
     * @return double [distacia]
     */
    public double funcionObjetivo(DatosPAV datos) {
        double distacia = 0.0;
        double[] cAct = null, cVec = null;
        for (int i = 0; i < this.id_ciudades.length - 1; i++) {
            cAct = datos.coordenadas(datos.codigo(this.id_ciudades[i]));
            cVec = datos.coordenadas(datos.codigo(this.id_ciudades[i + 1]));
            distacia += this.dH(cAct, cVec);
        }
        return distacia;
    }

    /**
     * Metodo que se encarga de determinar la distacia entre dos latitudes,
     * parandolas a radianes
     * y mutiplicando por el radio de la circunferencia ecuatorial
     * 
     * @param coordenadas1 latitud
     * @param coordenadas2 longitud
     * @return double
     */
    public double dH(double[] coordenadas1, double[] coordenadas2) {
        double lat1 = coordenadas1[0];
        double lat2 = coordenadas2[0];

        double deltaLat = 0.0;
        if (lat1 < lat2) {
            deltaLat = lat2 - lat1;
        } else {
            deltaLat = lat1 - lat2;
        }
        double alpha = (deltaLat * Math.PI) / 180;
        return (double) alpha * 6378;
    }
}