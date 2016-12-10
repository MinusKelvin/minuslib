#version 330 core

in vec4 c;
in vec2 texcoord;

uniform sampler2D tex;

out vec4 color;

void main() {
	color = c * texture(tex, texcoord);
}
