package uk.co.informaticslab.sandpit.domain;

import java.util.LinkedList;

/**
 * Created by tom on 26/06/2015.
 */
public class DepthMap {

    private final Dimension2D dimensions;
    private final short min;
    private final short max;
    private final LinkedList<Short> trustedSample;

    public DepthMap(Dimension2D dimensions, short[] sample, short... untrustedValues) {
        short min = Short.MAX_VALUE;
        short max = Short.MIN_VALUE;

        LinkedList<Short> trusted = new LinkedList<>();
        for (short s : sample) {
            for (short untrustedValue : untrustedValues) {
                if (s == untrustedValue) {
                    s = 0;
                }
            }
            trusted.add(s);
            if (s < min && s != 0) {
                min = s;
            }
            if (s > max) {
                max = s;
            }
        }
        this.min = min;
        this.max = max;
        this.trustedSample = trusted;
        this.dimensions = dimensions;
    }

    /**
     * @return the minimum (non zero) trusted value
     */
    public short getMin() {
        return min;
    }

    /**
     * @return the maximum trusted value
     */
    public short getMax() {
        return max;
    }

    /**
     * @return the trusted depth map sample with all untrusted values set to 0
     */
    public LinkedList<Short> getTrustedSample() {
        return trustedSample;
    }

    /**
     * @return the 2D dimensions
     */
    public Dimension2D getDimensions() {
        return dimensions;
    }
}
