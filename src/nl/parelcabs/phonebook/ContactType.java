package nl.parelcabs.phonebook;

public enum ContactType {
    CARS("Autos", "carListFilename"),
    PASSENGERS(" Passagiers","passengerListFilename"),
    DRIVERS(" Chauffeurs","driverListFilename"),
    PLANNERS("Planners","plannerListFilename"),
    OTHER("Overige", "otherListFilename");

    private final String description;
    private final String inputFilenamePropertyName;

    ContactType(String description, String inputFilenamePropertyName) {
        this.description = description;
        this.inputFilenamePropertyName = inputFilenamePropertyName;
    }

    public String toString(){
        return description;
    }

    public static ContactType findByName(String name) {
        ContactType result = null;
        for (ContactType contactType : values()) {
            if (contactType.name().equalsIgnoreCase(name)) {
                result = contactType;
                break;
            }
        }
        return result;
    }

    public String getDescription(){
        return description;
    }

    public String getInputFilenamePropertyName(){
        return inputFilenamePropertyName;
    }

}
