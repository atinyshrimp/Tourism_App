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

### Tasks
- Set up Firebase
- Create the Firebase database and cloud storage
- Code the retrieval of Firebase resources in Android Studio for the Home and Details Fragment
- Code the ***Like*** tab to display liked and visited places

### Setting up Firebase and its Realtime Database and Cloud Storage

To manage all the data associated with the places, we decided to use Firebase. I created the project on Firebase and set it up. I linked the Firebase project to the Android Studio project.
Then I created a realtime database in Firebase, designed a structure to hold all of the data we needed and access it easily.
This database has 3 main parts : Client, Lieu and Saved_lieu. 
Client holds the users credentials and are used for authentification.

![image](https://github.com/atinyshrimp/Tourism_App/assets/103419843/1002cd3b-2bee-4e1a-a0c0-111ff124e5ca)

Lieu holds all of the data of each of the places : their name, description, transport info, conditions for free access, number of visits, ...

![image](https://github.com/atinyshrimp/Tourism_App/assets/103419843/e7b817de-87db-46a7-93e3-0a796c3a9fd5)

Saved_lieu is the link between the two other "tables". It holds for every person, the places this person has liked and if they visited it or not

![image](https://github.com/atinyshrimp/Tourism_App/assets/103419843/091b49a6-bec0-429a-934c-23a96f2fb472)

Another important side of firebase is the cloud storage. Since we are displaying places, we need pictures of those places, and for a nicer UI, we also decided to use pictures to display the transport numbers/letters.
To do that, we also organised this part in 3 parts : LieuImage, Transport and Category.

![image](https://github.com/atinyshrimp/Tourism_App/assets/103419843/c4c82d48-8c45-4fbe-a8cc-b5f1ecf46165)

LieuImage holds every picture of every place present in the database.
Transport is subdivised in smaller category to separate every kind of transport : bus, rer, train and subway.
And Category simply holds the pictures for the cateogories : events, museum, garden,...
Every file uploaded in the cloud storage is very precisely named and placed in order to retrieve it easily. 
All of the picture are png : allow us to only have one type of extension which is easier for retrieval.

### Retrieval of Firebase data in Android Studio

After both the Firebase realtime database and the cloud storage were set, I needed to retrieve the data stored in those from Android Studio.
To do so, I used Firebase functions such as "setValue()" to modify, "removeValue()" to delete or simply "value" to retrieve the data. 
I took care of the retrieval of the Lieu data for the Details and Home fragment. I also took care of the retrieval of all the pictures for Home and Details : picture of the place, of the categories, of the transports, ...

### Liked and visited places tab

This page essentially uses the "Saved_lieu" part of the database to correlate a user and the places he liked and visited.
For this tab, to display the name of the user (and then use it to display the liked and visited places), I retrieved the pseudo from the login to be able to use it in this fragment.
The first part of this tab displays the places that the user liked and the second part, the places he visited. 
With this part, I took care of the logic behind liking and visiting places from Details, Home or Like tabs : the Firebase database updates when those actions are done.
The user can like and dislike places, and if he marks a place as visited, this will record the date of the visit. He can always change his mind by "Disliking" or "Forgetting a place".
This last action not only updates the user settings but also the place settings : the number of visit is decreased when a place is "unvisited" (and increased when visited obviously).

![image](https://github.com/atinyshrimp/Tourism_App/assets/103419843/c52200e1-c82a-43ea-8af3-7f5d9db4869e)

### Challenges & Takeaways

At first it was pretty hard getting familiar with Firebase especially to understad how to retrieve the data in the best way possible.
A lot of thought was put into the structure of the database (which is a bit different than the one I have been used to in SQL) and it was modified a lot to best suit our need.
In the end when that structure was good and the retrieval of the data in Android Studio had been understood, it was fairly easy to retrieve the rest of the data.
The main issue was really understanding all that had already been done in the activities and the fragments to be able to retrieve the data appropriately. Indeed, while I was creating all the structure of the database in Firebase, my teammates were working on Android Studio, and being able to understand all the links between all the activities that had been created in the meantime was pretty tough. Undesrtanding it all took a bit of time but once it was done, it went pretty smoothly.



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
 
## Conclusion
Write something here...

### Honorable mentions
*features we worked hard on even if they're not the main attraction*

### Things we wish we could've done
- Interactivity with the tab in the Home Fragment
- SearchView when clicking on the categories
