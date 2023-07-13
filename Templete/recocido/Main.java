package recocido;

import java.io.File;

/**
 * Clase para ejecutar un proceso de optimización usando recocido simulado.
 * 
 * @author Benjamin Torres Saavedra
 * @author Verónica E. Arriola
 * @version 0.1
 */
public class Main {
	/**
	 * Recibe la dirección de un archivo .tsp y utiliza recocido simulado
	 * para encontrar una solución al problema del agente viajero en esa
	 * ciudad.
	 * El programa se podrá ejecutar como:
	 * java recocido.Main <archivo.tsp>
	 * 
	 * @param args Nombre del archivo tsp.
	 */
	public static void main(String[] args) {
		int interacciones = 1000;
		File file = new File("data\\" + args[0]);
		if (file.isFile()) {
			DatosPAV datos = new DatosPAV(file);
			Solucion s = new SolucionReal(datos.numCiudades());
			float tInicial = (float) ((float) s.evaluar(datos) * (2.0));// Puede modificar este factor sin problema
			RecocidoSimulado recocido = new RecocidoSimulado(s, tInicial, datos);
			System.out.println("Temperatura Inicial: " + tInicial);
			System.out.println("Solucion inicial: " + s.toString() + "\n Distancia: " + s.evaluar(datos) + "km");
			for (int i = 0; i < interacciones; i++) {
				s = recocido.ejecutar();
				// System.out.println("==================================================================");
				// System.out.println("La mejor solucion temporal es: [Iteracion "+(i+1)+"]");
				// System.out.println(s.toString() + "\nDistacia: " + s.evaluar(datos)+"km");
			}
			System.out.println("\n------------------------------------" + "\nLa mejor solucion encontrada fue:");
			System.out.println(s.toString() + "\nDistacia: " + s.evaluar(datos) + "km");
		} else {
			System.out.println("Error");
		}
	}
}