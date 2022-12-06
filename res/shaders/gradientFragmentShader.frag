#version 400 core

//https://stats.stackexchange.com/questions/520025/how-to-find-parameters-of-sigmoid-function-given-two-thresholds



in vec3 oPointColor;
in float oBloom;
in vec3 oPosition;
varying vec3 vPosition;

uniform float uScale;

out vec3 out_color;



void main(void)
{
    out_color = oPointColor; //0.5);//min(sigmx,sigmy));

}