package com.mattg;

import org.json.JSONObject;
import org.json.JSONString;
import org.json.JSONStringer;
import org.json.XML;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.ls.LSOutput;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Scanner;

public class ParserApp {
    Scanner in;
    String command;
    int numberCommand;
    private  FileReader reader;
    //boolean flags to control the flow of application when branching
    private boolean isXml = false;
    private boolean isJson = false;
    private boolean isFilePath = false;
    //default to file creation
    private boolean createFile = true;
    private boolean showResult = true;
    private boolean isStringToConvert;
    private boolean copyToClipBoard = true;
    private Path filesPath;
    private int count = 0;

    public void runApplication(){
        checkDirectory();

        System.out.println(GuiStrings.mainTitle);
        in = new Scanner(System.in);

        while(true) {
            //keep the scanner open to parse all user input
            if (in.hasNext() && !in.hasNextInt()) {
                command = in.nextLine();
                if (isXml) {
                    //input is filepath to xml document
                    readXMLToJSON(command.trim());
                    resetBooleans();
                } else if (isJson) {
                    //input is filepath to json document
                    if(!command.startsWith("'")) {
                       command = "'" + command.replaceAll("\"","'") + "'";
                    } else
                        command.replaceAll("\"","'");
                    readJSONtoXML(command.trim());
                    resetBooleans();
                }
                parseCommand(command);
            }

            if (in.hasNextInt()) {
                numberCommand = in.nextInt();
                parseNumberCommand(numberCommand);
            }
        }

    }

    /**
     * Checks if the directory for file output exists, if it doesn't it will be created
     */
    private void checkDirectory() {
        System.out.println("check directory called");
        try {
            Path path = Paths.get("conversions/");
            if(!Files.exists(path)) {
                Path directory = Files.createDirectories(path);
                System.out.println("directory created! your files will be saved here --> " + filesPath);
                filesPath = Path.of(directory.toString());
                System.out.println(filesPath);
            } else
                filesPath = path.toAbsolutePath();
        } catch (IOException e){
            System.out.println(GuiStrings.errorString("Error Creating Directory: "));
            System.out.println(GuiStrings.errorString( e.getLocalizedMessage()));
            System.out.println(GuiStrings.errorString( String.valueOf(e.getCause())));
        }

    }

    private void resetBooleans(){
        isJson = false;
        isXml = false;
    }

    /**
     * Handle integer inputs when user is choosing from options menu
     * @param numberCommand number from the console
     */
    private void parseNumberCommand(int numberCommand) {
        switch (numberCommand) {
            case 1 -> {
                System.out.println("Please enter the file path to your XML document for JSON conversion.");
                isXml = true;
                isJson = false;
                isFilePath = true;
                break;
            }
            case 2 -> {
                System.out.println("Please enter your XML to convert to JSON.");
                isXml = true;
                isJson = false;
                isFilePath = false;
                break;
            }
            case 3 -> {
                System.out.println("Please enter the filepath of your JSON document for XML conversion.");
                isXml = false;
                isJson = true;
                isFilePath = true;
                break;
            }
            case 4 -> {
                System.out.println("Please enter a JSON string to convert to XML");
                isXml = false;
                isJson = true;
                isFilePath = false;
                break;
            }
            case 5 -> {
                closeProgram();
                break;
            }
            default -> {
                count++;
                if (count == 3) {
                    System.out.println(GuiStrings.mainTitle);
                    count = 0;
                } else
                    System.out.println("Not a valid option...");
                break;
            }
        }
    }

