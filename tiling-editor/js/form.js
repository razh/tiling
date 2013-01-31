var Form = (function() {
  return {
    createIntegerForm: function( options ) {
      var $id    = options.$id    || [],
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

      var form = '<div class ="control-group">';
      form += '<label class="control-label span3" for="' + name + '">' + name + '</label>';
      form += '<div class="controls span9">';
      form += '<input class="input-medium" type="number" id = "' +
              name  + '" value = "' +
              value + '" min="'   +
              min   + '" max="'   +
              max   + '" step="'  +
              step  + '">';
      form += '</div></div>';

      $id.append( form );
      $id.find( '#' + name ).change( function() {
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
      var $id    = options.$id    || [],
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

      var form = '<div class ="control-group">';
      form += '<label class="control-label span3" for="' + name + '">' + name + '</label>';
      form += '<div class="controls span9">';
      form += '<input class="input-medium" type="number" id = "' +
              name  + '" value = "' +
              value + '" min="'   +
              min.toFixed( digits )   + '" max="'   +
              max.toFixed( digits )   + '" step="'  +
              step  + '">';
      form += '</div></div>';


      $id.append( form );
      $id.find( '#' + name ).change( function() {
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
      var $id    = options.$id    || [],
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

      // TODO: Not implemented.
    },

    createColorForm: function( options ) {
      var $id    = options.$id    || [],
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
    }
  };
}) ();
