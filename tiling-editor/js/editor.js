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

  setupGUI();

  loop();
}

function setupGUI() {
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
  });

  Form.createModal({
    name: 'load-pattern',
    label: 'Load Pattern',
    type: ModalType.LOAD
  });

  Form.createModal({
    name: 'export-pattern',
    label: 'Export Pattern',
    type: ModalType.EXPORT
  });

  // Auto-select text areas when modals are open.
  $( '.modal' ).on({
    shown: function() {
      _editor.setState( EditorState.TEXT_EDITING );
      $( this ).find( 'textarea' ).select();
    },
    hidden: function() {
      _editor.setState( EditorState.DEFAULT );
    }
  });

  // Individual modals.
  $( '#export-modal' ).on({
    show: function() {
      $( this ).find( 'textarea' ).val( JSON.stringify( _editor.exportLevel() ) );
    }
  });

  $( '#export-pattern-modal' ).on({
    show: function() {
      $( this ).find( 'textarea' ).val( JSON.stringify( _editor.getPattern().toJSON() ) );
    }
  });

  $( '#load-modal' ).on({
    show: function() {
      var $modal = $( this );
      $modal.find( '#load-modal-button' ).click(function() {
        var json = $modal.find( 'textarea' ).val();
        if ( json.length > 0 ) {
          _editor.setLevel( new Level().fromJSON( json ) );
        }
      });
  }});

  $( '#load-pattern-modal' ).on({
    show: function() {
      var $modal = $( this );
      $modal.find( '#load-pattern-modal-button' ).click(function() {
        var json = $modal.find( 'textarea' ).val();
        if ( json.length > 0 ) {
          _editor.setPattern( new Pattern().fromJSON( json ) );
        }
      });
  }});

  // Name changes.
  $( '#level-name' ).change(function() {
    _editor.getLevel().setName( $( this ).val() );
  });

  // Setup action buttons.
  var buttonStates = {
    move:        EditorState.DEFAULT,
    add:         EditorState.ADDING_SHAPE,
    remove:      EditorState.REMOVING_SHAPE,
    copy:        EditorState.COPYING_SHAPE,
    addLight:    EditorState.ADDING_LIGHT,
    removeLight: EditorState.REMOVING_LIGHT,
    copyLight:   EditorState.COPYING_LIGHT,
    addEdge:     EditorState.ADDING_EDGE,
    removeEdge:  EditorState.REMOVING_EDGE
  };

  $.each( buttonStates, function( key, value ) {
    _editor._actionButtons[ key ].click(function() {
      _editor.setState( value );
    });
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
    var sides = parseInt( _editor._patternUI.sides.val(), 10 );
    var geometry = Geometry.createRegularPolygon( sides );
    _editor.getPattern().addShape( new Shape().setWidth( 50 )
                                              .setHeight( 50 )
                                              .setNumSides( sides )
                                              .setVertices( geometry.vertices )
                                              .setEdges( geometry.edges )
                                              .setColor( new Color( 128, 128, 128, 1.0 ) )
                                              .setAltColor( new Color( 255, 255, 255, 1.0 ) ) );
  });
  _editor._patternUI.remove.click(function( event ) {
    event.preventDefault();
    var selected = $( '.selected' );
    if ( selected.length === 0 ) {
      return;
    }

    selected.removeClass( 'selected' );

    var index = parseInt( selected.attr( 'id' )
                                  .replace( 'pattern', '' ), 10 );
    _editor.getPattern().removeShapeByIndex( index );
  });
  _editor._patternUI.name.change(function() {
    _editor.getPattern().setName( $( this ).val() );
  });

  // Setup toggling alternative colors button.
  _editor._toggleButtons.altColors.click(function() {
    _editor.toggleAltColors();
  });
  _editor._toggleButtons.useWebGL.click(function() {
    _editor.toggleWebGL();
  });

  // Prevent inputs from triggering key commands when focused.
  $( ':input' ).on({
    focus: function() {
      _editor.setState( EditorState.TEXT_EDITING );
    },
    blur: function() {
      _editor.setState( EditorState.DEFAULT );
    }
  });

  // Prevent forms from submitting (except for those inputs in the level inspector pane).
  $( 'form:not(#level-pane) :input' ).on({
    keydown: function( event ) {
      if ( event.which === 13 ) {
        event.preventDefault();
      }
    }
  });
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
  ADDING_LIGHT:   4,
  REMOVING_LIGHT: 5,
  COPYING_LIGHT:  6,
  ADDING_EDGE:    7,
  REMOVING_EDGE:  8,
  TEXT_EDITING:   9
};

