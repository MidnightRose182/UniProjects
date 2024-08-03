import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Files;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;

//Histogram, Spider Chart, Scatter plot regression line


public class DataProcessing {
    /*
    In Order
    CE101 = Team Project
    CE141 = Mathematics for Computing
    CE142 = Mathematics for Engineers
    CE151 = Introduction to Programming
    CE152 = Object-Oriented Programming
    CE153 = Introduction to Databases
    CE154 = Web Development
    CE155 = Network Fundamentals
    CE161 = Fundamentals of Digital Systems
    CE162 = Digital Electronic Systems
    CE163 = Foundations of Electronics
    CE164 = Foundations of Electronics II
    */
    public class StudentData {
        int regNum;
        String course;
        String[] scores;
        double[] stats;
        double mean;
        double median;
        double IQR;
        double sd;
        double sas; //Student Ability Score. This rewards high average marks as well as consistency

        /*
        To determine student performance bracket I will be combining average scores as a measure of skill and standard deviation
        as a measure of consistency.
        Levels of performance:

        Very High:
        Average = 75+, Ability Score = 60+

        High:
        Average = 65+, Ability Score 50+

        Average:
        Average = 40+, Ability Score 30+

        Poor:
        Anything else
         */

        StudentData(int regNum, String course, String[] scores, double[] stats) {
            this.regNum = regNum;
            this.course = course;
            this.scores = scores;
            this.stats = stats;

            int total =  0;
            for (int i = 0 ; i < stats.length ; i++) {
                total += stats[i];
            }
            this.mean = total/stats.length;
            this.median = stats.length%2 == 0 ? (stats[stats.length/2]+stats[(stats.length/2)+1])/2 : stats[Math.round(stats.length/2)];
            //this.IQR = can't be bothered to do this one rn, idk if its even useful tbh
            total = 0;
            for (int i = 0 ; i < stats.length ; i++) {
                total += Math.pow(stats[i]-mean, 2); //Sum of variance from the mean
            }
            this.sd = Math.sqrt(total/stats.length);
            //This means that if someone has a standard deviation of 5 or lower, it will all be considered the same
            if (sd < 10) {
                this.sas = (10*mean)/10;
            } else {
                this.sas = (10*mean)/sd;
            }
        }
    }

    public class ModuleData {
        String name;
        double[] scores;
        double mean;
        double median;
        double Q1;
        double Q3;
        double IQR;
        double sd;
        String difficulty;

        ModuleData(String name, ArrayList<Integer> scores) {
            this.name = name;
            this.scores = new double[scores.size()];
            for (int i = 0 ; i < scores.size() ; i++) {
                this.scores[i] = scores.get(i);
            }
            sort(this.scores);

            int total =  0;
            for (int i = 0 ; i < this.scores.length ; i++) {
                total += this.scores[i];
            }
            this.mean = total/this.scores.length;

            int len = this.scores.length;

            this.median = len%2 == 0 ? (this.scores[len/2]+this.scores[(len/2)+1])/2 : this.scores[Math.round(len/2)];

            this.Q1 = len%4 == 0 ? (this.scores[(Math.round(len/4))-1]) : (this.scores[(Math.round((len-1)/4))-1]);
            this.Q3 = len%4 == 0 ? (this.scores[(Math.round(3*len/4))-1]) : (this.scores[Math.round(3*(len-1)/4)]);

            this.IQR = Q3-Q1;

            for (int i = 0 ; i < this.scores.length ; i++) {
                total += Math.pow(this.scores[i]-mean, 2); //Sum of variance from the mean
            }
            this.sd = Math.sqrt(total/this.scores.length);
//------------------------------------------------------------------------------------------------------------------
            int count = 0;
            double meanIQR = 0;

            for (double s : this.scores) {
                if (Q1 <= s && s <= Q3) {
                    meanIQR += s;
                    count += 1;
                }
            }
            meanIQR /= count;

            if (meanIQR >= 70) {
                difficulty = "Very Easy";
            } else if (meanIQR >= 60) {
                difficulty = "Easy";
            } else if (meanIQR >= 50) {
                difficulty = "Average";
            } else if (meanIQR >= 40) {
                difficulty = "Hard";
            }
        }
    }

    public static String[] moduleNames = {"CE101", "CE141", "CE142", "CE151", "CE152", "CE153", "CE154", "CE155", "CE161", "CE162", "CE163", "CE164"};

