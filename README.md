# ParserUtil
A Java command line utility to parse XML to JSON, or vice versa. Options include new file creation, console output, and clipboard copying of conversion results.  The provided test xml file is included and can be converted to JSON by entering "test" in the console. By default results are copied to clipboard, the ouput is logged to the console, and a .txt file is created in the projects directory /output/myconversions.


The basic commands that can be run once the program is running:

    1.commands - will show an available commands
    2.createfile - toggles whether or not new files will be created (default on)
    3.showresult - toggles whether or not results will print to console (default on)
    4.copy - toggles whether or not results are copied to clipboard (default on)
    5.test - will run an example address book conversion
    6.jsonfile - program will fetch file at entered path and convert to xml
    7.xmlfile - progrom will fetch file at entered path and convert to json
    8.jsonstring - program will convert entered json to xml
    9.xmlstring - program will convert entered xml to json
    10.exit - closes the program.
    
To run the application:
    1. Run the .bat file in the project directory : ParserUtil\out\artifacts\ParserUtil_jar
    2. From command line navigate to above directory and run: java -jar ParserUtil.jar
    
    
