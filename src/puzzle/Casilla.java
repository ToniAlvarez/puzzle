package puzzle;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Created by Toni on 01/6/17.
 */
public class Casilla {

    private boolean blanca;
    private int numCasilla;

    private Rectangle2D.Float rectangle = new Rectangle2D.Float();

    private BufferedImage imagen = null;


    /**
     * Constructor con el numero de Casilla
     */
    public Casilla(int numCasilla) {
        blanca = false;
        this.numCasilla = numCasilla;
    }

    /**
     * paintComponent de la clase que pinta el rectángulo
     *
     * @param graphics
     */
    public void paintComponent(Graphics graphics, int x, int y, int dim) {
        Graphics2D g2d = (Graphics2D) graphics;

        //Reubicar la casilla en el lugar que le corresponde cada vez que se pinta
        rectangle.setRect(x, y, dim, dim);

        if (blanca)
            g2d.fill(rectangle);
        else
            g2d.drawImage(imagen, null, x, y);
    }

    /*
     *  - GETTERS Y SETTERS
     */

    public boolean getBlanca() {
        return blanca;
    }

    public void setBlanca(boolean blanca) {
        this.blanca = blanca;
    }

    public int getNumCasilla() {
        return numCasilla;
    }

    public void setImagen(BufferedImage imagen) {
        this.imagen = imagen;
    }
}