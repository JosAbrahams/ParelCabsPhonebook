package nl.parelcabs.phonebook;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PhonebookDefaults {
    private final String phonebookBuilderPropertiesFilename;
    private final Properties phonebookProperties;

    public PhonebookDefaults(String phonebookPropertiesFilename) {
        this.phonebookBuilderPropertiesFilename = phonebookPropertiesFilename;
        phonebookProperties = getProperties();
    }

    private Properties getProperties(){
        Properties properties = null;
        String message = null;
        String propertyFilename = phonebookBuilderPropertiesFilename;
        try (FileReader propertyFileReader = new FileReader(propertyFilename)) {
            properties = new Properties();
            properties.load(propertyFileReader);
        } catch (FileNotFoundException e) {
            message = "Kan propertyfile " + propertyFilename + " niet vinden.";
        } catch (IOException e) {
            message = "Fout tijdens het laden van properties uit properyfile " + propertyFilename + ".";
        }
        if (message != null) {
            System.err.println("Fatale fout:" + message + "\nHet programma wordt gestopt.");
            System.exit(-1);
        }
        return properties;
    }

    public String getDefaultFilename(ContactType contactType) {
        String inputFilenamePropertyName = contactType.getInputFilenamePropertyName();
        if (inputFilenamePropertyName == null) {
            throw new IllegalArgumentException("No default filename available for contactType '" + contactType + "'");
        } else {
            return phonebookProperties.getProperty(inputFilenamePropertyName);
        }
    }

    public String getDefaultOutputFilename() {
        return phonebookProperties.getProperty(PhonebookConstants.getOutputFilenamePropertyName());
    }
}