var Editor = function() {
  this._canvasContainer = $( '.canvas-container' );

  this.WIDTH = this._canvasContainer.width();
  this.HEIGHT = this._canvasContainer.height();

  // WebGL.
  this._usingWebGL = false;

  this._scene = new THREE.Scene();
  this._renderer = new THREE.WebGLRenderer();
  this._camera = new THREE.OrthographicCamera( 0, 0, this.WIDTH, this.HEIGHT, 0.1, 15000 );

  this._scene.add( this._camera );
  this._camera.position.z = 10000;

  this._renderer.setSize( this.WIDTH, this.HEIGHT );
  this._canvasContainer.append( this._renderer.domElement );

  // Canvas.
  this._canvas = document.createElement( 'canvas' );
  this._ctx = this._canvas.getContext( '2d' );
  this._canvasContainer.append( this._canvas );

  this._canvas.width = this.WIDTH;
  this._canvas.height = this.HEIGHT;

  // UI elements.
  this._inspectorPane = $( '#inspector-pane' );
  this._patternPane   = $( '#pattern-pane' );
  this._levelPane     = $( '#level-pane' );
  this._actionButtons = {
    move:        $( '#move-button' ), // Default.
    add:         $( '#add-shape-button' ),
    remove:      $( '#remove-shape-button' ),
    copy:        $( '#copy-shape-button' ),
    addLight:    $( '#add-light-button' ),
    removeLight: $( '#remove-light-button' ),
    copyLight:   $( '#copy-light-button' ),
    addEdge:     $( '#add-edge-button' ),
    removeEdge:  $( '#remove-edge-button' )
  };
  this._snappingUI = {
    button: $( '#snapping-button' ),
    form:   $( '#snapping-radius' )
  };
  this._patternUI = {
    add:    $( '#add-pattern-shape-button' ),
    remove: $( '#remove-pattern-shape-button' ),
    sides:  $( '#add-pattern-shape-sides' ),
    name:   $( '#pattern-name' )
  };
  this._toggleButtons = {
    exportPattern: $( '#export-level-with-pattern' ),
    altColors:     $( '#show-alt-colors' ),
    useWebGL:      $( '#use-webGL' )
  };

  this._prevTime = Date.now();
  this._currTime = this._prevTime;

  this._shapes = [];
  this._lights = [];
  this._levelName = '';

  this._translate = {
    x: 0,
    y: 0
  };
  this._rotation = 0;

  // Stage attributes.
  this._scale = 1.0;
  this._stagePosition = {
    x: 0,
    y: 0
  };

  // Input offset.
  this._offset = {
    x: 0,
    y: 0
  };

  this._stroke = 0.0;
  this._backgroundColor = new Color( 100, 100, 100, 1.0 );
  this._ambientColor = new Color( 0, 0, 0, 1.0 );
  this._altColors = false;

  this._running = true;

  this._pattern = new Pattern();
  this.loadPatternInspector( this._pattern );

  this._level = new Level();
  this.loadLevel( this._level );
  this._graph = this._level.getGraph();

  // For adding shapes.
  this.getPattern().setBrushByIndex(0);

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

  var i, n;
  for ( i = 0, n = this._shapes.length; i < n; i++ ) {
    this._shapes[i].update( elapsedTime );
  }

  for ( i = 0, n = this._lights.length; i < n; i++ ) {
    this._lights[i].update( elapsedTime );
  }
};

Editor.prototype.draw = function() {
  if ( !this.usingWebGL() ) {
    this.drawCanvas();
  } else {
    this.drawWebGL();
    this.drawCanvasOverlay();
  }
};

