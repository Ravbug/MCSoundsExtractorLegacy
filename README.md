# MCSoundsExtractor
# Information
This is a simple utility which extracts the sound files from Minecraft and dumps them into a folder. This works for any Minecraft version newer than 1.7.2. The sound files for older Minecraft versions are located inside minecraft/assets (if 1.7.0 is the newest version installed) or minecraft/assets/virtual/legacy if you have newer versions.

This program requires Java 8 or higher. It supports and has been tested on Windows and Mac. In theory it supports Linux but this case has not been tested.

# How to use:
1. Download the precompiled JAR file from the list above. It's named MCExtractor.jar
2. To open the program, double-click the JAR file in your file browser. To run it from the command line, type java -jar path/to/MCExtractor.jar
3. If Minecraft is installed in its default location, then all you have to do is choose where you want to place the sound files, choose the version of the game you want. If Minecraft is not installed in the default location, you will likely get an error message and you must direct the program to where you installed Minecraft.
4. Press Extract. The progress bar will fill up and when complete, you will get a dialog stating the export finished successfully. If there is a problem during the export, the program will stop exporting and display a dialog with the stack trace.
5. Once the program completes, go to the folder you picked as the output folder. Inside it you will find a folder named minecraft, with a subfolder named sounds, with multiple subdirectories. Inside these are .ogg audio files. If you cannot open .ogg audio. I recommend the MediaHuman Audio converter which can convert them into .wav, .mp3, .acc, among others.

# Compiling it yourself
This program was writen using Eclipse 2018-09. It uses Gson to parse JSON text into a Java Map, and a jar is included inside.
Steps:
1. Clone this repro and unzip it
2. Open Eclipse, go to File --> Open Projects from File System, and pick the downloaded folder.
3. In the Package Explorer, expand MCSoundExplorer, expand lib, right-click gson-2.8.5.jar and press Build Path > Add to Build Path
4. You should be able to compile it now. Press the green play button to run it.

# Known Issues
1. It looks nasty on Windows.
2. High DPI displays on Windows and Linux cause the program to draw small.
3. The UI does not match on Windows and Mac. 
