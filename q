[33meb35dba[m[33m ([m[1;36mHEAD -> [m[1;32mmain[m[33m, [m[1;31morigin/main[m[33m)[m Tweaked game time
[33m2f536cb[m Fixed issue where spamming new game causes multiple count down timer commands to stack
[33mee1ffa0[m General cleaning of GameActivity.java
[33md905b19[m When power up is activated, a new tile board is generated
[33mc80cd17[m Prevented multiple CountDownTimer instances occuring simultaniously
[33m3e62652[m Power up bar is no longer incremented by score during the power up mode
[33mf1bcc79[m Game timer now pauses when power up is activated
[33m197d7ef[m Power up now has a timer
[33m36417ea[m Visual effect of activated power up added
[33meb7187e[m General cleaning. Fixed issue where reaching over the power up capacity resets the power up progress bar
[33mbb21013[m Powerup is now expressed as a progress bar
[33md8a7170[m Merged files
[33m68cc87e[m Merge branch 'main' of https://github.com/ProgrammerNammer/Tumble-Mobile-Application
[33m69299bd[m Powerup system framework added
[33m4df2f0b[m Letter dice is not anymore shown unarranged
[33m9e5fe60[m Game Over layout cleaning
[33ma7a020b[m Powerup layout added but not yet implemented*
[33m13e4c15[m Fixed issue where multiple cdt_timer funcions exists simultaniosuly when creating a new game
[33m0b279bb[m New game and exit navigation added to Game Over layout
[33m6cf8629[m Game Over screen added
[33mededbf5[m Revamped hitbox of letter dice for firsst letter die selection. It will now consider the whole letter die as a hitbox.
[33m8f54503[m Fixed the word formed issue getting cleared immediately
[33mccfda47[m Fixed issue where non-neighbor letter dice can be selected
[33m4054171[m onClick state layout design revamped
[33m1ee56be[m Letter dice now has a dynamic response to onClick
[33mb5d2daf[m Fixed issue where non-String arguements were given to setText method, causing crash
[33m8624117[m Refactored LetterDieAdapter
[33m5e3d1b8[m Scoreboard layout revamped
[33m7c887ad[m Dynamic scoring system
[33m91a93f9[m Submitted words are now recorded nad invalidates same words in the future
[33m88a226e[m Adjusted scoring system
[33m14e5aba[m Hooked scoring system
[33m722c645[m Temporarily disabled login & register activities for development purposes
[33mab66aee[m Cleaning files
[33m1d1e63d[m Fixed condition with isValidWord() to show appropriate answer
[33m82b5662[m Memoized results returned from Dictionary API
[33m3d2b983[m Reworked feedback
[33m5fed231[m Refactoring login / register activity.
[33m0a3fbec[m Register activity layout overhaul. Fixed possible issue where overlapping ID values in the views
[33madd9113[m Login activity layout overhaul. Refactored layout item names to follow convention
[33m065e722[m Merge branch 'main' of https://github.com/ProgrammerNammer/Tumble-Mobile-Application
[33m353c9ff[m Add files via upload
[33md9be7b0[m Merge branch 'main' of https://github.com/ProgrammerNammer/Tumble-Mobile-Application
[33m583b3e8[m Decreased hitbox of letter die.
[33m18fc516[m Merge pull request #4 from ProgrammerNammer/master
[33mcd1b2f0[m Merge branch 'master' of https://github.com/ProgrammerNammer/Tumble-Mobile-Application
[33mcf53eab[m API AND FIREBASE CAPABILITIES INTEGRATED WITHIN GAME
[33m0b6e76d[m Refactored layout that would prevent the game activity layout warping when the User forms a long word
[33m3cbed15[m Merge branch 'master' of https://github.com/ProgrammerNammer/Tumble-Mobile-Application
[33me285da1[m Added Firebase Functionalities
[33mcda502e[m Swipe-like effect on selecting letter dice. Now, on release, the word currently being formed would be cleared out. Further modularized handlers. Temporarily removed connection of view to adapter.
[33mc89361a[m Added configuration files for letter (in format of CSV). Here, letter dice are now generated following a preset of letters. Added a class for letter generation.
[33m4feb504[m Added Firebase Functionalities
[33mbc0d681[m Tile selecting now modularized into Player class
[33me9783ae[m Auto-formatted the whole program
[33mc666ec2[m Already visited tiles can no longer be tapped.
[33m0bf749d[m Only neighboring dice of the latest die selected can be selected.
[33m249f3c8[m Refactoring of GameActivity to be more readable. Created Player class to modularize player separately from GameActivity, it is used to manage scores, words submitted, and similar. Hooked letter dice selection to word formed on the layout.
[33m9ca0e31[m Merge branch 'main' of https://github.com/ProgrammerNammer/Tumble-Mobile-Application
[33m6403846[m Refactored gameActivity to render letter dice using an Adapter. Letter die now has an onClickListener() and changes in color according to its state. Refactored viewadapter and viewholder to be prefixed with scoreboard.
[33m46ff018[m Merge pull request #3 from ProgrammerNammer/master
[33m9f2a77f[m Dictionary API implemented. See comments under GameActivity.java on how to use the api
[33m8b87e2b[m Decreased game grid to 4x4 to adhere to official rules. Changed timer style to be more pronounced
[33m360235b[m Formatting fixing and modularizing. Added the letter dice in the game
[33mdf39cff[m Successful merge
[33m6cbd53d[m Merge pull request #2 from ProgrammerNammer/master
[33mb28e986[m Added Timer and Board
[33md4ac3c0[m Merge pull request #1 from ProgrammerNammer/master
[33m5e71da1[m Update MainActivity.java
[33m5c09cf0[m Added ScoreboardActivity and GameActivity as an activity under the manifests. Added Navigation and Exit functionality
[33m3fc04ad[m Gameboard initial sketch added
[33ma727240[m Bug fixed where scores would overflow
[33m5a4750d[m Best Plays scoreboard added
[33m6cd0ff9[m Tumble main menu layout added
[33mb79804b[m First commit
