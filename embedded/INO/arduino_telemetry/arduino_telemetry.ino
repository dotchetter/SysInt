#include <DHT.h>
#include <SPI.h>
#include <RH_ASK.h>

#define DHT_PIN 7
#define LIGHT_PIN A0
#define DHT_TYPE DHT11
#define COMMS_KEY 21474
#define TELEMETRY_DELIVERY_INTERVAL_TIME 5000
#define LIGHT_SENSOR_MAX 1024

typedef struct {
    float humidity;
    float temperature;
    short auth;
    short light;
}TEMP_SENSOR_DATA;

typedef struct {
    char message[RH_ASK_MAX_MESSAGE_LEN] = {0};
    int auth;
}OUTGOING_MSG;

// Classes structs and variable instantiations

DHT dht(DHT_PIN, DHT_TYPE);
RH_ASK driver(2000, 11, 12, 0);
TEMP_SENSOR_DATA data_struct;
OUTGOING_MSG msg_struct;

uint8_t radio_buffer[RH_ASK_MAX_MESSAGE_LEN];
uint8_t radio_buffer_size = sizeof radio_buffer;
unsigned long last_telemetry_delivery = 0;
byte radio_send_buf[sizeof(TEMP_SENSOR_DATA)] = {0};
bool newMessage = false;

/* -- program logic begins here -- */

void setup() {
    
    Serial.begin(9600);
    dht.begin();
    //driver.init();
    //pinMode(8, OUTPUT);
    data_struct.auth = COMMS_KEY;
}

void sendTelemetryDataToServer() {
    String output = "";
          
    data_struct.humidity = dht.readHumidity();
    data_struct.temperature = dht.readTemperature();
    data_struct.light = (LIGHT_SENSOR_MAX - analogRead(LIGHT_PIN));
    data_struct.light = (data_struct.light / (LIGHT_SENSOR_MAX / 100));

    output += "<";
    output += data_struct.temperature;
    output += ";";
    output += data_struct.humidity;
    output += ";";
    output += data_struct.light;
    output += ">";
    
    Serial.println(output);
}

void loop() {
    
    if ((millis() - last_telemetry_delivery) >= TELEMETRY_DELIVERY_INTERVAL_TIME) {
        sendTelemetryDataToServer();
        last_telemetry_delivery = millis();
    }
}
