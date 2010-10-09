io.setPath('/js/socket.io/');

socket = new io.Socket();

if (socket.connect()) {
	socket.on('message', function(message) {
		window.console.log('message arrived' + message);
		message = JSON.parse(message);

		if (message['action'] == 'focusChanged' && message['presentation'] == presentationID) {
			var shapeNo = message['shapeNo'];

			var width = $("#images img").width();
			currentTranslateX = width * -1 * shapeNo;
			$("#images").css("-webkit-transform",
					"translateX(" + currentTranslateX + "px)");
			$("#images").css("-moz-transform",
					"translateX(" + currentTranslateX + "px)");
			$("#images").css("-o-transform",
					"translateX(" + currentTranslateX + "px)");
			$("#images").css("transform",
					"translateX(" + currentTranslateX + "px)");
		}
	});
};