Editor.prototype.drawCanvas = function() {
  this._canvas.style.backgroundColor = this.getBackgroundColor().toHexString();

  this._ctx.clearRect( 0, 0, this.WIDTH, this.HEIGHT );

  // Show ambient color.
  this._ctx.fillStyle = this.getAmbientColor().toString();
  this._ctx.fillRect( 0, 0, this.WIDTH, 14 ); // 14 px is height of the various UI elements.

  this._ctx.save();
  this._ctx.translate( this.getTranslateX(), this.HEIGHT + this.getTranslateY() );
  this._ctx.rotate( this.getRotation() );
  // Coordinates are reversed in the OpenGL game.
  this._ctx.scale( 1, -1 );

  // Show outline of camera/stage boundaries.
  this._ctx.save();
  this._ctx.translate( this.getStageX(), this.getStageY() );

  if ( this.getBackgroundColor().getBrightness() < 186 ) {
    this._ctx.strokeStyle = 'rgba( 255, 255, 255, 1.0 )';
  } else {
    this._ctx.strokeStyle = 'rgba( 0, 0, 0, 1.0 )';
  }
  this._ctx.lineWidth = 1;
  this._ctx.strokeRect( 0, 0, 1280 / this.getScale(), 720 / this.getScale() );

  this._ctx.restore();

  // Draw shapes.
  var i, n;
  for ( i = 0, n = this._shapes.length; i < n; i++ ) {
    this._shapes[i].draw( this._ctx, this.getStroke(), this.showingAltColors() );
  }

  // Draw lights.
  for ( i = 0, n = this._lights.length; i < n; i++ ) {
    this._lights[i].draw( this._ctx );
  }

  this._graph.draw( this._ctx, this._shapes );

  this._ctx.restore();
};

Editor.prototype.drawWebGL = function() {
  this._renderer.render( this._scene, this._camera );
};

Editor.prototype.drawCanvasOverlay = function() {
  this._ctx.clearRect( 0, 0, this.WIDTH, this.HEIGHT );

  this._ctx.save();
  this._ctx.translate( this.getTranslateX(), this.HEIGHT + this.getTranslateY() );
};

