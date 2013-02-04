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

  // Create modals.
  Form.createModal({
    name: 'load',
    label: 'Load',
    type: ModalType.LOAD
  });

  Form.createModal({
    name: 'export',
    label: 'Export',
    type: ModalType.EXPORT
  })

  // Auto-select text areas when modals are open.
  $( '#load-modal' ).on({
    shown: function() {
      _editor.setState( EditorState.TEXT_EDITING );
      $( '#load-text-area' ).select();
    },
    hidden: function() {
      _editor.setState( EditorState.DEFAULT );
    }
  });
  $( '#export-modal' ).on({
    shown: function() {
      _editor.setState( EditorState.TEXT_EDITING );
      $( '#export-text-area' ).select();
    },
    hidden: function() {
      _editor.setState( EditorState.DEFAULT );
    }
  });

  // Name changes.
  $( '#level-name' ).change(function() {
    _editor.getLevel().setName( $( this ).val() );
  });

  // Setup action buttons.
  _editor._actionButtons.move.click(function() {
    _editor.setState( EditorState.DEFAULT );
  });
  _editor._actionButtons.add.click(function() {
    _editor.setState( EditorState.ADDING_SHAPE );
  });
  _editor._actionButtons.remove.click(function() {
    _editor.setState( EditorState.REMOVING_SHAPE );
  });
  _editor._actionButtons.copy.click(function() {
    _editor.setState( EditorState.COPYING_SHAPE );
  });

  // Setup snapping controls.
  _editor._snappingUI.button.click(function() {
    _editor.toggleSnapping();
  });
  Form.createFloatForm({
    $id:    _editor._snappingUI.form,
    object: _editor,
    name:   'snappingRadius',
    getter: 'getSnappingRadius',
    setter: 'setSnappingRadius',
    min:    0,
    max:    200,
    step:   1,
    simple: true
  });

  // Setup pattern controls.
  _editor._patternUI.add.click(function( event ) {
    event.preventDefault();
    var sides = _editor._patternUI.sides.val();
    var geometry = Geometry.createRegularPolygon( sides );
    _editor.getPatterns()[0].addShape( new Shape().setWidth( 50 )
                                                  .setHeight( 50 )
                                                  .setNumSides( sides )
                                                  .setVertices( geometry.vertices )
                                                  .setEdges( geometry.edges )
                                                  .calculateRadius()
                                                  .setColor( new Color( 0, 0, 0, 1.0 ) ) );
  });
  _editor._patternUI.remove.click(function( event ) {
    event.preventDefault();
    var selected = $( '.selected' );
    selected.removeClass( 'selected' );

    var index = parseInt( selected.attr( 'id' )
                                  .replace( 'pattern', '' ), 10 );
    _editor.getPatterns()[0].removeShapeByIndex( index );
  });

  // Prevent form inputs from submitting.
  $( 'form input' ).keydown(function( event ) {
    if ( event.which === 13 ) {
      event.preventDefault();
    }
  });

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
  DEFAULT:        0,
  ADDING_SHAPE:   1,
  REMOVING_SHAPE: 2,
  COPYING_SHAPE:  3,
  TEXT_EDITING:   4
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
  this._actionButtons = {
    move: $( '#move-button' ), // Default.
    add: $( '#add-shape-button' ),
    remove: $( '#remove-shape-button' ),
    copy: $( '#copy-shape-button' )
  };
  this._snappingUI = {
    button: $( '#snapping-button' ),
    form: $( '#snapping-radius' )
  };
  this._patternUI = {
    add: $( '#add-pattern-shape-button' ),
    remove: $( '#remove-pattern-shape-button' ),
    sides: $( '#add-pattern-shape-sides' )
  };

  this._prevTime = Date.now();
  this._currTime = this._prevTime;

  this._shapes = [];

  this._translate = {
    x: 0,
    y: 0
  };
  this._rotation = 0;

  this._offset = {
    x: 0,
    y: 0
  };

  this._running = true;

  this._patterns = [];
  this._patterns.push( new Pattern( './json/example_pattern.json' ) );

  this._patternIndex = 0;
  this.loadPatternInspector( this._patterns[ this._patternIndex ] );

  this._level = new Level( './json/example_level.json' );
  this.load( this._level );

  // For adding shapes.
  this._brush = null;
  this.setBrushByIndex(0);

  this._state = null;
  this.setState( EditorState.DEFAULT );

  this._selected = null;
  this._snapping = false;
  this._snappingRadius = 10;
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
  this._canvas.style.backgroundColor = this.getBackgroundColor().toHexString();

  this._ctx.clearRect( 0, 0, this.WIDTH, this.HEIGHT );

  this._ctx.save();
  this._ctx.translate( this.getTranslateX(), this.HEIGHT + this.getTranslateY() );
  this._ctx.rotate( this.getRotation() );
  // Coordinates are reversed in the OpenGL game.
  this._ctx.scale( 1, -1 );

  for ( var i = 0, n = this._shapes.length; i < n; i++ ) {
    this._shapes[i].draw( this._ctx );
  }

  this._ctx.restore();
};

