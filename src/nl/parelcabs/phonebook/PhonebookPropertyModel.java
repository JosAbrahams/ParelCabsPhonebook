package nl.parelcabs.phonebook;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Locale;

public class PhonebookPropertyModel {

    private final PropertyChangeSupport propertyChangeSupport;

    private final HashMap<ContactType, String> inputFilenames = new HashMap<>();
    private final PhonebookController controller;

    public PhonebookPropertyModel(PhonebookController controller) {
        this.controller = controller;
        propertyChangeSupport = new PropertyChangeSupport(this);
        EnumSet<ContactType> allContactTypes = EnumSet.allOf( ContactType.class);
        for (ContactType contactType : allContactTypes) {
            inputFilenames.put(contactType, getInputFilename(contactType));
        }

    }
    public String getPhonebookPropertyFilename() {
        return phonebookPropertyFilename;
    }

    public void setPhonebookPropertyFilename(String phonebookPropertyFilename) {
        this.phonebookPropertyFilename = phonebookPropertyFilename;
    }

    private String phonebookPropertyFilename;

    public String getInputFilename(ContactType contactType) {
        return inputFilenames.get(contactType);
    }

    public void setInputFilename(ContactType contactType, String inputFilename) {
        inputFilenames.put(contactType, inputFilename);
        propertyChangeSupport.firePropertyChange(contactType.name(), null, inputFilename);
    }

    public String getOutputContactsFilename() {
        return outputContactsFilename;
    }

    public void setOutputContactsFilename(String outputContactsFilename) {
        this.outputContactsFilename = outputContactsFilename;
        propertyChangeSupport.firePropertyChange("outputfile", null, outputContactsFilename);
    }

    private String outputContactsFilename;

    public void addPropertyChangeListener(PropertyChangeListener listener){
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public String getSuggestedNewOutputFilename() {
        String outputFolder = (String) controller.getProperty(PhonebookConstants.getOutputFolderPropertyName());
        LocalDateTime ldt = LocalDateTime.now();
        String formattedDateStr = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.getDefault()).format(ldt);
        String fileSeparator = File.separator;
        String outputFilename = PhonebookConstants.getDefaultOutputFilename() + formattedDateStr + ".xml";
        String suggestedNewOutputFilename = outputFolder + fileSeparator + outputFilename;
        System.out.println(suggestedNewOutputFilename);
        return suggestedNewOutputFilename;
    }

    //public boolean isNewOutputFilenameOk(String newOutputFilename){
     //   return true;
   // }
}
