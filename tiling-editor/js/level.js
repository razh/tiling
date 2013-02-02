var Level = function() {
  this._name = '';

  this._backgroundColor = new Color();
  this._shapes = [];
};

Level.prototype.setName = function( name ) {
  this._name = name;
};

Level.prototype.addShape = function( shape ) {
};

Level.prototype.fromJSON = function( json ) {
  var jsonObject = JSON.parse( json );

  return new Level();
};

Level.prototype.toJSON = function() {
  var object = {};

  object.backgroundColor = this._backgroundColor;
  object.shapes = this._shapes;

  return object;
};
