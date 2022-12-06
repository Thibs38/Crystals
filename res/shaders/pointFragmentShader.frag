#version 400 core

//https://stats.stackexchange.com/questions/520025/how-to-find-parameters-of-sigmoid-function-given-two-thresholds

const float bloomThresholdMin = 0.01;
const float bloomThresholdMax = 0.99;

const float bloomL1 = log(1.0 / bloomThresholdMin - 1.0);
const float bloomL2 = log(1.0 / bloomThresholdMax - 1.0);
const float bloomD = 1.0 / (bloomL2 - bloomL1);

in vec3 oPointColor;
in float oBloom;
flat in vec3 oPosition;
in vec3 vPosition;

uniform float uScale;

out vec3 out_color;

float sigmoid(float x, float a, float b){
      return 1.0 / (1.0 + exp(-((x - bloomD * (a * bloomL2 - b * bloomL1)) / (bloomD * (b - a)))));
}

void main(void)
{
      float sigmx = sigmoid((abs(gl_FragCoord.x - oPosition.x)*2), uScale,uScale + oBloom+0.0001);
      float sigmy = sigmoid((abs(gl_FragCoord.y - oPosition.y)*2), uScale,uScale + oBloom+0.0001);
      out_color = oPointColor*(1-sqrt(sigmx*sigmx + sigmy*sigmy));//*sigmx*sigmy; //0.5);//min(sigmx,sigmy));

}