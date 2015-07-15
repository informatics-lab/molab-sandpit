package uk.co.informaticslab.sandpit.utils;

import uk.co.informaticslab.sandpit.domain.DepthMap;
import uk.co.informaticslab.sandpit.domain.Dimension2D;

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
        float[] heightMap = new float[quadSize * quadSize];
        Dimension2D dimensions = depthMap.getDimensions();
        short[] values = depthMap.getSample();

        int i = 0;
        for (int y = 0; y < dimensions.getHeight(); y++) {
            for (int x = 0; x < dimensions.getWidth(); x++) {
                heightMap[(y * quadSize) + x] = mapDepthMapValueToHeightMapValue(values[i++], depthMap.getMin(), depthMap.getMax());
            }
        }

        return heightMap;
    }


}
