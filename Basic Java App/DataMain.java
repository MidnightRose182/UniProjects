import java.util.Arrays;
import java.util.List;

public class DataMain extends DataProcessing {

    public static void main(String[] args) {
        ApplicationGUI ap = new ApplicationGUI() ;

        /* Leave this stuff here for testing purposes
        DataProcessing d = new DataProcessing() ;
        List<StudentData> sDatabase = d.readData("EducationTool\\dataset\\DataFileFor2022-23-problem_statement.csv");
        List<ModuleData> mDatabase = d.createModuleDatabase();
         */

        /*
        for(int i = 0; i<sDatabase.size(); i++){
            System.out.println("---------------------------------------------------");
            System.out.println(sDatabase.get(i).course);
            System.out.println(sDatabase.get(i).regNum);
            System.out.println(Arrays.toString(sDatabase.get(i).scores));
        }
        */

        /*
        for(int i = 0; i<mDatabase.size(); i++){
            System.out.println("---------------------------------------------------");
            System.out.println(mDatabase.get(i).name);
            System.out.println(mDatabase.get(i).mean);
            System.out.println(Arrays.toString(mDatabase.get(i).scores));
        }
         */
    }
// Next steps are to add more USEFUL visualisation methods (regression line, histogram, stuff you can make analysis from)
// As well as to add module difficulty (histograms can help observe module averages as well as distribution)
}
