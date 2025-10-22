package nl.parelcabs.phonebook;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class OutputfileChooserPanel extends FileChooserPanel {

    public OutputfileChooserPanel(){
        super();
        setFileTypeLabelText(PhonebookConstants.getOutputFileCaption());
        String defaultFilename = getDefaultFilename();
        setFilename(defaultFilename);
        setChangeButtonName();
        setFileNameExtensionFilter(new FileNameExtensionFilter("xml", "xml"));
    }

    @Override
    public String getDefaultFilename() {
        PhonebookDefaults defaults = new PhonebookDefaults(PhonebookApp.getPhonebookBuilderPropertiesFilename());
        String defaultOutputFilename = defaults.getDefaultOutputFilename();
        String defaultOutputDirectoryName = defaultOutputFilename.replaceFirst("[^\\\\]*$", "");
        LocalDateTime ldt = LocalDateTime.now();
        String formattedDateStr = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.getDefault()).format(ldt);
        String newOutputFilename = "contacts" + formattedDateStr + ".xml";
        return defaultOutputDirectoryName + newOutputFilename;
     }

    @Override
    public String getFileChooserTitle() {
        return "Selecteer het uitvoerbestand voor de contacten";
    }

    @Override
    public void setChangeButtonName() {
        changeFilenameButton.setName(PhonebookConstants.getOutputFileChangeButtonName());
    }

    @Override
    public boolean validateChosenFile(File chosenFile) {
        if (chosenFile.exists() && chosenFile.isFile()) {
            UIManager.put("OptionPane.cancelButtonText", "Nee");
            UIManager.put("OptionPane.okButtonText", "Ja");
            int result = JOptionPane.showConfirmDialog(this,
                    "Het gekozen uitvoerbestand bestaat reeds.\nWilt u dit bestand overschrijven?\"",
                    "Let op",
                    JOptionPane.OK_CANCEL_OPTION);

            return (result == JOptionPane.OK_OPTION);
        } else {
            return true;
        }
    }
}
