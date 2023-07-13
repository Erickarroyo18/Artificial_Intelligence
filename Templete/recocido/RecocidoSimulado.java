package recocido;

/**
 * Clase con los métodos necesarios para implementar el algoritmo
 * de recocido simulado junto con la solución a un problema particular.
 * 
 * @author Benjamin Torres
 * @author Verónica E. Arriola
 * @version 0.1
 */
public class RecocidoSimulado {

	/** Es la calificación que otorga la heurística a la solución actual. */
	private float valor;

	/** Parámetros del recocido. */
	private float temperatura;
	private DatosPAV misDatos;

	/** Solución actual. */
	private Solucion sol;

	/**
	 * Inicializa los valores necesarios para realizar el
	 * recocido simulado durante un número determinado de iteraciones.
	 * 
	 * @param inicial     Instancia de la clase para el problema particular que
	 *                    se quiere resolver. Contine la propuesta de solución
	 *                    inicial.
	 * @param temperatura <code>float</code> con el valor actual .
	 * @param decaimiento <code>float</code> que será usado para hacer decaer el
	 *                    valor de temperatura.
	 */
	public RecocidoSimulado(Solucion inicial, float temperaturaInicial, DatosPAV datos) {
		this.misDatos = datos;
		this.sol = inicial;
		this.temperatura = temperaturaInicial;
	}

	/**
	 * Función que calcula una nueva temperatura en base a
	 * la anterior y el decaemiento usado.
	 * Observe que el decrecimiento de la temperatura es exponencial.
	 * Por lo que, la grafica de este decrecimiento se acerca de forma asintotica
	 * al eje x. Por tanto, el valor de la temperatura nunca sera mejor o igual a
	 * cero.
	 * 
	 * @return nueva temperatura
	 */
	public float nuevaTemperatura() {
		double rp = (double) random(80, 99) / 100;
		return this.temperatura = (float) (this.temperatura * rp);
	}

	/**
	 * Genera y devuelve la solución siguiente a partir de la solución
	 * actual.
	 * Estos criterios de evaluación prentender respetar el pseudocodigo
	 * proporcionado.
	 * En caso de no obtener mejorías en las soluciones tras cada iteración
	 * puede probar modificando la desigualdad del primer if o modificando el signo
	 * la variable exponente.
	 * Observe que el metodo .evaluar() retorna la suma de las distancias entre las
	 * ciudades en el orden de recorrido. (Puede ver este recorrido con el metodo)
	 * toString de la clase SolucionReal
	 * 
	 * Recordemos que cualquier proba pertenece al intervalo [0,1], por lo que si
	 * elevamos a e a un exponente positivo, esta potencia podrá tomar cualquier
	 * valor real positivo, incluso mayor que uno. Por lo tanto, no me hace mucho
	 * sentido que el exponente sea positivo. Entonces, la linea 87 vuelve negativo
	 * al exponente para que el valor de la exp se reduzca a valores entre 0 y 1.
	 * Puede comentar esa linea (87) si es necesario, el programa seguira funcinando
	 * sin problema.
	 * 
	 * @return Solucion nueva
	 */
	public Solucion seleccionarSiguienteSolucion() {
		Solucion siguiente = new SolucionReal((SolucionReal) this.sol.siguienteSolucion());
		double valorSig = siguiente.evaluar(misDatos);
		double valorAct = this.sol.evaluar(misDatos);
		double delta = valorSig - valorAct;
		if (delta > 0) {
			return siguiente;
		} else {
			double r = (double) (random(0, 100) * 1.0 / 100);
			double exponente = (double) (delta / this.temperatura);
			 exponente = -exponente;
			double p = (double) Math.exp(exponente);
			if (r < p) {
				return siguiente;
			} else {
				return this.sol;
			}
		}
	}

	/**
	 * Ejecuta el algoritmo con los parámetros con los que fue inicializado y
	 * devuelve una solución.
	 * 
	 * @param
	 * @return Solución al problema
	 */
	public Solucion ejecutar() {
		Solucion act = this.sol;
		float t = this.temperatura = act.evaluar(misDatos);
		float epsilon = 0.001f;
		while (true) {
			if (t < epsilon) {
				this.valor = act.evaluar(misDatos);
				return this.sol = act;
			} else {
				act = this.seleccionarSiguienteSolucion();
			}
			t = this.nuevaTemperatura();
		}
	}

	/**
	 * Metodo que genera un numero aleatorio dados un min y un max
	 * 
	 * @param min int
	 * @param max int
	 * @return int
	 */
	public int random(int min, int max) {
		return (int) (Math.random() * (max + 1 - min)) + min;
	}
}