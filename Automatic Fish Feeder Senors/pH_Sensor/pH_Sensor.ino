#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#define FIREBASE_HOST "automaticfishfeeder-7941e-default-rtdb.firebaseio.com" // Firebase host
#define FIREBASE_AUTH "AQWNYi5n5m2UOLpUsPGjDtMvq7V0chJiRIOv38hq" //Firebase Auth code
#define WIFI_SSID "MTN-MobileWiFi-E5573" //Enter your wifi Name
#define WIFI_PASSWORD "QFBB1YHH" // Enter your password
const int analogInPin = A0;
int sensorValue = 0;
unsigned long int avgValue;
float b;
int buf[10], temp = 0;

void setup()
{
  Serial.begin(9600);
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

void loop()
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
  Firebase.setInt("SensorData/15267/phValue", phValue);
}
