// Mouse down.
function onMouseDown( event ) {
  var input = transformCoords( event.pageX, event.pageY );

  switch ( _editor.getState() ) {
    case EditorState.DEFAULT:
      onMouseDownDefault( input );
      break;

    case EditorState.ADDING_SHAPE:
      onMouseDownAddingShape( input );
      break;

    case EditorState.REMOVING_SHAPE:
      onMouseDownRemovingShape( input );
      break;

    case EditorState.COPYING_SHAPE:
      onMouseDownCopyingShape( input );
      break;
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

  var shape = new Shape().copy( brush );
  shape.setPosition( input.x, input.y );
  _editor.addShape( shape );

  _editor.setState( EditorState.DEFAULT );
}

function onMouseDownRemovingShape( input ) {
  var hit = _editor.hit( input.x, input.y );
  if ( hit !== null ) {
    _editor.removeShape( hit );
  }

  _editor.setState( EditorState.DEFAULT );
}

function onMouseDownCopyingShape( input ) {
  if ( _editor.hasSelected() ) {
    var shape = new Shape().copy( _editor.getSelected() )
                           .setPosition( input.x, input.y );
    _editor.addShape( shape );
    _editor.setState( EditorState.DEFAULT );
    console.log( 'copy ')
  } else {
    _editor.setSelected( _editor.hit( input.x, input.y ) );
    console.log( 'select' );
  }
}


// Mouse move.
function onMouseMove( event ) {
  var input = transformCoords( event.pageX, event.pageY );

  switch ( _editor.getState() ) {
    case EditorState.DEFAULT:
    case EditorState.ADDING_SHAPE:
    case EditorState.REMOVING_SHAPE:
      onMouseMoveDefault( input );
      break;

    case EditorState.COPYING_SHAPE:
      break;
  }
}

function onMouseMoveDefault( input ) {
  if ( _editor.hasSelected() ) {
    var selected = _editor.getSelected();
    selected.setPosition( input.x + _editor.getOffsetX(),
                          input.y + _editor.getOffsetY() );
    _editor._inspectorPane.find( '#x' ).val( selected.getX() );
    _editor._inspectorPane.find( '#y' ).val( selected.getY() );
  }
}


// Mouse up.
function onMouseUp( event ) {
  switch ( _editor.getState() ) {
    case EditorState.DEFAULT:
    case EditorState.ADDING_SHAPE:
    case EditorState.REMOVING_SHAPE:
      _editor.setSelected( null );
      break;

    case EditorState.COPYING_SHAPE:
      break;
  }
}


// Key down.
function onKeyDown( event ) {
  console.log( event.which );
  switch ( event.which ) {
    // ESC.
    case 27:
      quit();
      break;

    // Space.
    case 32:
      break;

    // A.
    case 65:
      _editor.setState( EditorState.ADDING_SHAPE );
      break;

    // Delete.
    case 46:
      _editor.setState( EditorState.REMOVING_SHAPE );
      break;

    // C.
    case 67:
      _editor.setState( EditorState.COPYING_SHAPE );
      break;
  }
}

function transformCoords( x, y ) {
  return {
    x: x - _editor._canvas.offsetLeft,
    y: y - _editor._canvas.offsetTop
  };
}
