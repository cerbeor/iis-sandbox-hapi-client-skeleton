package test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.Patient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
         patient = ctx.newXmlParser().parseResource(Patient.class,  Files.readString( Path.of("src/main/resources/samples/Patient/" + fileNamePatient)));
         immunization = ctx.newXmlParser().parseResource(Immunization.class,  Files.readString( Path.of("src/main/resources/samples/Immunization/" + fileNameImmunization)));
         
         ResourceClient.write(patient);

         response = ResourceClient.read("Patient", patient.getId());
         // System.err.println(response);

         ResourceClient.write(immunization);

         response = ResourceClient.read("Immunization", immunization.getId());
         // System.err.println(response);

         ResourceClient.delete("Immunization", patient.getId());

         ResourceClient.delete("Patient", patient.getId());
         
         // response = ResourceClient.read("Patient", patient.getId());
         // System.err.println(response);

      } catch (DataFormatException | IOException e) {
         System.err.println("unreadable file");
         e.printStackTrace();
      }
      

   }

}
