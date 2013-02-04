var Pattern = function() {
  this._name = '';
  this._shapes = [];

  this._canvasArray = [];
  this._ctxArray = [];

  this._jsonData = null;
  if ( arguments.length !== 0 ) {
    this.fromURL( arguments[0] );
  }

  // Keeping track of this when we need to redraw.
  this._$id = null;
};

Pattern.prototype.fromURL = function( url ) {
  this._jsonData = (function() {
    var json = null;
    $.ajax({
      'async': false,
      'global': false,
      'url': url,
      'dataType': 'json',
      'success': function( data ) {
        json = data;
      }
    });
    return json;
  }) ();

  if ( this._jsonData !== null ) {
    this.fromJSON( JSON.stringify( this._jsonData ) );
  }
};

Pattern.prototype.fromJSON = function( json ) {
  var jsonObject = JSON.parse( json );

  this.setName( jsonObject.name || '' );
  this._shapes = [];
  var shape = null;
  for ( var i = 0, n = jsonObject.shapes.length; i < n; i++ ) {
    shape = new Shape().fromJSON( JSON.stringify( jsonObject.shapes[i] ) );
    this._shapes.push( shape );
  }

  return this;
};

Pattern.prototype.toJSON = function() {
  var object = {};

  object.name = this._name;
  object.shapes = this._shapes;

  return object;
};

Pattern.prototype.createInspector = function( $id ) {
  this._$id = $id;
  if ( $id.length !== 0 ) {
    $id.empty();
  }

  this._canvasArray = [];
  this._ctxArray = [];

  // Constructs string of canvas elements with count equal to this._shapes.length.
  var canvasStrings = new Array( this._shapes.length + 1 ).join( '<canvas/>' );
  $id.append( canvasStrings );

  this._canvasArray = $id.find( 'canvas' );

  // On click, clear selection and select clicked brush.
  var $canvasArray = this._canvasArray;
  var shapes = this._shapes;
  var pattern = this;
  $canvasArray.click(function() {
    $canvasArray.removeClass( 'selected' );

    var $this = $( this );
    var index = parseInt( $this.attr( 'id' )
                               .replace( 'pattern', '' ), 10 );
    _editor.setBrushByIndex( index );
    _editor.loadShapeInspector( shapes[ index ], true );
    _editor._inspectorPane.find( 'input' ).change(function() {
      pattern.drawShape( pattern._ctxArray[ index ],
                         index,
                         $canvasArray[ index ].width,
                         $canvasArray[ index ].height );
    });

    $this.addClass( 'selected' );
  });

  // Draw to all canvases.
  var canvas, ctx, shape = null;
  for ( i = 0, n = this._shapes.length; i < n; i++ ) {
    canvas = this._canvasArray[i];
    canvas.width = canvas.height;
    canvas.setAttribute( 'id', 'pattern' + i );

    ctx = canvas.getContext( '2d' );
    this._ctxArray.push( ctx );

    this.drawShape( ctx, i, canvas.width, canvas.height );
  }

  if ( this._shapes.length > 0 ) {
    $( this._canvasArray[0] ).addClass( 'selected' );
  }
};

Pattern.prototype.drawShape = function( ctx, shapeIndex, width, height ) {
  ctx.clearRect( 0, 0, width, height );
  ctx.save();

  shape = this.getShapes()[ shapeIndex ];
  ctx.scale( 1, -1 );
  ctx.translate( 1.5 * shape.getWidth() - shape.getX(),
                 -height + ( 1.5 * shape.getHeight() - shape.getY() ) );
  shape.draw( ctx );

  ctx.restore();
};

Pattern.prototype.getName = function() {
  return this._name;
};

Pattern.prototype.setName = function( name ) {
  this._name = name;
};

Pattern.prototype.getShapes = function() {
  return this._shapes;
};

Pattern.prototype.addShape = function( shape ) {
  this._shapes.push( shape );
  this.createInspector( this._$id );
};

Pattern.prototype.removeShape = function( shape ) {
  var index = this._shapes.indexOf( shape );
  this.removeShapeByIndex( index );
};

Pattern.prototype.removeShapeByIndex = function( index ) {
  if ( index !== -1 ) {
    this._shapes.splice( index, 1 )
    this.createInspector( this._$id );
  }
};
