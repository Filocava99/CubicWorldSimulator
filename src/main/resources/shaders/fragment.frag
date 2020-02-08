#version 330

in  vec2 outTexCoord;
out vec4 fragColor;

//Will hold the value of the texture unit that we want to work with
uniform sampler2D texture_sampler;

void main()
{
    fragColor = texture(texture_sampler, outTexCoord);
}