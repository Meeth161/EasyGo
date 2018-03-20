// EASYGO-Real time determination of location of the public transport busses using IOT
//Developed by BYTE-APP team in rajasthan hackathon 4.0
//Team members :*Meeth A Thakkar,CSE,BITM College,Ballari.
//              *Mohammed Amjad,ECE,BITM College,Ballari.
//              *S R Sharana Basava,CSE,BITM College,Ballari.
//              *Neelam Vishal Vivek,CSE,BITM College,Ballari.             
//Developed date : 21/03/2018






#include "TinyGPS++.h"
#include "SoftwareSerial.h"
#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#define FIREBASE_AUTH "tIcz8QQqcaMqGT1KDqfXnDvvTjJCFyhCU303Q01E"
#define FIREBASE_HOST "easygo-1521375923558.firebaseio.com"
#define WIFI_SSID "amjad"
#define WIFI_PASSWORD "12346578"
long id =ESP.getChipId();
int flag=0;
double lati,longi,speedy,sati,temp1,temp2;
SoftwareSerial ss(13, 15); //d7=RX=pin 13=>connect dps tx, d8=TX=pin 15
TinyGPSPlus gps;//This is the GPS object that will pretty much do all the grunt work with the NMEA data work with the NMEA data
void setup()
{
  Serial.begin(9600);//This opens up communications to the Serial monitor in the Arduino IDE
  ss.begin(9600);//This opens up communications to the GPS
  Serial.println("GPS Start");//Just show to the monitor that the sketch has started
WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED) {
  Serial.print(".");
  delay(500);
  }
  Serial.println();
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);

}

void loop()
{
  while(ss.available())//While there are characters to come from the GPS
  {
    gps.encode(ss.read());//This feeds the serial NMEA data into the library one char at a time
  }
  if(gps.location.isUpdated())//This will pretty much be fired all the time anyway but will at least reduce it to only after a package of NMEA data comes in
  {
    //Get the latest info from the gps object which it derived from the data sent by the GPS unit
    Serial.println("Latitude:");
    Serial.println(gps.location.lat(), 6);
    Serial.println("Longitude:");
    Serial.println(gps.location.lng(), 6);
    lati=gps.location.lat();
    longi=gps.location.lng();
//  if(Firebase.getFloat("sw")== 1.0)
//    {
      Serial.println("push func");
      Firebase.setString(String(id)+"x",String(lati,6));
      Firebase.setString(String(id)+"y",String(longi,6));
      Serial.println("push done");
//    }
  }

}
