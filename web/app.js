
var connect = require('/Users/mlippert/Entwicklung/express/support/connect/lib/connect');
var ejs = require('/Users/mlippert/Entwicklung/express/support/ejs');
var express = require('/Users/mlippert/Entwicklung/express/lib/express');
var io = require('/Users/mlippert/Entwicklung/socket.io-node');
var Buffer = require('buffer').Buffer;

var PresentationProvider = require('./presentation-provider').PresentationProvider;
var presentationProvider = new PresentationProvider();

var app = express.createServer(
		connect.logger(),
	    connect.bodyDecoder()
);

app.configure(function(){
    app.use(express.staticProvider(__dirname + '/public'));
});

app.set('view engine', 'ejs');
app.set('views', __dirname + '/views');


var home = function(req, res) {
    presentationProvider.findAllOverview(function(error, result) {
        res.render('overview', {
            locals: {
        		presentations: result
            }
        });
    });
}

var upload = function(req, res) {
    presentationProvider.save(req.body, function(error, result) {
        res.send(JSON.stringify(result), { 'Content-Type': 'application/json' }, 200);            
    });
}

var getimage = function(req, res) {
    presentationProvider.findById(req.params.id, function(error, result) {
    	var image = result.shapes[req.params.image].image;
    	var bufferedImage = new Buffer(image, "base64");
    	res.contentType(result.shapes[req.params.image].imagetype);
    	res.send(bufferedImage);
    });
}

var presentation = function(req, res) {
    presentationProvider.findById(req.params.id, function(error, result) {
        res.render('presentation', {
            locals: {
        		presentation: req.params.id,
                noshapes: result.shapes.length
            }
        });
    });
}

app.get('/', home);
app.put('/upload', upload);
app.get('/:id/:image', getimage);
app.get('/:id', presentation);

app.listen(3000);
console.log('Express server started on port %s', app.address().port);

var socket = io.listen(app);
socket.on('connection', function(client) {

	  client.on('message', function(message) {
		  console.log(message);

		  try {
			  request = JSON.parse(message);
		  } catch (SyntaxError) {
			  log('Invalid JSON:');
			  log(message);
			  return false;
		  }

		  request.id = client.sessionId
		  client.broadcast(JSON.stringify(request));
	  });

	  client.on('disconnect', function() {
		  console.log('socket disconnect');
	  })
});
