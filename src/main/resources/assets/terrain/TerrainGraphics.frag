
varying vec3 positionCoordinate;

void main(){

    float reMapped = positionCoordinate.y / 255.0;
    vec4 color;
    float modNum = mod(reMapped,0.1);
    if ((modNum < 0.005) && (reMapped != 0.0)) {
        float gScaleValue = 1.0 - (200.0 * modNum);
        color = vec4(vec3(gScaleValue), 1.0);
    } else {
        color = vec4(vec3(reMapped), 1.0);
    }
    gl_FragColor = color;
}