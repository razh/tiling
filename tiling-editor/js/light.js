var Light = function() {
  Shape.call( this );

  this.setWidth( 10 )
      .setHeight( 10 )
      .setVertices( [  0.5,  0.5,
                       0.5, -0.5,
                      -0.5, -0.5,
                      -0.5,  0.5 ] )
      .setEdges( [ 0, 1, 2, 3, 0 ] )
      .setColor( new Color( 255, 255, 255, 1.0 ) );

  this._z = 100;
  this._distance = 0;
};

Light.prototype = new Shape();
Light.prototype.constructor = Light;

// Fall-off distance in the Java code is not the same.
// Approximate difference in scales.
Light.scaleFactor = 10;

Light.prototype.draw = function( ctx ) {
  Shape.prototype.draw.call( this, ctx );

  ctx.save();
  ctx.translate( this.getX(), this.getY() );

  // Draw distance.
  ctx.beginPath();
  ctx.arc( 0, 0, this.getDistance() / Light.scaleFactor, 0, Math.PI * 2 );
  ctx.closePath();

  ctx.strokeStyle = this.getColor().toString();
  ctx.stroke();

  // Draw z (height).
  ctx.beginPath();
  ctx.moveTo( 0, 0 );
  ctx.lineTo( this.getZ(), 0 );
  ctx.lineTo( 0, this.getZ() );
  ctx.lineTo( -this.getZ(), 0 );
  ctx.lineTo( 0, -this.getZ() );
  ctx.lineTo( this.getZ(), 0 );
  ctx.closePath();

  if ( _editor.getBackgroundColor().getBrightness() < 186 ) {
    ctx.strokeStyle = 'rgba( 255, 255, 255, 1.0 )';
  } else {
    ctx.strokeStyle = 'rgba( 0, 0, 0, 1.0 )';
  }
  ctx.stroke();

  ctx.restore();
};

Light.prototype.createInspector = function( $id ) {
  if ( $id.length !== 0 ) {
    $id.empty();
  }

  // X.
  Form.createFloatForm({
      $id:    $id,
      object: this,
      name:   'x',
      getter: 'getX',
      setter: 'setX',
      min:    0,
      max:    _editor.WIDTH,
      step:   1,
      digits: 1
  });

  // Y.
  Form.createFloatForm({
    $id:    $id,
    object: this,
    name:   'y',
    getter: 'getY',
    setter: 'setY',
    min:    0,
    max:    _editor.HEIGHT,
    step:   1,
    digits: 1
  });

  // Z.
  Form.createIntegerForm({
    $id:    $id,
    object: this,
    name:   'z',
    getter: 'getZ',
    setter: 'setZ',
    min:    0,
    max:    Math.max( _editor.WIDTH, _editor.HEIGHT ),
    step:   1
  });

  // Color.
  Form.createColorForm({
    $id:    $id,
    object: this,
    getter: 'getColor'
  });

  // Distance.
  Form.createIntegerForm({
    $id:    $id,
    object: this,
    name:   'distance',
    getter: 'getDistance',
    setter: 'setDistance',
    min:    0,
    max:    Math.max( _editor.WIDTH, _editor.HEIGHT ) * Light.scaleFactor,
    step:   Light.scaleFactor
  });

  $id.find( ':input' ).on({
    focus: function() {
      _editor.setState( EditorState.TEXT_EDITING );
    },
    blur: function() {
      _editor.setState( EditorState.DEFAULT );
    }
  });
};

Light.prototype.getZ = function() {
  return this._z;
};

Light.prototype.setZ = function( z ) {
  this._z = z;
  return this;
};

Light.prototype.getDistance = function() {
  return this._distance;
};

Light.prototype.setDistance = function( distance ) {
  this._distance = distance;
  return this;
};

Light.prototype.fromJSON = function( json ) {
  var jsonObject = JSON.parse( json );

  var color = new Color().fromJSON( JSON.stringify( jsonObject.color ) );

  return this.setX( jsonObject.x || 0 )
             .setY( jsonObject.y || 0 )
             .setZ( jsonObject.z || 0 )
             .setColor( color )
             .setDistance( jsonObject.distance || 0 );
};

Light.prototype.toJSON = function() {
  var object = {};

  object.x        = this.getX();
  object.y        = this.getY();
  object.z        = this.getZ();
  object.color    = this.getColor().toJSON();
  object.distance = this.getDistance();

  return object;
};

Light.prototype.clone = function() {
  return new Light().setPosition( this.getX(), this.getY() )
                    .setColor( this.getColor() )
                    .setDistance( this.getDistance() );
};
