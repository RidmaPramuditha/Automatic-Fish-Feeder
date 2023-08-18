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
int fireStatus = 0;
int temp=0;
#define trigPin 12
#define echoPin 13
long duration;
int distance;
Servo servo;
OneWire oneWire(ONE_WIRE_BUS);  
DallasTemperature sensors(&oneWire);

void setup() {
  Serial.begin(9600);
  servo.attach(D4);
  sensors.begin();
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
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
} 

void temperature(){
  sensors.requestTemperatures(); 
  temp=(int)sensors.getTempCByIndex(0);
  Firebase.setInt("SensorData/15267/temp", temp);
}

void waterLevel(){
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
  distance = duration * 0.034/2;
  distance = map(distance,0,15, 100, 0);
  Serial.println(distance);
  Firebase.setInt("SensorData/15267/waterLevel", (int)distance);
}

void fishFeeding()
{
  fireStatus = Firebase.getInt("FishFeeding/15267/triggerValue");
  
  if (fireStatus == 1) {
    Serial.println("Moter ON");
    servo.attach(D4);
    servo.write(360);
  }
  else {
    Serial.println("Moter OFF");
    servo.detach();
  }
}
