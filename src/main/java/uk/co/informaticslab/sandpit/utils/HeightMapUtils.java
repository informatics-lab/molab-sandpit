package uk.co.informaticslab.sandpit.utils;

import uk.co.informaticslab.sandpit.domain.DepthMap;
import uk.co.informaticslab.sandpit.domain.Dimension2D;

import java.util.LinkedList;

/**
 * Created by tom on 26/06/2015.
 */
public class HeightMapUtils {

    private static final int RGB_RANGE_MIN = 0;
    private static final int RGB_RANGE_MAX = 255;

    public static float mapDepthMapValueToHeightMapValue(int rawDepthMapValue, int minDepthMapValue, int maxDepthMapValue) {
        if (rawDepthMapValue == 0) {
            return 0f;
        }
        int t1 = rawDepthMapValue - minDepthMapValue;
        double t2 = t1 / ((double) (maxDepthMapValue - minDepthMapValue));
        int t3 = (int) (t2 * RGB_RANGE_MAX);
        t3 = Math.abs(t3 - RGB_RANGE_MAX);
        return t3;
    }

    public static float[] createHeightMapFromDepthMap(DepthMap depthMap, int quadSize) {
        LinkedList<Short> values = depthMap.getTrustedSample();
        float[] heightMap = new float[quadSize * quadSize];
        Dimension2D dimensions = depthMap.getDimensions();

        int i = 0;
        for (int y = 0; y < dimensions.getHeight(); y++) {
            for (int x = 0; x < dimensions.getWidth(); x++) {
                heightMap[(y * quadSize) + x] = mapDepthMapValueToHeightMapValue(values.get(i++), depthMap.getMin(), depthMap.getMax());
            }
        }

        return gaussBlur(heightMap, 512, 512, 1);
    }

    // source channel, width, height, radius
    private static float[] gaussBlur(float[] scl, int w, int h, int r) {
        float[] tcl = new float[scl.length];

        double rs = Math.ceil(r * 2.57);     // significant radius
        for (int i = 0; i < h; i++)
            for (int j = 0; j < w; j++) {
                double val = 0, wsum = 0;
                for (double iy = i - rs; iy < i + rs + 1; iy++)
                    for (double ix = j - rs; ix < j + rs + 1; ix++) {
                        int x = (int)Math.min(w - 1, Math.max(0, ix));
                        int y = (int)Math.min(h - 1, Math.max(0, iy));
                        double dsq = (ix - j) * (ix - j) + (iy - i) * (iy - i);
                        double wght = Math.exp(-dsq / (2 * r * r)) / (Math.PI * 2 * r * r);
                        val += scl[y * w + x] * wght;
                        wsum += wght;
                    }
                tcl[i * w + j] = Math.round(val / wsum);
            }
        return tcl;
    }

}
