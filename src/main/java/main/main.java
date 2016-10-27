package main;

public class main {

    public static void main(String[] args) {

        SnackChooser s = new SnackChooser();
        final String generate = "gen";
        for (String cmd : args) {
            if (cmd.equalsIgnoreCase(generate)) {
                s.GenerateSampleDataAndWrite();
            }
        }

        try {
            s.LoadSnackData();
            s.DisplaySnackForToday();
        } catch (Exception e) {
            System.out.println("\n\nError. " + e.getCause() + ". If the snacks file is not yet generated, please run this program with '" + generate + "' as a commandline argument");
        }
    }
}
