$(document).ready(
		function() {

			currentTranslateX = 0;
			
			var path = document.location.pathname;
			if (path.length > 1) {
				presentationID = path.substring(1);
			}
			
			$("#images").css("-webkit-transform",
					"translateX(" + currentTranslateX + "px)");
			$("#images").css("-moz-transform",
					"translateX(" + currentTranslateX + "px)");
			$("#images").css("-o-transform",
					"translateX(" + currentTranslateX + "px)");
			$("#images").css("transform",
					"translateX(" + currentTranslateX + "px)");

			var width = $("#images img").width();
			var imagecount = $("#images img").length;
			$("#images").css("width", (imagecount * width) + "px");

			$("#next").click(next);
			$("#prev").click(prev);
			
	        if ('createTouch' in document) {
				$("#images").get(0).addEventListener('touchstart', touchstart, false);
				$("#images").get(0).addEventListener('touchend', touchend, false);
	        } else {
				$("#images").get(0).addEventListener('mousedown', mousedown, false);
				$("#images").get(0).addEventListener('mouseup', mouseup, false);
	        }
			
		});

mousedown = function(event) {
	startX = event.pageX;
    event.preventDefault()
}

mouseup = function(event) {
	var move = event.pageX - startX;
	if (move < 0) {
		next();
	}
	else {
		prev();
	}
}

touchstart = function(event) {
    var touch = event.targetTouches[0]
    startX = touch.pageX
}

touchend = function(event) {
    var touch = event.changedTouches[0]
	var move = touch.pageX - startX;
	if (move < 0) {
		next();
	}
	else {
		prev();
	}
}

next = function() {
	var width = $("#images img").width();
	var maxWidth = width * $("#images img").length;

	var transformProperty = $("#images").css("-webkit-transform")
			|| $("#images").css("-moz-transform")
			|| $("#images").css("-o-transform")
			|| $("#images").css("transform");

	var currentTranslateX = parseInt(transformProperty.replace(/translateX\(/i,
			""));

	if (currentTranslateX - width > -maxWidth) {
		currentTranslateX -= width;
		$("#images").css("-webkit-transform",
				"translateX(" + currentTranslateX + "px)");
		$("#images").css("-moz-transform",
				"translateX(" + currentTranslateX + "px)");
		$("#images").css("-o-transform",
				"translateX(" + currentTranslateX + "px)");
		$("#images")
				.css("transform", "translateX(" + currentTranslateX + "px)");

		var selectedShapeNo = currentTranslateX / width * -1;

		socket.send(JSON.stringify( {
			action : 'focusChanged',
			shapeNo : selectedShapeNo,
			presentation: presentationID
		}));
	}
};

prev = function() {
	var width = $("#images img").width();
	var maxWidth = width * $("#images img").length;

	var transformProperty = $("#images").css("-webkit-transform")
			|| $("#images").css("-moz-transform")
			|| $("#images").css("-o-transform")
			|| $("#images").css("transform");

	var currentTranslateX = parseInt(transformProperty.replace(/translateX\(/i,
			""));

	if (currentTranslateX + width <= 0) {
		currentTranslateX += width;
		$("#images").css("-webkit-transform",
				"translateX(" + currentTranslateX + "px)");
		$("#images").css("-moz-transform",
				"translateX(" + currentTranslateX + "px)");
		$("#images").css("-o-transform",
				"translateX(" + currentTranslateX + "px)");
		$("#images")
				.css("transform", "translateX(" + currentTranslateX + "px)");

		var selectedShapeNo = currentTranslateX / width * -1;

		socket.send(JSON.stringify( {
			action : 'focusChanged',
			shapeNo : selectedShapeNo,
			presentation : presentationID
		}));
	}
};