Editor.prototype.hit = function( x, y ) {
  var hit = null;
  var i;
  // Check lights first since generally, they are smaller and harder to hit.
  for ( i = this._lights.length - 1; i >= 0; i-- ) {
    hit = this._lights[i].hit( x, y );
    if ( hit !== null ) {
      return hit;
    }
  }

  for ( i = this._shapes.length - 1; i >= 0; i-- ) {
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
Editor.prototype.loadInspector = function( object, prototypical ) {
  this._inspectorPane.empty();

  object.createInspector( this._inspectorPane, prototypical );

  // Prevent shape inspector form inputs from triggering key commands.
  this._inspectorPane.find( ':input' ).on({
    focus: function() {
      _editor.setState( EditorState.TEXT_EDITING );
    },
    blur: function() {
      _editor.setState( EditorState.DEFAULT );
    }
  });
};

Editor.prototype.loadPatternInspector = function( pattern ) {
  this._patternPane.empty();

  $( '#pattern-name' ).val( pattern.getName() );
  pattern.createInspector( this._patternPane );
};

Editor.prototype.loadLevelInspector = function( level ) {
  this._levelPane.empty();

  Form.createTextForm({
    $id:    this._levelPane,
    object: this,
    name:   'level-name',
    getter: 'getLevelName',
    setter: 'setLevelName'
  });

  Form.createFloatForm({
    $id:    this._levelPane,
    object: this,
    name:   'stage-x',
    getter: 'getStageX',
    setter: 'setStageX',
    min:    -Math.max( this.WIDTH, this.HEIGHT ),
    max:    Math.max( this.WIDTH, this.HEIGHT ),
    step:   0.1,
    digits: 1
  });

  Form.createFloatForm({
    $id:    this._levelPane,
    object: this,
    name:   'stage-y',
    getter: 'getStageY',
    setter: 'setStageY',
    min:    -Math.max( this.WIDTH, this.HEIGHT ),
    max:    Math.max( this.WIDTH, this.HEIGHT ),
    step:   0.1,
    digits: 1
  });

  Form.createFloatForm({
    $id:    this._levelPane,
    object: this,
    name:   'scale',
    getter: 'getScale',
    setter: 'setScale',
    min:    0.1,
    max:    10.0,
    step:   0.1,
    digits: 1
  });

  Form.createFloatForm({
    $id:    this._levelPane,
    object: this,
    name:   'stroke',
    getter: 'getStroke',
    setter: 'setStroke',
    min:    0.0,
    max:    20.0,
    step:   0.1,
    digits: 1
  });

  Form.createColorForm({
    $id:    this._levelPane,
    object: this,
    getter: 'getBackgroundColor'
  });

  Form.createColorForm({
    $id:    this._levelPane,
    object: this,
    getter: 'getAmbientColor',
    prefix: 'amb'
  });

  // Prevent level inspector form inputs from triggering key commands.
  this._levelPane.find( ':input' ).on({
    focus: function() {
      _editor.setState( EditorState.TEXT_EDITING );
    },
    blur: function() {
      _editor.setState( EditorState.DEFAULT );
    }
  });
};

// State.
Editor.prototype.getState = function() {
  return this._state;
};

Editor.prototype.setState = function( state ) {
  // Update state toggle buttons.
  var buttonNames = [
    'move',
    'add',
    'remove',
    'copy',
    'addLight',
    'removeLight',
    'copyLight',
    'addEdge',
    'removeEdge'
  ];

  if ( 0 <= state && state < buttonNames.length ) {
    this._actionButtons[ buttonNames[ state ] ].button( 'toggle' );
  }

  this._state = state;
};

// Shapes.
Editor.prototype.getShapes = function() {
  return this._shapes;
};

Editor.prototype.addShape = function( shape ) {
  this._shapes.push( shape );
  this._scene.add( shape.getWebGLObject() );
};

Editor.prototype.removeShape = function( shape ) {
  var index = this._shapes.indexOf( shape );
  if ( index !== -1 ) {
    this._shapes.splice( index, 1 );
    this._graph.removeIndex( index );
  }
};

Editor.prototype.indexOfShape = function( shape ) {
  return this._shapes.indexOf( shape );
};

Editor.prototype.shapeAt = function( index ) {
  if ( 0 <= index && index < this._shapes.length ) {
    return this._shapes[ index ];
  }
};

// Lights.
Editor.prototype.getLights = function() {
  return this._lights;
};

Editor.prototype.addLight = function( light ) {
  this._lights.push( light );
};

Editor.prototype.removeLight = function( light ) {
  var index = this._lights.indexOf( light );
  if ( index !== -1 ) {
    this._lights.splice( index, 1 );
  }
};

// Stage.
Editor.prototype.getStageX = function() {
  return this.getStagePosition().x;
};

Editor.prototype.setStageX = function( stageX ) {
  this._stagePosition.x = stageX;
};

Editor.prototype.getStageY = function() {
  return this.getStagePosition().y;
};

Editor.prototype.setStageY = function( stageY ) {
  this._stagePosition.y = stageY;
};

Editor.prototype.getStagePosition = function() {
  return this._stagePosition;
};

Editor.prototype.setStagePosition = function( stagePosition ) {
  if ( arguments.length === 1 ) {
    this.setStageX( arguments[0].x );
    this.setStageY( arguments[0].y );
  } else if ( arguments.length === 2 ) {
    this.setStageX( arguments[0] );
    this.setStageY( arguments[1] );
  }
};

// Scale.
Editor.prototype.getScale = function() {
  return this._scale;
};

Editor.prototype.setScale = function( scale ) {
  this._scale = scale;
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
};

Editor.prototype.translateX = function( translateX ) {
  this.setTranslateX( this.getTranslateX() + translateX );
};

Editor.prototype.translateY = function( translateY ) {
  this.setTranslateY( this.getTranslateY() + translateY );
};

Editor.prototype.translate = function() {
  if ( arguments.length === 1 ) {
    this.translateX( arguments[0].x );
    this.translateY( arguments[0].y );
  } else if ( arguments.length === 2 ) {
    this.translateX( arguments[0] );
    this.translateY( arguments[1] );
  }
};

// Rotation.
Editor.prototype.getRotation = function() {
  return this._rotation;
};

Editor.prototype.setRotation = function( rotation ) {
  this._rotation = rotation;
};

Editor.prototype.rotate = function( angle ) {
  this._rotation += angle;
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

// Stroke.
Editor.prototype.getStroke = function() {
  return this._stroke;
};

Editor.prototype.setStroke = function( stroke ) {
  this._stroke = stroke;
};

// Alternative colors.
Editor.prototype.showingAltColors = function() {
  return this._altColors;
};

Editor.prototype.setShowingAltColors = function( altColors ) {
  this._altColors = altColors;
};

Editor.prototype.toggleAltColors = function() {
  this.setShowingAltColors( !this.showingAltColors() );
};

// Background color.
Editor.prototype.getBackgroundColor = function() {
  return this._backgroundColor;
};

Editor.prototype.setBackgroundColor = function( backgroundColor ) {
  this._backgroundColor = backgroundColor;
};

// Ambient color.
Editor.prototype.getAmbientColor = function() {
  return this._ambientColor;
};

Editor.prototype.setAmbientColor = function( ambientColor ) {
  this._ambientColor = ambientColor;
};

// Patterns.
Editor.prototype.getPattern = function() {
  return this._pattern;
};

Editor.prototype.setPattern = function( pattern ) {
  this._pattern = pattern;
  this.loadPatternInspector( pattern );
};

// Brush.
Editor.prototype.getBrush = function() {
  return this.getPattern().getBrush();
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
    this.loadInspector( selected );
  }
};

Editor.prototype.hasSelected = function() {
  return this._selected !== undefined && this._selected !== null;
};

// WebGL.
Editor.prototype.usingWebGL = function() {
  return this._usingWebGL;
};

Editor.prototype.setUsingWebGL = function( webGL ) {
  this._usingWebGL = webGL;
};

Editor.prototype.toggleWebGL = function() {
  this.setUsingWebGL( !this.usingWebGL() );
};

// Graphs.
Editor.prototype.getGraph = function() {
  return this._graph;
};

Editor.prototype.setGraph = function( graph ) {
  this._graph = graph;
};

// Level name.
Editor.prototype.getLevelName = function() {
  return this._levelName;
};

Editor.prototype.setLevelName = function( name ) {
  this._levelName = name;
  $( '#lname' ).val( name );
};

// Levels.
Editor.prototype.getLevel = function() {
  return this._level;
};

Editor.prototype.setLevel = function( level ) {
  this.loadLevel( level );
  this._level = level;
};

Editor.prototype.loadLevel = function( level ) {
  this.setLevelName( level.getName() );
  this.setStagePosition( level.getStagePosition() );
  this.setScale( level.getScale() );
  this.setStroke( level.getStroke() );
  this.setBackgroundColor( level.getBackgroundColor() );
  this.setAmbientColor( level.getAmbientColor() );

  if ( level.getPattern() !== null ) {
    this.setPattern( level.getPattern() );
  }

  this._shapes = [];
  var levelShapes = level.getShapes();
  var i, n;
  for ( i = 0, n = levelShapes.length; i < n; i++ ) {
    this.addShape( levelShapes[i] );
  }

  this._lights = [];
  var levelLights = level.getLights();
  for ( i = 0, n = levelLights.length; i < n; i++ ) {
    this.addLight( levelLights[i] );
  }

  this._graph = level.getGraph();

  this.loadLevelInspector( level );
};

Editor.prototype.exportLevel = function() {
  var level = new Level();

  level.setName( this.getLevelName() );
  level.setStagePosition( this.getStagePosition() );
  level.setScale( this.getScale() );
  level.setStroke( this.getStroke() );
  level.setBackgroundColor( this.getBackgroundColor() );
  level.setAmbientColor( this.getAmbientColor() );
  level.setPattern( this.getPattern() );
  level.setGraph( this.getGraph() );
  level._shapes = this.getShapes();
  level._lights = this.getLights();

  return level.toJSON( this._toggleButtons.exportPattern.hasClass( 'active' ) );
};
