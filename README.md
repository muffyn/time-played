# Time Played
Adds a widget to the screen that displays your time played. Should show the same time that Hans or the account summary tab will show.

Your account age will not be leaked.

* Why is the time stuck at 0:00:00?

Try disabling/re-enabling the timer and toggling your time played button in account summary. If that doesn't work, I might've messed something up so contact me lol.

* Why do the seconds sometimes jump ahead to x:00 when world hopping?

The client doesn't have access to precise time info, so I'm emulating it by using the most recent time given. This means your time should always be accurate within a minute. When hopping worlds, the variable updates, meaning that I can get a more accurate time. This may mean skipping ahead up to a minute, which might be a bit jarring but it's more accurate.

* Why isn't the timer completely accurate with real time?

The timer tries its best to stay accurate with game ticks rather than real time. It updates according to the tickrate of the server and the number of ticks that runelite detects. If either runelite or the server has significant lag spikes, the timer may become desynced with real time. To fix any desyncs, hop worlds or toggle the time played in account summary.

* Why does the report button flash?

Disable the "Report Button" plugin on RuneLite. It conflicts and tries to take control of the report button every 500ms.

---
Feel free to add suggestions or pull requests. Contact `Muffyn#0971` on Discord for more info or to talk about anything speedrunning related :)