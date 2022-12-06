#version 400 core


in vec2 position;
in float value;
uniform mat4 uProjectionViewMatrix;
uniform highp float uScale;
uniform vec3 uPosition;
uniform int uGradientMin;
uniform vec3 uGradientValueMin;
uniform int uGradientMax;
uniform vec3 uGradientValueMax;

out vec3 oPointColor;
out float oBloom;
out vec3 oPosition;
varying vec3 vPosition;



void main(void)
{
    vec3 worldPosition = vec3(position.x * uScale + uPosition.x, position.y * uScale + uPosition.y, uPosition.z);

    gl_Position = uProjectionViewMatrix * vec4(worldPosition,1.0);
    vPosition = worldPosition;
    oPosition = worldPosition;
    gl_PointSize = uScale+10.0;
    vec3 c = uGradientValueMax-uGradientValueMin;
    float g = uGradientMax-uGradientMin;
    oPointColor = vec3(c.x / g * value + uGradientMin/g, c.y / g * value + uGradientMin/g, c.z / g * value + uGradientMin/g);
    oPointColor = mix(uGradientValueMin,uGradientValueMax, value/g);
    //vec3(color>>24 & 0xFF / 255, color>>16 & 0xFF / 255, color>>8 & 0xFF / 255);
}