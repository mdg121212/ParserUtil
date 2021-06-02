package com.mattg;

/**
 * Class to provide command line strings to append to text, so that we don't have to
 * create them when we need to use them
 */
public class GuiStrings {
    public static String filler =    ":::==:::==:::==:::==:::==:::==:::==:::==:::==:::==:::==:::\n";
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
                                       "::: versa.  Entering 'createfile' toggles whether or   :::\n" +
                                       "::: not a new file with the result will be created,    :::\n" +
                                       "::: 'showresult' toggles printing the result to the    :::\n" +
                                       "::: console, 'exit' closes the program, and 'commands' :::\n" +
                                       "::: lists the available commands. Results by default   :::\n" +
                                       "::: are copied to clipboard, entering 'copy' toggles   :::\n" +
                                       "::: feature. Enter 'test' for XML to JSON address book :::\n" +
                                       "::: conversion example.   Enjoy!                       :::\n" +
                                                  filler;
    public static String commands = filler + "'json file' - next input should be a file path to JSON document.\n" +
            "'xmlfile' - next input should be a file path to XML document.\n" +
            "'jsonstring' - next input should be JSON.\n" +
            "'xmlstring' - next input should be XML.\n" +
            "'createfile' - toggles file creation from results, on by default.\n" +
            "'showresult' - toggles results being shown in console, on by default.\n" +
            "'copy' - toggles results being copied to clipboard, on by default.\n" +
            "'test' - converts provided example xml to json.\n" +
            "'exit' - exits the program.\n" + filler;

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
        String error = "**(ERROR)**";
        return error + text + error;
    }


}
