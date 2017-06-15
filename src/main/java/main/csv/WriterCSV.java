package main.csv;

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.slf4j.LoggerFactory;

public class WriterCSV {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(WriterCSV.class);
    private final char COMMA_DELIMITER = ',';
    private final String FILE_HEADER = "Name#Rating#RecentByDays";//CSV file header
    
    public void WriteToCSV(final String fileName, ArrayList<Snack> snacks) {        

        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName), COMMA_DELIMITER)) {
            writer.writeNext(FILE_HEADER.split("#"));
            for(Snack sn: snacks) {
                String template = sn.getName()+"#"+sn.getRating().toString()+"#"+sn.getRecent().toString();
                writer.writeNext(template.split("#"));
            }
        } catch (IOException ex) {logger.error("{}", ex.getMessage(), ex);}            
    }
}
