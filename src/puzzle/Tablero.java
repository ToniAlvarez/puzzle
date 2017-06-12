package puzzle;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Toni on 01/6/17.
 */
public class Tablero extends JPanel {

    private static final int DIM_MATRIZ = 3;
    private static final int MAXIMO = 601;
    private static final int LADO = MAXIMO / DIM_MATRIZ;

    private static final String IMAGEN_DEFECTO = "img/cara.jpg";

    private Casilla[][] casillas = new Casilla[DIM_MATRIZ][DIM_MATRIZ];

    private int numMovimientos = 0;
    private String imagen = IMAGEN_DEFECTO;

    //Constructor sin parámetros
    public Tablero() {
        iniciar();
    }

    /**
     * Método que inicializa el tablero, creando las casillas en orden natural y procesando la imagen
     */
    public void iniciar() {

        for (int fila = 0; fila < DIM_MATRIZ; fila++) {
            for (int columna = 0; columna < DIM_MATRIZ; columna++) {

                //Rellenar array de casillas
                casillas[fila][columna] = obtenerCasilla(columna + (fila * 3));
            }
        }

        trozearImagen();

        //El juego se ha (re)iniciado, poner la casilla blanca visible
        obtenerCasilla(0).setBlanca(false);

        repaint();
    }

    /**
     * Método que a partir de un evento de teclado, comprueba si el movimiento es válido y lo ejecuta
     *
     * @param ke evento
     */

    public void moverPieza(KeyEvent ke) throws MovimientoIncorrecto {

        //Si la tecla pulsada no es ninguna de las que generan movimiento, ignorar el evento
        if ("ASDWasdw".indexOf((KeyEvent.getKeyText(ke.getKeyCode())).charAt(0)) == -1) {
            //System.out.println("Tecla ignorada");
            return;
        }

        boolean casillaEncontrada = false;

        Casilla casillaVacia;

        for (int fila = 0; fila < DIM_MATRIZ; fila++) {
            for (int columna = 0; columna < DIM_MATRIZ; columna++) {
                if (casillas[fila][columna].getBlanca()) {

                    switch ((KeyEvent.getKeyText(ke.getKeyCode())).charAt(0)) {
                        case 'W':
                            //Si la blanca no está en la ultima fila se puede mover hacia arriba
                            if (fila != DIM_MATRIZ - 1) {
                                casillaVacia = casillas[fila][columna];
                                casillas[fila][columna] = casillas[fila + 1][columna];
                                casillas[fila + 1][columna] = casillaVacia;
                                movimiento();
                            } else {
                                throw new MovimientoIncorrecto("Movimiento NORTE incorrecto");
                            }
                            break;
                        case 'D':
                            //Si la blanca no está en la primera columna se puede mover hacia la derecha
                            if (columna != 0) {
                                casillaVacia = casillas[fila][columna];
                                casillas[fila][columna] = casillas[fila][columna - 1];
                                casillas[fila][columna - 1] = casillaVacia;
                                movimiento();
                            } else {
                                throw new MovimientoIncorrecto("Movimiento ESTE incorrecto");
                            }
                            break;
                        case 'S':
                            //Si la blanca no esta en la primera fila se puede mover hacia abajo
                            if (fila != 0) {
                                casillaVacia = casillas[fila][columna];
                                casillas[fila][columna] = casillas[fila - 1][columna];
                                casillas[fila - 1][columna] = casillaVacia;
                                movimiento();
                            } else {
                                throw new MovimientoIncorrecto("Movimiento SUD incorrecto");
                            }
                            break;
                        case 'A':
                            //Si la blanca no esta en la ultima columna se puede mover hacer la izquierda
                            if (columna != DIM_MATRIZ - 1) {
                                casillaVacia = casillas[fila][columna];
                                casillas[fila][columna] = casillas[fila][columna + 1];
                                casillas[fila][columna + 1] = casillaVacia;
                                movimiento();
                            } else {
                                throw new MovimientoIncorrecto("Movimiento OESTE incorrecto");
                            }
                            break;
                    }

                    casillaEncontrada = true;
                    break;
                }

            }

            //Si ya se ha encontrado la blanca, no hace falta buscar más
            if (casillaEncontrada)
                break;
        }
    }


