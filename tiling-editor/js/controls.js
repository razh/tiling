var MouseState = {
  UP:   0,
  DOWN: 1
};

var Input = (function() {
  this._mouseState = MouseState.UP;

  return {
    getMouseState: function() {
      return this._mouseState;
    },

    setMouseState: function( mouseState ) {
      this._mouseState = mouseState;
    }
  };
}) ();

// Mouse down.
function onMouseDown( event ) {
  var input = transformCoords( event.pageX, event.pageY );
  Input.setMouseState( MouseState.DOWN );

  var onMouseDownFunctions = [
    onMouseDownDefault,
    onMouseDownAddingShape,
    onMouseDownRemovingShape,
    onMouseDownCopyingShape,
    onMouseDownAddingLight,
    onMouseDownRemovingLight,
    onMouseDownCopyingLight,
    onMouseDownAddingEdge,
    onMouseDownRemovingEdge
  ];

  if ( _editor.getState() !== EditorState.TEXT_EDITING ) {
    onMouseDownFunctions[ _editor.getState() ].call( this, input );
  }
}

function onMouseDownDefault( input ) {
  _editor.setSelected( _editor.hit( input.x, input.y ) );
  if ( _editor.hasSelected() ) {
    var selected = _editor.getSelected();
    _editor.setOffset( selected.getX() - input.x,
                       selected.getY() - input.y );
  }
}

function onMouseDownAddingShape( input ) {
  var brush = _editor.getBrush();
  if ( brush === undefined || brush === null ) {
    return;
  }

  _editor.addShape( brush.clone().setPosition( input.x, input.y ) );
  _editor.setState( EditorState.DEFAULT );
}

function onMouseDownRemovingShape( input ) {
  var hit = _editor.hit( input.x, input.y );
  if ( hit !== null && !( hit instanceof Light ) ) {
    _editor.removeShape( hit );
  }

  _editor.setState( EditorState.DEFAULT );
}

function onMouseDownCopyingShape( input ) {
  if ( _editor.hasSelected() ) {
    if ( _editor.getSelected() instanceof Light ) {
      return;
    }

    var shape =  _editor.getSelected()
                        .clone()
                        .setPosition( input.x, input.y );
    _editor.addShape( shape );
    _editor.setState( EditorState.DEFAULT );
  } else {
    _editor.setSelected( _editor.hit( input.x, input.y ) );
  }
}

function onMouseDownAddingLight( input ) {
  _editor.addLight( new Light().setPosition( input.x, input.y ) );
  _editor.setState( EditorState.DEFAULT );
}

function onMouseDownRemovingLight( input ) {
  var hit = _editor.hit( input.x, input.y );
  if ( hit !== null && hit instanceof Light ) {
    _editor.removeLight( hit );
  }

  _editor.setState( EditorState.DEFAULT );
}

function onMouseDownCopyingLight( input ) {
  if ( _editor.hasSelected() ) {
    if ( !_editor.getSelected() instanceof Light ) {
      return;
    }

    var light = _editor.getSelected()
                       .clone()
                       .setPosition( input.x, input.y );
    _editor.addLight( light );
    _editor.setState( EditorState.DEFAULT );
  } else {
    _editor.setSelected( _editor.hit( input.x, input.y ) );
  }
}

function onMouseDownAddingEdge( input ) {
  if ( _editor.hasSelected() ) {
    if ( _editor.getSelected() instanceof Light ) {
      return;
    }

    var hit = _editor.hit( input.x, input.y );
    if ( hit !== null ) {
      var srcIndex = _editor.indexOfShape( _editor.getSelected() ),
          dstIndex = _editor.indexOfShape( hit );
      if ( srcIndex !== -1 && dstIndex !== -1 ) {
        _editor.getGraph().addEdge( srcIndex, dstIndex );
      }
    }

    _editor.setState( EditorState.DEFAULT );
  } else {
    _editor.setSelected( _editor.hit( input.x, input.y ) );
  }
}

function onMouseDownRemovingEdge( input ) {
  if ( _editor.hasSelected() ) {
    if ( _editor.getSelected() instanceof Light ) {
      return;
    }

    var hit = _editor.hit( input.x, input.y );
    if ( hit !== null ) {
      var srcIndex = _editor.indexOfShape( _editor.getSelected() ),
          dstIndex = _editor.indexOfShape( hit );
      if ( srcIndex !== -1 && dstIndex !== -1 ) {
        _editor.getGraph().removeEdge( srcIndex, dstIndex );
      }
    }

    _editor.setState( EditorState.DEFAULT );
  } else {
    _editor.setSelected( _editor.hit( input.x, input.y ) );
  }
}


