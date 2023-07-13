/**
 * @author Arroyo Erick
 * @version 1.0
 * Clase que reprensenta un perceptron, dado que se requiere que el mismo 'aprenda' a resolver 
 * las compuertas logicas AND y OR, y dado que la logica que siguen ambos tipos de aprendizaje
 * son analogas, se implementa una sola clase que pueda resolverlas. Lo unico en lo que difieren es 
 * el entrenamiento que le damos a perceptron.
 * En la clase main, tenemos dos conjuntos de entrenamiento, uno para la compurta AND y otro para OR, 
 * y uno de pruebas.
 */
import java.util.Stack;
import java.util.Random;

public class Perceptron {
    private float w0, w1, w2, w3;
    private float alpha, theta;
    private final float x0 = -1.0f;// Entrada Bias, este toma el minimo valor que puede tomar alguna entrada xn
                                   // Puede ajustar su valor a 1.0f igualmente por conveniencia

    /**
     * Metodo contructor de clase Perceptron
     * 
     * @param theta float que representa el sesgo
     * @param alpha float que representa el factor de aprendizaje
     */
    public Perceptron(float theta, float alpha) {
        this.w0 = new Random().nextFloat();
        this.w1 = new Random().nextFloat();
        this.w2 = new Random().nextFloat();
        this.w3 = new Random().nextFloat();
        this.alpha = alpha;
        this.theta = theta;
    }

    /**
     * Metodo que se encarga de entrenar al perceptron dado un conjunto de
     * entrenamiento.
     * 
     * @param input
     */
    public void train(float[][] input) {
        float y = 0.0f, error = 0.0f;
        int file = 0, recal = 0;
        printWeights("Pesos iniciales");
        while (file < input.length) {
            System.out.println(String.format("[Recalculo %d][Fila %d]", recal, file));
            y = (x0 * w0) + (w1 * input[file][0]) + (w2 * input[file][1]) + (w3 * input[file][2]);// Funcion suma
            y -= theta;
            // Funcion de activacion
            // if(y>=theta){ Puede descomentar esta linea y comentar la de abajo para probar
            // la función de activacion con respecto al sesgo
            if (y >= 0) {
                y = 1;
            } else {
                y = -1;
            }
            error = input[file][3] - y;// Se calcula el error
            System.out.println(String.format("[y = %f][error = %f]", y, error));
            if (error == 0.0f) {
                file++;
            } else {
                // Recalculamos pesos
                if (error != 0) {
                    w0 = w0 + (error * alpha * x0);
                    w1 = w1 + (error * alpha * input[file][0]);
                    w2 = w2 + (error * alpha * input[file][1]);
                    w3 = w3 + (error * alpha * input[file][2]);
                }
                file = 0;
                recal++;
                printWeights("Pesos recalculados");
            }
        }
    }

    /**
     * Metodo que imprime el valor de los pesos y del factor de aprendizaje
     * 
     * @param message String
     */
    public void printWeights(String message) {
        System.out.println(message);
        System.out.println(String.format("[w0 = %f]", w0));
        System.out.println(String.format("[w1 = %f]", w1));
        System.out.println(String.format("[w2 = %f]", w2));
        System.out.println(String.format("[w3 = %f]", w3));
        System.out.println(String.format("[alpha = %f]", alpha));
    }

    /**
     * Metodo que toda un conjunto de entradas [x1,x2,x3] para determinar su valor
     * de salida
     * 
     * @param std Arreglo unidimensional de dimension, de al menos 3, que contiene
     *            las entradas
     * @return 1 si el valor es mayor o igual a cero, -1 en otro caso
     */
    public float query(float[] std) {
        return ((w0 * x0) + (w1 * std[0]) + (w2 * std[1]) + (w3 * std[2]) >= 0) ? 1 : -1;
    }

    /**
     * Metodo que aplica un entrenamiento al perceptron por cada conjunto de
     * entrenamiento contenido en la pila, los ultimos dos atributos permiten
     * un entrenamiento personalizado.
     * 
     * @param sets   Stack que almacena los conjutos de entrenamiento
     * @param num    int que determina con cuantos conjuntos se entrenara, -1 si se
     *               quiere entrenar con todos los conjutnos.
     * @param select int que determina con que conjunto en especifico se entrenara
     *               puede tomar valores en [0, sets.size] o -1 si no es requerido
     *               uno en especifico
     */
    public void setTraining(Stack<float[][]> sets, int num, int select) {
        int setI = 0;
        while (!sets.empty()) {
            if (select != -1) {
                System.out.println(String.format("#### ENTRENAMIENTO [%d] ####", setI + 1));
                this.train(sets.get(select));
                return;
            } else if (select == -1 && num > 0) {
                System.out.println(String.format("#### ENTRENAMIENTO [%d] ####", setI + 1));
                this.train(sets.pop());
            } else {
                System.out.println(String.format("#### ENTRENAMIENTO [%d] ####", setI + 1));
                this.train(sets.pop());
            }
            setI++;
            num--;
            if (num == 0)
                    return;
        }
    }
}