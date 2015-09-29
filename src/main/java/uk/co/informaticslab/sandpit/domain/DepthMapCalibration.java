package uk.co.informaticslab.sandpit.domain;

import com.jme3.math.Vector2f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by tom on 29/09/2015.
 */
public class DepthMapCalibration {

    private static final Logger LOG = LoggerFactory.getLogger(DepthMapCalibration.class);

    private final short minValue;
    private final short maxValue;
    private final Vector2f depthMapDimensions;

    public DepthMapCalibration(short minValue, short maxValue, Vector2f depthMapDimensions) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.depthMapDimensions = depthMapDimensions;
        LOG.debug("Min :{}", minValue);
        LOG.debug("Max :{}", maxValue);
        LOG.debug("Dims :{}", depthMapDimensions);
    }

    public short getMinValue() {
        return minValue;
    }

    public short getMaxValue() {
        return maxValue;
    }

    public Vector2f getDepthMapDimensions() {
        return depthMapDimensions;
    }
}
