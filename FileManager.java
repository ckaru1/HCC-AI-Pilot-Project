package Final_Project;

import java.io.*;

public class FileManager {

    private final int ARR_SIZE = 200;
    private BufferedReader br;
    private BufferedWriter bw;

    public FileManager(String in, String out) throws Exception {
        br = new BufferedReader(new FileReader(in));
        bw = new BufferedWriter(new FileWriter(out));
    }

    public FileManager(String in) throws Exception {
        br = new BufferedReader(new FileReader(in));
        bw = null;
    }

    public LinearRegressionModel readDataPairs() {
        try {
            LinearRegressionModel model = new LinearRegressionModel();
            String line = br.readLine();
            line = br.readLine();

            while (line != null) {
                String[] data = line.split(",");
                double xVal = Double.parseDouble(data[0].trim());
                double yVal = Double.parseDouble(data[1].trim());
                model.addDataPair(xVal, yVal);
                line = br.readLine();
            }

            return model;

        } catch (Exception e) {
            return null;
        }
    }

    public Crocodilian[] readCrocData() {
        try {
            Crocodilian[] crocs = new Crocodilian[ARR_SIZE];
            int count = 0;
            String line = br.readLine();
            line = br.readLine();

            while (line != null) {
                String[] data = line.split(",");
                String fam = data[0].trim();
                String name = data[1].trim();
                char gen = data[2].trim().charAt(0);
                double w = Double.parseDouble(data[3].trim());
                double l = Double.parseDouble(data[4].trim());

                if (count < crocs.length) {
                    crocs[count] = new Crocodilian(fam, name, gen, w, l);
                    count++;
                } else {
                    System.out.println("The array is full");
                }
                line = br.readLine();
            }

            Crocodilian[] full = new Crocodilian[count];
            for (int i = 0; i < count; i++) {
                full[i] = crocs[i];
            }

            return full;
        } catch (Exception e) {
            return null;
        }
    }

    public void writeToOutput(String text) throws Exception {
        bw.write(text);
        bw.newLine();
    }

    public void flushAndCloseFiles() {
        try {
            if (bw != null) {
                bw.flush();
                bw.close();
            }
            br.close();
        } catch (Exception e) {
            System.out.println("There was a problem closing the files");
        }
    }
}