<html>
  <head>
    <title></title>
  </head>
  <body>

  <h1>Realtidsdata</h1>
  <div>
    <p>Temperature</p>
    <p id="temperature"></p>
  </div>
  <div>
    <p>Humidity</p>
    <p id="humidity"></p>
  </div>
  <div>
    <p>Lumen</p>
    <p id="lumen"></p>
  </div>
  <h1>Databas</h1>
  <div id="database"></div>

  <script>

    let tempws = new WebSocket("ws://localhost:8080/Webservices_war_exploded/live/temperature");
    tempws.onmessage = function(e)
    {
      document.getElementById("temperature").innerHTML = e.data;
    }

    let humidws = new WebSocket("ws://localhost:8080/Webservices_war_exploded/live/humidity");
    humidws.onmessage = function(e)
    {
      document.getElementById("humidity").innerHTML = e.data;
    }

    let lumws = new WebSocket("ws://localhost:8080/Webservices_war_exploded/live/lumen");
    lumws.onmessage = function(e)
    {
      document.getElementById("lumen").innerHTML = e.data;
    }

     fetch("http://localhost:8080/Webservices_war_exploded/SensorsDb")
             .then(response => response.text())
             .then(data => document.getElementById("database").innerHTML = data);

  </script>
  </body>
</html>
