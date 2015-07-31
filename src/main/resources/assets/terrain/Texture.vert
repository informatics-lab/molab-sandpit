
//#extension GL_ARB_texture_rectangle : enable

//my values passed in by the material definition
uniform sampler2D m_ColorMap;


//gloabl attribute passed in technique
uniform mat4 g_WorldViewProjectionMatrix;
//uniform mat4 g_WorldMatrix;
//uniform mat4 g_ViewMatrix;
//uniform mat4 g_ProjectionMatrix;
//uniform mat4 g_ModelMatrix;

//attributes that change for each call
attribute vec3 inPosition;
attribute vec2 inTexCoord;
attribute vec3 inNormal;

//stuff that we want to pass over to the fragment shader
varying vec4 positionCoordinate;
//varying vec2 textureCoordinate;
//varying vec3 normal;

void main() {

    positionCoordinate = vec4(inPosition, 1.0);

    gl_Position = g_WorldViewProjectionMatrix * positionCoordinate;

}