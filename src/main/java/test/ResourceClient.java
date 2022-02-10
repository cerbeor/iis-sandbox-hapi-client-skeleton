package test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.OperationOutcome;




public abstract class ResourceClient {
    private static final FhirContext CTX = CustomClientBuilder.getCTX();
    private static final String TENANT_B = "default";
    private static final String FLORENCE = "https://florence.immregistries.org/iis-sandbox/fhir";

    private ResourceClient(){}

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private static String add_timestamp(String message){
        LocalDateTime now = LocalDateTime.now();  
        return dtf.format(now) + " " + message;
    }

    public static String read(String resourceType, String resourceId){
        return read(resourceType, resourceId, FLORENCE, TENANT_B, TENANT_B, TENANT_B);
    }

    public static String write(IBaseResource resource){
        return write(resource, FLORENCE, TENANT_B, TENANT_B, TENANT_B);
    }

    public static String delete(String resourceType, String resourceId){
        return delete(resourceType, resourceId, FLORENCE, TENANT_B, TENANT_B, TENANT_B);
    }
    public static String update(IResource resource){
        return update( resource, FLORENCE, TENANT_B, TENANT_B, TENANT_B);
    }
  
  
    public static String read(String resourceType, String resourceId, String iisUrl, String tenantId, String username, String password) {
        IGenericClient client = new CustomClientBuilder(iisUrl, tenantId, username, password).getClient();
        IBaseResource resource;
        String response;
        try {
            resource = client.read().resource(resourceType).withId(resourceId).execute();
            response = CTX.newXmlParser().setPrettyPrint(true).encodeResourceToString(resource);
          } catch (ResourceNotFoundException e) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
            LocalDateTime now = LocalDateTime.now();  
            response = dtf.format(now) + "Resource not found";
            e.printStackTrace();
          }
        return response;
    }
  
  
    public static String write(IBaseResource resource, String iisUrl, String tenantId, String username, String password) {
        IGenericClient client = new CustomClientBuilder(iisUrl, tenantId, username, password).getClient();
        MethodOutcome outcome;
        String response;
        try {
           // Create the resource on the server
           outcome = client.create().resource(resource).execute();
           // Log the ID that the server assigned
           IIdType id = outcome.getId();
           if (id != null){  
                response = "Created resource, got ID: " + id.getIdPart();
           }else {
               response = "Response includes no id";
           }
        } catch (DataFormatException e) {
           response = "ERROR Writing FHIR Resource";
           e.printStackTrace();
        }
        return add_timestamp(response);
    }

  
    public static String delete(String resourceType, String iisUrl, String resourceId, String tenantId, String username, String password) {
        IGenericClient client = new CustomClientBuilder(iisUrl, tenantId, username, password).getClient();
        String response;
        MethodOutcome outcome = client.delete().resourceById(new IdType(resourceType, resourceId)).execute();

        OperationOutcome opeOutcome = (OperationOutcome) outcome.getOperationOutcome();
        if (opeOutcome != null) {
            response = opeOutcome.getIssueFirstRep().getDetails().getCodingFirstRep().getCode();
        }
        else{
            response = "Resource not found";
        }
        return add_timestamp(response);
    }

    public static String update(IBaseResource resource, String iisUrl, String tenantId, String username, String password) {
        IGenericClient client = new CustomClientBuilder(iisUrl, tenantId, username, password).getClient();
        String response;
        MethodOutcome outcome = client.update().resource(resource).execute();

        IIdType id = outcome.getId();

        if (id != null){
            response = "Updated resource, got ID: " + id.getIdPart();
        }
        // OperationOutcome opeOutcome = (OperationOutcome) outcome.getOperationOutcome();

        // if (opeOutcome != null) {
        //     response = opeOutcome.getIssueFirstRep().getDetails().getCodingFirstRep().getCode();
        // }
        else{
            response = "Resource not found";
        }
        return add_timestamp(response);
    }
    
}
