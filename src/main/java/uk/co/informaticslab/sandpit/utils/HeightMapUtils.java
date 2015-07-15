package uk.co.informaticslab.sandpit.utils;

import com.jme3.math.Vector2f;
import java.util.ArrayList;
import java.util.List;
import uk.co.informaticslab.sandpit.domain.DepthMap;
import uk.co.informaticslab.sandpit.domain.Dimension2D;

/**
 * Created by tom on 26/06/2015.
 */
public class HeightMapUtils {
            
    public static final List<Vector2f> HEIGHTMAP_COORDS = getHeightmapCoords(320, 240);
    
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

    public static float[] createHeightMapFromDepthMap(DepthMap depthMap, int width, int height) {
        float[] heightMap = new float[width * height];
        Dimension2D dimensions = depthMap.getDimensions();
        short[] values = depthMap.getSample();

        int i = 0;
        for (int y = 0; y < dimensions.getHeight(); y++) {
            for (int x = 0; x < dimensions.getWidth(); x++) {
                heightMap[(y * height) + x] = mapDepthMapValueToHeightMapValue(values[i++], depthMap.getMin(), depthMap.getMax());
            }
        }
        return heightMap;
    }
    
    public static List<Vector2f> getHeightmapCoords(int heightmapWidth, int heightmapHeight) {
        List<Vector2f> coords = new ArrayList<>();
        for(int y = 0; y < heightmapHeight; y++){
            for(int x = 0; x < heightmapWidth; x++){
                Vector2f coord = new Vector2f(x,y);
                coords.add(coord);
            }
        }
        return coords;
    }


}
