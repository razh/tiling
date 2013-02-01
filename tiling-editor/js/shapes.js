var Shape = function() {
  this._position = {
    x: 0,
    y: 0
  };

  this._vertices = [];
  this._edges = [];

  this._color = new Color();

  this._width = 0;
  this._height = 0;

  this._rotation = 0.0;

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
  // Translate and scale.
  x = ( x - this.getX() ) / this.getWidth();
  y = ( y - this.getY() ) / this.getHeight();

  var distance = x * x + y * y;
  if ( distance > this.getRadius() ) {
    return false;
  }

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
  }
  else if ( arguments.length === 2 ) {
    this.setX( arguments[0] );
    this.setY( arguments[1] );
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

  var max = {
    x: 0,
    y: 0
  };

  var width = this.getWidth(),
      height = this.getHeight();
  var x, y;
  var distanceSquared = 0;

  for ( var i = 0, n = edges.length; i < n; i++ ) {
    x = width  * this._vertices[ 2 * this._edges[i] ];
    y = height * this._vertices[ 2 * this._edges[i] + 1 ];

    distanceSquared = Math.max( distanceSquared, x * x + y * y );
  }

  this.setRadius( Math.sqrt( distanceSquared ) );

  return this;
};

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

  var x        = jsonObject.x,
      y        = jsonObject.y,
      width    = jsonObject.width,
      height   = jsonObject.height
      rotation = jsonObject.rotation;

  var vertices = jsonObject.vertices,
      edges    = jsonObject.edges;

  var color = new Color();
  color.fromJSON( JSON.stringify( jsonObject.color ) );

  return this.setPosition( x, y )
             .setWidth( width )
             .setHeight( height )
             .setRotation( rotation )
             .setVertices( vertices )
             .setEdges( edges )
             .setColor( color );
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
  }
  else if ( arguments.length === 4 ) {
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
  this.setRed( jsonObject.red )
      .setGreen( jsonObject.green )
      .setBlue( jsonObject.blue )
      .setAlpha( jsonObject.alpha );

};
