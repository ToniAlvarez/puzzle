package puzzle;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Tony on 1/6/17.
 */
public class Imagen {


    /**
     * Método que genera una BufferedImage a partir de una Image
     *
     * @param img
     * @return BufferedImage
     */
    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage)
            return (BufferedImage) img;

        BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);

        Graphics g = bufferedImage.getGraphics();
        g.drawImage(img, 0, 0, null);

        return bufferedImage;
    }
}