    /**
     * Después de cada movimiento, repintar el tablero y comprobar si se ha resuelto el puzzle_viejo
     */
    private void movimiento() {

        numMovimientos++;

        System.out.println("\nMovimiento número " + numMovimientos);
        System.out.println("----------------------");

        pintarTablero();

        repaint();

        if (puzzleResuelto()) {
            //Pintar la casilla 'vacia'
            casillas[0][0].setBlanca(false);

            //Avisar al usuario
            JOptionPane.showMessageDialog(this, "Puzzle terminado en tan solo " + numMovimientos + " movmientos!");
            System.out.println("Puzzle terminado!!!");
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        //Color de la casilla blanca/vacia
        g2d.setColor(Color.GRAY);

        //Pintar Casillas
        int y = 0;

        for (int fila = 0; fila < DIM_MATRIZ; fila++) {
            //Reiniciar coordenada y (origen horizontal) en cada fila
            int x = 0;

            for (int columna = 0; columna < DIM_MATRIZ; columna++) {
                casillas[fila][columna].paintComponent(g2d, x, y, LADO);

                //Aumentar origen horizontal en la dimension del lado de una casilla
                x += LADO;
            }

            //Aumentar origen vertical en la dimension del lado de una casilla
            y += LADO;
        }


        //Dibujar cuadrícula negra encima de las casillas
        g2d.setColor(Color.BLACK);
        int origen = 0;

        for (int i = 0; i <= DIM_MATRIZ; i++) {

            //Lineas horizontales
            g.drawLine(0, origen, MAXIMO, origen);

            //Lineas verticales
            g.drawLine(origen, 0, origen, MAXIMO);

            //Aumentar ambos orígenes en la dimension del lado de una casilla
            origen += LADO;
        }

    }

    /**
     * Pintar la matriz de casillas en la consola
     */
    public void pintarTablero() {
        System.out.println("  -   0  -  1  -  2");

        for (int fila = 0; fila < DIM_MATRIZ; fila++) {
            System.out.print(fila + " - ");

            for (int columna = 0; columna < DIM_MATRIZ; columna++)
                System.out.print("[ " + casillas[fila][columna].getNumCasilla() + " ] ");

            System.out.println();
        }
    }


    /**
     * Devuelve:
     * - True si el puzzle está resulto
     * - False en caso contrario
     */

    public boolean puzzleResuelto() {

        for (int fila = 0; fila < DIM_MATRIZ; fila++)
            for (int columna = 0; columna < DIM_MATRIZ; columna++)
                if (casillas[fila][columna].getNumCasilla() != (fila * 3 + columna))
                    return false;

        return true;
    }

    public Casilla obtenerCasilla(int numCasilla) {

        for (int fila = 0; fila < DIM_MATRIZ; fila++)
            for (int columna = 0; columna < DIM_MATRIZ; columna++)
                if (casillas[fila][columna] != null && numCasilla == casillas[fila][columna].getNumCasilla())
                    return casillas[fila][columna];

        return new Casilla(numCasilla);
    }

    public void desordenar() {
        int numCasillas = DIM_MATRIZ * DIM_MATRIZ - 1;

        int[] arrayOrdenado = new int[numCasillas + 1];
        int[] arrayDesordenado = new int[numCasillas + 1];

        //Rellenar el primer array con enteros ordenados
        for (int i = 0; i < arrayOrdenado.length; i++)
            arrayOrdenado[i] = i;

        //Generador de numeros aleatorios
        Random rand = new Random();
        int randomNum;

        //Booleano para activar/desactivar el debug del algoritmo de ordenación
        boolean debug_ordenacion = true;

        if (debug_ordenacion) {
            System.out.println("\n===== Contenidos de los arrays antes de pasar por el algoritmo =====");
            System.out.println("Fuente: " + Arrays.toString(arrayOrdenado));
            System.out.println("Resultado: " + Arrays.toString(arrayDesordenado));
        }

        //Algoritmo de ordenacion basado en Fisher–Yates, Durstenfeld's version
        for (int i = 0; i < arrayOrdenado.length; i++) {

            randomNum = rand.nextInt(numCasillas + 1 - i);

            if (debug_ordenacion) {
                System.out.println("\n===== Iteración nº " + (i + 1) + " del algoritmo de ordenacion =====");
                System.out.println("\nGenerando nº aleatorio. Rango: 0 - " + (numCasillas - i) + ". Resultado: " + randomNum);
            }

            arrayDesordenado[i] = arrayOrdenado[randomNum];
            arrayOrdenado[randomNum] = arrayOrdenado[arrayOrdenado.length - 1 - i];

            if (debug_ordenacion) {
                System.out.println("Fuente: " + Arrays.toString(arrayOrdenado));
                System.out.println("Resultado: " + Arrays.toString(arrayDesordenado));
            }
        }

        System.out.println("###### REORDENANDO ARRAY #######");

        //Ordenar el array bidimensional a partir del resultado del algoritmo de ordenación

        int fila;
        int columna;

        while (numCasillas >= 0) {
            fila = 2 - numCasillas / DIM_MATRIZ;
            columna = 2 - numCasillas % DIM_MATRIZ;

            System.out.println("Buscando casilla " + arrayDesordenado[numCasillas]);
            casillas[fila][columna] = obtenerCasilla(arrayDesordenado[numCasillas]);

            numCasillas--;
        }

        if (debug_ordenacion) {
            System.out.println("\n===== Contenido del array bidimensional de Casillas desordenado =====");
            pintarTablero();
        }

        //Por último, la casilla 0 no se tiene que pintar
        obtenerCasilla(0).setBlanca(true);

        numMovimientos = 0;

        repaint();
    }


    @Override public Dimension getPreferredSize() {
        return new Dimension(MAXIMO, MAXIMO);
    }


    /**
     * A partir de una imagen cualquiera, genera una imagen cuadrada,
     * la recorta en una matriz del tamaño del tablero,
     * y le asigna la subimagen correspondiente a cada casilla
     */
    public void trozearImagen() {
        try {
            BufferedImage imgIn = ImageIO.read(new File(imagen));

            int alto = imgIn.getHeight();
            int ancho = imgIn.getWidth();
            int ladoMenor = (alto > ancho) ? ancho : alto;

            //Genera una imagen cuadrada usando el lado más pequeño
            imgIn = imgIn.getSubimage(0, 0, ladoMenor, ladoMenor);

            //Redimensionar al tamaño del tablero
            imgIn = Imagen.toBufferedImage(imgIn.getScaledInstance(MAXIMO, MAXIMO, Image.SCALE_SMOOTH));

            //Cortar la imagen y asignar subimagenes a las Casillas
            for (int fila = 0; fila < DIM_MATRIZ; fila++) {
                for (int columna = 0; columna < DIM_MATRIZ; columna++) {

                    //Recortar la imagen y adjudicar cada subimagen a la casilla correspondiente
                    Casilla casilla = obtenerCasilla(columna + (fila * 3));

                    casilla.setImagen(imgIn.getSubimage(columna * 200, fila * 200, LADO, LADO));

                    System.out.println("Casilla " + (columna + (fila * 3)));
                    System.out.println("Origen imagen: " + columna * 200 + " - " + fila * 200);
                }
            }

        } catch (IOException e) {
            System.err.println("Error al procesar la imagen del puzzle");
        }

    }

    /*
     *  GETTERS AND SETTER
     */

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
