/**
 * @author Arroyo Erick
 * @version 1.0
 * Clase main, esta nos permite entrenar a un perceptron e interactuar con el mismo
 * el atributo test puede modificarlo (añadiendo o eliminando) pues este es el conjunto de prueba.
 * Nota: Bajo esta implementacion podemos garantizar que el entrenamiento si va terminar y mimizará el error.
 */
import java.util.Stack;
import javax.swing.JOptionPane;

public class Main {
        private Perceptron myPerceptron;
        private final float[][] test = { { -1f, -1f, -1f, }, { 1f, -1f, 1f }, { -1f, -1f, 1f }, { 1f, 1f, 1f } };

        /**
         * Metodo contructor
         * 
         * @param theta float
         * @param alpha float
         */
        public Main(float theta, float alpha) {
                this.myPerceptron = new Perceptron(theta, alpha);
        }

        /**
         * Metodo que crea y retorna el conjunto de entrenamiento para la compuerta OR
         * Puede modificar los arreglos [set2, set3, set4] para entrenar al perceptron
         * NO MODIFICAR los arreglos [set0, set1]
         * 
         * @return Stack<float[][]>
         */
        public Stack<float[][]> getOrSets() {
                Stack<float[][]> sets = new Stack<>();
                float[][] set0 = { { 1f, 1f, 1f, 1f }, { -1f, -1f, -1f, -1f } };
                float[][] set1 = { { 1f, 1f, 1f, 1f }, { -1f, 1f, 1f, 1f }, { 1f, -1f, 1f, 1f }, { 1f, 1f, -1f, 1f },
                                { -1f, -1f, 1f, 1f }, { -1f, 1f, -1f, 1f },
                                { 1f, -1f, -1f, 1f }, { -1f, -1f, -1f, -1f } };
                float[][] set2 = { { 1f, 1f, 1f, 1f }, { 1f, -1f, 1f, 1f }, { -1f, -1f, 1f, 1f },
                                { -1f, -1f, -1f, -1f } };
                float[][] set3 = { { -1f, -1f, -1f, -1f }, { 1f, 1f, 1f, 1f }, { 1f, -1f, -1f, 1f },
                                { 1f, -1f, -1f, 1f } };
                float[][] set4 = { { 1f, 1f, 1f, 1f }, { 1f, -1f, 1f, 1f }, { 1f, 1f, -1f, 1f }, { -1f, 1f, 1f, 1f },
                                { -1f, -1f, -1f, -1f } };
                sets.add(set4);
                sets.add(set3);
                sets.add(set2);
                sets.add(set1);
                sets.add(set0);
                return sets;
        }

        /**
         * Metodo que crea y retorna el conjunto de entrenamiento para la compuerta AND
         * Puede modificar los arreglos [set2, set3, set4] para entrenar al perceptron
         * NO MODIFICAR los arreglos [set0, set1]
         * 
         * @return Stack<float[][]>
         */
        public Stack<float[][]> getAndSets() {
                Stack<float[][]> sets = new Stack<>();
                float[][] set0 = { { 1f, 1f, 1f, 1f }, { -1f, -1f, -1f, -1f } };
                float[][] set1 = { { 1f, 1f, 1f, 1f }, { -1f, 1f, 1f, -1f }, { 1f, -1f, 1f, -1f }, { 1f, 1f, -1f, -1f },
                                { -1f, -1f, 1f, -1f }, { -1f, 1f, -1f, -1f },
                                { 1f, -1f, -1f, -1f }, { -1f, -1f, -1f, -1f } };
                float[][] set2 = { { 1f, 1f, 1f, 1f }, { 1f, -1f, 1f, -1f }, { -1f, -1f, 1f, -1f },
                                { 1f, 1f, -1f, -1f } };
                float[][] set3 = { { 1f, 1f, 1f, 1f }, { -1f, -1f, -1f, -1f }, { 1f, 1f, 1f, 1f }, { -1f, 1f, 1f, -1f },
                                { 1f, -1f, -1f, -1f } };
                float[][] set4 = { { 1f, 1f, 1f, 1f }, { 1f, 1f, -1f, -1f }, { 1f, -1f, 1f, -1f }, { -1f, 1f, 1f, -1f },
                                { 1f, -1f, -1f, -1f } };
                sets.add(set4);
                sets.add(set3);
                sets.add(set2);
                sets.add(set1);
                sets.add(set0);
                return sets;
        }

