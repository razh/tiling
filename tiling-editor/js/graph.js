var Graph = function() {
  this._edges = [];
};

Graph.prototype.getEdges = function() {
  return this._edges;
};

Graph.prototype.getNeighboars = function( index ) {
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

  var index = srcList.indexOf( dst );
  if ( index !== -1 ) {
    srcList.push( dst );
  }
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
};

Graph.prototype.fromJSON = function( json ) {
};

Graph.prototype.toJSON = function() {
  var object = {};

  object.edges = this.getEdges();

  return object;
};
