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