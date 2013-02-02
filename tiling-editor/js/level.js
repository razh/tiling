var Level = function() {
  this._backgroundColor = new Color();
  this._shapes = [];
};

Level.prototype.addShape = function( shape ) {
};

Level.prototype.fromJSON = function( json ) {
  var jsonObject = JSON.parse( json );
};

Level.prototype.toJSON = function() {

};
