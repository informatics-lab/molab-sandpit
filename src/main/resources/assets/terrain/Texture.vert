
#extension GL_ARB_texture_rectangle : enable

//my values passed in by the material definition
uniform sampler2DRect m_RawDepthMap;
uniform float m_MaxDepthValue;
uniform float m_MinDepthValue;
uniform float m_TerrainHeight;
uniform float m_TerrainWidth;

//gloabl attribute passed in technique
uniform mat4 g_WorldViewProjectionMatrix;
uniform mat4 g_WorldMatrix;
uniform mat4 g_ViewMatrix;
uniform mat4 g_ProjectionMatrix;
uniform mat4 g_ModelMatrix;

//attributes that change for each call
attribute vec3 inPosition;
attribute vec2 inTexCoord;
attribute vec3 inNormal;

//stuff that we want to pass over to the fragment shader
varying vec4 positionCoordinate;
varying vec2 textureCoordinate;
varying vec3 normal;

//test
varying vec3 myVar;

void main() {

    positionCoordinate = vec4(inPosition, 1.0);
    textureCoordinate = inTexCoord;
    normal = inNormal;

    //flips the texture
    //textureCoordinate = vec2(inTexCoord.s, 1.0 - inTexCoord.t);

//    vec3 displacementValue = texture2DRect( m_RawDepthMap, inTexCoord.xy ).xyz;
//    float displacementHeight = displacementValue.y;
//    myVar = vec3(positionCoordinate.x, displacementHeight, positionCoordinate.z);

//	displacedPosition = position + normalize( normal ) * df;
    if(positionCoordinate.x<1.0) {
        positionCoordinate.y = 5.0;
    }

    gl_Position = g_WorldViewProjectionMatrix * positionCoordinate;

}