/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.informaticslab.sandpit.io.impl;

import uk.co.informaticslab.sandpit.domain.Dimension2D;
import uk.co.informaticslab.sandpit.io.Camera3D;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tom
 */
public class Mock3DCamera implements Camera3D {

    @Override
    public Dimension2D getDepthMapDimensions() {
        return new Dimension2D(320, 240);
    }

    @Override
    public short[] sampleDepthMap() {

        List<short[]> rowValues = new ArrayList<>();
        int totalLength = 0;

        File dataFile = new File("height-map-2.dat");
        try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {

            for (String row = reader.readLine(); row != null; row = reader.readLine()) {

                String[] strings = row.split(",");
                int rowLength = strings.length;
                totalLength += rowLength;

                short[] values = new short[rowLength];
                for (int i = 0; i < rowLength; i++) {
                    values[i] = Short.parseShort(strings[i]);
                }

                rowValues.add(values);
            }

        } catch (IOException e) {
            throw new RuntimeException(e); // TODO better error handling
        }

        int i = 0;
        short[] depthMap = new short[totalLength];
        for (short[] rowData : rowValues) {
            for (short value : rowData) {
                depthMap[i++] = value;
            }
        }
        return depthMap;
    }

}
