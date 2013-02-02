var Shape = function() {
  this._position = {
    x: 0,
    y: 0
  };

  this._width = 0;
  this._height = 0;
  this._rotation = 0.0;

  this._numSides = 0;
  this._vertices = [];
  this._edges = [];

  this._color = new Color();

  // For collision.
  this._radius = 0;
};

Shape.prototype.update = function( elapsedTime ) {};

Shape.prototype.draw = function( ctx ) {
  ctx.save();
  ctx.translate( this.getX(), this.getY() );
  ctx.rotate( -this.getRotation() );
  ctx.scale( this.getWidth(), this.getHeight() );

  ctx.beginPath();

  var x = this._vertices[ 2 * this._edges[0] ],
      y = this._vertices[ 2 * this._edges[0] + 1 ];
  ctx.moveTo( x, y );

  for ( var i = 1, n = this._edges.length; i < n; i++ ) {
    x = this._vertices[ 2 * this._edges[i] ];
    y = this._vertices[ 2 * this._edges[i] + 1 ];

    ctx.lineTo( x, y );
  }

  ctx.closePath();

  ctx.fillStyle = this.getColor().toString();
  ctx.fill();

  ctx.restore();
};

Shape.prototype.hit = function( x, y ) {
  if ( this.contains( x, y ) ) {
    return this;
  }

  return null;
};

Shape.prototype.contains = function( x, y ) {
  var point = this.worldToLocalCoordinates( x, y );
  x = point.x;
  y = point.y;

  var numVertices = this._vertices.length / 2;
  var contains = false;
  var xi, yi, xj, yj;
  var i, j;
  for ( i = 0, j = numVertices - 1; i < numVertices; j = i++ ) {
    xi = this._vertices[ 2 * i ];
    yi = this._vertices[ 2 * i + 1 ];
    xj = this._vertices[ 2 * j ];
    yj = this._vertices[ 2 * j + 1 ];

    if ( ( ( yi > y ) != ( yj > y ) ) &&
         ( x < ( xj - xi ) * ( y - yi ) / ( yj - yi ) + xi ) ) {
      contains = !contains;
    }
  }

  return contains;
};

Shape.prototype.getX = function() {
  return this.getPosition().x;
};

Shape.prototype.setX = function( x ) {
  this._position.x = x;
  return this;
};

Shape.prototype.getY = function() {
  return this.getPosition().y;
};

Shape.prototype.setY = function( y ) {
  this._position.y = y;
  return this;
};

Shape.prototype.getPosition = function() {
  return this._position;
};

Shape.prototype.setPosition = function() {
  if ( arguments.length === 1 ) {
    this.setX( arguments[0].x );
    this.setY( arguments[0].y );
  } else if ( arguments.length === 2 ) {
    this.setX( arguments[0] );
    this.setY( arguments[1] );
  }

  return this;
};

Shape.prototype.translateX = function( translateX ) {
  this.setX( this.getX() + translateX );
  return this;
};

Shape.prototype.translateY = function( translateY ) {
  this.setY( this.getY() + translateY );
  return this;
};

Shape.prototype.translate = function() {
  if ( arguments.length === 1 ) {
    this.translateX( arguments[0].x );
    this.translateY( arguments[0].y );
  } else if ( arguments.length === 2 ) {
    this.translateX( arguments[0] );
    this.translateY( arguments[1] );
  }

  return this;
};

Shape.prototype.getWidth = function() {
  return this._width;
};

Shape.prototype.setWidth = function( width ) {
  this._width = width;
  return this;
};

Shape.prototype.getHeight = function() {
  return this._height;
};

Shape.prototype.setHeight = function( height ) {
  this._height = height;
  return this;
};

Shape.prototype.scale = function() {
  if ( arguments.length === 1 ) {
    this.setWidth( this.getWidth() * arguments[0] );
    this.setHeight( this.getHeight() * arguments[0] );
  } else if ( arguments.length === 2 ) {
    this.setWidth( this.getWidth() * arguments[0] );
    this.setHeight( this.getHeight() * arguments[1] );
  }

  return this;
};

