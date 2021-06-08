package com.mattg;


import org.json.JSONObject;
import org.json.XML;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Locale;
import java.util.Scanner;

public class ParserApp {
    Scanner in;
    String command;
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
    private boolean isFirstRun = true;

    public void runApplication(boolean isFirst){
        isFirstRun = isFirst;
        checkDirectory();
        if(isFirst) {
            System.out.println(GuiStrings.mainTitle);
        }
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
                    readJSONtoXML(command);
                    resetBooleans();
                }
                parseCommand(command);
            }

        }

    }

    /**
     * Checks if the directory for file output exists, if it doesn't it will be created
     */
    private void checkDirectory() {
        try {
            Path path = Paths.get("output"+ File.separator + "myconversions" + File.separator);
            if(!Files.exists(path)) {
                Path directory = Files.createDirectories(path);
                filesPath = Paths.get((String.valueOf(directory)));
                if(isFirstRun)
                    System.out.println("directory created! your files will be saved here --> " + filesPath);


            } else
                filesPath = path.toAbsolutePath();
            if(isFirstRun)
                System.out.println("Your conversions will be created here: " + filesPath);

        } catch (IOException e){
            System.out.println(GuiStrings.errorString("Error Creating Directory: "));
            System.out.println(GuiStrings.errorString( e.getLocalizedMessage()));
            System.out.println(GuiStrings.errorString( String.valueOf(e.getCause())));
        }

    }

    private void resetBooleans(){
        isJson = false;
        isXml = false;
        isFilePath = false;
    }

    /**
     * Takes the console input and handles it based on a switch statement
     * @param command user input to be handled
     */
    private void parseCommand(String command) {
        String formatted = command.toLowerCase(Locale.ROOT);
        switch (formatted) {
            case "commands" : {
                System.out.println(GuiStrings.commands);
                count = 0;
                break;
            }
            case "test" : {
                isFilePath = true;
                readXMLToJSON("testxml.xml");
                count = 0;
                break;
            }
            case "exit" : {
                closeProgram();
                count = 0;
                break;
            }
            case "xmlfile" : {
                System.out.println("**Enter the file path of XML to be converted**");
                isXml = true;
                isFilePath = true;
                isJson = false;
                isStringToConvert = false;
                count = 0;
                break;
            }
            case "jsonfile" : {
                System.out.println("**Enter the file path of JSON to be converted**");
                isJson = true;
                isXml = false;
                isStringToConvert = false;
                isFilePath = true;
                count = 0;
                break;
            }
            case "jsonstring" : {
                System.out.println("**Enter the JSON to be converted**");
                isJson = true;
                isXml = false;
                isStringToConvert = true;
                isFilePath = false;
                count = 0;
                break;
            }
            case "copy" : {
                copyToClipBoard = !copyToClipBoard;
                count = 0;
                break;
            }
            case "xmlstring" : {
                System.out.println("**Enter XML to be converted**");
                isXml = true;
                isJson = false;
                isFilePath = false;
                isStringToConvert = true;
                count = 0;
                break;
            }
            case "showresult" : {
                count = 0;
                if (!showResult) {
                    System.out.println("Results of conversions will now be printed to the console");
                    showResult = true;
                    break;
                } else
                    System.out.println("Results of conversions will not be printed to the console");
                showResult = false;
                break;
            }
            case "createfile" : {
                count = 0;
                if (!createFile) {
                    System.out.println("Now files will be generated for your conversions.  They will exist here --> " + filesPath);
                    createFile = true;
                    break;
                } else
                    System.out.println("File creation turned off");
                createFile = false;
                break;
            }
            default : {
                count++;
                if (count == 3) {
                    System.out.println(GuiStrings.mainTitle);
                    count = 0;
                    break;
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
     */
    private void readXMLToJSON(String filePathOrString) {
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
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.println("You entered an invalid file path, please try again");
                in = new Scanner(System.in);
            } catch (IOException e) {
                System.out.print(GuiStrings.errorString(e.getLocalizedMessage()));
                System.out.print(GuiStrings.errorString(String.valueOf(e.getCause())));
                in = new Scanner(System.in);
            }
        } else {
            System.out.println("hitting else block");
            String results = jsonStringFromXML(command.trim());
            if(createFile) createFileFromResult(results);
            if(showResult) System.out.println("Your xml converted to : \n" + results);
            if(copyToClipBoard) copyToClipBoard(results);
        }

    }
    /**
     * Creates a string from JSON input converted to XML
     * @param filePathOrString Either a file path to JSON file, or JSON string
     */
    private void readJSONtoXML(String filePathOrString) {
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
                String results = xmlStringFromJson(textBuilder.toString());
                if(createFile) createFileFromResult(results);
                if(showResult) System.out.println("Your JSON converted to : \n" +results);
                if(copyToClipBoard) copyToClipBoard(results);
            } catch (FileNotFoundException e) {
                System.out.println("You entered an invalid file path, please try again");
                in = new Scanner(System.in);
            } catch (IOException e) {
                System.out.print(GuiStrings.errorString(e.getLocalizedMessage()));
                System.out.print(GuiStrings.errorString(String.valueOf(e.getCause())));
                in = new Scanner(System.in);
            }
        } else {
            String results;
            try {
                JSONObject json = new JSONObject(command.trim());
                String jsonString = json.toString();
                results = xmlStringFromJson(jsonString);
                if(createFile) createFileFromResult(results);
                if(showResult) System.out.println("Your JSON converted to : \n" +results);
                if(copyToClipBoard) copyToClipBoard(results);
            } catch (Exception e){
                System.out.println(GuiStrings.errorString("Error parsing JSON " + e.getMessage()));
                in = new Scanner(System.in);
            }

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
     * @return String XML string value converted from JSON string input
     */
    private  String xmlStringFromJson(String jsonString){
        String xmlReturnString = "";
        try{
            JSONObject jsonObject = new JSONObject(jsonString);
            xmlReturnString = "<?xml version=\"1.0\" encoding=\"ISO-8859-15\"?>" + XML.toString(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("***GOT XML FROM JSON***");
        //create and return standard formatted xml string that can be written to file, or printed to console
        return xmlReturnString;
    }

    /**
     * Writes the result of a conversion to a file in the directory
     * @param content contents of the file
     */
    public void createFileFromResult(String content){
        String fileTitle = isXml ? "XMLconverted" : "JSONconverted";
        try{
            System.out.println("filespath = " + filesPath);
            String directoryName = filesPath.toString();
            Files.write(Paths.get(directoryName, fileTitle + System.currentTimeMillis() + ".txt"), Collections.singleton(content),
                    StandardCharsets.UTF_8);

        }catch (IOException e){
            System.out.println(GuiStrings.errorString("Couldn't create file"));
            System.out.println(GuiStrings.errorString(e.getLocalizedMessage()));
            System.out.println(GuiStrings.errorString(String.valueOf(e.getCause())));
            in = new Scanner(System.in);
        }
    }
}