        /**
         * Metodo que imprime el conjunto de prueba junto con sus salidas calculadas
         * 
         * @param String que representa la operacion que se prueba, null prueba general
         */
        public void proveTest(String gate) {
                int p = 100 / this.test.length;
                int sum = 0, andSum = 0, orSum = 0;
                String message;
                if (gate.equals("null")) {
                        message = "";
                        for (int i = 0; i < this.test.length; i++) {
                                String xs = String.format("[%f, %f,%f]", this.test[i][0], this.test[i][1],
                                                this.test[i][2]);
                                float out = this.myPerceptron.query(this.test[i]);
                                System.out.println(xs + " = " + out);
                                andSum += (out == getlogicValue(this.test[i], "AND")) ? p : 0;
                                orSum += (out == getlogicValue(this.test[i], "OR")) ? p : 0;
                        }
                        message += String.format("Evaluación del entrenamiento  [%s %d] para AND \n", "%",
                                        andSum);
                        message += String.format("Evaluación del entrenamiento  [%s %d] para OR \n", "%",
                                        orSum);
                } else {
                        message = "";
                        for (int i = 0; i < this.test.length; i++) {
                                String xs = String.format("[%f, %f,%f]", this.test[i][0], this.test[i][1],
                                                this.test[i][2]);
                                float out = this.myPerceptron.query(this.test[i]);
                                System.out.println(xs + " = " + out);
                                sum += (out == getlogicValue(this.test[i], gate)) ? p : 0;
                        }
                        message += String.format("Evaluación del entrenamiento  [%s %d] \n", "%", sum);
                }
                System.out.println(message);
        }

        /**
         * Metodo que obtiene el valor booleano correspondiente a una prueba
         * 
         * @param in   float[] arreglo de entradas
         * @param gate nombre de operacion logica
         * @return float valor deseadp
         */
        public float getlogicValue(float[] in, String gate) {
                if (gate.equals("AND")) {
                        float sum = 0f;
                        for (float i : in)
                                sum += i;
                        return (sum == 3f) ? 1 : -1;
                } else if (gate.equals("OR")) {
                        float sum = 0f;
                        for (float i : in)
                                sum += i;
                        return (sum > -3f) ? 1 : -1;
                }
                return 0;
        }

        public void run() {
                String[] botones = { "ENTRENAR AND", "ENTRENAR OR", "PROBAR", "SALIR" };
                int ventana = 0;
                boolean trainAND = false, trainOr = false;
                while (ventana != 3) {
                        ventana = JOptionPane.showOptionDialog(null,
                                        "SELECCIONA UNA OPCION",
                                        "\t PERPEPTRON",
                                        JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.QUESTION_MESSAGE, null,
                                        botones, botones[0]);
                        switch (ventana) {
                                case 0:
                                        if (!trainOr) {
                                                System.out.println("========== ENTRENANDO AND==========");
                                        //Puede modificar los ultimos dos atributos, tal que:
                                        // @param num es el numero de conjuntos que usaremos para entrenar, si pone -1, se usaran todos.
                                        // @param select indica que conjunto en especifico se usara para entrenar, usa -1 si no se requiere
                                                this.myPerceptron.setTraining(getAndSets(), -1, -1);
                                                trainAND = true;
                                                System.out.println("========== ENTRENAMIENTO FINALIZADO ==========");
                                        } else {
                                                System.out.println(
                                                                "#### YA SE HA ENTRENADO EL PERCEPTRON PARA OR ###\n Vuelve a ejecutar el programa si deseas entrenar.");
                                        }
                                        break;
                                case 1:
                                        if (!trainAND) {
                                                System.out.println("========== ENTRENANDO OR==========");
                                        //Puede modificar los ultimos dos atributos, tal que:
                                        // @param num es el numero de conjuntos que usaremos para entrenar, si pone -1, se usaran todos.
                                        // @param select indica que conjunto en especifico se usara para entrenar, usa -1 si no se requiere
                                                this.myPerceptron.setTraining(getOrSets(), -1, -1);
                                                trainOr = true;
                                                System.out.println("========== ENTRENAMIENTO FINALIZADO ==========");
                                        } else {
                                                System.out.println(
                                                                "#### YA SE HA ENTRENADO EL PERCEPTRON PARA AND ###\n Vuelve a ejecutar el programa si deseas entrenar.");
                                        }
                                        break;
                                case 2:
                                        if (trainAND) {
                                                System.out.println("========== APLICANDO PRUEBAS AND==========");
                                                this.proveTest("AND");
                                        } else if (trainOr) {
                                                System.out.println("========== APLICANDO PRUEBAS OR==========");
                                                this.proveTest("OR");
                                        } else {
                                                System.out.println(
                                                                "========== APLICANDO PRUEBAS (sin entrenamiento)==========");
                                                this.proveTest("null");
                                        }
                                        break;
                                case 3:
                                        System.out.println("========== ADIOS :D==========");
                                        break;
                        }
                }
        }

        public static void main(String[] args) {
                Main main = new Main(0.3f, 0.4f);
                main.run();
        }
}