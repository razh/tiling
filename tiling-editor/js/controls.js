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
  }
}

function onMouseDownDefault( input ) {
  _editor.select( _editor.hit( input.x, input.y ) );
  if ( _editor.getUser().hasSelected() ) {
    var selected = _editor.getUser().getSelected();
    _editor._offset.x = selected.getX() - input.x;
    _editor._offset.y = selected.getY() - input.y;
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

function onMouseMove( event ) {
  var input = transformCoords( event.pageX, event.pageY );

  if ( _editor.getUser().hasSelected() ) {
    var selected = _editor.getUser().getSelected();
    selected.setPosition( input.x + _editor._offset.x,
                          input.y + _editor._offset.y );
    _editor._inspectorPane.find( '#x' ).val( selected.getX() );
    _editor._inspectorPane.find( '#y' ).val( selected.getY() );
  }
}

function onMouseUp( event ) {
  _editor.getUser().setSelected( null );
}

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

    // a.
    case 65:
      _editor.setState( EditorState.ADDING_SHAPE );
      break;

    case 46:
      _editor.setState( EditorState.REMOVING_SHAPE );
      break;
  }
}

function transformCoords( x, y ) {
  return {
    x: x - _editor._canvas.offsetLeft,
    y: y - _editor._canvas.offsetTop
  };
}
