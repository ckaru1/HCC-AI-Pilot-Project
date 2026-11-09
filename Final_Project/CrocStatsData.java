package Final_Project;

/**
 * CrocStatsData
 *
 * Performs the Step 2 statistical calculations on a Crocodilian array:
 *   - mean weight and mean length
 *   - weight (x, independent) vs. length (y, dependent) regression line
 * for (a) the entire data set, (b) each common name, and (c) each gender
 * within each common name. Results are written through a FileManager.
 *
 * Groups are discovered from the data itself, so the class works for any
 * file with the same format (it does not hard-code species or genders).
 */
public class CrocStatsData {

    private static final int MAX_GROUPS = 50;
    private static final char ANY_GENDER = '*';
    private static final String DIVIDER =
            "==========================================================================================";
    private static final String SUB_DIVIDER =
            "------------------------------------------------------------------------------------------";

    private Crocodilian[] crocs;
    private int numCrocs;

    /**
     * Copies every non-null Crocodilian into this object's own array, so a
     * partially filled array (e.g. size 200 holding 160 crocs) is handled safely.
     */
    public CrocStatsData(Crocodilian[] data) {
        crocs = new Crocodilian[data.length];
        numCrocs = 0;
        for (int i = 0; i < data.length; i++) {
            if (data[i] != null) {
                crocs[numCrocs] = data[i];
                numCrocs++;
            }
        }
    }

    public int getNumCrocs() {
        return numCrocs;
    }

    // ------------------------------------------------------------------
    // Group filtering
    // name == null   -> any common name
    // gender == '*'  -> any gender
    // ------------------------------------------------------------------

    private boolean matches(Crocodilian c, String name, char gender) {
        boolean nameOk = (name == null) || c.getCommonName().equalsIgnoreCase(name);
        boolean genderOk = (gender == ANY_GENDER)
                || Character.toUpperCase(c.getGender()) == Character.toUpperCase(gender);
        return nameOk && genderOk;
    }

    public int countGroup(String name, char gender) {
        int n = 0;
        for (int i = 0; i < numCrocs; i++) {
            if (matches(crocs[i], name, gender)) {
                n++;
            }
        }
        return n;
    }

    // ------------------------------------------------------------------
    // Means
    // ------------------------------------------------------------------

    public double meanWeight(String name, char gender) {
        double sum = 0;
        int n = 0;
        for (int i = 0; i < numCrocs; i++) {
            if (matches(crocs[i], name, gender)) {
                sum += crocs[i].getWeight();
                n++;
            }
        }
        if (n == 0) {
            return 0;
        }
        return sum / n;
    }

    public double meanLength(String name, char gender) {
        double sum = 0;
        int n = 0;
        for (int i = 0; i < numCrocs; i++) {
            if (matches(crocs[i], name, gender)) {
                sum += crocs[i].getLength();
                n++;
            }
        }
        if (n == 0) {
            return 0;
        }
        return sum / n;
    }

    // ------------------------------------------------------------------
    // Discovering the groups present in the data
    // ------------------------------------------------------------------

    /** Distinct common names, in the order they first appear in the data. */
    public String[] getCommonNames() {
        String[] temp = new String[MAX_GROUPS];
        int n = 0;
        for (int i = 0; i < numCrocs; i++) {
            String name = crocs[i].getCommonName();
            boolean found = false;
            for (int j = 0; j < n; j++) {
                if (temp[j].equalsIgnoreCase(name)) {
                    found = true;
                }
            }
            if (!found && n < MAX_GROUPS) {
                temp[n] = name;
                n++;
            }
        }
        String[] names = new String[n];
        for (int i = 0; i < n; i++) {
            names[i] = temp[i];
        }
        return names;
    }

    /** Distinct genders (upper-case) present for one common name. */
    public char[] getGenders(String name) {
        char[] temp = new char[MAX_GROUPS];
        int n = 0;
        for (int i = 0; i < numCrocs; i++) {
            if (matches(crocs[i], name, ANY_GENDER)) {
                char g = Character.toUpperCase(crocs[i].getGender());
                boolean found = false;
                for (int j = 0; j < n; j++) {
                    if (temp[j] == g) {
                        found = true;
                    }
                }
                if (!found && n < MAX_GROUPS) {
                    temp[n] = g;
                    n++;
                }
            }
        }
        char[] genders = new char[n];
        for (int i = 0; i < n; i++) {
            genders[i] = temp[i];
        }
        return genders;
    }

    // ------------------------------------------------------------------
    // Regression (weight = x, length = y)
    // ------------------------------------------------------------------

