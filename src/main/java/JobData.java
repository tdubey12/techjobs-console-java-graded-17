import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * Created by LaunchCode
 */
public class JobData {

    private static final String DATA_FILE = "src/main/resources/job_data.csv";
    private static boolean isDataLoaded = false;

    private static ArrayList<HashMap<String, String>> allJobs;

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all of the values of the given field
     */
    public static ArrayList<String> findAll(String field) {

        // load data, if not already loaded, it will populate alljobs arraylist
        loadData();

        // create an arraylist of string to store the values for the given field parameter
        ArrayList<String> values = new ArrayList<>();

        //iterate all rows from alljobs arraylist stored as hashmap
        for (HashMap<String, String> row : allJobs) {
            //read value from row hashmap from given field parameter(key)
            String aValue = row.get(field);

            //to avoid adding duplicates, check values arraylist contains or not avalue
            if (!values.contains(aValue)) {
                //add avalue to values arraylist
                values.add(aValue);
            }
        }

        return values;
    }

    public static ArrayList<HashMap<String, String>> findAll() {

        // load data, if not already loaded
        loadData();
        // return alljobs arraylist
        return allJobs;
    }

    /**
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     *
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column   Column that should be searched.
     * @param value Value of teh field to search for
     * @return List of all jobs matching the criteria
     */
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {

        // load data, if not already loaded
        loadData();
        // creating arraylist of hashmap to store matching jobs from alljobs
        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();
        //iterate all rows from alljobs arraylist stored as hashmap
        for (HashMap<String, String> row : allJobs) {
            //read value from row hashmap from given column parameter(key)
            String aValue = row.get(column);
            //check if lowercase avalue contains given value parameter in lower case
            if (aValue.toLowerCase().contains(value.toLowerCase())) {
            // add matching row to jobs arraylist
                jobs.add(row);
            }
        }

        return jobs;
    }

    /**
     * Search all columns for the given term
     *
     * @param value The search term to look for
     * @return      List of all jobs with at least one field containing the value
     */
    public static ArrayList<HashMap<String, String>> findByValue(String value) {

        // load data, if not already loaded
        loadData();

        // TODO - implement this method
            // creating arraylist of hashmap to store matching jobs from alljobs
        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();
            // //iterate all rows from alljobs arraylist stored as hashmap
        for (HashMap<String, String> row : allJobs) {

            // neeed to read all the keys from hashmap row using keyset method that returns a set <string> object
            Set<String> keys= row.keySet();
            //iterate keys
            for(String key:keys){

                //read a key value from row hashmap
                String aValue = row.get(key);

                //check if lowercase avalue contains given value parameter in lower case
                if (aValue.toLowerCase().contains(value.toLowerCase())) {
                    // add matching row to jobs arraylist
                    jobs.add(row);
                    //to avoid duplicate break the inner for loop that is iterating keys
                    break;
                }
            }



        }

        return jobs;
    }

    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {

            // Open the CSV file and set up pull out column header info and records
            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();

            // Put the records into a more friendly format
            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();

                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }

                allJobs.add(newJob);
            }

            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }

}
