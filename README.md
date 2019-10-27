# Overview
The Leinwand Graphics Framework – short Leinwand GF or just LGF is a 2D graphics framework for Android and Java Desktop (Windows, Linux, Mac).  "Leinwand" is the German word for canvas.

Created 2018-2019 by Philip Heyse

## License
Apace V2


# Features and purpose
 - The framework is very easy to use
 -- LGF provides many features such as
 --- Screen scaling
 --- Images, fonts and animations
 --- Running in Android and on Java Desktop (e.g. for faster development and testing)
 --- collision detection
 --- Scrolling
 --- Text effects
 --- Saving and loading states
 --- User input via touch mouse

# Using the framework
## Android
In the dependencies section of the „build.gralde“ file simply add this line:

```
implementation 'de.bright-side.lgf:lgf-android:1.0.1'
```
(Please replace "1.0.1" with the current framwork version.)


## Java Deskop (Windows, Linux, Mac)
In the Maven pom.xml file simply add this dependency:
```xml
	<dependency>
		<groupId>de.bright-side.lgf</groupId>
		<artifactId>lgf-pc</artifactId>
		<version>1.0.0</version>
	</dependency>
```
(Please replace "1.0.0" with the current framwork version.)


## Further information
Please look at the file "LeinwandGF-Documentation.odt" for more information:
 - usage
 - concepts
 - PC and Andorid demo project