# Tourism App
Goal: In a team of 4, make an app, preferably tourism/education-themed, from scratch

## Contents
- [Overview](#overview)
- [Authentification](#authentification)
- [Geolocation](#geolocation)
- [Database Management](#database-management)
- [Activity Details](#activity-details)

## Overview

As the Paris Olympics approach, we thought it would be a good idea to make an app that lists most ***FREE*** activities accessible within Paris, to make discovering the French capital easier to tourists.

### Task Distribution
|Task|Contributor|
|----|-----------|
|Authentification|Ali|
|Geolocation|Auriane|
|Database Management|Salomé|
|Activity Details|Joyce|

### App preview
- Feature 1 \
*GIF here*
- Feature 2 \
*GIF here*
- Feature 3 \
*GIF here*

Our design was solely made on Figma, while taking inspiration from many designs on dribbble, mostly the following:
- [Travel App Design by Anik Deb for Grapeslab](https://dribbble.com/shots/18741087-Travel-App-Design)
- [Loka Travel App by Ceptari Tyas for Piqo Studio](https://dribbble.com/shots/16123852-Loka-Travel-App)
- [Travel App by Listya Dwi Ariadi](https://dribbble.com/shots/15536071-Travel-App)

## Authentification
by Ali

## Geolocation
by Auriane


## Database Management
by Salomé
To manage all the data associated with the places, we decided to use Firebase. I created the project on Firebase and set it up. I linked the Firebase project to the Android Studio project.
Then I created a realtime database in Firebase, designed a structure to hold all of the data we needed and access it easily.
This database has 3 main parts : Client, Lieu and Saved_lieu. 
Client holds the users credentials and are used for authentification.
![image](https://github.com/atinyshrimp/Tourism_App/assets/103419843/3f2e3b88-f672-4157-a3f2-5ce7d71fb4f8)
Lieu holds all of the data of each of the places : their name, description, transport info, conditions for free access, number of visits, ...
![image](https://github.com/atinyshrimp/Tourism_App/assets/103419843/2ec2c93b-cdf0-4e5d-8a32-90529f95fb1d)
Saved_lieu is the link between the two other "tables". It holds for every person, the places this person has liked and if they visited it or not
![image](https://github.com/atinyshrimp/Tourism_App/assets/103419843/eaa37521-0237-4c2a-8ae8-4edff6483a40)

Another important side of firebase is the cloud storage. Since we are displaying places, we need pictures of those places, and for a nicer UI, we also decided to use pictures to display the transport numbers/letters.
To do that, we also organised this part in 3 parts : LieuImage, Transport and Category.
![image](https://github.com/atinyshrimp/Tourism_App/assets/103419843/727b728b-ac79-4174-bd13-9db45d9632cf)
LieuImage holds every picture of every place present in the database.
Transport is subdivised in smaller category to separate every kind of transport : bus, rer, train and subway.



## Activity Details
by Joyce

### Tasks
- Design the pages
- Code the two main ***Activity*** files according to the designs
- Code the different ***Fragment*** files according to the designs

### Activity Code


### Fragment Code

### Challenges & Takeaways
- Working with the default BottomNavView provided by AndroidStudio
    ```kotlin
    setupActionBarWithNavController(navController, appBarConfiguration)
    navView.setupWithNavController(navController)
    ```
    - The app wouldn't lauch because of these lines of code above
    - I ended up doing a "custom" bottom nav bar instead
