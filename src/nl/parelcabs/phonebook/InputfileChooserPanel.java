package nl.parelcabs.phonebook;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class InputfileChooserPanel extends FileChooserPanel {

    private final ContactType contactType;

    InputfileChooserPanel(ContactType contactType) {
        super();
        this.contactType = contactType;
        setChangeButtonName();
        setFileTypeLabelText(PhonebookConstants.getFileTypeCaption(contactType));
        setFilename(getDefaultFilename());
        setFileNameExtensionFilter(new FileNameExtensionFilter("csv", "csv"));
    }


    @Override
    public void setChangeButtonName() {
        changeFilenameButton.setName(contactType.name());
    }

    @Override
    public boolean validateChosenFile(File chosenFile) {
        if (chosenFile.exists() && chosenFile.isFile()) {
            return true;
        } else {
            JOptionPane.showMessageDialog(this,"Het gekozen bestand bestaat niet.\nKies een bestaand bestand.","Foutmelding", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public String getDefaultFilename(){
        PhonebookDefaults defaults = new PhonebookDefaults(PhonebookApp.getPhonebookBuilderPropertiesFilename());
        return defaults.getDefaultFilename(contactType);
    }

    @Override
    public String getFileChooserTitle() {
        return "Selecteer het invoerbestand voor " + contactType.getDescription();
    }
}
