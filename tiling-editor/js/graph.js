var Graph = function() {
  this._edges = [];
};

Graph.prototype.draw = function( ctx, shapes ) {
  ctx.beginPath();
  var srcList;
  var srcShape, dstShape;
  for ( var i = this._edges.length - 1; i >= 0; i-- ) {
    srcShape = shapes[i];
    srcList  = this._edges[i];
    if ( srcShape !== undefined && srcList !== undefined ) {
      for ( var j = srcList.length - 1; j >= 0; j-- ) {
        dstShape = shapes[ srcList[j] ];

        if ( dstShape !== undefined ) {
          ctx.moveTo( srcShape.getX(), srcShape.getY() );
          ctx.lineTo( dstShape.getX(), dstShape.getY() );
        }

      }
    }
  }
  ctx.closePath();

  // See Mark Ransom's answer on StackOverflow.
  // http://stackoverflow.com/questions/946544/good-text-foreground-color-for-a-given-background-color
  var backgroundColor = _editor.getBackgroundColor();
  var brightness = 0.299 * backgroundColor.getRed()   +
                   0.587 * backgroundColor.getGreen() +
                   0.114 * backgroundColor.getBlue();

  if ( brightness < 186 ) {
    ctx.strokeStyle = 'rgba( 255, 255, 255, 1.0 )';
  } else {
    ctx.strokeStyle = 'rgba( 0, 0, 0, 1.0 )';
  }

  ctx.lineWidth = 0.5;

  ctx.stroke();
};

Graph.prototype.getEdges = function() {
  return this._edges;
};

Graph.prototype.getNeighbors = function( index ) {
  return this._edges[ index ] || [];
};

Graph.prototype.addEdge = function( src, dst ) {
  this.addDirectedEdge( src, dst );
  this.addDirectedEdge( dst, src );
};

Graph.prototype.addDirectedEdge = function( src, dst ) {
  var srcList = this._edges[ src ];
  if ( srcList === undefined ) {
    srcList = [];
  }

  // If srcList is empty, or if it does not contain srcList.
  if ( srcList.length === 0 || srcList.indexOf( dst ) === -1 ) {
    srcList.push( dst );
  }

  srcList.sort();
  this._edges[ src ] = srcList;
};

Graph.prototype.removeEdge = function( src, dst ) {
  this.removeDirectedEdge( src, dst );
  this.removeDirectedEdge( dst, src );
};

Graph.prototype.removeDirectedEdge = function( src, dst ) {
  var srcList = this._edges[ src ];
  if ( srcList !== undefined ) {
    var index = srcList.indexOf( dst );
    if ( index !== -1 ) {
      srcList.splice( index, 1 );
    }
  }

  this._edges[ src ] = srcList;
};

// Remove shape at index and update all indices.
Graph.prototype.removeIndex = function( index ) {
  if ( 0 <= index && index < this._edges.length ) {
    var i, j;
    for ( i = this._edges.length - 1; i >= 0; i-- ) {
      if ( this._edges[i] === undefined ) {
        continue;
      }

      for ( j = this._edges[i].length - 1; j >= 0; j-- ) {
        // Remove edge
        if ( this._edges[i][j] === index ) {
          this._edges[i].splice( j, 1 );
        }
        // Subtracting fixes stored data to match new indices (with edge removed).
        else if ( this._edges[i][j] > index ) {
          this._edges[i][j]--;
        }
      }
    }

    this._edges.splice( index, 1 );
  }
};

Graph.prototype.fromJSON = function( json ) {
  this._edges = JSON.parse( json ) || [];
  return this;
};

Graph.prototype.toJSON = function() {
  return this.getEdges();
};
