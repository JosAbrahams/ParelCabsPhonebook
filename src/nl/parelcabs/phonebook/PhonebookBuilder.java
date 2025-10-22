package nl.parelcabs.phonebook;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PhonebookBuilder {

    private final Properties phonebookBuilderProperties;

    public PhonebookBuilder(PhonebookPropertyModel model) throws ContactsFileNotGeneratedException {
        String phonebookBuilderPropertiesFilename = model.getPhonebookPropertyFilename();
        phonebookBuilderProperties = getProperties(phonebookBuilderPropertiesFilename);
    }

    public void build() throws ContactsFileNotGeneratedException {
        List<String> result = new ArrayList<>();
        buildHeader(result);
        buildGroups(result);
        buildContactEntries(result);
        buildFooter(result);
        writeResult(result, phonebookBuilderProperties.getProperty("outputFilename"));
    }

    private void buildHeader(List<String> result) {
        result.add("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        result.add("<vp_contact>");
    }

    private void buildGroups(List<String> result) {
        result.add("\t<root_group>");
        for(ContactType group : ContactType.values()) {
            result.add("\t\t<group display_name=\"" + group + "\" ring=\"\" />");
        }
        result.add("\t</root_group>");
    }

    private void buildContactEntries(List<String> result) {
        List<ContactEntry> passengerContactEntries = new InputListReader(ContactType.PASSENGERS, phonebookBuilderProperties.getProperty("passengerListFilename")).readInputfile();
        List<ContactEntry> driverContactEntries = new InputListReader(ContactType.DRIVERS, phonebookBuilderProperties.getProperty("driverListFilename")).readInputfile();
        List<ContactEntry> plannerContactEntries = new InputListReader(ContactType.PLANNERS, phonebookBuilderProperties.getProperty("plannerListFilename")).readInputfile();
        List<ContactEntry> carContactEntries = new InputListReader(ContactType.CARS, phonebookBuilderProperties.getProperty("carListFilename")).readInputfile();
        List<ContactEntry> otherContactEntries = new InputListReader(ContactType.OTHER, phonebookBuilderProperties.getProperty("otherListFilename")).readInputfile();

        List<ContactEntry> allEntries = new ArrayList<>(passengerContactEntries);
        allEntries.addAll(driverContactEntries);
        allEntries.addAll(plannerContactEntries);
        allEntries.addAll(carContactEntries);
        allEntries.addAll(otherContactEntries);
        processContactEntries(result, allEntries);
    }

    private void buildFooter(List<String> result){
        result.add("</vp_contact>");
    }

    private void processContactEntries(List<String> result, List<ContactEntry> entries) {
        result.add("\t<root_contact>");
        for (ContactEntry entry : entries) {
            result.add(entry.format());
        }
        result.add("\t</root_contact>");
    }

    private void writeResult(List<String> result, String outputFilename) throws ContactsFileNotGeneratedException {
        try {
            Charset charset = StandardCharsets.UTF_8;
            FileWriter writer = new FileWriter(outputFilename, charset);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            for (String line : result) {
                writer.write(line + '\n');
            }
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            String message = "Fout tijdens het schrijven van '" + outputFilename + "': " + e.getMessage();
            System.err.println(message);
            throw new ContactsFileNotGeneratedException(message);
        }
    }

    protected static Properties getProperties(String phonebookPropertyFilename) throws ContactsFileNotGeneratedException{
        Properties properties = null;
        String message = null;
        try (FileReader propertyFileReader = new FileReader(phonebookPropertyFilename)) {
            properties = new Properties();
            properties.load(propertyFileReader);
        } catch (FileNotFoundException e) {
            message = "Kan propertyfile " + phonebookPropertyFilename + "niet vinden.";
        } catch (IOException e) {
            message = "Fout tijdens laden van properties uit bestand" + phonebookPropertyFilename + " :" + e.getMessage();
        }
        if (message != null) {
            System.err.println(message);
            throw new ContactsFileNotGeneratedException(message);
        }
        return properties;
    }
}
