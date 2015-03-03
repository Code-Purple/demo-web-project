(function($) {

	$(document).ready(function() {

		try {
			var recognition = new webkitSpeechRecognition();
		} catch(e) 
		{
			var recognition = Object;
		}
		recognition.continuous = true;
		recognition.interimResults = true;

		var finalText = '';
		var interimResult = '';
		var textArea = $('#speech-page-content');
		var textAreaID = 'speech-page-content';

		$("#enable-speech").click(function()
				{
			startRecognition();
				});

		var startRecognition = function() 
		{
			textArea.focus();
			recognition.start();
		};

		$("#disable-speech").click(function()
				{
			recognition.stop();
				});

		recognition.onresult = function (event) 
		{
			var pos = textArea.getCursorPosition() - interimResult.length;
			textArea.val(textArea.val().replace(interimResult, ''));
			interimResult = '';
			textArea.setCursorPosition(pos);
			for (var i = event.resultIndex; i < event.results.length; ++i) {
				if (event.results[i].isFinal) 
				{
					finalText += event.results[i][0].transcript;
					insertAtCaret(textAreaID, event.results[i][0].transcript);
				} else 
				{
					isFinished = false;
					insertAtCaret(textAreaID, event.results[i][0].transcript + '\u200B');
					interimResult += event.results[i][0].transcript + '\u200B';
				}
			}
		};
	});
})(jQuery);