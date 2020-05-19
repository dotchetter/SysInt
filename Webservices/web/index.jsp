<html>
  <head>
    <title></title>
  </head>
  <body>

  <h1>Realtidsdata</h1>
  <div id="temperature"></div>
  <div id="humidity"></div>
  <div id="lumen"></div>
  <h1>Databas</h1>
  <div id="database"></div>

  <script>
    let ws = new WebSocket("ws://localhost:8080/Webservices_war_exploded/live/temperature");
    ws.onmessage = function(e)
    {
      document.getElementById("temperature").innerHTML = e.data;
    }

     fetch("http://localhost:8080/Webservices_war_exploded/SensorsDb")
             .then(response => response.text())
             .then(data => document.getElementById("database").innerHTML = data);

  </script>
  </body>
</html>
