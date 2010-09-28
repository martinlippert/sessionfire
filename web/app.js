
/**
 * Module dependencies.
 */
var connect = require('/Users/mlippert/Entwicklung/express/support/connect/lib/connect');
var ejs = require('/Users/mlippert/Entwicklung/express/support/ejs');
var express = require('/Users/mlippert/Entwicklung/express/lib/express');
var io = require('/Users/mlippert/Entwicklung/socket.io-node');
var Buffer = require('buffer').Buffer;

var body;

var app = express.createServer(
		connect.logger(),
	    connect.bodyDecoder()
);

app.configure(function(){
    app.use(express.staticProvider(__dirname + '/public'));
});

app.set('view engine', 'ejs');
app.set('views', __dirname + '/views');


var func = function(req, res) {
    res.send('Hello World');
}

var upload = function(req, res) {
	body = req.body;
	res.send(JSON.stringify({'id': 'meine id', 'action': 'meine action'}),
			                { 'Content-Type': 'application/json' });
}

var getimage = function(req, res) {
	var id = req.params.id;
	var image = body.shapes[id].image;
	
	res.contentType(body.shapes[id].imagetype);
    var bufferedImage = new Buffer(image, "base64");
	res.send(bufferedImage);
}

app.get('/', func);
app.put('/upload', upload);
app.get('/presentation/:id', getimage);


app.get('/presentation', function(req, res){
    res.render('presentation', {
        locals: {
            noshapes: body.shapes.length
        }
    });
});


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
