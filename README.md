# SoloCarry
CMPUT 301 Project

[Download APK here](https://drive.google.com/file/d/1ukCrTG6G9D1yFZSl8hC974KbUdBqNRra/view?usp=share_link)


# Table Of Content
- [Introduction](#Introduction)
- [Setting Up](#Setting-Up)
- [How To Use](#How-To-Use)
- [Features](#Features)
  - [User](#User)
  - [Friends System](#Friends-System)
  - [Invitation System](#Invitation-System)
  - [Chat System](#Chat-System)
  - [QR Code System](#QR-Code_system)
  - [Ranking](#Ranking)
- [Project Structure](#Project-Structure)
- [Project Showcase](#Project-Showcase)
- [Tools & Libraries](#Tools-&-Libraries)
- [Contributors](#Contributors)
- [Copyright](#Copyright)

# Introduction
The ultimate challenge for code enthusiasts! With this game, you can test your skills and explore a variety of codes by scanning them. Each code you scan generates a score, and you can decide whether or not to share it with the community.

As you scan different types of codes, you'll be able to accumulate points and track your progress on the leaderboard. You can compete with other players and see who can score the highest.

The best part? You get to decide whether to share your scores or keep them to yourself. If you choose to share, you'll be able to connect with like-minded individuals and build a network of fellow code enthusiasts. If you prefer to keep your scores private, that's totally fine too - the game is all about having fun and challenging yourself.

Whether you're a seasoned coder or just starting out, the Scan Code Game is the perfect way to test your skills and learn something new. So why not give it a try today and see how many codes you can scan? Who knows - you might just discover your new favorite coding challenge!

# Setting Up
1.This application will be running on andorid mobile.

2.The system will generate scores and it will be appended to the database when user decidede to share with the community.

3.All User must register a Google email account inorder to play this game.

4.The system is implemented an algorithm to calculation the score of the code.

5.We have a leaderboard to recorded all the player score of the player in region or friend.

6.We also have friend system that allow player to add eachother to compete.

7.Player also can chat with eachother inside the application.

8.We will be test the features and UI desgion of all views.


# How To Use
1. register an Gmail account to one click log in 

2. on the main screen there are 3 floating buttion at the bottom center.
    1. Middle button is the camera function which allow you to scan code.
    2. Left buttion is the leaderboard which allows you to check the current ranking in the world or friend.
    3. Right buttuon is chat feature which allows you check with your friend but not allow to chat with stranger.

3. top left corner have the drop down button.
    1. First button allows you to see your code list.
    2. Second button allows enter the friend system
          i. search friend with email and send a friend request.
          ii. review your friend list.
    3. Log out

4. Top right profile icon button is your profile statstic.
    


# Features
## User
### Authentication through Google.
### View user-relative statistics/profiles 
### View the current QR code they have
### View each QR code's score and total scores they have

### Access the ranking system and view their rank between rank or friend

## Friends System
### Searching other by using usernames.
### Able to add other user into friend list
### View friends profiles

### Compete QR score among friends 
### Remove a user from friend list

## Chat System
### Sending messages to user's friends
## QR Code System
### Recording QR code through camera 
### Verifying the validity and authenticity of all QR code  
### Adding verified QR code and its region into user account 
### Analyzing the QR code and add corresponding score into user account 
### Able to store multiple QR codes with its compressed image  
### All code are sorted base on score in descending order 
### Maintaining the uniqueness of all QR code stored in one user account

### Ensure no duplicate QR code for same user
### Allow user to remove unwanted QR code 
### Qr code name are generated randomly 
### Able to store user's comment on the QR code they have scanned 
### Allow other user to see all comments on the QR code
## Ranking
### Based on the region, display the users that have the highest sum of QR code scores
### User can choose the ranking between friend. 


# Tools & Libraries
tools:
  
    figma
  
    lucid chart
  
    github
  
    discord

Libraires:
    
    'com.makeramen:roundedimageview:2.3.0'
    
    'com.google.firebase:firebase-bom:31.1.1'
    
    "org.jetbrains.kotlin:kotlin-bom:1.8.0"
    
    'com.auth0:java-jwt:3.18.2'
    
    'com.google.firebase:firebase-analytics'
    
    'com.google.firebase:firebase-auth'
    
    'com.firebaseui:firebase-ui-storage:7.2.0'
    
    'androidx.core:core-splashscreen:1.0.0'
    
    'com.google.android.gms:play-services-maps:18.1.0'
    
    'com.google.android.gms:play-services-auth:20.4.1'
    
    'androidx.appcompat:appcompat:1.6.1'
    
    'com.google.android.material:material:1.8.0'
    
    'androidx.constraintlayout:constraintlayout:2.1.4'
    
    'com.google.android.libraries.places:places:3.0.0'
    
    'androidx.navigation:navigation-fragment:2.5.3'
    
    'androidx.navigation:navigation-ui:2.5.3'
    
    'androidx.annotation:annotation:1.6.0'
    
    'androidx.lifecycle:lifecycle-livedata-ktx:2.5.1'
    
    'androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1'
    
    'com.github.bumptech.glide:glide:4.14.2'
    
    'com.github.clans:fab:1.6.4'
    
    'io.github.krupen:fabulousfilter:0.0.6'
    
    'com.google.firebase:firebase-firestore:24.4.4'
    
    "com.kongzue.dialogx:DialogX:0.0.47"
    
    'com.google.firebase:firebase-storage'
    
    'com.squareup.picasso:picasso:2.8'
    
    'com.github.ybq:Android-SpinKit:1.4.0'
    
    "com.kongzue.dialogx.style:DialogXMIUIStyle:0.0.47"
    
    "io.getstream:stream-chat-android-ui-components:5.13.0"
    
    'com.github.yuriy-budiyev:code-scanner:2.3.2'
    
    'com.github.bumptech.glide:compiler:4.14.2'
    
    'org.junit.jupiter:junit-jupiter-api:5.3.2'
    
    'org.junit.jupiter:junit-jupiter-engine:5.3.2'
    
    'androidx.test.ext:junit:1.1.5'
    
    'androidx.test.espresso:espresso-core:3.5.1'




# Contributors
<table>
  <tr>
    <td align="center">
      <a href="https://github.com/NorthstarWang">
        <img src="https://avatars0.githubusercontent.com/u/61356439?s=460&v=4" width="100px;" alt="Username"/>
        <br />
        <sub><b><a href="https://github.com/NorthstarWang">NorthstarWang</a></b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/kensu1996">
        <img src="https://avatars0.githubusercontent.com/u/73719978?s=460&v=4" width="100px;" alt="Username"/>
        <br />
        <sub><b><a href="https://github.com/kensu1996">kensu1996</a></b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/LawrenceYJH">
        <img src="https://avatars0.githubusercontent.com/u/90285257?s=460&v=4" width="100px;" alt="Username"/>
        <br />
        <sub><b><a href="https://github.com/LawrenceYJH">LawrenceYJH</a></b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/WenxinLi311">
        <img src="https://avatars0.githubusercontent.com/u/122634469?v=4" width="100px;" alt="Username"/>
        <br />
        <sub><b><a href="https://github.com/WenxinLi311">WenxinLi311</a></b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/qijia2">
        <img src="https://avatars0.githubusercontent.com/u/74459800?v=4" width="100px;" alt="Username"/>
        <br />
        <sub><b><a href="https://github.com/qijia2">qijia2</a></b></sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/Kiana315">
        <img src="https://avatars0.githubusercontent.com/u/93419255?v=4" width="100px;" alt="Username"/>
        <br />
        <sub><b><a href="https://github.com/Kiana315">Kiana315</a></b></sub>
      </a>
    </td>
  </tr>
</table>


# Copyright
## The MIT License (MIT)

Copyright (c) 2023 [CMPUT301W23T36 University of Alberta]

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), which is named "Solo Carry", to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT, OR OTHERWISE, ARISING FROM, OUT OF, OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

The name "Solo Carry" shall not be used in advertising or otherwise to promote the sale, use, or other dealings in this Software without prior written authorization from [Yang Wang](1527638985@qq.com).

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

## Enforcement of copyright
We take the protection of its copyright very seriously. If We discover that you have used our copyright materials in contravention of the license above, we may bring legal proceedings against you seeking monetary damages and an injunction to stop you from using those materials. You could also be ordered to pay legal costs.

The name "Solo Carry" shall not be used in advertising or otherwise to promote the sale, use or other dealings in this Software without prior written authorization from [Yang Wang](1527638985@qq.com).

Any unauthorized use may result in violations of copyright laws. [Yang Wang](1527638985@qq.com) reserves the right to take legal action against any individuals, organizations, or entities that breach the copyright of this Software.

## Infringing material

If you become aware of any material on the website that you believe infringes your or any other person's copyright, please report this by email to [Yang Wang](1527638985@qq.com)


