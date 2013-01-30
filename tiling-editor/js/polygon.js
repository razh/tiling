var PolygonFactory = (function() {
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
      return PolygonFactory.createRegularPolygon(3);
    },

    createTetragon: function() {
      return PolygonFactory.createRegularPolygon(4);
    },

    createHexagon: function() {
      return PolygonFactory.createRegularPolygon(6);
    },

    createOctagon: function() {
      return PolygonFactory.createRegularPolygon(8);
    }
  };
}) ();
