package nl.parelcabs.phonebook;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.EnumSet;
import java.util.Properties;

public class PhonebookController implements ActionListener {

    private final PhonebookPropertyModel model;
    private PhonebookPropertyView view;
    private final Properties properties;

    public PhonebookController(PhonebookContext context)
    {
        properties = (Properties) context.get("properties");
        model = new PhonebookPropertyModel(this);
    }

    public void start(){
        view = new PhonebookPropertyView(this);
        view.setVisible(true);
        model.addPropertyChangeListener(view);
    }

    public void setPhonebookPropertiesFilename(String phonebookPropertiesFilename) {
        model.setPhonebookPropertyFilename(phonebookPropertiesFilename);
    }

    PhonebookPropertyModel getModel(){
        return model;
    }

    public void setDefaultValuesOnModel(){
        EnumSet<ContactType> allContactTypes = EnumSet.allOf( ContactType.class);
        PhonebookDefaults defaults = new PhonebookDefaults(PhonebookApp.getPhonebookBuilderPropertiesFilename());
        for (ContactType contactType: allContactTypes) {
            model.setInputFilename(contactType, defaults.getDefaultFilename(contactType));
        }
        model.setOutputContactsFilename(model.getSuggestedNewOutputFilename());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source instanceof ChangeButton) {
            String name = ((ChangeButton) source).getName();
            ContactType type = ContactType.findByName(name);
            if (null != type) {
                System.out.println("Action performed for " + type);
                view.editInputFilename(type);
            } else if (PhonebookConstants.getOutputFileChangeButtonName().equals(name)) {
                view.editOutputFilename();
            }
        } else if (source instanceof JButton) {
            String name = ((JButton)source).getName();
            if (PhonebookConstants.getGenerateButtonName().equals(name)) {
                generatePhonebook();
            }
        }
    }

    private void generatePhonebook() {
        if (validateChosenFiles()) {
            try {
                //Properties properties = getProperties(model.getPhonebookPropertyFilename());
                updatePropertiesFromModel(properties);
                persistProperties(properties);
                PhonebookBuilder phonebookBuilder = new PhonebookBuilder(model);
                phonebookBuilder.build();
            } catch (ContactsFileNotGeneratedException cfnge) {
                UIManager.put("OptionPane.okButtonText", "Ok");
                JOptionPane.showMessageDialog(null,
                        "Er is geen nieuw contactenbestand aangemaakt:" + cfnge.getMessage(),
                        "Let op:",
                        JOptionPane.WARNING_MESSAGE);
            }
        } else {
            UIManager.put("OptionPane.okButtonText", "Ok");
            JOptionPane.showMessageDialog(null,
                    "Er is geen nieuw contactenbestand aangemaakt.",
                    "Let op:",
                    JOptionPane.WARNING_MESSAGE);

        }
    }

    public boolean validateChosenFiles() {
        boolean allFilesOk = true;
        for (ContactType type : ContactType.values()) {
            File inputFile = new File(model.getInputFilename(type));
            if ((!inputFile.exists() || !inputFile.isFile()) && allFilesOk) {
                allFilesOk = false;
                UIManager.put("OptionPane.okButtonText", "Ok");
                JOptionPane.showMessageDialog(null,
                        "Het gekozen invoerbestand voor " + type.getDescription() + " bestaat niet.\nKies een ander bestand.",
                        "Fout",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        if (allFilesOk) {
            File outputFile = new File(model.getOutputContactsFilename());
            if (outputFile.exists() && outputFile.isFile()) {
                UIManager.put("OptionPane.cancelButtonText", "Nee");
                UIManager.put("OptionPane.okButtonText", "Ja");
                int result = JOptionPane.showConfirmDialog(null,
                        "Het gekozen uitvoerbestand bestaat reeds.\nWilt u dit bestand overschrijven?\"",
                        "Let op",
                        JOptionPane.OK_CANCEL_OPTION);

                allFilesOk = (result == JOptionPane.OK_OPTION);
            }
        }
        return allFilesOk;
    }

    private void updatePropertiesFromModel(Properties properties) {

        EnumSet<ContactType> allContactTypes = EnumSet.allOf( ContactType.class);
        for (ContactType contactType: allContactTypes) {
            properties.setProperty(contactType.getInputFilenamePropertyName(), model.getInputFilename(contactType));
        }
        properties.setProperty(PhonebookConstants.getOutputFilenamePropertyName(), model.getOutputContactsFilename());
    }

    public Object getProperty(String propertyKey) {
        return properties.getProperty(propertyKey);
    }

    private void persistProperties(Properties properties) throws ContactsFileNotGeneratedException{
        try {
            FileWriter propertyWriter = new FileWriter(PhonebookApp.getPhonebookBuilderPropertiesFilename());
            properties.store(propertyWriter, null);
        } catch (IOException e) {
            String message = "Fout tijdens het updaten van de property file:" + e.getMessage();
            System.out.println(message);
            throw new ContactsFileNotGeneratedException(message);
        }
    }
}