    /**
     * Builds a LinearRegressionModel for one group and returns its equation.
     *
     * Two details of LinearRegressionModel matter here:
     *   1. The default constructor only holds 100 pairs, but the full data
     *      set has 160, so the alternate constructor is used with arrays
     *      sized exactly to the group.
     *   2. The pair counter is static (shared by every model), so each model
     *      is built and used immediately, before the next one is created.
     */
    public String regressionLine(String name, char gender) {
        int n = countGroup(name, gender);
        double[] xs = new double[n];
        double[] ys = new double[n];
        int k = 0;
        for (int i = 0; i < numCrocs; i++) {
            if (matches(crocs[i], name, gender)) {
                xs[k] = crocs[i].getWeight();
                ys[k] = crocs[i].getLength();
                k++;
            }
        }

        LinearRegressionModel model = new LinearRegressionModel(xs, ys, n);
        try {
            return model.toString();
        } catch (IllegalStateException e) {
            return "Not enough data for a regression line";
        }
    }

    // ------------------------------------------------------------------
    // Output formatting
    // ------------------------------------------------------------------

    private String genderName(char g) {
        if (g == 'F') {
            return "Female";
        } else if (g == 'M') {
            return "Male";
        }
        return String.valueOf(g);
    }

    private String meanLine(String label, String name, char gender) {
        return String.format("%-34s n = %3d   mean weight = %8.2f kg   mean length = %6.3f m",
                label, countGroup(name, gender), meanWeight(name, gender), meanLength(name, gender));
    }

    private String regressionOutputLine(String label, String name, char gender) {
        return String.format("%-34s n = %3d   %s",
                label, countGroup(name, gender), regressionLine(name, gender));
    }

    /** Writes all six Step 2 results to the output file. */
    public void writeResults(FileManager fm) throws Exception {
        String[] names = getCommonNames();

        fm.writeToOutput(DIVIDER);
        fm.writeToOutput("Crocodilian Data Results");
        fm.writeToOutput("Records analyzed: " + numCrocs);
        fm.writeToOutput(DIVIDER);
        fm.writeToOutput("");

        // 1. Means: entire data set
        fm.writeToOutput("1. Mean weight and length - entire data set");
        fm.writeToOutput(SUB_DIVIDER);
        fm.writeToOutput(meanLine("All crocodilians", null, ANY_GENDER));
        fm.writeToOutput("");

        // 2. Means: by common name
        fm.writeToOutput("2. Mean weight and length - by common name");
        fm.writeToOutput(SUB_DIVIDER);
        for (int i = 0; i < names.length; i++) {
            fm.writeToOutput(meanLine(names[i], names[i], ANY_GENDER));
        }
        fm.writeToOutput("");

        // 3. Means: by common name and gender
        fm.writeToOutput("3. Mean weight and length - by common name and gender");
        fm.writeToOutput(SUB_DIVIDER);
        for (int i = 0; i < names.length; i++) {
            char[] genders = getGenders(names[i]);
            for (int j = 0; j < genders.length; j++) {
                String label = names[i] + " (" + genderName(genders[j]) + ")";
                fm.writeToOutput(meanLine(label, names[i], genders[j]));
            }
        }
        fm.writeToOutput("");

        fm.writeToOutput("Regression lines: x = weight (kg), y = length (m)");
        fm.writeToOutput("");

        // 4. Regression: entire data set
        fm.writeToOutput("4. Regression line - entire data set");
        fm.writeToOutput(SUB_DIVIDER);
        fm.writeToOutput(regressionOutputLine("All crocodilians", null, ANY_GENDER));
        fm.writeToOutput("");

        // 5. Regression: by common name
        fm.writeToOutput("5. Regression line - by common name");
        fm.writeToOutput(SUB_DIVIDER);
        for (int i = 0; i < names.length; i++) {
            fm.writeToOutput(regressionOutputLine(names[i], names[i], ANY_GENDER));
        }
        fm.writeToOutput("");

        // 6. Regression: by common name and gender
        fm.writeToOutput("6. Regression line - by common name and gender");
        fm.writeToOutput(SUB_DIVIDER);
        for (int i = 0; i < names.length; i++) {
            char[] genders = getGenders(names[i]);
            for (int j = 0; j < genders.length; j++) {
                String label = names[i] + " (" + genderName(genders[j]) + ")";
                fm.writeToOutput(regressionOutputLine(label, names[i], genders[j]));
            }
        }
        fm.writeToOutput("");
        fm.writeToOutput(DIVIDER);
    }
}