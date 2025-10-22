package nl.parelcabs.phonebook;

import java.util.HashMap;

public class PhonebookConstants {

    static HashMap<ContactType, String> fileTypeCaptions = new HashMap<>();

    static {
        fileTypeCaptions.put(ContactType.PASSENGERS, "Passagiersbestand");
        fileTypeCaptions.put(ContactType.DRIVERS, "Chauffeursbestand");
        fileTypeCaptions.put(ContactType.CARS, "Auto's bestand");
        fileTypeCaptions.put(ContactType.PLANNERS, "Plannersbestand");
        fileTypeCaptions.put(ContactType.OTHER, "Overige bestand");
    }

    public static String getFileTypeCaption(ContactType contactType) {
        return fileTypeCaptions.get(contactType);
    }

    public static String getOutputFileCaption() {
        return "Output contactbestand";
    }

    public static String getOutputFileChangeButtonName() {
        return "Output";
    }

    public static String getDefaultOutputFilename() {
        // This name will later be suffixed with today's date (yyyymmdd), and a '.xml' suffix.
        return "Parelcabs-Contacten-";
    }

    public static String getGenerateButtonName(){
        return "GenerateContacts";
    }

    public static String getOutputFilenamePropertyName(){
        return "outputFilename";
    }

    public static String getOutputFolderPropertyName(){
        return "outputFolder";
    }
}
