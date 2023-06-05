#include "DHT.h"
#define LED_PIN 13
#define DHTPIN 14
#define DHTTYPE DHT11

DHT dht(DHTPIN, DHTTYPE);

void setup() {
  Serial.begin(9600);
  dht.begin();
  Serial.println("DHTxx test!");
  pinMode(LED_BUILTIN, OUTPUT);
  pinMode(LED_PIN, OUTPUT);
}

void loop() {
  // displayTemp()
  // led()
}

void led() {
  digitalWrite(LED_BUILTIN, LOW);
  digitalWrite(LED_PIN, LOW);
  delay(1000);
  digitalWrite(LED_BUILTIN, HIGH);
  digitalWrite(LED_PIN, HIGH);
}

void displayTemp() {
  Serial.println("Temperature = " + String(dht.readTemperature()) + " °C");
  delay(1000);
}
