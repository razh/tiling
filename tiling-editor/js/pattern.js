var Pattern = function( jsonURL ) {
  jsonURL = jsonURL || '';

  var jsonData = (function() {
    var json = null;
    $.ajax({
      'async': false,
      'global': false,
      'url': jsonURL,
      'dataType': 'json',
      'success': function( data ) {
        json = data;
      }
    });
    return json;
  }) ();

  this._name = '';
  this._shapes = [];

  this._canvasArray = [];
  this._ctxArray = [];

  if ( jsonData !== null ) {
    this.fromJSON( jsonData );
  }
};

Pattern.prototype.fromJSON = function( json ) {
  this._name = json.name || '';

  this._shapes = [];
  var shape = null;
  for ( var i = 0, n = json.shapes.length; i < n; i++ ) {
    shape = new Shape();
    shape.fromJSON( JSON.stringify( json.shapes[i] ) );
    this._shapes.push( shape );
  }
};

Pattern.prototype.createInspector = function( $id ) {
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
  $canvasArray.click(function() {
    $canvasArray.removeClass( 'selected' );

    var $this = $( this );
    var index = parseInt( $this.attr( 'id' )
                               .replace( 'pattern', '' ), 10 );
    _editor.setBrushByIndex( index );

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

    shape = this._shapes[i];
    ctx.translate( 1.5 * shape.getWidth() - shape.getX(),
                   1.5 * shape.getHeight() - shape.getY() );
    shape.draw( ctx );
  }
};

Pattern.prototype.getShapes = function() {
  return this._shapes;
};
