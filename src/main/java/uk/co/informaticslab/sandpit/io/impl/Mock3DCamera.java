/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.informaticslab.sandpit.io.impl;

import uk.co.informaticslab.sandpit.domain.Dimension2D;
import uk.co.informaticslab.sandpit.io.Camera3D;

/**
 *
 * @author Tom
 */
public class Mock3DCamera implements Camera3D {

    @Override
    public Dimension2D getDepthMapDimensions() {
        return new Dimension2D(320,240);
    }

    @Override
    public Dimension2D getColorMapDimensions() {
        return new Dimension2D(320,240);
    }

    @Override
    public Dimension2D getIRMapDimensions() {
        return new Dimension2D(320,240);
    }

    @Override
    public short[] sampleDepthMap() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public short[] sampleIRMap() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int[] sampleColorMap() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
