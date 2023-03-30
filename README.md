# Time Played
Adds a widget to the screen that displays your time played. Should show the same time that Hans or the account summary tab will show.

Your account age will not be leaked.

* Why do the seconds sometimes jump ahead to x:00 when world hopping?

The client doesn't have access to precise time info, so I'm emulating it by using the most recent time given. This means your time should always be accurate within a minute. When hopping worlds, the variable updates, meaning that I can get a more accurate time. This may mean skipping ahead up to a minute, which might be a bit jarring but it's more accurate.

---
Feel free to add suggestions or pull requests. Contact `Muffyn#0971` on Discord for more info or to talk about anything speedrunning related :)