Shape.prototype.getRotation = function() {
  return this._rotation;
};

Shape.prototype.setRotation = function( rotation ) {
  this._rotation = rotation;
  return this;
};

Shape.prototype.rotate = function( angle ) {
  this._rotation -= angle;
  return this;
};

Shape.prototype.getRadius = function() {
  return this._radius;
};

Shape.prototype.setRadius = function( radius ) {
  this._radius = radius;
  return this;
};

Shape.prototype.getColor = function() {
  return this._color;
};

Shape.prototype.setColor = function() {
  this.getColor().set.apply( this.getColor(), arguments );
  return this;
};

Shape.prototype.getNumSides = function() {
  return this._numSides;
};

Shape.prototype.setNumSides = function( numSides ) {
  this._numSides = numSides;
  return this;
};

Shape.prototype.getVertices = function() {
  return this._vertices;
};

Shape.prototype.setVertices = function( vertices ) {
  this._vertices = vertices;
  return this;
};

Shape.prototype.getEdges = function() {
  return this._edges;
};

Shape.prototype.setEdges = function( edges ) {
  this._edges = edges;
  return this;
};

Shape.prototype.calculateRadius = function() {
  var max = {
    x: 0,
    y: 0
  };

  var width = this.getWidth(),
      height = this.getHeight();
  var x, y;
  var distanceSquared = 0;

  for ( var i = this._edges.length - 1; i >= 0; i-- ) {
    x = width  * this._vertices[ 2 * this._edges[i] ];
    y = height * this._vertices[ 2 * this._edges[i] + 1 ];

    distanceSquared = Math.max( distanceSquared, x * x + y * y );
  }

  return this.setRadius( Math.sqrt( distanceSquared ) );
}

Shape.prototype.createInspector = function( $id ) {
  if ( $id.length !== 0 ) {
    $id.empty();
  }

  // X.
  Form.createIntegerForm({
    $id:    $id,
    object: this,
    name:   'x',
    getter: 'getX',
    setter: 'setX',
    min:    0,
    max:    _editor.WIDTH,
    step:   1
  });

  // Y.
  Form.createIntegerForm({
    $id:    $id,
    object: this,
    name:   'y',
    getter: 'getY',
    setter: 'setY',
    min:    0,
    max:    _editor.HEIGHT,
    step:   1
  });

  // Width.
  Form.createFloatForm({
    $id:    $id,
    object: this,
    name:   'width',
    getter: 'getWidth',
    setter: 'setWidth',
    min:    0,
    max:    _editor.WIDTH,
    step:   0.01
  });

  // Height.
  Form.createFloatForm({
    $id:    $id,
    object: this,
    name:   'height',
    getter: 'getHeight',
    setter: 'setHeight',
    min:    0,
    max:    _editor.HEIGHT,
    step:   0.01
  });

  // Color.
  Form.createColorForm({
    $id:    $id,
    object: this,
    getter: 'getColor',
  });

  // Rotation.
  Form.createFloatForm({
    $id:    $id,
    object: this,
    name:   'rotation',
    getter: 'getRotation',
    setter: 'setRotation',
    min:    -2 * Math.PI,
    max:    2 * Math.PI,
    step:   0.001,
    digits: 3
  });
};

Shape.prototype.fromJSON = function( json ) {
  var jsonObject = JSON.parse( json );

  var sides    = jsonObject.sides || 0,
      vertices = jsonObject.vertices,
      edges    = jsonObject.edges;

  if ( vertices === undefined || edges === undefined ) {
    var geometry = Geometry.createRegularPolygon( sides );
    vertices = geometry.vertices;
    edges = geometry.edges;
  }

  var color = new Color();
  color.fromJSON( JSON.stringify( jsonObject.color ) );

  return this.setX( jsonObject.x || 0 )
             .setY( jsonObject.y || 0 )
             .setWidth( jsonObject.width || 1 )
             .setHeight( jsonObject.height || 1 )
             .setRotation( jsonObject.rotation || 0 )
             .setNumSides( sides )
             .setVertices( vertices )
             .setEdges( edges )
             .calculateRadius()
             .setColor( color );
};

