<html>
  <head>
    <title></title>
  </head>
  <body>

  <h1>Live</h1>
  <div>
    <p>Temperature</p>
    <p id="liveTemperature"></p>
  </div>
  <div>
    <p>Humidity</p>
    <p id="liveHumidity"></p>
  </div>
  <div>
    <p>Lumen</p>
    <p id="liveLumen"></p>
  </div>
  <h1>Db</h1>
  <table>
    <tr>
      <th>Temperature</th>
      <th>Humidity</th>
      <th>Lumen</th>
    </tr>
    <tr>
      <td id = "dbTemperature"></td>
      <td id = "dbHumidity"></td>
      <td id = "dbLumen"></td>
    </tr>
  </table>

  <script>

    let tempws = new WebSocket("ws://localhost:8080/Webservices_war_exploded/live/temperature");
    tempws.onmessage = function(e)
    {
      let data = JSON.parse(e.data);
      document.getElementById("liveTemperature").innerHTML = data.value + " * " + data.created;
    }

    let humidws = new WebSocket("ws://localhost:8080/Webservices_war_exploded/live/humidity");
    humidws.onmessage = function(e)
    {
      let data = JSON.parse(e.data);
      document.getElementById("liveHumidity").innerHTML = data.value + " * " + data.created;;
    }

    let lumws = new WebSocket("ws://localhost:8080/Webservices_war_exploded/live/lumen");
    lumws.onmessage = function(e)
    {
      let data = JSON.parse(e.data);
      document.getElementById("liveLumen").innerHTML = data.value + " * " + data.created;;
    }

     fetch("http://localhost:8080/Webservices_war_exploded/db/temperature")
             .then(response => response.text())
             .then(data => document.getElementById("dbTemperature").innerHTML = data);

    fetch("http://localhost:8080/Webservices_war_exploded/db/humidity")
            .then(response => response.text())
            .then(data => document.getElementById("dbHumidity").innerHTML = data);

    fetch("http://localhost:8080/Webservices_war_exploded/db/lumen")
            .then(response => response.text())
            .then(data => document.getElementById("dbLumen").innerHTML = data);

  </script>
  </body>
</html>
