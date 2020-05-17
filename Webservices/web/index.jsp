<html>
  <head>
    <title></title>
  </head>
  <body>
  <div id="wsMsg"></div>
  <script>
    let ws = new WebSocket("ws://localhost:8080/Webservices_war_exploded/SensorsRealTime");
    ws.onmessage = function(e)
    {
      document.getElementById("wsMsg").innerHTML = e.data;
    }

    // fetch("http://localhost:8080/Webservices_war_exploded/SensorsDb")
    //         .then(response => response.text())
    //         .then(data => document.getElementById("wsMsg").innerHTML = data);

  </script>
  </body>
</html>
