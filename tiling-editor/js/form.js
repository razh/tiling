var Form = (function() {
  return {
    createIntegerForm: function( options ) {
      var $id    = options.$id    || $( 'body' ),
          object = options.object || {},
          name   = options.name   || '',
          getter = options.getter || '',
          setter = options.setter || '',
          min    = options.min    || 0,
          max    = options.max    || 100,
          step   = options.step   || 1;

      var getterFunction = object[ getter ],
          setterFunction = object[ setter ];

      if ( $id.length === 0 ||
            getterFunction === undefined ||
            setterFunction === undefined ) {
        return;
      }

      var value = getterFunction.call( object );

      var simple = options.simple || false;
      var form = '';

      if ( !simple ) {
        form += '<div class ="control-group">';
        form += '<label class="control-label span4" for="' + name + '">' + name + '</label>';
        form += '<div class="controls span8">';
      }

      form += '<input class="input-small" type="number" id = "' +
              name  + '" value="' +
              value + '" min="'   +
              min   + '" max="'   +
              max   + '" step="'  +
              step  + '">';

      if ( !simple ) {
        form += '</div></div>';
      }

      $id.append( form );
      $id.find( '#' + name ).change(function() {
        var $this = $( this );
        var temp = parseInt( $this.val(), 10 );

        if ( temp < min ) {
          temp = min;
        } else if ( temp > max ) {
          temp = max;
        }

        $this.val( temp );

        setterFunction.call( object, temp );
      });
    },

    createFloatForm: function( options ) {
      var $id    = options.$id    || $( 'body' ),
          object = options.object || {},
          name   = options.name   || '',
          getter = options.getter || '',
          setter = options.setter || '',
          min    = options.min    || 0.0,
          max    = options.max    || 1.0,
          step   = options.step   || 1,
          digits = options.digits || 2;

      var getterFunction = object[ getter ],
          setterFunction = object[ setter ];

      if ( $id.length === 0 ||
            getterFunction === undefined ||
            setterFunction === undefined ) {
        return;
      }

      var value = getterFunction.call( object );

      var simple = options.simple || false;
      var form = '';

      if ( !simple ) {
        form += '<div class ="control-group">';
        form += '<label class="control-label span4" for="' + name + '">' + name + '</label>';
        form += '<div class="controls span8">';
      }

      form += '<input class="input-small" type="number" id = "' +
              name  + '" value="' +
              value + '" min="'   +
              min.toFixed( digits )   + '" max="'   +
              max.toFixed( digits )   + '" step="'  +
              step  + '">';

      if ( !simple ) {
        form += '</div></div>';
      }

      $id.append( form );
      $id.find( '#' + name ).change(function() {
        var $this = $( this );
        var temp = parseFloat( $this.val() ).toFixed( digits );

        if ( temp < min ) {
          temp = min;
        } else if ( temp > max ) {
          temp = max;
        }

        $this.val( temp );

        setterFunction.call( object, temp );
      });
    },

    createTextForm: function( options ) {
      var $id    = options.$id    || $( 'body' ),
          object = options.object || {},
          name   = options.name   || '',
          getter = options.getter || '',
          setter = options.setter || '';

      var getterFunction = object[ getter ],
          setterFunction = object[ setter ];

      if ( $id.length === 0 ||
            getterFunction === undefined ||
            setterFunction === undefined ) {
        return;
      }

      var value = getterFunction.call( object );

      var simple = options.simple || false;
      var form = '';

      if ( !simple ) {
        form += '<div class="control-group">';
        form += '<label class="control-label span4" for="' + name + '">' + name + '</label>';
        form += '<div class="controls span8">';
      }

      form += 'input class="input-small" type="text" id="' +
              name  + '" value=' +
              value + '">';

      if ( !simple ) {
        form += '</div></div>';
      }

      $id.append( form );
      $id.find( '#' + name ).change(function() {
        setterFunction.call( object, $( this ).val() );
      });
    },

    createColorForm: function( options ) {
      var $id    = options.$id    || $( 'body' ),
          object = options.object || {},
          getter = options.getter || '';

      var getterFunction = object[ getter ];

      if ( $id.length === 0 ||
            getterFunction === undefined ) {
        return;
      }

      var color = getterFunction.call( object );

      // Red.
      var intOptions = {
        $id:    $id,
        object: color,
        name:   'red',
        getter: 'getRed',
        setter: 'setRed',
        min:    0,
        max:    255,
        step:   1
      };

      Form.createIntegerForm( intOptions );

      // Green.
      intOptions.name = 'green';
      intOptions.getter = 'getGreen';
      intOptions.setter = 'setGreen';

      Form.createIntegerForm( intOptions );

      // Blue.
      intOptions.name = 'blue';
      intOptions.getter = 'getBlue';
      intOptions.setter = 'setBlue';

      Form.createIntegerForm( intOptions );

      // Alpha.
      var floatOptions = {
        $id:    $id,
        object: color,
        name:   'alpha',
        getter: 'getAlpha',
        setter: 'setAlpha',
        min:    0.0,
        max:    1.0,
        step:   0.01
      };

      Form.createFloatForm( floatOptions );
    },

    createModal: function( options ) {
      var $id   = options.$id   || $( 'body' ),
          name  = options.name  || '',
          type  = options.type  || ModalType.EXPORT,
          label = options.label || '',
          rows  = options.rows  || 5;
      console.log( $id)

      var modal = '<div class="modal hide fade" id="'+
                  name + '-modal" tabindex="-1" role="dialog" aria-labelledby="' +
                  name + '-modal-label" aria-hidden="true">';
      modal += '<div class="modal-header">';
      modal += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>';
      modal += '<h3 id="' +
               name + '-modal-label">' +
               label + '</h3>';
      modal += '</div>';
      modal += '<div class="modal-body">';
      modal += '<textarea id="' +
               name + '-text-area" rows="' +
               rows + '"></textarea>';
      modal += '</div>';
      modal += '<div class="modal-footer">';
      modal += '<button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>'
      if ( type === ModalType.LOAD ) {
        modal += '<button class="btn btn-primary">' +
                 label + '</button>';
      }
      modal += '</div>';
      modal += '</div>';
      console.log( modal );

      $id.append( modal );
    }
  };
}) ();


var ModalType = {
  LOAD: 0,
  EXPORT: 1
};