    /**
     * Takes the console input and handles it based on a switch statement
     * @param command user input to be handled
     */
    private void parseCommand(String command) {
        String formatted = command.toLowerCase(Locale.ROOT);
        switch (formatted) {
            case "options" -> {
                showMenu("options");
                break;
            }
            case "exit" -> {
                closeProgram();
                break;
            }
            case "xmlfile" -> {
                System.out.println("**Enter the file path of XML to be converted**");
                isXml = true;
                isFilePath = true;
                isJson = false;
                isStringToConvert = false;
                break;
            }
            case "jsonfile" -> {
                System.out.println("**Enter the file path of JSON to be converted**");
                isJson = true;
                isXml = false;
                isStringToConvert = false;
                isFilePath = true;
                break;
            }
            case "jsonstring" -> {
                System.out.println("**Enter the JSON to be converted**");
                isJson = true;
                isXml = false;
                isStringToConvert = true;
                isFilePath = false;
                break;
            }
            case "copy" -> {
                copyToClipBoard = !copyToClipBoard;
                break;
            }
            case "xmlstring" -> {
                System.out.println("**Enter XML to be converted**");
                isXml = true;
                isJson = false;
                isFilePath = false;
                isStringToConvert = true;
                break;
            }
            case "showresult" -> {
                if (!showResult) {
                    System.out.println("Results of conversions will now be printed to the console");
                    showResult = true;
                } else
                    System.out.println("Results of conversions will not be printed to the console");
                showResult = false;
                break;
            }
            case "createfile" -> {
                if (!createFile) {
                    System.out.println("Now files will be generated for your conversions.  They will exist here --> " + filesPath);
                    createFile = true;
                } else
                    System.out.println("File creation turned off");
                createFile = false;
                break;
            }
            default -> {
                count++;
                if (count == 3) {
                    System.out.println(GuiStrings.mainTitle);
                    count = 0;
                } else
                    System.out.println("...");
                break;
            }
        }

    }
    /**
     * Exits the program and displays a message to indicate the closure.
     */
    private void closeProgram() {
        System.out.println(GuiStrings.exitTitle);
        System.exit(1);
    }

    /**
     * Show a command line menu describing program functionality
     */
    private void showMenu(String menuType) {
        if(menuType.equalsIgnoreCase("options")){
            System.out.println(GuiStrings.optionsTitle);
            System.out.println(GuiStrings.menuLine(1, "Convert XML document to JSON by filepath."));
            System.out.println(GuiStrings.menuLine(2, "Input XML string and create JSON."));
            System.out.println(GuiStrings.menuLine(3, "Convert JSON document to XML by filepath."));
            System.out.println(GuiStrings.menuLine(4, "Input JSON string and create XML."));
            System.out.println(GuiStrings.menuLine(5, "Exit program."));
        }

    }

    /**
     * Attempts to copy the result to the system clipboard for pasting
     * into schema validator, .txt file, etc
     * @param outputText results text to copy to clipboard (if supported)
     */
    private void copyToClipBoard(String outputText) {
        try {
            StringSelection text = new StringSelection(outputText);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(text, text);
            System.out.println("***RESULTS COPIED TO CLIPBOARD***");
        } catch (Exception e){
            System.out.println(GuiStrings.errorString("Error copying results to clipboard..."));
            System.out.println(GuiStrings.errorString(e.getLocalizedMessage()));
            in = new Scanner(System.in);
        }

    }

