package Final_Project;

public class Crocodilian {

    private String family;
    private String commonName;
    private char gender;
    private double weight_kg;
    private double length_m;

    public Crocodilian() {
        family = "Standard";
        commonName = "Crocodile";
        gender = 'f';
        weight_kg = 750;
        length_m = 5.0;
    }

    public Crocodilian(String fam, String name, char gen, double w, double l) {
        family = fam;
        commonName = name;
        gender = gen;
        weight_kg = w;
        length_m = l;
    }

    public String getFamily() {
        return family;
    }

    public String getCommonName() {
        return commonName;
    }

    public char getGender() {
        return gender;
    }

    public double getWeight() {
        return weight_kg;
    }

    public double getLength() {
        return length_m;
    }

    public String toString() {
        return ("Family: " + getFamily() + "Common Name: " + getCommonName() + "Gender: "
                + getGender() + "Weight" + getWeight() + "Length" + getLength());
    }
}