$(
  function() {
   init();
  }
);

window.requestAnimFrame = (function() {
  return window.requestAnimationFrame       ||
         window.webkitRequestAnimationFrame ||
         window.mozRequestAnimationFrame    ||
         window.oRequestAnimationFrame      ||
         window.msRequestAnimationFrame     ||
         function( callback ) {
            window.setTimeout( callback, 1000 / 60 );
         };
}) ();

var _editor;

function init() {
  _editor = new Editor();

  _editor._canvas.addEventListener( 'mousedown', onMouseDown, null );
  _editor._canvas.addEventListener( 'mousemove', onMouseMove, null );
  _editor._canvas.addEventListener( 'mouseup', onMouseUp, null );

  document.addEventListener( 'keydown', onKeyDown, null );

  loop();
}

function loop() {
  if ( !_editor.isRunning() ) {
    return;
  }

  _editor.tick();
  requestAnimFrame( loop );
}

function quit() {
  _editor.stop();
}

var EditorState = {
  DEFAULT: 0,
  ADDING_SHAPE: 1,
  REMOVING_SHAPE: 2,
  DUPLICATING_SHAPE: 3
};

var Editor = function() {
  this._canvasContainer = $( '.canvas-container' );

  this._backgroundCanvas = document.createElement( 'canvas' );
  this._backgroundCtx = this._backgroundCanvas.getContext( '2d' );
  this._canvasContainer.append( this._backgroundCanvas );

  this._canvas = document.createElement( 'canvas' );
  this._ctx = this._canvas.getContext( '2d' );
  this._canvasContainer.append( this._canvas );

  this.WIDTH = this._canvasContainer.width();
  this.HEIGHT = this._canvasContainer.height();

  this._backgroundCanvas.width = this.WIDTH;
  this._backgroundCanvas.height = this.HEIGHT;

  this._canvas.width = this.WIDTH;
  this._canvas.height = this.HEIGHT;

  this._backgroundColor = new Color( 100, 100, 100, 1.0 );

  this._inspectorPane = $( '#inspector-pane' );
  this._patternPane = $( '#pattern-pane' );

  this._prevTime = Date.now();
  this._currTime = this._prevTime;

  this._shapes = [];

  this._translate = {
    x: 0,
    y: 0
  };

  this._offset = {
    x: 0,
    y: 0
  };

  var ve = PolygonFactory.createHexagon();
  console.log( ve )
  this._testShape = new Shape()
    .setPosition( 50, 100 )
    .setWidth( 50 )
    .setHeight( 50 )
    .setRotation( ( 10 * Math.PI / 180 ).toFixed(3) )
    .setVertices( ve.vertices )
    .setEdges( ve.edges )
    .setColor( 0, 0, 120, 0.2 );
  console.log( this._testShape.getRadius() );
  this._shapes.push( this._testShape );

  var ve2 = PolygonFactory.createTriangle();
  this._testShape2 = new Shape()
    .setPosition( 200, 120 )
    .setWidth( 50 )
    .setHeight( 50 )
    .setRotation( ( 10 * Math.PI / 180 ).toFixed(3) )
    .setVertices( ve2.vertices )
    .setEdges( ve2.edges )
    .setColor( 0, 0, 120, 0.2 );
  this._shapes.push( this._testShape2 );

  this._running = true;

  this._user = new User();
  this._pattern = new Pattern( './json/example.json' );
  this._pattern.createInspector( this._patternPane );

  // For adding shapes.
  this._brush = null;
  this._state = EditorState.DEFAULT;

  this.setBrushByIndex(0);
};

Editor.prototype.tick = function() {
  this.update();
  this.draw();
};

Editor.prototype.update = function() {
  this._currTime = Date.now();
  var elapsedTime = this._currTime - this._prevTime;
  this._prevTime = this._currTime;

  for ( var i = 0, n = this._shapes.length; i < n; i++ ) {
    this._shapes[i].update( elapsedTime );
  }
};

Editor.prototype.draw = function() {
  this._canvas.style.backgroundColor = this._backgroundColor.toHexString();

  this._ctx.clearRect( 0, 0, this.WIDTH, this.HEIGHT );

  this._ctx.save();
  this._ctx.setTransform( 1, 0, 0, 1, this._translate.x, this._translate.y );

  for ( var i = 0, n = this._shapes.length; i < n; i++ ) {
    this._shapes[i].draw( this._ctx );
  }

  this._ctx.restore();
};

Editor.prototype.hit = function( x, y ) {
  var hit = null;
  for ( var i = 0, n = this._shapes.length; i < n; i++ ) {
    hit = this._shapes[i].hit( x, y );
    if ( hit !== null ) {
      return hit;
    }
  }

  return null;
};

Editor.prototype.isRunning = function() {
  return this._running;
};

Editor.prototype.stop = function() {
  this._running = false;
};

Editor.prototype.getUser = function() {
  return this._user;
};

Editor.prototype.loadShapeInspector = function( shape ) {
  this._inspectorPane.empty();

  shape.createInspector( this._inspectorPane );
};

Editor.prototype.select = function( shape ) {
  this._user.setSelected( shape );

  if ( shape !== null ) {
    this.loadShapeInspector( shape );
  }
};

Editor.prototype.getState = function() {
  return this._state;
};

Editor.prototype.setState = function( state ) {
  this._state = state;
};

Editor.prototype.getShapes = function() {
  return this._shapes;
};

Editor.prototype.addShape = function( shape ) {
  this._shapes.push( shape );
};

Editor.prototype.removeShape = function( shape ) {
  this._shapes.splice( this._shapes.lastIndexOf( shape ), 1 );
};

Editor.prototype.getOffsetX = function() {
  return this.getOffset().x;
};

Editor.prototype.getOffsetY = function() {
  return this.getOffset().y;
};

Editor.prototype.getOffset = function() {
  return this._offset;
};

Editor.prototype.getBrush = function() {
  return this._brush;
};

Editor.prototype.setBrushByIndex = function( brushIndex ) {
  this._brush = this._pattern.getShapes()[ brushIndex ];
};


/*
  User.
*/
var User = function() {
  this._selected = null;
};

User.prototype.getSelected = function() {
  return this._selected;
};

User.prototype.setSelected = function( selected ) {
  this._selected = selected;
};

User.prototype.hasSelected = function() {
  return this._selected !== undefined && this._selected !== null;
};
