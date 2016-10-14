
Nice to see you are one of the few people in the world who read README files :-)

To run this project, you need Gradle. 
Install Gradle and navigate to this project's folder in the commandline and simply type "gradle". The project will build and a jar will be created in ./build/libs.
Go to the libs folder and run the project with "java -jar snacker.jar gen" the first time you run it.
Running it with the commandline parameter "gen" will generate the default snack json file.
Now you can modify the json file with names and ratings of your choice.
From then on, you can simply run the program with 


Or, install the Gradle plugin in Netbeans and directly run it from the IDE.

Gradle can also create eclipse project files for you. So just run gradle for this project and you'll be able to open the project in Eclipse too. Netbeans is better though :-)
