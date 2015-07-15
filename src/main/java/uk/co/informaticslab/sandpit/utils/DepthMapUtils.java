package uk.co.informaticslab.sandpit.utils;

import uk.co.informaticslab.sandpit.domain.DepthMap;
import uk.co.informaticslab.sandpit.io.Camera3D;
import uk.co.informaticslab.sandpit.io.impl.Senz3DCamera;

/**
 * Created by tom on 26/06/2015.
 */
public class DepthMapUtils {

    public static DepthMap getDepthMapFromCamera(Camera3D camera) {
        return new DepthMap(camera.getDepthMapDimensions(), camera.sampleDepthMap(), Senz3DCamera.UNTRUSTED_VALUE);
    }

}
