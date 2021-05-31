package com.mattg;

/**
 * Class to provide command line strings to append to text, so that we don't have to
 * create them when we need to use them
 */
public class GuiStrings {

    public static String newLine = "\n";
    private static String error = "**(ERROR)**";
    public static String filler =    "\n:::==:::==:::==:::==:::==:::==:::==:::==:::==:::==:::==:::\n";
    public static String exitTitle = filler +
                                     "\n:::==:::==:::==:::==:::==   Bye  ==:::==:::==:::==:::==:::\n" +
                                     filler;
    public static String optionsTitle = filler +
                                     ":::==:::==:::==:::==:::   Options  :::==:::==:::==:::==:::" +
                                     filler;
    public static String mainTitle = filler +
                                      ":::==:::==  <>      XML Parsing Utility      <> ==:::==:::\n" +
                                       ":::==:::==:::==:::==:::==:::==:::==:::==:::==:::==:::==:::\n" +
                                       "::: Enter 'jsonfile' or 'xmlfile', then a path to get  :::\n" +
                                       "::: that file and convert it from either JSON to XML or:::\n" +
                                       "::: vice versa. Enter 'jsonstring' or 'xmlstring' to   :::\n" +
                                       "::: convert a string value from JSON to XML or vice    :::\n" +
                                       "::: versa.  Enter 'options' to see a menu, 'createfile':::\n" +
                                       "::: toggles whether or not a new file with the result  :::\n" +
                                       "::: will be created, 'showresult' toggles printing the :::\n" +
                                       "::: result to the console, 'exit' closes the program.  :::\n" +
                                                  filler;

    /**
     * For creating a menu line
     * @param number number of the menu option
     * @param text menu option
     * @return String value to print out on command line
     */
    public static String menuLine(int number, String text) {
        return "(" + number + ") " + text;
    }


    /**
     * Error message
     * @param text error description
     * @return A formatted error message to print to console
     */
    public static String errorString(String text){
        return error + newLine + "* * " + text + newLine + error;
    }


}
