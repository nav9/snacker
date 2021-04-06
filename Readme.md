![Alt text](images/snacker.jpeg?raw=true "Snacker")

Nice to see you are one of the few people in the world who read Readme files :-)  
This is a markdown file: https://en.wikipedia.org/wiki/Markdown  

# About  
This is a free and Open Source project meant to give a person an element of surprise and novelty by making choices for the person based on a software 
algorithm which makes those choices from a set of options.  
Originally meant to select an optimal combination of daily snacks for a company, based on the snack preferences of the employees, this program
can also be extended to encompass other functionalities where people need to make a random choice.  

The master branch is used for active collaborative development.   
The release branch is used only when a version of the project is ready for release. The release branch is ideally operated only by the repository owner.  
  
# Running this project  
* Because this project uses Gradle, you may find it tough to configure and run it. Please ask Navin for help. Otherwise, you can also simply create a new java project, use the java files of this project and use the dependency jar files manually. Gradle just helps in downloading those dependency libraries automatically and building.
* To run this project, you need Gradle.   
* Install Gradle and navigate to this project's folder in the commandline and simply type "gradle". The project will build and a jar will be created in ./build/libs.  
* Go to the libs folder and run the project with "java -jar snacker.jar gen" the first time you run it.  
* Running it with the commandline parameter "gen" will generate the default snack CSV file. Just make sure the CSV file is not open in any program when you run snacker, because snacker needs to delete the CSV file occassionally to update it.  
* Now you can modify the CSV file with names and ratings of your choice.  
* From then on, you can simply run the program with "java -jar snacker.jar".  
* You can also install the Gradle plugin in Netbeans and directly run it from the IDE. It may take a long time to load the project for the first time if dependencies have to be loaded. The gradle build file contains support for creating an Eclipse project too.   
* Using Netbeans instead of Eclipse is recommended. Netbeans is just much cooler, has better features and is easier to use than Eclipse.  
  
# TODO list  
* The SnackChooser class has a constant named maxTolerableRecentness set to 3 days. Rather than have the value in code, it would be better to have the value in the JSON/CSV file so that the end user would be able to edit it.  
* Needs permission: Would be nice if the User did not have to manually execute this program. Perhaps if the program ran on a server and the results could be visible via a browser, it would be simpler. Also open to the possibility of allowing Users to click on which snack they have a craving for on a particular day so that it would override the algorithm's choice for that day.  
  
# Known issues/bugs    
* If the number of snacks in the CSV are too few, the program goes into an infinite loop.   

