var Level = function() {
  this._name = '';

  this._scale = 1.0;
  this._stroke = 0.0;

  this._patternURL = '';
  this._pattern = null;

  this._backgroundColor = new Color( 0, 0, 0, 1.0 );
  this._ambientColor = new Color( 0, 0, 0, 1.0 );

  this._shapes = [];
  this._lights = [];

  this._graph = new Graph();

  this._jsonData = null;
  if ( arguments.length !== 0 ) {
    this.fromURL( arguments[0] );
  }
};

Level.prototype.getName = function() {
  return this._name;
};

Level.prototype.setName = function( name ) {
  this._name = name;
};

Level.prototype.getScale = function() {
  return this._scale;
};

Level.prototype.setScale = function( scale ) {
  this._scale = scale;
};

Level.prototype.getStroke = function() {
  return this._stroke;
};

Level.prototype.setStroke = function( stroke ) {
  this._stroke = stroke;
};

Level.prototype.getPattern = function() {
  return this._pattern;
};

Level.prototype.setPattern = function( pattern ) {
  this._pattern = pattern;
};

Level.prototype.getBackgroundColor = function() {
  return this._backgroundColor;
};

Level.prototype.setBackgroundColor = function( backgroundColor ) {
  this._backgroundColor.set( backgroundColor );
};

Level.prototype.getAmbientColor = function() {
  return this._ambientColor;
};

Level.prototype.setAmbientColor = function( ambientColor ) {
  this._ambientColor.set( ambientColor );
};

Level.prototype.getShapes = function() {
  return this._shapes;
};

Level.prototype.addShape = function( shape ) {
  this._shapes.push( shape );
};

Level.prototype.getLights = function() {
  return this._lights;
};

Level.prototype.addLight = function( light ) {
  this._lights.push( light );
};

Level.prototype.getGraph = function() {
  return this._graph;
};

Level.prototype.setGraph = function( graph ) {
  this._graph = graph;
};

Level.prototype.fromURL = function( url ) {
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

Level.prototype.fromJSON = function( json ) {
  var jsonObject = JSON.parse( json );

  var backgroundColor = new Color();
  backgroundColor.fromJSON( JSON.stringify( jsonObject.backgroundColor ) );

  var ambientColor = new Color();
  ambientColor.fromJSON( JSON.stringify( jsonObject.ambientColor ) );

  this._shapes = [];
  var i, n;
  for ( i = 0, n = jsonObject.shapes.length; i < n; i++ ) {
    this.addShape( new Shape().fromJSON( JSON.stringify( jsonObject.shapes[i] ) ) );
  }

  this._lights = [];
  for ( i = 0, n = jsonObject.lights.length; i < n; i++ ) {
    this.addLight( new Light().fromJSON( JSON.stringify( jsonObject.lights[i] ) ) );
  }

  if ( jsonObject.pattern === undefined ) {
    this._patternURL = jsonObject.patternURL;
    this.setPattern( new Pattern( this._patternURL ) );
  } else {
    this.setPattern( new Pattern().fromJSON( JSON.stringify( jsonObject.pattern ) ) );
  }

  if ( jsonObject.patternShapes !== undefined ) {
    for ( i = 0, n = jsonObject.patternShapes.length; i < n; i++ ) {
      this.addShape( this.loadPatternShapeFromJSON( JSON.stringify( jsonObject.patternShapes[i] ) ) );
    }
  }

  var graph = new Graph().fromJSON( JSON.stringify( jsonObject.graph ) );

  this.setName( jsonObject.name || '' );
  this.setScale( jsonObject.scale || 1.0 );
  this.setStroke( jsonObject.stroke || 0.0 );
  this.setBackgroundColor( backgroundColor );
  this.setAmbientColor( ambientColor );
  this.setGraph( graph );

  return this;
};

Level.prototype.toJSON = function( pattern ) {
  var object = {};

  object.name  = this.getName();
  object.scale = this.getScale();
  object.stroke = this.getStroke();

  if ( pattern ) {
    if ( this._patternURL.length !== 0 ) {
      object.patternURL = this._patternURL;
    } else {
      object.pattern = this.getPattern().toJSON();
    }
  }

  object.shapes = [];
  var i, n;
  for ( i = 0, n = this._shapes.length; i < n; i++ ) {
    object.shapes.push( this._shapes[i].toJSON( this.getScale() ) );
  }

  object.lights = [];
  for ( i = 0, n = this._lights.length; i < n; i++ ) {
    object.lights.push( this._lights[i].toJSON( this.getScale() ) );
  }

  object.graph = this.getGraph();

  object.backgroundColor = this.getBackgroundColor();
  object.ambientColor = this.getAmbientColor();

  return object;
};

Level.prototype.loadPatternShapeFromJSON = function( json ) {
  var jsonObject = JSON.parse( json );

  var index = jsonObject.index || 0;
  var shape = this._pattern.getShapes()[ index ].clone();

  if ( jsonObject.x !== undefined ) {
    shape.setX( jsonObject.x );
  }

  if ( jsonObject.y !== undefined ) {
    shape.setY( jsonObject.y );
  }

  if ( jsonObject.width !== undefined ) {
    shape.setWidth( jsonObject.width );
  }

  if ( jsonObject.height !== undefined ) {
    shape.setHeight( jsonObject.height );
  }

  if ( jsonObject.rotation ) {
    shape.setRotation( jsonObject.rotation );
  }

  if ( jsonObject.color ) {
    var color = new Color();
    color.fromJSON( JSON.stringify( jsonObject.color ) );
    shape.setColor( color );
  }

  return shape;
};
