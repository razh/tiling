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
  console.log("hello");
  _editor = new Editor();
  loop();
}

function loop() {
  _editor.tick();
  requestAnimFrame( loop );
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
};

Editor.prototype.tick = function() {
  this.update();
  this.draw();
};

Editor.prototype.update = function() {
  this._currTime = Date.now();
  var elapsedTime = this._currTime - this._prevTime;
  this._prevTime = this._currTime;
};

Editor.prototype.draw = function() {
  this._ctx.clearRect( 0, 0, this.WIDTH, this.HEIGHT );

  this._ctx.fillStyle = 'rgba( 255, 0, 0, 1.0 );';
  this._ctx.fillRect( 0, 0, this.WIDTH, this.HEIGHT );
};

