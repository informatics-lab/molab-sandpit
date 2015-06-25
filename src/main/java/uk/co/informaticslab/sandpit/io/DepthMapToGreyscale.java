package uk.co.informaticslab.sandpit.io;

import uk.co.informaticslab.sandpit.io.impl.Mock3DCamera;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DepthMapToGreyscale {

    public static void main(String[] args) throws IOException {

        Mock3DCamera camera = new Mock3DCamera();
        short[] depthMap = camera.sampleDepthMap();

        BufferedImage image = new BufferedImage(320, 240, BufferedImage.TYPE_INT_RGB);

        int min = Integer.MAX_VALUE;
        int max = 0;

        int i = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {

                int rgb = depthMap[i++];
                if (rgb == 32001) {
                    rgb = 0;
                } else {

                    int t1 = rgb - 521;
                    double t2 = t1 / 465.0;
                    int t3 = (int) (t2 * 255);

                    rgb = rgba(t3, t3, t3, 0);
                }

                image.setRGB(x, y, rgb);
            }
        }
        System.out.println("min: " + min);
        System.out.println("max: " + max);

        ImageIO.write(image, "png", new File("depth-map-img.png"));
    }

    private static int rgba(int r, int g, int b, int a) {
        return ((a & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8) |
                ((b & 0xFF) << 0);
    }
}
