var Geometry = (function() {
  return {
    createRegularPolygon: function( sides ) {
      var subdivAngle = -( Math.PI * 2 / sides );

      var vertices = [];
      var edges = [];

      for ( var i = 0; i < sides; i++ ) {
        vertices.push( Math.sin( i * subdivAngle ) );
        vertices.push( Math.cos( i * subdivAngle ) );

        edges.push(i);
      }

      edges.push(0);

      return {
        vertices: vertices,
        edges: edges
      };
    },

    createTriangle: function() {
      return Geometry.createRegularPolygon(3);
    },

    createTetragon: function() {
      return Geometry.createRegularPolygon(4);
    },

    createHexagon: function() {
      return Geometry.createRegularPolygon(6);
    },

    createOctagon: function() {
      return Geometry.createRegularPolygon(8);
    },

    createPyramid: function( sides ) {
      var subdivAngle = -( Math.PI * 2 / sides );

      var geometry = new THREE.Geometry();

      // Top vertex.
      geometry.vertices.push( new THREE.Vector3( 0, 0, 1 ) );

      for ( var i = 0; i < sides; i++ ) {
        geometry.vertices.push(
          new THREE.Vector3(
            Math.sin( i * subdivAngle ),
            Math.cos( i * subdivAngle ),
            0
          )
        );

        geometry.faces.push( new THREE.Face3( 0, i + 1, ( i + 1 ) % sides + 1 ) );
      }

      geometry.computeFaceNormals();
      geometry.computeBoundingSphere();

      return geometry;
    }
  };
}) ();
