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

void loop(){
   digitalWrite(motorPinA, HIGH);
  digitalWrite(motorPinB, LOW);

  delay(10000);
  digitalWrite(motorPinC, HIGH);
  digitalWrite(motorPinD, LOW);
}

//servo D4---->2
//temp D2---->4
//triger---->D6
//echo---->D7
//
//D0 16
//D1 5
//D3 0
//D5 14
