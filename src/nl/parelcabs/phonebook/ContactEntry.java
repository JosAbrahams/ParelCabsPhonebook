package nl.parelcabs.phonebook;

public class ContactEntry {

    private StringBuffer temp;
    private String firstName;
    private final String lastName;
    private final String displayName;
    private String mobileNumber;
    private String homeNumber;
    private String emergencyNumber;

    private final String[] parts;
    private final Enum<ContactType> contactType;

    ContactEntry(Enum<ContactType> contactType, String content){
        this.contactType = contactType;
        parts = content.split(";");
        firstName = parts[1];
        firstName = firstName.replaceAll("\\?","");
        String lastNameRaw = parts[4];
        String lastNamePrefix = parts[3];
        String initials = parts[2];
        initials = initials.replaceAll("\\?","");
        if (firstName.isEmpty() && !initials.isEmpty()){
            firstName = initials;
        }
        if ("".equals(lastNamePrefix)) {
            lastName = lastNameRaw;
        } else {
            lastName = lastNamePrefix + " " + lastNameRaw;
        }
        displayName = (firstName + " " + lastName).replaceAll("^ *", "");
        if (parts.length > 14) {
            mobileNumber = parts[14];
            mobileNumber = mobileNumber.replaceAll(" ", "");
        } else {
            mobileNumber = "";
        }
        if (parts.length > 13) {
            homeNumber = parts[13];
            homeNumber = homeNumber.replaceAll(" ", "");
        } else {
            homeNumber = "";
        }
        if (parts.length >15) {
            emergencyNumber = parts[15];
            emergencyNumber = emergencyNumber.replaceAll(" ", "");
        } else {
            emergencyNumber = "";
        }

    }

    @Override
    public String toString(){
        String rawName = parts[0] + ";" + parts[1] + ";" + parts[2] + ";" + parts[3] + ";" + parts[4];
        int len = rawName.length();

        String fullPad = "                                                                                     ";
        String pad = fullPad.substring(0,100 - len);
        return rawName + pad  + contactType + " " + firstName + " " + lastName + " home=" + homeNumber + " mobile=" + mobileNumber + " noodnr=" + emergencyNumber;
    }

    String format(){
        return "\t\t<contact display_name=\"" + displayName + "\" office_number=\"" + homeNumber + "\" mobile_number=\"" + mobileNumber + "\" other_number=\"" + emergencyNumber + "\" group_id_name=\"" + contactType + "\" default_photo=\"\" auto_divert=\"\" />";
    }
}