// Mouse move.
function onMouseMove( event ) {
  var input = transformCoords( event.pageX, event.pageY );

  switch ( _editor.getState() ) {
    case EditorState.DEFAULT:
      // Can only use these vars on webkit.
      if ( event.webkitMovementX !== undefined ) {
        onMouseMoveDefault( input, event.webkitMovementX, event.webkitMovementY );
      } else if ( event.mozMovementX !== undefined ) {
        onMouseMoveDefault( input, event.mozMovementX, event.mozMovementY );
      }
      break;

    case EditorState.ADDING_SHAPE:
    case EditorState.REMOVING_SHAPE:
    case EditorState.COPYING_SHAPE:
    case EditorState.ADDING_LIGHT:
    case EditorState.REMOVING_LIGHT:
    case EditorState.COPYING_LIGHT:
    case EditorState.ADDING_EDGE:
    case EditorState.REMOVING_EDGE:
      break;
  }
}

function onMouseMoveDefault( input, dx, dy ) {
  if ( _editor.hasSelected() ) {
    var selected = _editor.getSelected();
    selected.setPosition( input.x + _editor.getOffsetX(),
                          input.y + _editor.getOffsetY() );

    if ( _editor.isSnapping() ) {
      selected.snap( _editor.getShapes() );
    }

    _editor._inspectorPane.find( '#x' ).val( selected.getX() );
    _editor._inspectorPane.find( '#y' ).val( selected.getY() );
  } else if ( Input.getMouseState() === MouseState.DOWN ) {
    _editor.translate( dx, dy );
  }
}

// Mouse up.
function onMouseUp( event ) {
  Input.setMouseState( MouseState.UP );

  switch ( _editor.getState() ) {
    case EditorState.DEFAULT:
    case EditorState.ADDING_SHAPE:
    case EditorState.REMOVING_SHAPE:
    case EditorState.ADDING_LIGHT:
    case EditorState.REMOVING_LIGHT:
      _editor.setSelected( null );
      break;

    case EditorState.COPYING_SHAPE:
    case EditorState.COPYING_LIGHT:
    case EditorState.ADDING_EDGE:
    case EditorState.REMOVING_EDGE:
      break;
  }
}


// Key down.
function onKeyDown( event ) {
  if ( _editor.getState() === EditorState.TEXT_EDITING ) {
    return;
  }

  switch ( event.which ) {
    // q.
    case 81:
      quit();
      break;

    // Space.
    case 32:
      event.preventDefault();
      _editor.toggleAltColors();
      _editor._toggleButtons.altColors.button( 'toggle' );
      break;

    // A.
    case 65:
      _editor.setState( EditorState.ADDING_SHAPE );
      break;

    // Backspace.
    case 8:
    // Delete.
    case 46:
      event.preventDefault();
      _editor.setState( EditorState.REMOVING_SHAPE );
      break;

    // C.
    case 67:
      _editor.setState( EditorState.COPYING_SHAPE );
      break;

    // E.
    case 69:
      _editor.setState( EditorState.ADDING_EDGE );
      break;

    // V.
    case 86:
      _editor.setState( EditorState.DEFAULT );
      break;

    // S.
    case 83:
      if ( event.ctrlKey ) {
        $( '#export-modal' ).modal( 'toggle' );
      } else {
        _editor.toggleSnapping();
      }
      break;

    // O.
    case 79:
      if ( event.ctrlKey ) {
        $( '#load-modal' ).modal( 'toggle' );
      }
      break;

    // R.
    case 82:
      _editor.setTranslate( 0, 0 );
      break;

    // Enter.
    case 13:
      break;

    // Numbers.
    case 48:
    case 49:
    case 50:
    case 51:
    case 52:
    case 53:
    case 54:
    case 55:
    case 56:
    case 57:
      var index = event.which - 49;
      if ( index === -1 ) {
        index = 9;
      }

      _editor.getPattern()._canvasArray.removeClass( 'selected' );
      _editor._patternPane.find( '#pattern' + index ).addClass( 'selected' );
      _editor.getPattern().setBrushByIndex( index );
      break;

    default:
      console.log( event.which );
      break;
  }
}

function transformCoords( x, y ) {
  return {
    x: x - _editor._canvas.offsetLeft - _editor.getTranslateX(),
    y: _editor.HEIGHT - ( y - _editor._canvas.offsetTop - _editor.getTranslateY() )
  };
}
