package Final_Project;

import java.io.File;

public class main {
    public static void main(String[] args) throws Exception {
        FileManager fileManager = new FileManager("Final_Project/Saltwater_croc_growth.csv");
        LinearRegressionModel model = fileManager.readDataPairs();

        System.out.println("\nLinear Regression Model Formula:\n");
        System.out.println(model + "\n");

        double[] ages = { 1.0, 3.0, 5.0, 10.0 };

        for (int i = 0; i < ages.length; i++) {
            double age = ages[i];
            double predictedLength = model.findYgivenX(age);
            System.out.println("Length for age " + age + ": " + predictedLength + "cm");
        }

        fileManager.flushAndCloseFiles();
    }
}