<html>
  <head>
    <title></title>
  </head>
  <body>
  <h1>Realtidsdata</h1>
  <div id="TEMPERATURE"></div>
  <div id="HUMIDITY"></div>
  <div id="LUMEN"></div>
  <h1>Databas</h1>
  <div id="database"</body>

  <script>
    let ws = new WebSocket("ws://localhost:8080/Webservices_war_exploded/SensorsRealTime");
    ws.onmessage = function(e)
    {
      let data = JSON.parse(e.data);
      document.getElementById(data.sensorType).innerHTML = e.data;
    }

     fetch("http://localhost:8080/Webservices_war_exploded/SensorsDb")
             .then(response => response.text())
             .then(data => document.getElementById("database").innerHTML = data);

  </script>
  </body>
</html>