Shape.prototype.toJSON = function() {
  var object = {};

  object.x        = this.getX();
  object.y        = this.getY();
  object.width    = this.getWidth();
  object.height   = this.getHeight();
  object.rotation = this.getRotation();

  if ( this.getNumSides() <= 0 ) {
    object.vertices = this.getVertices();
    object.edges    = this.getEdges();
  } else {
    object.sides = this.getNumSides();
  }

  object.color = this.getColor().toJSON();

  return object;
};

Shape.prototype.clone = function() {
  return new Shape().setPosition( this.getPosition() )
                    .setWidth( this.getWidth() )
                    .setHeight( this.getHeight() )
                    .setRotation( this.getRotation() )
                    .setVertices( this.getVertices() )
                    .setEdges( this.getEdges() )
                    .calculateRadius()
                    .setColor( this.getColor() );
};

// May be optimized (calculate localToWorldCoordinates for self only once).
Shape.prototype.snap = function( shapes ) {
  var localVertices = this.getVertices(),
      localNumVertices = localVertices.length / 2,
      localVertex = null;

  var vertices = [],
      numVertices = 0,
      transformedVertex = null;

  var i, j, k;

  // Transform vertices from local to world coordinates.
  var transformedLocalVertices = [];
  for ( k = 0; k < localNumVertices; k++ ) {
    localVertex = this.localToWorldCoordinates( localVertices[ 2 * k ],
                                                           localVertices[ 2 * k + 1 ] );
    transformedLocalVertices.push( localVertex.x  );
    transformedLocalVertices.push( localVertex.y );
  }

  // Indices of nearest shape, that shape's nearest vertex, and this shape's nearest vertex, respectively.
  var imin, jmin, kmin;
  var shape;

  var minDistanceSquared = Number.MAX_VALUE;
  var distanceSquared;
  for ( i = shapes.length - 1; i >= 0; i-- ) {
    shape = shapes[i];
    if ( this === shape ) {
      continue;
    }

    // Transform the shape's vertices to world coords.
    vertices = shape.getVertices();
    numVertices = vertices.length / 2;
    for ( j = 0; j < numVertices; j++ ) {
      transformedVertex = shape.localToWorldCoordinates( vertices[ 2 * j ],
                                                         vertices[ 2 * j + 1 ] );
      // Get distance to every local vertex.
      for ( k = 0; k < localNumVertices; k++ ) {
        localVertex = {
          x: transformedLocalVertices[ 2 * k ],
          y: transformedLocalVertices[ 2 * k + 1 ]
        };
        distanceSquared = ( transformedVertex.x - localVertex.x ) *
                          ( transformedVertex.x - localVertex.x ) +
                          ( transformedVertex.y - localVertex.y ) *
                          ( transformedVertex.y - localVertex.y );

        // Set new minDistance and indices of min.
        if ( distanceSquared < minDistanceSquared ) {
          imin = i;
          jmin = j;
          kmin = k;

          minDistanceSquared = distanceSquared;
        }
      }
    }
  }

  // Compare distance to snapping radius.
  if ( minDistanceSquared < _editor.getSnappingRadius() ) {
    var nearestShape = shapes[ imin ];
    var nearestVertex = nearestShape.localToWorldCoordinates(
      nearestShape.getVertices()[ 2 * jmin ],
      nearestShape.getVertices()[ 2 * jmin + 1 ]
    );
    var localVertex = {
      x: transformedLocalVertices[ 2 * kmin ],
      y: transformedLocalVertices[ 2 * kmin + 1 ]
    };

    var dx = nearestVertex.x - localVertex.x,
        dy = nearestVertex.y - localVertex.y;

    this.translate( dx, dy );
  }
}

