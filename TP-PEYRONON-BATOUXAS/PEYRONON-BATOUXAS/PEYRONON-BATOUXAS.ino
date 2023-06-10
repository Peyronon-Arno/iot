
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
      WiFi.begin(ssid.c_str(), password.c_str());
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
  server.begin();
  server.on("/getTemperature", HTTP_GET, getTemperature);
  server.on("/getHumidity", HTTP_GET, getHumidity);
  server.on("/saveCredentials", HTTP_POST, saveCredentials);
}

void getTemperature() {
  server.send(200, "text/plain", "Temperature de la salle :" + String(dht.readTemperature()) + " °C\r\n");
}

void getHumidity() {
  server.send(200, "text/plain", "Humidité de la salle :" + String(dht.readHumidity()) + " %\r\n");
}

void saveCredentials() {
  if (server.args() > 0) {
    server.arg("ssid");
    server.arg("password");
  }
  String ssidGet = "Iphone de Arno";
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

void loop() {
  server.handleClient();
}
