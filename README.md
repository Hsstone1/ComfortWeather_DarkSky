# ComfortWeather_DarkSky
Weather Application

Comfort Weather is a weather application that derives its data from the Dark Sky API, which unfortunately will be dicontinued by the end of 2021 due to exclusivity purchase rights by Apple.

# About
Comfort Weather has many features standard to weather apps, but many that set it appart. Namely the creation of a 'Comfort Index' which takes in a multitude of meteorlogical factors to create a value between 0 and 100, inclusive. The higher the number, the more plesant it feels outside. 

# Unlimited Locations
Comfort Weather uses a SQL lite database to store previously searched addresses with the ability to favorite, and delete any address they please. There is also a cooldown time of 5 minutes between refreshes to limit API calls as it is not a free service.

# Satelite Imagery
Comfort Weather parses images from the National Weather Center to display live satelite images in the app, something many weather apps fail to do. The addition of satelite imagry lets the user look at large meteorlogical formations as opposed to the weather at one location.

# Interactive Radar
Comfort Weather uses a free radar API to retrieve the radar images, but an interactive warning system is in place to allow the user to view a list of all warnings in the country, not just at their current location, with a polygon of the warning geofence displayed over the radar.

# Tropical Tracker
Comfort Weather uses data from the National Hurricane Center to create an advanced collection of resources for viewing tropical activity, such as temperature of the oceans, current storm path, probability of winds and even how far away the system  is.
