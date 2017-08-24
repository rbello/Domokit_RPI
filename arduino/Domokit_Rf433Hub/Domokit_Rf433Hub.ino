//============================================================================
// Name        : Domokit_Rf433.ino
// Author      : R. BELLO <https://github.com/rbello>
// Version     : 1.1
// Copyright   : Creative Commons (by)
// Description : 
// Date        : august 2017
//============================================================================

#include <RCSwitch.h>
#include <Arduilink.h>

#define NODE_ID 4

#define INT_433_SENSOR  0     // Interruption pin for RF 433 sensor (#2)
#define PIN_433_EMITTER 7     // Pin for RF 433 emitter

// Sensors RF 433Mhz
RCSwitch rf433read = RCSwitch();
RCSwitch rf433write = RCSwitch();

// Create the link
Arduilink link = Arduilink(NODE_ID);

void setup() {

  // Init serial
  Serial.begin(9600);

  // Init RF 433
  rf433read.enableReceive(INT_433_SENSOR);
  rf433read.setPulseLength(321);
  rf433write.enableTransmit(PIN_433_EMITTER);
  rf433write.setPulseLength(320);
  rf433write.setProtocol(2);
  rf433write.setRepeatTransmit(4);

  link.addSensor(0, S_ACTION, "RF433Emitter", "opcode", sendRf)->verbose = true;
  link.addSensor(1, S_INFO, "RF433Receiver", "opcode")->verbose = true;
  link.addSensor(2, S_INFO, "RF433EchoReceiver", "opcode")->verbose = true;

  // Init serial protocol
  link.init();

}

long lastSentValue = 0;
unsigned long lastSentTime = 0;

boolean error = false;


void sendRf(const char* str) {
  long code = String(str).toInt();
  // Rf433 send
  rf433write.send(code, 32);
  lastSentValue = code;
  lastSentTime = millis();
}

void loop() {

  link.handleInput();

  if (lastSentValue != 0 && millis() - lastSentTime > 300) {
    lastSentValue = 0;
  }

  // RF 433 receiver
  if (rf433read.available())
  {
    unsigned long value = rf433read.getReceivedValue();
    if (value == 0)
    {
      if (!error) {
        Serial.println("ERROR");
        error = true;
      }
    }
    else if (value == lastSentValue) {
      if (error) error = false;
      link.setValue(2, value);
    }
    else {
      if (error) error = false;
      link.setValue(1, value);
    }
    rf433read.resetAvailable();
  }

}

