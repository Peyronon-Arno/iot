#include <LittleFS.h>

//littlefs
// Connectivité wifi
// a) client | (ssid, pwd)
// si ko
// b) ssid esp + mac, pwd
// dht (capteur temp)
// service getTemp / setCredentials
// mDNS--> Taper http://esp.local au lieu de taper une adresse ip

#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>
#include "DHT.h"
#include "LittleFS.h"

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
  WiFi.mode(WIFI_STA);
  LittleFS.setConfig(LittleFSConfig);
  if (!fileSystem->begin()) {
    Serial.println("Erreur lors de l'initialisation de LittleFS");
    return;
  }
  tryConnection();
  Serial.print("Échec de connexion au réseau");
  Serial.println(ssid);

  createEndpoint();
  server.begin();

  server.on("/getTemperature", HTTP_GET, getTemperature);
  server.on("/getHumidity", HTTP_GET, getHumidity);
  server.on("/saveCredentials", HTTP_POST, saveCredentials);
}

void tryConnection() {
  unsigned long startTime = millis();
  // Tentative de connexion au premier réseau
  readCredentials();
  if (ssid != "" && password != "") {
    while (millis() - startTime < 10000) {
      Serial.println("On essaie de se connecter");
      Serial.println(ssid + " " + password);
      Serial.println(ssid.c_str());
      Serial.println(password.c_str());
      WiFi.begin(ssid.c_str(), password.c_str());
      Serial.println(".");
      if (WiFi.waitForConnectResult() == WL_CONNECTED) {
        Serial.println("Connexion établie au réseau Wi-Fi");
        Serial.print("Adresse IP: ");
        Serial.println(WiFi.localIP());
        return;
      }
      delay(500);
    }
  }
}

void createEndpoint() {
  // Si tentative échouée --> création du point d'accès
  WiFi.mode(WIFI_AP);
  WiFi.softAP("ESP8862-" + String(WiFi.macAddress()), "MotDePasse");
  Serial.println("Point d'accès SoftAP créé");
  Serial.println("Adresse IP du point d'accès: ");
  Serial.println(WiFi.softAPIP());
}

void getTemperature() {
  server.send(200, "text/plain", "Temperature de la salle :" + String(dht.readTemperature()) + " °C\r\n");
}

void getHumidity() {
  server.send(200, "text/plain", "Humidité de la salle :" + String(dht.readHumidity()) + " %\r\n");
}

void saveCredentials() {
  if (server.args() > 0) {
    String ssidGet = server.arg("ssid");
    String pwdGet = server.arg("password");
    File configFile = fileSystem->open("/credentials.txt", "w");
    if (!configFile) {
      Serial.println("Erreur lors de l'ouverture du fichier de configuration");
      return;
    }
    configFile.println(ssidGet);
    configFile.println(pwdGet);
    configFile.close();
  }
  server.send(200, "text/plain", "Credentials ok");
}

void readCredentials() {
  File configFile = fileSystem->open("/credentials.txt", "r");
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
  //Serial.println("Temperature = " + String(dht.readTemperature()) + " °C");
  //delay(1000);
}
