/**
 * @author Arroyo Erick
 * @version 1.0
 */
public class Individuo {
    int[] estado;
    int fitness, dimension;

    /**
     * Construtor de clase
     * 
     * @param fitness
     * @param dimension
     */
    Individuo(int fitness, int dimension) {
        this.dimension = dimension;
        this.fitness = fitness;
        this.estado = new int[this.dimension];
    }

    /**
     * Metodo que modifica el estado de un individuo [Muta]
     */
    public void mutacion() {
        for (int i = 0; i < this.dimension; i++) {
            this.estado[i] = ((Math.random() * 9) < 2) ? (int) (Math.random() * this.dimension) : this.estado[i];
        }
    }

    @Override
    public String toString() {
        String arreglo = "";
        for (int i : estado)
            arreglo += i + ",";
        return String.format("Fitness [%d], estado [%s]", this.fitness, arreglo);
    }

    /**
     * Metodo que inicializa la matriz
     */
    public void field() {
        String[][] board = new String[this.dimension][this.dimension];
        for (int i = 0; i < this.dimension; i++) {
            for (int j = 0; j < this.dimension; j++) {
                board[i][j] = "";
            }
        }
        for (int o = 0; o < this.dimension; o++) {
            board[this.estado[o]][o] = "X";
        }
        print(board);
    }

    /**
     * Metodo que permite visualizar una representacion del individuo optimo
     * 
     * @param board String[][]
     */
    public void print(String[][] board) {
        System.out.println(line());
        int fila = this.dimension - 1;
        for (int i = this.dimension - 1; i >= 0; i--) {
            String out = String.valueOf(fila);
            for (int j = 0; j < this.dimension; j++) {
                out += "|";
                if (board[i][j].equals("X")) {
                    out += "X";
                } else {
                    out += " ";
                }
            }
            fila--;
            System.out.println(out + "|");
        }
        System.out.println(line() + "\n" + colum());

    }

    /**
     * Metodo que imprime una linea
     * 
     * @return String
     */
    public String line() {
        String line = " -";
        for (int i = 0; i < this.dimension; i++) {
            line += "--";
        }
        return line;
    }

    /**
     * Metodo que carga el formato de las columnas
     * 
     * @return String
     */
    public String colum() {
        String colums = "  ";
        for (int i = 0; i < this.dimension; i++) {
            colums += String.valueOf(i) + " ";
        }
        return colums;
    }
}