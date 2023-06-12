
//littlefs
// Connectivité wifi
// a) client | (ssid, pwd)
// si ko
// b) ssid esp + mac, pwd
// dht (capteur temp)
// service getTemp / setCredentials
// mDNS--> Taper http://esp.local au lieu de taper une adresse ip

#include <LittleFS.h>
#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>
#include "DHT.h"
#include "LittleFS.h"
#include <ESP8266mDNS.h>
#include <ArduinoJson.h>
#include <ESP8266HTTPClient.h>

#define DHTPIN 2
#define DHTTYPE DHT11
DHT dht(DHTPIN, DHTTYPE);

ESP8266WebServer server(80);
LittleFSConfig LittleFSConfig;
FS* fileSystem = &LittleFS;

String ssid;
String password;

void setup() {
  Serial.begin(115200);
  delay(1000);
  dht.begin();
  LittleFS.setConfig(LittleFSConfig);
  if (!fileSystem->begin()) {
    Serial.println("Erreur lors de l'initialisation de LittleFS");
    return;
  }  
  server.begin();
  server.on("/getTemperature", HTTP_GET, getTemperature);
  server.on("/getHumidity", HTTP_GET, getHumidity);
  server.on("/saveCredentials", HTTP_POST, saveCredentials);
  server.on("/averagetemp", HTTP_GET, handleAverageTemperature);
  tryConnection();
}

void tryConnection() {
  unsigned long startTime = millis();
  readCredentials();
  WiFi.mode(WIFI_STA);

  if (ssid != "" && password != "") {
    while (millis() - startTime < 30000) {
      Serial.println("On essaie de se connecter");
      ssid.trim();
      password.trim();
      Serial.println("...");
      //WiFi.begin(ssid.c_str(), password.c_str());
      WiFi.begin("info-licence-a", "pm01web02pm03web04");
      if (WiFi.waitForConnectResult() == WL_CONNECTED) {
        Serial.println("Connexion établie au réseau Wi-Fi");
        Serial.print("Adresse IP: ");
        Serial.println(WiFi.localIP());
        MDNS.begin(ssid);
        MDNS.addService("http", "tcp", 80);
        return;
      }
      delay(500);
    }
  } else {
    Serial.print("Échec de connexion au réseau");
    Serial.println(ssid);
    createEndpoint();
  }
}

void createEndpoint() {
  WiFi.mode(WIFI_AP);
  WiFi.softAP("ESP8862-" + String(WiFi.macAddress()), "MotDePasse");
  Serial.println("Point d'accès SoftAP créé");
  Serial.println("Adresse IP du point d'accès: ");
  Serial.println(WiFi.softAPIP());
}

void getTemperature() {
  StaticJsonDocument<200> jsonDoc;
  jsonDoc["temperature"] = dht.readTemperature();
  String jsonString;
  serializeJson(jsonDoc, jsonString);
  server.send(200, "application/json", jsonString);
}

void getHumidity() {
  StaticJsonDocument<200> jsonDoc;
  jsonDoc["humidity"] = dht.readTemperature();
  String jsonString;
  serializeJson(jsonDoc, jsonString);
  server.send(200, "application/json", jsonString);
}

void saveCredentials() {
  if (server.args() > 0) {
    server.arg("ssid");
    server.arg("password");
  }
  String ssidGet = "";
  String pwdGet = "arno2002";
  File configFile = fileSystem->open("/test.txt", "w");
  if (!configFile) {
    Serial.println("Erreur lors de l'ouverture du fichier de configuration");
    return;
  }
  configFile.println(ssidGet);
  configFile.println(pwdGet);
  configFile.close();
  server.send(200, "text/plain", "Credentials ok");
}

void readCredentials() {
  File configFile = fileSystem->open("/test.txt", "r");
  if (!configFile) {
    Serial.println("Erreur lors de l'ouverture du fichier de configuration");
    return;
  }
  ssid = configFile.readStringUntil('\n');
  password = configFile.readStringUntil('\n');
  configFile.close();
  Serial.print("SSID : ");
  Serial.println(ssid);
  Serial.print("Password : ");
  Serial.println(password);
}

float getRemoteTemperature() {
  WiFiClient client;
  HTTPClient http;

  http.begin(client, "http://192.168.1.92/temperature");

  int httpResponseCode = http.GET();

  float remoteTemperature = 0.0;  // Valeur par défaut si la requête échoue

  if (httpResponseCode == HTTP_CODE_OK) {
    Serial.println("response OK")
    String response = http.getString();
    Serial.println(response);
     // Créer un objet JSON
    DynamicJsonDocument doc(256);
    
    // Désérialiser la réponse JSON
    DeserializationError errordeserializejson = deserializeJson(doc, response);
    
    // Vérifier s'il y a eu une erreur lors de la désérialisation
    if (errordeserializejson) {
        Serial.print("Erreur lors de la désérialisation : ");
        Serial.println(error.c_str());
        return;
    }
    
    // Récupérer la valeur de "temperature"
    float temperature = doc["temperature"];
    
    // Afficher la valeur récupérée
    Serial.print("Température reçu : ");
    Serial.println(temperature);
    remoteTemperature = response.toFloat();
  }

  http.end();

  return remoteTemperature;
}

// Calculer la moyenne des températures reçues
float calculateAverageTemperature() {
  float remoteTemperature = getRemoteTemperature();
  return (dht.readTemperature() + remoteTemperature) / 2;
}

void handleAverageTemperature() {
  float averageTemperature = calculateAvera
  geTemperature();
  
  StaticJsonDocument<200> jsonDoc;
  
  if (isnan(averageTemperature)) {
    jsonDoc["error"] = "Erreur lors de la lecture de la température !";
  } else {
    jsonDoc["temperature"] = temperature;
  }
  
  String response;
  serializeJson(jsonDoc, response);
  
  server.send(200, "application/json", response);
}

void loop() {
  server.handleClient();
}