Shape.prototype.worldToLocalCoordinates = function( x, y ) {
  // Translate.
  x -= this.getX();
  y -= this.getY();

  // Rotate.
  var rotation = this.getRotation();
  if ( rotation !== 0 ) {
    var cos = Math.cos( rotation ),
        sin = Math.sin( rotation );

    var rx = cos * x - sin * y,
        ry = sin * x + cos * y;

    x = rx;
    y = ry;
  }

  // Scale.
  x /= this.getWidth();
  y /= this.getHeight();

  return {
    x: x,
    y: y
  };
};

Shape.prototype.localToWorldCoordinates = function( x, y ) {
  // Scale.
  x *= this.getWidth();
  y *= this.getHeight();

  // Rotate.
  var rotation = this.getRotation();
  if ( rotation !== 0 ) {
    var cos = Math.cos( rotation ),
        sin = Math.sin( rotation );

    var rx =  cos * x + sin * y,
        ry = -sin * x + cos * y;

    x = rx;
    y = ry;
  }

  // Translate.
  x += this.getX();
  y += this.getY();

  return {
    x: x,
    y: y
  };
};

/*
  Color
*/
var Color = function() {
  this._red = 0;
  this._green = 0;
  this._blue = 0;
  this._alpha = 0.0;

  if ( arguments.length !== 0 ) {
    this.set.apply( this, arguments );
  }
};

Color.prototype.set = function() {
  if ( arguments.length === 1 ) {
    this.setRed( arguments[0].getRed() );
    this.setGreen( arguments[0].getGreen() );
    this.setBlue( arguments[0].getBlue() );
    this.setAlpha( arguments[0].getAlpha() );
  } else if ( arguments.length === 4 ) {
    this.setRed( arguments[0] );
    this.setGreen( arguments[1] );
    this.setBlue( arguments[2] );
    this.setAlpha( arguments[3] );
  }
};

Color.prototype.getRed = function() {
  return this._red;
};

Color.prototype.setRed = function( red ) {
  this._red = red;
  return this;
};

Color.prototype.getGreen = function() {
  return this._green;
};

Color.prototype.setGreen = function( green ) {
  this._green = green;
  return this;
};

Color.prototype.getBlue = function() {
  return this._blue;
};

Color.prototype.setBlue = function( blue ) {
  this._blue = blue;
  return this;
};

Color.prototype.getAlpha = function() {
  return this._alpha;
};

Color.prototype.setAlpha = function( alpha ) {
  this._alpha = alpha;
  return this;
};

Color.prototype.toString = function() {
  return 'rgba( ' + ( ( 0.5 + this.getRed() )   << 0 ) +
         ', '     + ( ( 0.5 + this.getGreen() ) << 0 ) +
         ', '     + ( ( 0.5 + this.getBlue() )  << 0 ) +
         ', '     + this.getAlpha() + ' )';
};

Color.prototype.toHexString = function() {
    return "#" +
           ( ( 1 << 24 ) +
           ( ( ( 0.5 + this.getRed() )   << 0 ) << 16 ) +
           ( ( ( 0.5 + this.getGreen() ) << 0 ) << 8 ) +
           ( ( 0.5 + this.getBlue() )    << 0 ) ).toString( 16 ).slice(1);
}

Color.prototype.fromJSON = function( json ) {
  var jsonObject = JSON.parse( json );
  this.setRed(   jsonObject.red   || 0 )
      .setGreen( jsonObject.green || 0 )
      .setBlue(  jsonObject.blue  || 0 )
      .setAlpha( jsonObject.alpha || 1.0 );
};

Color.prototype.toJSON = function() {
  var object = {};

  object.red   = this.getRed();
  object.green = this.getGreen();
  object.blue  = this.getBlue();
  object.alpha = this.getAlpha();

  return object;
};
