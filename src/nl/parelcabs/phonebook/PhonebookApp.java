package nl.parelcabs.phonebook;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class PhonebookApp {

   public static String phonebookBuilderPropertiesFilename;
   public static PhonebookContext context;
   public static void main(String[] args) {
      if (args.length == 0 ) {
         throw new IllegalArgumentException("Geen filenaam meegegeven voor properties file");
      }
      phonebookBuilderPropertiesFilename = args[0];
      javax.swing.SwingUtilities.invokeLater(PhonebookApp::createAndShowGUI);
   }

   public static String getPhonebookBuilderPropertiesFilename(){
      return phonebookBuilderPropertiesFilename;
   }

   /**
    * Create the GUI and show it.  For thread safety,
    * this method should be invoked from the
    * event-dispatching thread.
    */
   private static void createAndShowGUI() {
      context = new PhonebookContext();
      context.put("properties", getProperties());
      PhonebookController controller = new PhonebookController(context);
      controller.setPhonebookPropertiesFilename(phonebookBuilderPropertiesFilename);
      controller.setDefaultValuesOnModel();
      controller.start();
   }

   private static Properties getProperties() {
      Properties properties = null;
      String propertyFilename = getPhonebookBuilderPropertiesFilename();
      try (FileReader propertyFileReader = new FileReader(propertyFilename)) {
         properties = new Properties();
         properties.load(propertyFileReader);
      } catch (FileNotFoundException e) {
         System.err.println("Cannot find property file " + propertyFilename);
      } catch (IOException e) {
         System.err.println("Error when loading properties from file " + propertyFilename + e.getMessage());
      }
      return properties;
   }
}