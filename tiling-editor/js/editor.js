$( function() {
  init();
});

window.requestAnimFrame = ( function() {
  return window.requestAnimationFrame       ||
         window.webkitRequestAnimationFrame ||
         window.mozRequestAnimationFrame    ||
         window.oRequestAnimationFrame      ||
         window.msRequestAnimationFrame     ||
         function( callback ) {
            window.setTimeout( callback, 1000 / 60 );
         };
})();

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

var Editor = function() {
  this._canvasContainer = $( '#canvas-container' );
  this._canvas = document.createElement( 'canvas' );
  this._ctx = this._canvas.getContext( '2d');

  this._canvasContainer.append( this._canvas );

  this.WIDTH = this._canvasContainer.width();
  this.HEIGHT = this._canvasContainer.height();

  this._canvas.width = this.WIDTH;
  this._canvas.height = this.HEIGHT;

  this._prevTime = Date.now();
  this._currTime = this._prevTime;

  this._shapes = [];

  this._translate = {
    x: 0,
    y: 0
  };

  this._testShape = new Shape();
  var ve = PolygonFactory.createHexagon();
  this._testShape.setWidth( 50 );
  this._testShape.setHeight( 50 );
  this._testShape.setVertices( ve.vertices );
  this._testShape.setEdges( ve.edges );
  this._testShape.setPosition( 50, 100 );
  this._testShape.setRotation( 10 * Math.PI / 180 );
  this._testShape.setColor( 0, 0, 120, 1.0 );
  console.log( this._testShape.getRadius() );
  this._shapes.push( this._testShape );

  this._running = true;

  this._user = new User();

  this._offset = {
    x: 0,
    y: 0
  };
};

Editor.prototype.tick = function() {
  this.update();
  this.draw();
};

Editor.prototype.update = function() {
  this._currTime = Date.now();
  var elapsedTime = this._currTime - this._prevTime;
  this._prevTime = this._currTime;

  this._testShape.rotate( 2.0 );
};

Editor.prototype.draw = function() {
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
