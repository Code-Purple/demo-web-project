(function($) {

	$(document).ready(function() {

		try {
			var recognition = new webkitSpeechRecognition();
		} catch(e) {
			var recognition = Object;
		}
		recognition.continuous = true;
		recognition.interimResults = false;
		recognition.lang = 'en';

		var interimResult = '';
		var textArea = $('#speech-page-content');
		var textAreaID = 'speech-page-content';
		var confidenceThresh = 0.5;

		// function called when enable is pressed
		$("#enable-speech").click(function(){
			startRecognition();
		});

		var startRecognition = function() {
			textArea.focus();
			recognition.start();
		};

		// function that checks existence of s in str
		var userSaid = function(str, s) {
			return str.indexOf(s) > -1;
		}

		recognition.onresult = function (event) 
		{
			var text;
			for (var i = event.resultIndex; i < event.results.length; ++i) 
			{
				if (event.results[i].isFinal) 
				{
					if (parseFloat(event.results[i][0].confidence) >= parseFloat(confidenceThresh))
					{
						text = event.results[i][0].transcript;
						insertAtCaret(textAreaID, event.results[i][0].transcript);

						// test to see if word recognizer works
						if (userSaid(text, 'ad'))
						{
							console.log('add')
						}

					}
				}
			}
		};
	});
})(jQuery);