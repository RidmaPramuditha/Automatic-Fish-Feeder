#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#define FIREBASE_HOST "automaticfishfeeder-7941e-default-rtdb.firebaseio.com" // Firebase host
#define FIREBASE_AUTH "AQWNYi5n5m2UOLpUsPGjDtMvq7V0chJiRIOv38hq" //Firebase Auth code
#define WIFI_SSID "Tilan" //Enter your wifi Name
#define WIFI_PASSWORD "Tilan@123" // Enter your password
#define motorPinA  16
#define motorPinB  5
#define motorPinC  0
#define motorPinD  14
int waterIn=0;
int waterOut=0;
int waterLevel=0;
const int analogInPin = A0;
int sensorValue = 0;
unsigned long int avgValue;
float b;
float phValue=0;
int buf[10], temp = 0;

void setup() {
  Serial.begin(9600);
  pinMode(motorPinA, OUTPUT);
  pinMode(motorPinB, OUTPUT);
  pinMode(motorPinC, OUTPUT);
  pinMode(motorPinD, OUTPUT);
  pinMode(analogInPin, INPUT);
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
  phSensorFirebase();
  
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

void phSensorRead()
{
  for (int i = 0; i < 10; i++)
  {
    buf[i] = analogRead(analogInPin);
    delay(10);
  }
  for (int i = 0; i < 9; i++)
  {
    for (int j = i + 1; j < 10; j++)
    {
      if (buf[i] > buf[j])
      {
        temp = buf[i];
        buf[i] = buf[j];
        buf[j] = temp;
      }
    }
  }
  avgValue = 0;
  for (int i = 2; i < 8; i++)
    avgValue += buf[i];

  float pHVol = (float)avgValue * 5.0 / 1024 / 4.3;
  float phValue = -5.70 * pHVol + 30.20;
  phValue = 14.2 + phValue;
  //float phValue = -3.0 * pHVol+17.5;
  Serial.print("sensor = ");
  Serial.println(phValue);
  delay(900);
  //Firebase.setFloat("SensorData/15267/phValue", phValue);
  
}

void phSensorFirebase()
{
  phSensorRead();
  Firebase.setFloat("SensorData/15267/phValue", phValue);
}

void automaticFishTankWaterIn()
{
  waterLevel = Firebase.getInt("SensorData/15267/waterLevel");
  phSensorRead();

  if (phValue >8 || phValue <6) {
    if (32 <waterLevel <80) {
      waterPumpOutOn();
      }
    else if(waterLevel <25){
      waterPumpOutOff();
      }
    else if (32 >waterLevel <80) {
      waterPumpInOn();
      }
    else if(waterLevel>=80){
      waterPumpInOff();
      }
}
}
