<%@ tag language="java" pageEncoding="US-ASCII"%>
<%@tag description="Song Information Page Tags" pageEncoding="UTF-8"%>

<%@ attribute name="title" required="true" rtexprvalue="true" %>
<%@ attribute name="song_title" required="true" rtexprvalue="true" %>
<%@ attribute name="artist" required="true" rtexprvalue="true" %>
<%@ attribute name="duration" required="true" rtexprvalue="true" %>
<%@ attribute name="released" required="true" rtexprvalue="true" %>
<%@ attribute name="karaoke_url" required="true" rtexprvalue="true" %>
<%@ attribute name="bg" required="true" rtexprvalue="true"%>
<%@ attribute name="album_cover" required="true" rtexprvalue="true"%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>${title }</title>
		<script type="text/javascript" src="../js/cufon-yui"></script>
		<script type="text/javascript" src="../js/jquery.min.js"></script>
		<link type="text/css" rel="stylesheet" href="../css/song_info_template.css"/>
		<style type="text/css">
			body {
				background: url(${bg })  center bottom no-repeat;
			}
			footer {
				background: url(${album_cover })  center no-repeat;
			
			}
		</style>
	
	</head>
	<html>
	<body>
		<div class="black"></div>
		<div id="wrapper">
			<div id="header">
				<h1>${song_title }</h1>
				<h2>${artist }  .  ${duration }  .  ${released }</h2>
			</div>
			<div id="middle">
				<a href="karaok.in/index.html" class="with_border">Return Home</a>
				<a href=${karaoke_url class="with_bg">Play Now!</a>
			</div>
			<div id="footer">
			</div>
		</div>
	</body>
</html>