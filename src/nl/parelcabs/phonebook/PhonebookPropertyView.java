package nl.parelcabs.phonebook;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EnumSet;
import java.util.HashMap;

public class PhonebookPropertyView extends JFrame implements PropertyChangeListener {

    private final PhonebookPropertyModel model;

    private final HashMap<ContactType, InputfileChooserPanel> inputfileChooserPanels = new HashMap<>();
    private final OutputfileChooserPanel outputFileChooserPanel;

    public PhonebookPropertyView(PhonebookController controller)  {
        super();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(1200, 500);
        this.model = controller.getModel();
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;

        EnumSet<ContactType> allContactTypes = EnumSet.allOf( ContactType.class);

        for (ContactType contactType: allContactTypes) {
            InputfileChooserPanel inputfileChooserPanel = new InputfileChooserPanel(contactType);
            inputfileChooserPanels.put(contactType,inputfileChooserPanel);
            inputfileChooserPanel.addActionListener(controller);
            add(inputfileChooserPanel, c);
            c.gridy++;
        }
        c.insets = new Insets(20,0,20,0);
        outputFileChooserPanel = new OutputfileChooserPanel();
        outputFileChooserPanel.setFilename(model.getSuggestedNewOutputFilename());
        outputFileChooserPanel.addActionListener(controller);
        add(outputFileChooserPanel, c);

        c.gridy++;
        c.anchor = GridBagConstraints.LINE_END;
        JButton generateButton = new JButton("Genereer contactbestand");
        generateButton.setName(PhonebookConstants.getGenerateButtonName());
        generateButton.addActionListener(controller);
        add(generateButton, c);
    }

    public void editInputFilename(ContactType contactType) {
        String newFilename = getFileChooserPanel(contactType).editFilename(model.getInputFilename(contactType));
        if (newFilename != null) {
            model.setInputFilename(contactType, newFilename);
        }
    }

    public void editOutputFilename() {
        String suggestedNewFilename = model.getSuggestedNewOutputFilename();
        String newFilename = outputFileChooserPanel.editFilename(suggestedNewFilename);
        if (newFilename != null) {
            model.setOutputContactsFilename(newFilename);
        }
    }

    private FileChooserPanel getFileChooserPanel(ContactType contactType) {
        return inputfileChooserPanels.get(contactType);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if ("outputfile".equals(propertyName)) {
            outputFileChooserPanel.setFilename((String) evt.getNewValue());
        } else {
            ContactType contactType = ContactType.findByName(evt.getPropertyName());
            InputfileChooserPanel changedPanel = inputfileChooserPanels.get(contactType);
            changedPanel.setFilename((String) evt.getNewValue());
        }
    }
}
