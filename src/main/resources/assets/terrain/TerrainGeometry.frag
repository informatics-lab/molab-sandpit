
uniform sampler2D m_RawDepthMap;

varying vec2 textureCoordinate;
varying vec3 positionCoordinate;

void main() {

    vec4 color;

    color = texture2D(m_RawDepthMap, textureCoordinate);

    gl_FragColor = color;
}