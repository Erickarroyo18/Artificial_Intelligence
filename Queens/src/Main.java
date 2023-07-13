/**
 * @author Arroyo Erick
 * @version 1.0
 */
public class Main {
    /**
     * Metodo que ejecuta la estructura del algoritmo genético
     * 
     * @param nxn         dimensiones
     * @param numInd      numero de individuos
     * @param iteraciones numero de iteraciones maximas
     */
    public void algoritmoGenetico(int nxn, int numInd, int iteraciones) {
        Poblacion poblacion = new Poblacion(numInd, nxn);
        poblacion.asignarAptitud();
        int cont = 0;
        while (!(poblacion.optimoEncontrado() || cont > iteraciones)) {
            Poblacion nuevPoblacion = new Poblacion(0, nxn);
            nuevPoblacion.poblacion.add(poblacion.elitismo(1).get(0));
            poblacion.cargarNormalizacion();
            while (nuevPoblacion.poblacion.size() < numInd) {
                Individuo i1 = poblacion.seleccionRuleta();
                Individuo i2 = poblacion.seleccionRuleta();
                Individuo hijo = poblacion.recombinacion(i1, i2);
                hijo.mutacion();
                nuevPoblacion.poblacion.add(hijo);
            }
            if (cont % 50 == 0) {
                System.out.println(
                        String.format("Mejor solucion en iteracion [%d] es [%s]", cont, poblacion.elitismo(1).get(0)));
            }
            poblacion = nuevPoblacion;
            poblacion.asignarAptitud();
            cont++;
        }
        if (poblacion.optimoEncontrado()) {
            Individuo op = poblacion.getOptimo();
            System.out.println(
                    String.format("Optimo encontrado en la iteracion [%d] es [%s]", cont, op));
            op.field();
        }
    }

    public static void main(String[] args) {
        Main m = new Main();
        m.algoritmoGenetico(11, 50, 1000);// Prueba de documento
    }
}