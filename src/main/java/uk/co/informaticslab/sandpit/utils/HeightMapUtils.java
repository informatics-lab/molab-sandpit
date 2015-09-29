package uk.co.informaticslab.sandpit.utils;

import com.jme3.math.Vector2f;
import uk.co.informaticslab.sandpit.domain.DepthMap;
import uk.co.informaticslab.sandpit.domain.DepthMapCalibration;

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

    public static float[] createHeightMapFromDepthMap(DepthMap depthMap, int scalingFactor) {
        short[] values = depthMap.getSample();
        float[] heightMap = new float[values.length];
        Vector2f dimensions = depthMap.getDimensions();
        int i = 0;
        for (int y = 0; y < dimensions.getY(); y++) {
            for (int x = 0; x < dimensions.getX(); x++) {
                //use this index to flip the heights array horizontally
                int index = (((int)dimensions.getX()-1) - x)+(y*((int)dimensions.getX()));
                heightMap[index] = mapDepthMapValueToHeightMapValue(values[i], depthMap.getMin(), depthMap.getMax()) / scalingFactor;
                i++;
            }
        }

        return heightMap;
    }

    public static float[] createHeightMapFromDepthMap(DepthMap depthMap, int scalingFactor, DepthMapCalibration depthMapCalibration) {
        short[] values = depthMap.getSample();
        float[] heightMap = new float[values.length];
        Vector2f dimensions = depthMapCalibration.getDepthMapDimensions();
        int i = 0;
        for (int y = 0; y < dimensions.getY(); y++) {
            for (int x = 0; x < dimensions.getX(); x++) {
                //use this index to flip the heights array horizontally
                int index = (((int)dimensions.getX()-1) - x)+(y*((int)dimensions.getX()));
                heightMap[index] = mapDepthMapValueToHeightMapValue(values[i], depthMapCalibration.getMinValue(), depthMapCalibration.getMaxValue()) / scalingFactor;
                i++;
            }
        }

        return heightMap;
    }

    public static float avgFloatArray(float[] floatArray) {
        float sum = 0;
        for( int i = 0; i < floatArray.length; i++) {
            sum += floatArray[i];
        }
        return sum / floatArray.length;
    }

}
