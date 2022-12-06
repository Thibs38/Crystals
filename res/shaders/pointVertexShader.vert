#version 400 core

const int MAX_CRYSTALS = 256;

struct Crystal
{
  vec4 color;
  float bloom;
};

in vec2 position;
in int crystal;
uniform mat4 uProjectionViewMatrix;
uniform highp float uScale;
uniform vec3 uPosition;
uniform Crystal uCrystals[MAX_CRYSTALS];

out vec3 oPointColor;
out float oBloom;
flat out vec3 oPosition;
out vec3 vPosition;


void main(void)
{
  oBloom = uCrystals[crystal].bloom;

  vec3 worldPosition = vec3(position.x * uScale + uPosition.x, position.y * uScale + uPosition.y, uPosition.z-1/(oBloom+1));

  gl_Position = uProjectionViewMatrix * vec4(worldPosition,1.0);
  vPosition = worldPosition;
  oPosition = worldPosition;
  gl_PointSize = uScale+10.0;
  oPointColor = uCrystals[crystal].color.xyz;
  //vec3(color>>24 & 0xFF / 255, color>>16 & 0xFF / 255, color>>8 & 0xFF / 255);
}