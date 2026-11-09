package Final_Project;

import java.io.File;

public class main {
    public static void main(String[] args) throws Exception {
        FileManager fileManager = new FileManager("Final_Project/Saltwater_croc_growth.csv");
        LinearRegressionModel model = fileManager.readDataPairs();
        FileManager fileManger2 = new FileManager("Final_Project/Crocodilia_individual_160_sorted.csv",
                "Final_Project/croc_data_results.txt");
        Crocodilian[] crocs = new Crocodilian[200];

        System.out.println("\nLinear Regression Model Formula:\n");
        System.out.println(model + "\n");

        double[] ages = { 1.0, 3.0, 5.0, 10.0 };

        for (int i = 0; i < ages.length; i++) {
            double age = ages[i];
            double predictedLength = model.findYgivenX(age);
            System.out.println("Length for age " + age + ": " + predictedLength + "cm");
        }
        fileManager.flushAndCloseFiles();

        Crocodilian[] readCrocs = fileManger2.readCrocData();
        if (readCrocs == null) {
            System.out.println("Could not read croc data");
            fileManger2.flushAndCloseFiles();
        } else {
            for (int i = 0; i < readCrocs.length; i++) {
                crocs[i] = readCrocs[i];
            }
        }

        CrocStatsData stats = new CrocStatsData(crocs);
        stats.writeResults(fileManger2);
        System.out.println("Results written to croc_data_results.txt");

        fileManger2.flushAndCloseFiles();

    }
}