    /**
     * Either reads an XML file and converts it to JSON, or reads an XML string and converts to JSON
     * @param filePathOrString Either a string, or a filepath (options menu/input can determine this)
     * @return String JSON converted from either a string or a file
     */
    private String readXMLToJSON(String filePathOrString) {
        if(isFilePath){
            try {
                reader = new FileReader(filePathOrString);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String lineInFile = bufferedReader.readLine();
                StringBuilder textBuilder = new StringBuilder();
                while (lineInFile != null) {
                    //read next line
                    textBuilder.append(lineInFile.trim());
                    lineInFile = bufferedReader.readLine();
                }
                bufferedReader.close();
                System.out.println("File read, creating json....");
                System.out.println(createFile + " " + showResult);
                String results = jsonStringFromXML(textBuilder.toString());
                if(createFile) {createFileFromResult(results);}
                if(showResult) {System.out.println("Your xml converted to : \n" + results);}
                if(copyToClipBoard) copyToClipBoard(results);
                return results;
            } catch (FileNotFoundException e) {
                System.out.println("You entered an invalid file path, please try again");
                in = new Scanner(System.in);
                return "";
            } catch (IOException e) {
                System.out.print(GuiStrings.errorString(e.getLocalizedMessage()));
                System.out.print(GuiStrings.errorString(String.valueOf(e.getCause())));
                in = new Scanner(System.in);
                return "";
            }
        } else {
            System.out.println("hitting else block");
            String results = jsonStringFromXML(command.trim());
            if(createFile) createFileFromResult(results);
            if(showResult) System.out.println("Your xml converted to : \n" + results);
            if(copyToClipBoard) copyToClipBoard(results);
            return results;
        }

    }
    /**
     * Creates a string from JSON input converted to XML
     * @param filePathOrString Either a file path to JSON file, or JSON string
     * @return String XML converted from JSON input
     */
    private  String readJSONtoXML(String filePathOrString) {
        if(isFilePath){
            try {
                reader = new FileReader(filePathOrString);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String lineInFile = bufferedReader.readLine();
                StringBuilder textBuilder = new StringBuilder();
                while (lineInFile != null) {
                    //read next line
                    textBuilder.append(lineInFile.trim());
                    lineInFile = bufferedReader.readLine();
                }
                bufferedReader.close();
                System.out.println("File read, creating xml....");
                String results = xmlStringFromJson(textBuilder.toString(), "root");
                if(createFile) createFileFromResult(results);
                if(showResult) System.out.println("Your JSON converted to : \n" +results);
                if(copyToClipBoard) copyToClipBoard(results);
                return results;
            } catch (FileNotFoundException e) {
                System.out.println("You entered an invalid file path, please try again");
                in = new Scanner(System.in);
                return "";
            } catch (IOException e) {
                System.out.print(GuiStrings.errorString(e.getLocalizedMessage()));
                System.out.print(GuiStrings.errorString(String.valueOf(e.getCause())));
                in = new Scanner(System.in);
                return "";
            }
        } else {
            String results = "";
            try {
                JSONParser parser = new JSONParser();
                JSONObject json = (JSONObject) parser.parse(command.trim());
                String jsonString = json.toString();
                results = xmlStringFromJson(jsonString, "root");
                if(createFile) createFileFromResult(results);
                if(showResult) System.out.println("Your JSON converted to : \n" +results);
                if(copyToClipBoard) copyToClipBoard(results);
            } catch (Exception e){
                System.out.println(GuiStrings.errorString("Error parsing JSON"));
                in = new Scanner(System.in);
            }

            return results;
        }

    }
    /**
     * Returns a JSON string from the XML string input
     * @param xmlString XML to be converted
     * @return String JSON string converted from XML input
     */
    private static String jsonStringFromXML(String xmlString){
        JSONObject json = XML.toJSONObject(xmlString);
        System.out.println("***GOT JSON FROM XML***");
        return json.toString();
    }

    /**
     * Creates and returns an XML string from the JSON input
     * @param jsonString JSON to convert
     * @param rootName name of the root element in the returned XML
     * @return String XML string value converted from JSON string input
     */
    private  String xmlStringFromJson(String jsonString, String rootName){
        JSONObject object = new JSONObject(jsonString);
        System.out.println("***GOT XML FROM JSON***");
        //create and return standard formatted xml string that can be written to file, or printed to console
        return "<?xml version=\"1.0\" encoding=\"ISO-8859-15\"?>\n<"+rootName+">" + XML.toString(object) + "</"+rootName+">";
    }

    /**
     * Writes the result of a conversion to a file in the directory
     * @param content contents of the file
     * @return whether or not the operation succeeded
     */
    public boolean createFileFromResult(String content){
        String fileTitle = isXml ? "XMLconverted" : "JSONconverted";
        BufferedWriter writer;
        FileWriter fileWriter;
        try{
            String directoryName = filesPath.toAbsolutePath().toString();
            writer = new BufferedWriter(new FileWriter( new File(directoryName, fileTitle + System.currentTimeMillis() + ".txt"), true));
            writer.append(content);
            writer.close();
            return true;
        }catch (IOException e){
            System.out.println(GuiStrings.errorString("Couldn't create file"));
            System.out.println(GuiStrings.errorString(e.getLocalizedMessage()));
            System.out.println(GuiStrings.errorString(String.valueOf(e.getCause())));
            in = new Scanner(System.in);
            return false;
        }
    }
}
