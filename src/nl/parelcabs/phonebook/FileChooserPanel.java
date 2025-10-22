package nl.parelcabs.phonebook;

import javax.swing.*;
import java.awt.event.ActionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public abstract class FileChooserPanel extends JPanel {

    private final JLabel fileTypeLabel = new JLabel();
    ChangeButton changeFilenameButton;
    private final JTextField fileNameField;
    private FileNameExtensionFilter filter;
    private final JFileChooser fileChooser = new JFileChooser();

    public FileChooserPanel() {

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        c.weightx = 1;

        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 0;
        c.gridy = 0;
        fileTypeLabel.setPreferredSize(new Dimension(170,30));
        add(fileTypeLabel, c);

        fileNameField = new JTextField();
        fileNameField.setEnabled(false);
        fileNameField.setDisabledTextColor(Color.BLACK);
        fileNameField.setPreferredSize(new Dimension(200,30));
        fileNameField.setColumns(70);
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 1;
        c.gridy = 0;
        add(fileNameField, c);

        changeFilenameButton = new ChangeButton();
        c.anchor = GridBagConstraints.LINE_END;
        c.gridx = 2;
        c.gridy = 0;
        add(changeFilenameButton, c);
    }

    public void setFileTypeLabelText(String fileTypeLabelText){
        fileTypeLabel.setText(fileTypeLabelText);
    }

    public void setFilename(String filename){
        fileNameField.setText(filename);
    }

    public void addActionListener(ActionListener listener) {
        changeFilenameButton.addActionListener(listener);
    }

    public String editFilename(String originalFilename){
        fileChooser.setFileFilter(filter);
        fileChooser.setDialogTitle(getFileChooserTitle());
        fileChooser.setAcceptAllFileFilterUsed(false);
        disableUpAndHomeFolderButtons(fileChooser);

        if (originalFilename != null) {
            fileChooser.setSelectedFile(new File(originalFilename));
        }
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File chosenFile = fileChooser.getSelectedFile();
            if (validateChosenFile(chosenFile)) {
                System.out.println("You chose to open this file: " +
                        fileChooser.getSelectedFile().getAbsolutePath());
                return fileChooser.getSelectedFile().getAbsolutePath();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }



    public void setFileNameExtensionFilter(FileNameExtensionFilter filter) {
        this.filter = filter;
    }

    public static void disableUpAndHomeFolderButtons(Container c) {
        int len = c.getComponentCount();
        for (int i = 0; i < len; i++) {
            Component comp = c.getComponent(i);
            if (comp instanceof JButton) {
                JButton b = (JButton) comp;
                Icon icon = b.getIcon();
                if (icon != null
                        && (icon == UIManager.getIcon("FileChooser.upFolderIcon") ||
                        icon == UIManager.getIcon("FileChooser.homeFolderIcon"))){
                    b.setEnabled(false);
                }
            } else if (comp instanceof Container) {
                disableUpAndHomeFolderButtons((Container) comp);
            }
        }
    }

    public abstract String getDefaultFilename();

    public abstract String getFileChooserTitle();

    public abstract void setChangeButtonName();

    public abstract boolean validateChosenFile(File chosenFile);

}
