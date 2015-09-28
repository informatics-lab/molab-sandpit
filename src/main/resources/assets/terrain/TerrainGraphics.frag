
uniform sampler2D m_ColorMap;

varying vec4 positionCoordinate;

void main() {

    vec4 color;

    float reMapped = positionCoordinate.z / (255.0/4.0);

    float modNum = mod(reMapped,(1.0/50.0));
//    if ((modNum < 0.0005)) {
        //point is at a defined interval so is a contour line
//        float gScaleValue = 1.0 - (200.0 * modNum);
//        color = vec4(vec3(0.0,0.0,0.0), 1.0);
//    } else {
        //get height color from texture
        color = texture2D(m_ColorMap, vec2(reMapped,reMapped));
 //   }
    gl_FragColor = color;
}