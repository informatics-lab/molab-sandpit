package uk.co.informaticslab.sandpit.domain;

/**
 * Created by tom on 26/06/2015.
 */
public class DepthMap {

    private final Dimension2D dimensions;
    private final short min;
    private final short max;
    private final short[] sample;
    private final short[] rawSample;


    public DepthMap(Dimension2D dimensions, short[] sample, short... untrustedValues) {
        short min = Short.MAX_VALUE;
        short max = Short.MIN_VALUE;

        short[] trusted = new short[sample.length];
        int i = 0;
        for (short s : sample) {
            for (short untrustedValue : untrustedValues) {
                if (s == untrustedValue) {
                    s = 0;
                }
            }
            trusted[i++] = s;
            if (s < min && s != 0) {
                min = s;
            }
            if (s > max) {
                max = s;
            }
        }
        this.min = min;
        this.max = max;
        this.rawSample = sample;
        this.sample = trusted;
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
    public short[] getSample() {
        return sample;
    }

    /**
     * @return the raw data
     */
    public short[] getRawSample() {
        return rawSample;
    }

    /**
     * @return the 2D dimensions
     */
    public Dimension2D getDimensions() {
        return dimensions;
    }
}
