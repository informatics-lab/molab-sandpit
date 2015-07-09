uniform mat4 g_WorldViewProjectionMatrix;

attribute vec3 inPosition;
attribute vec2 inTexCoord;

varying vec3 positionCoordinate;
varying vec2 textureCoordinate;

void main() {

    positionCoordinate = inPosition;

    //flips the texture
    textureCoordinate = vec2(inTexCoord.s, 1.0 - inTexCoord.t);;

    gl_Position = g_WorldViewProjectionMatrix * vec4(inPosition, 1.0);

}