    public static String[] courseNames = {"COMPUTER GAMES", "COMPUTER SCIENCE", "COMPTUING", "DATA SCI & ANALYTICS", "NEURAL TECH W PSYCH","STUDENT SUPPORT"};
    public static int count = 0;
    public ArrayList<Integer> CE101 = new ArrayList<>(); //Array lists to make it easy to add values in a loop
    public ArrayList<Integer> CE141 = new ArrayList<>();
    public ArrayList<Integer> CE142 = new ArrayList<>();
    public ArrayList<Integer> CE151 = new ArrayList<>();
    public ArrayList<Integer> CE152 = new ArrayList<>();
    public ArrayList<Integer> CE153 = new ArrayList<>();
    public ArrayList<Integer> CE154 = new ArrayList<>();
    public ArrayList<Integer> CE155 = new ArrayList<>();
    public ArrayList<Integer> CE161 = new ArrayList<>();
    public ArrayList<Integer> CE162 = new ArrayList<>();
    public ArrayList<Integer> CE163 = new ArrayList<>();
    public ArrayList<Integer> CE164 = new ArrayList<>();
    public ArrayList<ArrayList> moduleScores = new ArrayList<>();

// credits to https://www.java67.com/2015/08/how-to-load-data-from-csv-file-in-java.html for providing the base logic
    public List<StudentData> readData(String file) {
        List<StudentData> studentDataBase = new ArrayList<>();
        moduleScores.add(CE101);
        moduleScores.add(CE141);
        moduleScores.add(CE142);
        moduleScores.add(CE151);
        moduleScores.add(CE152);
        moduleScores.add(CE153);
        moduleScores.add(CE154);
        moduleScores.add(CE155);
        moduleScores.add(CE161);
        moduleScores.add(CE162);
        moduleScores.add(CE163);
        moduleScores.add(CE164);

        Path path = Paths.get(file);
        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.US_ASCII )) {
            String line = br.readLine(); //Skip first line since its titles, not redundant
            line = br.readLine();

            while (line != null) { //will end when no more lines to read
                String[] stats = line.split(",", -1);
                StudentData dataset = createStudentDataset(stats);
                studentDataBase.add(dataset);
                line = br.readLine();
                count += 1;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return studentDataBase;
    }

    public static void sort (double[] ints) {
        double[] copy = new double[ints.length];
        for (int i=0; i<ints.length; i++) {
            copy[i] = ints[i];
        } //copies the list

        for (int i=0; i<ints.length; i++) {
            double num = copy[i];
            int count = 0; //uses count to determine its position in the array, insertion sort
            for (int j = 0; j < ints.length; j++) {
                if (num > copy[j]) {
                    count++;
                }
            }
            ints[count] = num;
        }
    }

    private StudentData createStudentDataset(String[] lineData) {
        int regNum = Integer.parseInt(lineData[0]);
        String course = lineData[1];
        String[] scores = new String[12];

        // Empty string values != empty string values, this fixes that
        for (int i = 2 ; i < 17 ; i++) {
            try {
                Integer.parseInt(lineData[i]);
            } catch (NumberFormatException empty) {
                lineData[i] = null;
            }
        }
        /*Some data points e.g. CE101-4-FY and CE101-4-SP are the same course and
        just take place at different times of the year, because no one will have a score
        in both they can be consolidated into one score */

        scores[0] = lineData[2] == null ? lineData[3] : lineData[2]; //CE101
        scores[1] = lineData[4] == null ? lineData[5] : lineData[4]; //CE141
        scores[2] = lineData[6] == null ? lineData[7] : lineData[6]; //CE142

        for (int i = 0 ; i < 9 ; i++) {
            scores[i + 3] = lineData[i + 8];
                //CE151, CE152, CE153, CE154, CE155, CE161, CE162, CE163, CE164
                // = scores[3] -> scores[11] in order
        }

        for (int i = 0 ; i < scores.length ; i++) {
            if (scores[i] != null) {
                moduleScores.get(i).add(Integer.parseInt(scores[i]));
            }
        }


        int length = 0;
        for (int i = 0 ; i < scores.length ; i++) {
            if (scores[i] != null) { //Counting how many not null values
                length += 1;
            }
        }

        double[] stats = new double[length];
        String[] newScores = new String[length];
        int temp = length;
        for (int i = 0 ; i < scores.length ; i++) {
            if (scores[i] != null){
                stats[stats.length-temp] = Integer.parseInt(scores[i]);
                newScores[newScores.length-temp] = moduleNames[i] + ": " + scores[i];
                temp--;
            } //Removing null values and providing a string array for display purposes and an int array for calculation purposes
        }

        sort(stats);
        return new StudentData(regNum, course, newScores, stats);
    }

    public List<ModuleData> createModuleDatabase () {
        List<ModuleData> moduleDataBase = new ArrayList<>();
        for (int i = 0 ; i < moduleScores.size() ; i++) {
            ModuleData data = new ModuleData(moduleNames[i], moduleScores.get(i));
            moduleDataBase.add(data);
        } return moduleDataBase;
    }
}

