varying vec3 normal;

//test
varying vec4 positionCoordinate;
varying vec2 textureCoordinate;

void main(){

   vec4 color;
   color = vec4((normal * vec3(0.5)) + vec3(0.5), 1.0);

   if (positionCoordinate.x <= 1.0 || positionCoordinate.z <= 1.0) {
      color = vec4(0.0,0.0,1.0,1.0);
   }


   gl_FragColor = color;
}