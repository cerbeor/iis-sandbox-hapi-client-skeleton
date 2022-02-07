package test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.Patient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestApplication {
   private static final FhirContext ctx = CustomClientBuilder.getCTX();

   /**
    * This is the Java main method, which gets executed
    */
   public static void main(String[] args) {
      String fileNamePatient = "examplePatient.xml";
      String fileNameImmunization = "testImmunization.xml";
      // String fileName = "patient-example-a(pat1).xml"; String id = "pat1";

      Patient patient;
      Immunization immunization;
      String response;
      try {
         patient = ctx.newXmlParser().parseResource(Patient.class,
               new String(
                  Files.readAllBytes(Paths.get("src/main/resources/samples/Patient/" + fileNamePatient))));
         immunization = ctx.newXmlParser().parseResource(Immunization.class,
               new String(
                     Files.readAllBytes(Paths.get("src/main/resources/samples/Immunization/" + fileNameImmunization))));

         String id = patient.getIdentifier().get(0).getValue();

         response = ResourceClient.write(patient);
         System.err.println(response);
         response = ResourceClient.read("Patient", id);
         System.err.println(response);

         ResourceClient.write(immunization);

         response = ResourceClient.read("Immunization", immunization.getId());
         System.err.println(response);

         ResourceClient.delete("Immunization", id);

         response = ResourceClient.delete("Patient", id);

         // response = ResourceClient.read("Patient", id);
         System.err.println(response);

      } catch (DataFormatException | IOException e) {
         System.err.println("unreadable file");
         e.printStackTrace();
      }

   }

}
