#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#include <Servo.h>
#include <OneWire.h>
#include <DallasTemperature.h>
#define FIREBASE_HOST "automaticfishfeeder-7941e-default-rtdb.firebaseio.com" // Firebase host
#define FIREBASE_AUTH "AQWNYi5n5m2UOLpUsPGjDtMvq7V0chJiRIOv38hq" //Firebase Auth code
#define WIFI_SSID "Randula" //Enter your wifi Name
#define WIFI_PASSWORD "pass@123" // Enter your password
#define ONE_WIRE_BUS 2
#define motorPinA  16
#define motorPinB  5
#define motorPinC  0
#define motorPinD  14
int fireStatus = 0;
int waterStatus=0;
int temp = 0;
#define trigPin 12
#define echoPin 13
long duration;
int distance;
Servo servo;
OneWire oneWire(ONE_WIRE_BUS);
DallasTemperature sensors(&oneWire);

void setup() {
  Serial.begin(9600);
  servo.attach(D2);
  sensors.begin();
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
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

  fishFeeding();
  temperature();
  waterLevel();
  manualFishWaterChange();
  
}

void temperature() {
  sensors.requestTemperatures();
  temp = (int)sensors.getTempCByIndex(0);
  Firebase.setInt("SensorData/15267/temp", temp);
}

void waterLevel() {
  // Clears the trigPin
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  // Sets the trigPin on HIGH state for 10 micro seconds
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);

  // Reads the echoPin, returns the sound wave travel time in microseconds
  duration = pulseIn(echoPin, HIGH);

  // Calculate the distance
  distance = duration * 0.034 / 2;
  distance = map(distance, 0, 15, 100, 0);
  //distance = map(distance,0,78, 0, 100);
  Serial.println(distance);
  Firebase.setInt("SensorData/15267/waterLevel", (int)distance);
}

void fishFeeding()
{
  fireStatus = Firebase.getInt("FishFeeding/15267/triggerValue");

  if (fireStatus == 1) {
    servo.attach(D2);
    servo.write(360);
  }
  else {
    servo.detach();
  }
}

void manualFishWaterChange()
{
  waterStatus = Firebase.getInt("WaterChange/15267/triggerValue");

  if (waterStatus == 1) {
    waterPumpOutOn();
    waterPumpOutOff();
    waterPumpInOn();
    waterPumpInOff();
    Firebase.setInt("WaterChange/15267/triggerValue", 0);
  }
  
}

void waterPumpOutOn()
{
  if (distance >= 3)
    digitalWrite(motorPinA, HIGH);
  digitalWrite(motorPinB, LOW);
  digitalWrite(motorPinC, LOW);
  digitalWrite(motorPinD, LOW);
}

void waterPumpOutOff()
{
  if (distance >= 14)
    digitalWrite(motorPinA, LOW);
  digitalWrite(motorPinB, LOW);
  digitalWrite(motorPinC, LOW);
  digitalWrite(motorPinD, LOW);
}

void waterPumpInOn()
{
  if (distance >= 14 && distance >= 3)
    digitalWrite(motorPinA, LOW);
  digitalWrite(motorPinB, LOW);
  digitalWrite(motorPinC, HIGH);
  digitalWrite(motorPinD, LOW);
}

void waterPumpInOff()
{
  if (distance <= 3)
    digitalWrite(motorPinA, LOW);
  digitalWrite(motorPinB, LOW);
  digitalWrite(motorPinC, LOW);
  digitalWrite(motorPinD, LOW);
}
