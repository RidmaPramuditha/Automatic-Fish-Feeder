#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#include <Servo.h>
#define FIREBASE_HOST "automaticfishfeeder-7941e-default-rtdb.firebaseio.com" // Firebase host
#define FIREBASE_AUTH "AQWNYi5n5m2UOLpUsPGjDtMvq7V0chJiRIOv38hq" //Firebase Auth code
#define WIFI_SSID "Randula" //Enter your wifi Name
#define WIFI_PASSWORD "pass@123" // Enter your password
int fireStatus = 0;
Servo servo;
void setup() {
  Serial.begin(9600);
  servo.attach(D4);
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
