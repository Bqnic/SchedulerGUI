# SchedulerGUI - JavaFX project
SchedulerGUI is a simple to use and lightweight scheduler.

Primarily made it for personal use for my college organization, but is versatile enough to write your main tasks for specific days and go about your life.

You can create different compartments for tasks and have a main compartment which shows you all of your tasks and compartment that you put your task in.

Also, you can track your completed tasks, and number of your completed tasks.

# How to start SchedulerGUI
(Of course you need to have Java properly set up on your PC)

You can't run the .jar file with version of Java Runtime lower than 61.0!

In the root directory -> target, there you will find SchedulerGUI.jar, .jar file is not executable by nature and to run it you will have to use terminal/CMD.

In your terminal, use cd command to get to directory where SchedulerGUI.jar is (target)

Now use command:
java -jar SchedulerGUI.jar
This will open up the program.

If you don't want to use terminal for opening the .jar file, you can create a simple script, that opens SchedulerGUI.jar and then a simple launcher that opens that script.

You can put this launcher on desktop or taskbar, wherever.

# How to create a launcher on Linux

Create a new script file: Open a text editor and create a new file. Give it a name like SchedulerGUI.sh.

Add the script contents: In the script file, add the following lines:

```
#!/bin/bash
java -jar /path/to/your/project/SchedulerGUI.jar
```
Replace /path/to/your/project/SchedulerGUI.jar with the actual path to your JAR file. This ensures that the JAR file is executed when you run the script.

Save the script file: Save the file with the .sh extension. For example, save it as SchedulerGUI.sh.

Make the script file executable: Open a terminal and navigate to the directory where you saved the script file. Use the following command to make it executable:


```
    chmod +x launch_my_project.sh
```

This command grants execute permissions to the script file.

Create a desktop launcher: Right-click on your desktop and select "Create Launcher" or "Create Desktop Entry" (depending on your desktop environment).

Configure the desktop launcher: Fill in the required information in the launcher creation dialog. Here are the essential details:


    Name: Enter a name for the launcher (e.g., "SchedulerGUI").
    Command: Enter the full path to your script file (e.g., /path/to/SchedulerGUI.sh).
    Icon: Optionally, you can select an icon for your launcher.

Save the desktop launcher: Save the desktop launcher. It should create a shortcut file on your desktop.

Now, you should have a clickable file on your desktop that launches your JavaFX project when you click on it. Just double-click the launcher, and it will execute the script.

# For some reason it doesn't work on Windows

# After completing all of the above
Now you should have successfully entered the SchedulerGUI, read Help to see all available key combinations.


![alt text](https://github.com/Bqnic/SchedulerGUI/blob/main/screenshots/empty.png?raw=true)

![alt text](https://github.com/Bqnic/SchedulerGUI/blob/main/screenshots/someTasks.png?raw=true)

![alt text](https://github.com/Bqnic/SchedulerGUI/blob/main/screenshots/completed.png?raw=true)

# Additional notes
Being able to set deadline to any date is not a bug but a feature (it's hilarious)

Upon opening the app for the first time, in your Documents directory a save folder will be created for tasks, if you move or delete or in anyway change this directory nothing will work
