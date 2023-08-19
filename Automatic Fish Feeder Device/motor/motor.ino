#define motorPinA  16
#define motorPinB  5
#define motorPinC  0
#define motorPinD  14

void setup()
{
  pinMode(motorPinA, OUTPUT);
  pinMode(motorPinB, OUTPUT);
  pinMode(motorPinC, OUTPUT);
  pinMode(motorPinD, OUTPUT);

}

void loop() {
  waterPumpOutOn();
  waterPumpOutOff();
  waterPumpInOn();
  waterPumpInOff();
  
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
  if (distance => 14)
    digitalWrite(motorPinA, LOW);
  digitalWrite(motorPinB, LOW);
  digitalWrite(motorPinC, LOW);
  digitalWrite(motorPinD, LOW);
}

void waterPumpInOn()
{
  if (distance => 14 && distance >= 3)
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
//servo D2---->2
//temp D4---->4
//triger---->D6
//echo---->D7
//
//D0 16
//D1 5
//D3 0
//D5 14
