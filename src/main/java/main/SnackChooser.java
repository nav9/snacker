package main;

import main.csv.Snack;
import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import main.csv.WriterCSV;
import org.slf4j.LoggerFactory;

public class SnackChooser {

    private final org.slf4j.Logger log = LoggerFactory.getLogger(SnackChooser.class);

    private final String filename = "snack.csv";
    private Integer maxTolerableRecentness = 7;//days
    private final ArrayList<Snack> snacks = new ArrayList<>();
    private final WriterCSV writer = new WriterCSV();

    public SnackChooser() {
    }

    public void GenerateSampleDataAndWrite() {
        log.info("GenerateSampleDataAndWrite");
        
        snacks.clear();
        snacks.add(new Snack("Ice Cream (from Naturals)", 49, 14));
        snacks.add(new Snack("Rasmalai", 40, 10));
        snacks.add(new Snack("Chaat", 43, 0));
        snacks.add(new Snack("Lassi", 57, 10));
        snacks.add(new Snack("Fruit juices", 65, 10));
        snacks.add(new Snack("Mc Donalds Aaloo tikki burger", 70, 7));
        snacks.add(new Snack("Dry fruits", 47, 8));
        snacks.add(new Snack("Peanut butter sandwich", 36, 11));
        snacks.add(new Snack("Breadsticks and sauce", 26, 0));
        snacks.add(new Snack("Vada Pav", 26, 0));
        snacks.add(new Snack("Dhokla and jalebi", 26, 0));
        snacks.add(new Snack("Cupcakes", 32, 0));
        snacks.add(new Snack("Dilpasand", 34, 0));      
        
        WriteData(snacks);
    }
    
    public void WriteData(ArrayList<Snack> snackList) {
        DeleteTheOldFile();
        writer.WriteToCSV(filename, snacks);
    }

    public void LoadSnackData() {
        try {
            CSVReader reader = new CSVReader(new FileReader(filename));
            String[] nextLine;
            log.info("\n\nLoading data ...");
            snacks.clear();
            boolean fileHeaderLine = true;
            while((nextLine = reader.readNext()) != null) {
                if (fileHeaderLine) {fileHeaderLine=false;continue;}//ignore the first line of the CSV
                String name = nextLine[0];
                String rating = nextLine[1];
                String recent = nextLine[2];

                snacks.add(new Snack(name, Integer.parseInt(rating), Integer.parseInt(recent)));
                log.info("name: {}, rating: {}, recent by {} days", name, rating, recent);
            }

            log.info("... done loading");        
            log.info("maxTolerableRecentness = {}", maxTolerableRecentness);
        } catch (IOException | NumberFormatException ex) {log.error("{}", ex.getMessage(), ex);}
    }

