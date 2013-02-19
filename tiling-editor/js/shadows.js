var _shadowUniforms = {
  shadowColor: {
    type: 'c',
    value: new THREE.Color()
  },
  shadowAlpha: {
    type: 'f',
    value: 0
  },
  shadowOffset: {
    type: 'uVec2',
    value: new THREE.Vector2( 0, 0 )
  }
};

var _shadowShader = new THREE.ShaderMaterial({
  uniforms: _shadowUniforms,

  vertexShader:
    "uniform vec2 shadowOffset;\n" +
    "\n" +
    "void main()\n" +
    "{\n" +
    "  gl_Position = modelMatrix * vec4(position, 1.0);\n" +
    "  gl_Position.xy += shadowOffset;\n" +
    "  gl_Position = projectionMatrix * viewMatrix * gl_Position;\n" +
    "}",

  fragmentShader:
    "#ifdef GL_ES\n" +
    "precision mediump float;\n" +
    "#endif\n" +
    "uniform vec3 shadowColor;\n" +
    "uniform float shadowAlpha;\n" +
    "\n" +
    "void main()\n" +
    "{\n" +
    "  gl_FragColor = vec4(shadowColor, shadowAlpha);\n" +
    "}"
});
