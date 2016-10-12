package main;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
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
    private Integer maxRecentness;
    private Integer maxTolerableRecentness = 3;//days
    private final ArrayList<Snack> snacks = new ArrayList<>();
    
    SnackChooser() {}
    
    public void GenerateSampleDataAndWrite() {
        ArrayList<Snack> sn = new ArrayList<>();
        sn.add(new Snack("Dry Fruits", 5, 0));
        sn.add(new Snack("Ice Cream (from Creamstone)", 4, 0));
        sn.add(new Snack("Fruit juice", 7, 0));
        sn.add(new Snack("Boiled corn", 3, 0));
        sn.add(new Snack("Peanut butter jam sandwich", 8, 0));
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
        
        logger.info("\n... done loading");
        maxRecentness = snacks.size();
        logger.info("\nRecent days preset = {}", maxRecentness);        
    }
    
    public void DisplaySnackForToday() {
        AdjustMaxTolerableRecentnessIfDataIsTooLess();
        
        boolean foundSnack = false;
        while(foundSnack == false) {
            Snack s = snacks.get(GetRandomNumberInThisRange(0, snacks.size()-1));
            
            if (s.getRecent() < maxTolerableRecentness) {
                foundSnack = true;
                logger.info("\n\nSnack for today is: {} with rating {}. Recent by {} days\n\n", s.getName(), s.getRating(), s.getRecent());
                UpdateData(s.getName());
                WriteData(snacks);
            }
        }
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
