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

Shape.prototype.getX = function() {
  return this.getPosition().x;
};

Shape.prototype.setX = function( x ) {
  this._position.x = x;
};

Shape.prototype.getY = function() {
  return this.getPosition().y;
};

Shape.prototype.setY = function( y ) {
  this._position.y = y;
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
};

Shape.prototype.getWidth = function() {
  return this._width;
};

Shape.prototype.setWidth = function( width ) {
  this._width = width;
};

Shape.prototype.getHeight = function() {
  return this._height;
};

Shape.prototype.setHeight = function( height ) {
  this._height = height;
};

Shape.prototype.getRotation = function() {
  return this._rotation;
};

Shape.prototype.setRotation = function( rotation ) {
  this._rotation = rotation;
};

Shape.prototype.rotate = function( angle ) {
  this._rotation -= angle;
};

Shape.prototype.getColor = function() {
  return this._color;
};

Shape.prototype.setColor = function() {
  this.getColor().set.apply( this.getColor(), arguments );
};

Shape.prototype.getVertices = function() {
  return this._vertices;
};

Shape.prototype.setVertices = function( vertices ) {
  this._vertices = vertices;
};

Shape.prototype.getEdges = function() {
  return this._edges;
};

Shape.prototype.setEdges = function( edges ) {
  this._edges = edges;
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
};

Color.prototype.getGreen = function() {
  return this._green;
};

Color.prototype.setGreen = function( green ) {
  this._green = green;
};

Color.prototype.getBlue = function() {
  return this._blue;
};

Color.prototype.setBlue = function( blue ) {
  this._blue = blue;
};

Color.prototype.getAlpha = function() {
  return this._alpha;
};

Color.prototype.setAlpha = function( alpha ) {
  this._alpha = alpha;
};

Color.prototype.toString = function() {
  return 'rgba( ' + ( ( 0.5 + this.getRed() )   << 0 ) +
         ', '     + ( ( 0.5 + this.getGreen() ) << 0 ) +
         ','      + ( ( 0.5 + this.getBlue() )  << 0 ) +
         ','      + this.getAlpha() + ' )';
};
