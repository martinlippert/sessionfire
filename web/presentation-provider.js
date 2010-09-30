sys = require('sys')

var idCounter = 0;

PresentationProvider = function() {};
PresentationProvider.prototype.stored = [];
exports.PresentationProvider = PresentationProvider;

PresentationProvider.prototype.findById = function(id, callback) {
  var result = null;
  for (var i = 0; i < this.stored.length; i++) {
    if (this.stored[i].id == id ) {
      result = this.stored[i];
      break;
    }
  }
  callback(null, result);
};

PresentationProvider.prototype.findIndexById = function(id) {
	var index = undefined;
	for (var i = 0; i < this.stored.length; i++) {
		if (this.stored[i].id == id ) {
			index = i;
			break;
		}
	}
	if(index + '' === 'undefined') {
		index = idCounter++
	}
	return index;
};

PresentationProvider.prototype.save = function(data, callback) {
    var index = this.findIndexById(data.id);
    if(!data.id || data.id == 'null' || data.id == 'undefined') {
        data.id = "" + index;
    }
    this.stored[index] = data;
    
    sys.print('Saved! Now we have ' + this.stored.length + ' presentations stored.\n');
    var error = null;
    callback(error, JSON.stringify({'id': 'meine id', 'action': 'meine action'}));
};
