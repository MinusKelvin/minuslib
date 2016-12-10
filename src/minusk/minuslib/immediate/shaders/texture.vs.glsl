#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec4 color;
layout(location = 2) in vec2 tex;

uniform mat4 proj;

out vec4 c;
out vec2 texcoord;

void main() {
	gl_Position = proj * vec4(position, 1.0);
	c = color;
	texcoord = tex;
}
