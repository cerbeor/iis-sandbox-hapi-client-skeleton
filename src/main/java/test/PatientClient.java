package test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;


public class PatientClient {
    private static final FhirContext ctx = CustomClientBuilder.getCTX();
    private static final String TENANT_B = "TENANT-D";

    private PatientClient(){}

    public static String read( String resourceId){
        return read(resourceId, TENANT_B, TENANT_B, TENANT_B);
    }
  
    public static String read( String resourceId, String tenantId, String username, String password) {
        IGenericClient client = new CustomClientBuilder(tenantId, username, password).getClient();
        Patient patient;
        String response;
        try {
            patient = client.read().resource(Patient.class).withId(resourceId).execute();
            response = ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
          } catch (ResourceNotFoundException e) {
            response = "Resource not found";
            e.printStackTrace();
          }
        return response;
    }
  
    public static String write(Patient patient){
        return write(patient, TENANT_B, TENANT_B, TENANT_B);
    }
  
    public static String write(Patient patient,  String tenantId, String username, String password) {
        IGenericClient client = new CustomClientBuilder(tenantId, username, password).getClient();
        MethodOutcome outcome;
        String response;
        try {
           // Create the resource on the server
           outcome = client.create().resource(patient).execute();
           // Log the ID that the server assigned
           IIdType id = outcome.getId();
           response = "Created patient, got ID: " + id;
        } catch (DataFormatException e) {
           response = "ERROR Writing Patient";
           e.printStackTrace();
        }
        return response;
    }

    public static String delete( String resourceId){
        return delete(resourceId, TENANT_B, TENANT_B, TENANT_B);
    }
  
    public static String delete( String resourceId, String tenantId, String username, String password) {
        IGenericClient client = new CustomClientBuilder(tenantId, username, password).getClient();
        String response;
        MethodOutcome outcome = client.delete().resourceById(new IdType("Patient", resourceId)).execute();

        OperationOutcome opeOutcome = (OperationOutcome) outcome.getOperationOutcome();
        if (opeOutcome != null) {
            response = opeOutcome.getIssueFirstRep().getDetails().getCodingFirstRep().getCode();
        }
        else{
            response = "Resource not found";
        }
        return response;
    }
    
}
