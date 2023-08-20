#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#define FIREBASE_HOST "automaticfishfeeder-7941e-default-rtdb.firebaseio.com" // Firebase host
#define FIREBASE_AUTH "AQWNYi5n5m2UOLpUsPGjDtMvq7V0chJiRIOv38hq" //Firebase Auth code
#define WIFI_SSID "MTN-MobileWiFi-E5573" //Enter your wifi Name
#define WIFI_PASSWORD "QFBB1YHH" // Enter your password
#define motorPinA  16
#define motorPinB  5
#define motorPinC  0
#define motorPinD  14
int waterIn=0;
int waterOut=0;

void setup() {
  Serial.begin(9600);
  pinMode(motorPinA, OUTPUT);
  pinMode(motorPinB, OUTPUT);
  pinMode(motorPinC, OUTPUT);
  pinMode(motorPinD, OUTPUT);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.println("Connected.");
  Serial.println(WiFi.localIP());
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
}

void loop() {
  manualFishTankWaterIn();
  manualFishTankWaterOut();
  
}

void manualFishTankWaterIn()
{
  waterIn = Firebase.getInt("WaterIn/15267/triggerValue");

  if (waterIn == 1) {
    waterPumpInOn();
  }else{
    waterPumpInOff();
  }
  
}

void manualFishTankWaterOut()
{
  waterOut = Firebase.getInt("WaterOut/15267/triggerValue");

  if (waterOut == 1) {
    waterPumpOutOn();
  }else{
    waterPumpOutOff();
  }
  
}

void waterPumpOutOn()
{
  digitalWrite(motorPinA, HIGH);
  digitalWrite(motorPinB, LOW);
  digitalWrite(motorPinC, LOW);
  digitalWrite(motorPinD, LOW);
}

void waterPumpOutOff()
{
  digitalWrite(motorPinA, LOW);
  digitalWrite(motorPinB, LOW);
  digitalWrite(motorPinC, LOW);
  digitalWrite(motorPinD, LOW);
}

void waterPumpInOn()
{
  digitalWrite(motorPinA, LOW);
  digitalWrite(motorPinB, LOW);
  digitalWrite(motorPinC, HIGH);
  digitalWrite(motorPinD, LOW);
}

void waterPumpInOff()
{
  digitalWrite(motorPinA, LOW);
  digitalWrite(motorPinB, LOW);
  digitalWrite(motorPinC, LOW);
  digitalWrite(motorPinD, LOW);
}
