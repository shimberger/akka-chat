$(document).ready(function() {
	$('#message').css('background-color','red');	
	if ("WebSocket" in window) {
    	var ws = new WebSocket("ws://localhost:9998/ws/");		
	    ws.onopen = function() {
	        $('#message').css('background-color','green');
			$('#msg-form').submit(function() {
				ws.send($('#message').val());
				$('#message').val('');
				return false;
			});
	    };		
    	ws.onclose = function() {
	        $('#message').css('background-color','red');		
		};	
    	ws.onmessage = function (evt) { 
			var msg = evt.data; 
			$('#window').append(msg + '<br/>');
		};		
	} else {
		alert("No WebSockets support in your browser! Sorry pal!")
	}
});