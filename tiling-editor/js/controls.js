function onMouseDown( event ) {
  var input = transformCoords( event.pageX, event.pageY );

  _editor.select( _editor.hit( input.x, input.y ) );
  if ( _editor.getUser().hasSelected() ) {
    var selected = _editor.getUser().getSelected();
    _editor._offset.x = selected.getX() - input.x;
    _editor._offset.y = selected.getY() - input.y;
  }
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
  switch ( event.which ) {
    // ESC.
    case 27:
      quit();
      break;

    // Space.
    case 32:
      break;
  }
}

function transformCoords( x, y ) {
  return {
    x: x - _editor._canvas.offsetLeft,
    y: y - _editor._canvas.offsetTop
  };
}