    public void DisplaySnackForToday() {
        AdjustMaxTolerableRecentnessIfDataIsTooLess();

        boolean foundSnack = false;
        boolean exitWithoutMakingChoice = false;
        Snack chosenSnack = null;
        int howManySnacksToConsiderForRatings = 4;
        final int numberOfTriesForGettingValidSnackStack = 10;

        while(foundSnack == false) {
            HashSet<Snack> chosenSnacks = new HashSet<>();

            Integer iterations = 0;
            while(chosenSnacks.size() < howManySnacksToConsiderForRatings) {//get a few randomly chosen snacks
                iterations++;
                Snack s = snacks.get(GetRandomNumberInThisRange(0, snacks.size() - 1));
                if (s.getRecent() < maxTolerableRecentness) {
                    chosenSnacks.add(s);
                }
                //if we have been beating around the bush trying to get a few recent snacks and couldn't find howManySnacksToConsiderForRatings number of them, then reduce the maxTolerableRecentness (you could also program it to adjust the howManySnacksToConsiderForRatings)
                if (iterations % numberOfTriesForGettingValidSnackStack == 0) {
                    log.info("iter {}, numofTries={}, maxTolerableRecentness={}, snacks.size={}", iterations, numberOfTriesForGettingValidSnackStack, maxTolerableRecentness, snacks.size());
                    if (maxTolerableRecentness <= snacks.size()) {
                        maxTolerableRecentness++;
                        chosenSnacks.clear();
                        log.info("maxTolerableRecentness (in days) temporarily adjusted to: {}", maxTolerableRecentness);
                    }
                    else {
                        log.error("\n\nSomething is wrong with the recentness values in the JSON file. Please correct it.");
                    }
                }
            }

            Snack bestSnack = null;
            Integer highestRating = 0;
            Integer leastRecentness = snacks.size();
            //Find the one with the highest rating and least recentness
            for(Snack s : chosenSnacks) {
                if ((highestRating == 0 && leastRecentness == snacks.size())
                    || //if it's the first iteration, just assign it as the best snack
                        (s.getRating() > highestRating && s.getRecent() < leastRecentness)) {//for the remaining iterations, assess
                    highestRating = s.getRating();
                    leastRecentness = s.getRecent();
                    bestSnack = s;
                }
            }
            
            int i = 0;
            String snackOptions = "\n\n\nSnacks possible are:\n";
            for(Snack s : chosenSnacks) {
                ++i;
                snackOptions = snackOptions + "\n" + i + ". " + s.getName() + " with rating " + s.getRating() + " and recent by " + s.getRecent() + " days";
            }
            snackOptions = snackOptions + "\n\n" + (chosenSnacks.size() + 1) + ". Show me more snacks";
            snackOptions = snackOptions + "\n" + (chosenSnacks.size() + 2) + ". Exit";
            snackOptions += "\n\nBest snack for today is: " + bestSnack.getName() + " with rating " + bestSnack.getRating() + ". Recent by " + bestSnack.getRecent() + " days\n\n";
            snackOptions += "Please enter your choice:\n";
            log.info(snackOptions);

            Scanner reader = new Scanner(System.in);  // Reading from System.in
            int userChoice = reader.nextInt();

            i = 0;
            for(Snack s : chosenSnacks) {
                if (++i == userChoice) {
                    chosenSnack = s;
                    foundSnack = true;
                }
            }

            if (foundSnack == false) {
                if (userChoice == chosenSnacks.size() + 1) {
                    if (howManySnacksToConsiderForRatings < snacks.size() - 1) {
                        howManySnacksToConsiderForRatings++;
                    }//if the user wants to see a different set of snacks, show one more snack in the list
                    log.info("\n\n\nOk. Creating new list...\n\n");
                }
                else if (userChoice == chosenSnacks.size() + 2) {
                    exitWithoutMakingChoice = true;
                    log.info("Bye! :-)");
                    break;//exit the while loop and end the program
                }
                else {
                    log.error("\n\n\nWrong input. Creating new list...\n\n");
                }
            }
        }

        if (exitWithoutMakingChoice == false) {
            //should enter this area of code only when snack is found and snack object is assigned to it
            log.info("\n\n\nSnack chosen for today is: {}\n\n", chosenSnack.getName());
            UpdateData(chosenSnack.getName());
            WriteData(snacks);
        }
    }

    private void AdjustMaxTolerableRecentnessIfDataIsTooLess() {
        if (maxTolerableRecentness > snacks.size()) {
            maxTolerableRecentness = snacks.size();
        }
    }

    private void UpdateData(final String snackNameChosenForToday) {
        for(Snack s : snacks) {
            if (s.getName().equals(snackNameChosenForToday)) {
                s.setRecent(snacks.size());
            }
            else {
                Integer recentVal = s.getRecent();
                if (recentVal > 0) {
                    s.setRecent(recentVal - 1);
                }
            }
        }
    }

    private Integer GetRandomNumberInThisRange(Integer minimum, Integer maximum) {
        Random rn = new Random();
        int range = maximum - minimum + 1;
        return rn.nextInt(range) + minimum;
    }
    
    private void DeleteTheOldFile() {
        try {
            File file = new File(filename);
            if (file.delete()) {log.info("{} is deleted!", file.getName());} else {log.info("Delete operation is failed.");}
        } catch (Exception e) {log.error("{}", e.getMessage(), e);}
    }

}