Editor.prototype.hit = function( x, y ) {
  var hit = null;
  for ( var i = this._shapes.length - 1; i >= 0; i-- ) {
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

// Prototypical shapes don't have ability to set positions.
Editor.prototype.loadShapeInspector = function( shape, prototypical ) {
  this._inspectorPane.empty();

  shape.createInspector( this._inspectorPane, prototypical );
};

Editor.prototype.loadPatternInspector = function( pattern ) {
  this._patternPane.empty();

  $( '#pattern-name' ).val( pattern.getName() );
  pattern.createInspector( this._patternPane );
};

// State.
Editor.prototype.getState = function() {
  return this._state;
};

Editor.prototype.setState = function( state ) {
  // Update state toggle buttons.
  switch ( state ) {
    case EditorState.DEFAULT:
      this._actionButtons.move.button( 'toggle' );
      break;

    case EditorState.ADDING_SHAPE:
      this._actionButtons.add.button( 'toggle' );
      break;

    case EditorState.REMOVING_SHAPE:
      this._actionButtons.remove.button( 'toggle' );
      break;

    case EditorState.COPYING_SHAPE:
      this._actionButtons.copy.button( 'toggle' );
      break;
  }

  this._state = state;
};

// Shapes.
Editor.prototype.getShapes = function() {
  return this._shapes;
};

Editor.prototype.addShape = function( shape ) {
  this._shapes.push( shape );
};

Editor.prototype.removeShape = function( shape ) {
  var index = this._shapes.indexOf( shape );
  if ( index !== -1 ) {
    this._shapes.splice( index, 1 );
  }
};

// Translate.
Editor.prototype.getTranslateX = function() {
  return this.getTranslate().x;
};

Editor.prototype.setTranslateX = function( translateX ) {
  this._translate.x = translateX;
};

Editor.prototype.getTranslateY = function() {
  return this.getTranslate().y;
};

Editor.prototype.setTranslateY = function( translateY ) {
  this._translate.y = translateY;
};

Editor.prototype.getTranslate = function() {
  return this._translate;
};

Editor.prototype.setTranslate = function() {
  if ( arguments.length === 1 ) {
    this.setTranslateX( arguments[0].x );
    this.setTranslateY( arguments[0].y );
  } else if ( arguments.length === 2 ) {
    this.setTranslateX( arguments[0] );
    this.setTranslateY( arguments[1] );
  }

  return this;
};

Editor.prototype.translateX = function( translateX ) {
  this.setTranslateX( this.getTranslateX() + translateX );
  return this;
};

Editor.prototype.translateY = function( translateY ) {
  this.setTranslateY( this.getTranslateY() + translateY );
  return this;
};

Editor.prototype.translate = function() {
  if ( arguments.length === 1 ) {
    this.translateX( arguments[0].x );
    this.translateY( arguments[0].y );
  } else if ( arguments.length === 2 ) {
    this.translateX( arguments[0] );
    this.translateY( arguments[1] );
  }

  return this;
};

// Rotation.
Editor.prototype.getRotation = function() {
  return this._rotation;
};

Editor.prototype.setRotation = function( rotation ) {
  this._rotation = rotation;
};

Editor.prototype.rotate = function( angle ) {
  this._rotation -= angle;
  return this;
};

// Offset (mouse from shape).
Editor.prototype.getOffsetX = function() {
  return this.getOffset().x;
};

Editor.prototype.setOffsetX = function( offsetX ) {
  this._offset.x = offsetX;
};

Editor.prototype.getOffsetY = function() {
  return this.getOffset().y;
};

Editor.prototype.setOffsetY = function( offsetY ) {
  this._offset.y = offsetY;
};

Editor.prototype.getOffset = function() {
  return this._offset;
};

Editor.prototype.setOffset = function() {
  if ( arguments.length === 1 ) {
    this.setOffsetX( arguments[0].x );
    this.setOffsetY( arguments[0].y );
  } else if ( arguments.length === 2 ) {
    this.setOffsetX( arguments[0] );
    this.setOffsetY( arguments[1] );
  }
};

// Background color.
Editor.prototype.getBackgroundColor = function() {
  return this._backgroundColor;
};

Editor.prototype.setBackgroundColor = function( backgroundColor ) {
  this._backgroundColor = backgroundColor;
};

// Patterns.
Editor.prototype.getPatterns = function() {
  return this._patterns;
};

Editor.prototype.addPattern = function( pattern ) {
  this._patterns.push( pattern );
};

Editor.prototype.getPatternIndex = function() {
  return this._patternIndex;
};

Editor.prototype.setPatternIndex = function( patternIndex ) {
  this._patternIndex = patternIndex;

  this.loadPatternInspector( this.getPatterns()[ this._patternIndex ] );
};

// Brush.
Editor.prototype.getBrush = function() {
  return this._brush;
};

Editor.prototype.setBrushByIndex = function( brushIndex ) {
  this._brush = this.getPatterns()[ this.getPatternIndex() ].getShapes()[ brushIndex ];
};

// Snapping.
Editor.prototype.isSnapping = function() {
  return this._snapping;
};

Editor.prototype.setSnapping = function( snapping ) {
  this._snapping = snapping;
};

Editor.prototype.toggleSnapping = function() {
  this._snapping = !this._snapping;

  this._snappingUI.button.button( 'toggle' );
};

Editor.prototype.getSnappingRadius = function() {
  return this._snappingRadius;
};

Editor.prototype.setSnappingRadius = function( snappingRadius ) {
  this._snappingRadius = snappingRadius;
};

// Selected.
Editor.prototype.getSelected = function() {
  return this._selected;
};

Editor.prototype.setSelected = function( selected ) {
  this._selected = selected;

  if ( selected !== null ) {
    this.loadShapeInspector( selected );
  }
};

Editor.prototype.hasSelected = function() {
  return this._selected !== undefined && this._selected !== null;
};

// Levels.
Editor.prototype.getLevel = function() {
  return this._level;
};

Editor.prototype.setLevel = function( level ) {
  this.load( level );
  this._level = level;
};

Editor.prototype.load = function( level ) {
  $( '#level-name' ).val( level.getName() );

  this._shapes = [];
  var levelShapes = level.getShapes();
  for ( var i = 0, n = levelShapes.length; i < n; i++ ) {
    this.addShape( levelShapes[i] );
  }
};

Editor.prototype.export = function() {
  var object = {};

  return object;
};
