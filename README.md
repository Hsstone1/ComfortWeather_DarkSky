# ComfortWeather_DarkSky
Weather Application

Comfort Weather is a weather application that derives its data from the Dark Sky API, which unfortunately will be dicontinued by the end of 2021 due to exclusivity purchase rights by Apple.

## About
Comfort Weather has many features standard to weather apps, but many that set it appart. Namely the creation of a 'Comfort Index' which takes in a multitude of meteorlogical factors to create a value between 0 and 100, inclusive. The higher the number, the more plesant it feels outside. 
![CW_TopMain](https://user-images.githubusercontent.com/67842011/94703390-c389a200-030c-11eb-9043-abf36a418868.jpg)

## Advanced Forecasting
![CW_GraphMain](https://user-images.githubusercontent.com/67842011/94703391-c4223880-030c-11eb-88d1-81ca95a31060.jpg)
There is also the option for the user to view the weather forecast in the means of a graph. Comfort Weather allows the user to view many different meterological factors, all displayed on one graph. The user has the option to choose which factors to display.

## Unlimited Locations
Comfort Weather uses a SQL lite database to store previously searched addresses with the ability to favorite, and delete any address they please. There is also a cooldown time of 5 minutes between refreshes to limit API calls as it is not a free service.

## Satelite Imagery
![CW_Satellite](https://user-images.githubusercontent.com/67842011/94703389-c2f10b80-030c-11eb-9d8b-71da277396c5.jpg)
Comfort Weather parses images from the National Weather Center to display live satelite images in the app, something many weather apps fail to do. The addition of satelite imagry lets the user look at large meteorlogical formations as opposed to the weather at one location.

## Interactive Radar
![CW_RadarFull](https://user-images.githubusercontent.com/67842011/94703397-c4bacf00-030c-11eb-9141-0321467c99f6.jpg)
Comfort Weather uses a free radar API to retrieve the radar images, but an interactive warning system is in place to allow the user to view a list of all warnings in the country, not just at their current location, with a polygon of the warning geofence displayed over the radar.
![CW_Warnings](https://user-images.githubusercontent.com/67842011/94703393-c4223880-030c-11eb-91a9-1364852d0069.jpg)


## Tropical Tracker
![CW_Tropical](https://user-images.githubusercontent.com/67842011/94703395-c4bacf00-030c-11eb-977d-4b5a52fe2233.jpg)
Comfort Weather uses data from the National Hurricane Center to create an advanced collection of resources for viewing tropical activity, such as temperature of the oceans, current storm path, probability of winds and even how far away the system  is.


