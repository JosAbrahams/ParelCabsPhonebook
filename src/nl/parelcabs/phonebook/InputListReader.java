package nl.parelcabs.phonebook;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class InputListReader {

    String inputListFilename;
    Enum<ContactType> contactType;
    public InputListReader(Enum<ContactType> contactType , String inputListFilename) {
        this.inputListFilename = inputListFilename;
        this.contactType = contactType;
    }

    List<ContactEntry> readInputfile() {
        List<ContactEntry> contactEntries = new ArrayList<>();
        String contactEntryString;
        boolean firstLineRead = false;
        Charset charset = Charset.forName("windows-1252");
        try (FileReader fr = new FileReader(inputListFilename , charset);
             BufferedReader br = new BufferedReader(fr)) {
            while((contactEntryString=br.readLine())!=null) {
                if (firstLineRead) {
                    contactEntries.add(new ContactEntry(contactType, contactEntryString));
                }
                firstLineRead = true;
            }
        } catch (IOException ioe) {
            System.err.println("Error while reading input file '" + inputListFilename + "' :" + ioe.getMessage());
        }
       return contactEntries;
    }

}
