package main;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.LoggerFactory;

public class SnackChooser {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(SnackChooser.class);

    private final String filename = "snack.json";
    private final String snackNameString = "name";
    private final String snackRatingString = "rating";
    private final String recentSnacksString = "recent";
    private final String identifier = "listOfSnackObjects";
    private Integer maxTolerableRecentness = 7;//days
    private final ArrayList<Snack> snacks = new ArrayList<>();
    
    SnackChooser() {}
    
    public void GenerateSampleDataAndWrite() {
        ArrayList<Snack> sn = new ArrayList<>();
        sn.add(new Snack("Ice Cream (from Naturals)", 49, 14));
        sn.add(new Snack("Rasmalai", 40, 10));
        sn.add(new Snack("Chaat", 43, 0));
        sn.add(new Snack("Lassi", 57, 10));
        sn.add(new Snack("Fruit juices", 65, 10));
        sn.add(new Snack("Mc Donalds Aaloo tikki burger", 70, 7));
        sn.add(new Snack("Dry fruits", 47, 8));
        sn.add(new Snack("Peanut butter sandwich", 36, 11));
        sn.add(new Snack("Breadsticks and sauce", 26, 0));
        sn.add(new Snack("Vada Pav", 26, 0));
        sn.add(new Snack("Dhokla", 26, 0));
        sn.add(new Snack("Cupcakes", 32, 0));
        sn.add(new Snack("Momo", 34, 0));
        WriteData(sn);
    }

    public void WriteData(ArrayList<Snack> snackList) {            
        try {
            FileWriter file = new FileWriter(filename);
            JSONObject objectToWrite = new JSONObject();
            JSONArray list = new JSONArray();

            for(Snack sn: snackList) {
                JSONObject obj = new JSONObject();
                obj.put(snackNameString, sn.getName());
                obj.put(snackRatingString, sn.getRating());
                obj.put(recentSnacksString, sn.getRecent());
                list.add(obj);
            }

            objectToWrite.put(identifier, list);

            file.write(objectToWrite.toJSONString());
            file.flush();
            file.close();
        } catch (IOException ex) {logger.error("{}", ex.getCause());}
    }
    
    public void LoadSnackData() {
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {obj = parser.parse(new FileReader(filename));} catch (IOException | ParseException ex) {logger.info("{}", ex.getCause());}
        JSONObject jsonObject = (JSONObject) obj;
        JSONArray msg = (JSONArray) jsonObject.get(identifier);
        Iterator<JSONObject> iterator = msg.iterator();
        logger.info("\n\nLoading data ...");
        snacks.clear();
        
        while (iterator.hasNext()) {
            JSONObject ob = iterator.next();
            String name =(String) ob.get(snackNameString);
            Long rating =(Long) ob.get(snackRatingString);
            Long recentNum = (Long) ob.get(recentSnacksString);
            snacks.add(new Snack(name, rating.intValue(), recentNum.intValue()));
            logger.info("name: {}, rating: {}, recent by {} days", name, rating, recentNum);
        }
        
        logger.info("... done loading");
        logger.info("maxTolerableRecentness = {}", maxTolerableRecentness);        
    }
    
    public void DisplaySnackForToday() {
        AdjustMaxTolerableRecentnessIfDataIsTooLess();
        
        boolean foundSnack = false;
        Snack chosenSnack = null;
        final Integer howManySnacksToConsiderForRatings = 4;
        final Integer numberOfTriesForGettingValidSnackStack = 100;
        
        while(foundSnack == false) {
            HashSet<Snack> snackStack = new HashSet<>();
                        
            Integer iterations = 0;
            while(snackStack.size() < howManySnacksToConsiderForRatings) {//get a few randomly chosen snacks
                iterations++;
                Snack s = snacks.get(GetRandomNumberInThisRange(0, snacks.size() - 1));
                if (s.getRecent() < maxTolerableRecentness) {
                    snackStack.add(s);
                }
                //if we have been beating around the bush trying to get a few recent snacks and couldn't find howManySnacksToConsiderForRatings number of them, then reduce the maxTolerableRecentness (you could also program it to adjust the howManySnacksToConsiderForRatings)
                if (iterations % numberOfTriesForGettingValidSnackStack == 0) {
                    if (maxTolerableRecentness <= snacks.size()) {
                        maxTolerableRecentness++;
                        snackStack.clear();
                        logger.info("maxTolerableRecentness (in days) temporarily adjusted to: {}", maxTolerableRecentness);
                    } else {logger.error("\n\nSomething is wrong with the recentness values in the JSON file. Please correct it.");}
                }
            }
            
            Snack highestRatedSnack = null;
            Integer highestRating = 0;
            //Find the one with the highest rating
            for(Snack s: snackStack) {
                if (s.getRating() > highestRating) {highestRating = s.getRating();highestRatedSnack = s;}
            }
            
            int i = 0;
            String snackOptions = "\n\n\nSnacks possible are:\n";
            for(Snack s: snackStack) {
                ++i;
                snackOptions = snackOptions + "\n" + i + ". " + s.getName() + " with rating " + s.getRating() + " and recent by " + s.getRecent() + " days";
            }
            snackOptions = snackOptions + "\n" + (snackStack.size()+1) + ". Show me another slightly different set of snacks";
            snackOptions += "\n\nBest snack for today is: "+highestRatedSnack.getName()+" with rating "+highestRatedSnack.getRating()+". Recent by "+highestRatedSnack.getRecent()+" days\n\n";
            snackOptions += "Please enter your choice:\n";
            logger.info(snackOptions);
            
            Scanner reader = new Scanner(System.in);  // Reading from System.in
            int userChoice = reader.nextInt(); 
            
            i = 0;
            for(Snack s: snackStack) {
                if (++i == userChoice) {
                    chosenSnack = s;
                    foundSnack = true;      
                }      
            }
            
            if (foundSnack == false) {if (userChoice == snackStack.size()+1) {logger.info("\n\n\nOk. Creating new list...\n\n");} else {logger.error("\n\n\nWrong input. Creating new list...\n\n");}}            
        }
        
        //should enter this area of code only when snack is found and snack object is assigned to it
        logger.info("\n\n\nSnack chosen for today is: {}\n\n", chosenSnack.getName());
        UpdateData(chosenSnack.getName());
        WriteData(snacks);        
        
    }
    
    private void AdjustMaxTolerableRecentnessIfDataIsTooLess() {
        if (maxTolerableRecentness > snacks.size()) {maxTolerableRecentness = snacks.size();}
    }
    
    private void UpdateData(final String snackNameChosenForToday) {
        for(Snack s: snacks) {
            if (s.getName().equals(snackNameChosenForToday)) {s.setRecent(snacks.size());}
            else {
                Integer recentVal = s.getRecent();
                if (recentVal > 0) {s.setRecent(recentVal - 1);}
            }
        }
    }
    
    private Integer GetRandomNumberInThisRange(Integer minimum, Integer maximum) {
        Random rn = new Random();
        int range = maximum - minimum + 1;
        return  rn.nextInt(range) + minimum;
    }
        
}
