package test;

import io.github.linuxforhealth.hl7.HL7ToFHIRConverter;

public class TestConvert {

    public static void main(String[] args) {
        String hl7message = "MSH|^~\\&||EHR Sandbox v0.1.0|||20220105035724-0500||VXU^V04^VXU_V04|16414162447777|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS" +
        "PID|1||3^^^EHR^MR||Mraz^Sandee^Sanjuanita^^^^L||20220104|F|||^^East Annamarieton^Arkansas^^MY^P||||||||||||||O" +
        "ORC|RE|0^IIS|2^loria" +
        "RXA|0|1|20220105||10^IPV^CVX|4.5|mL^milliliters^UCUM||||^^^||||4|20290919|AD^Adams Laboratories, Inc.^MVX|||CP|A" +
        "RXR|C38238^Intradermal^NCIT|LT^Left Thigh^HL70163";
        HL7ToFHIRConverter ftv = new HL7ToFHIRConverter();
        String output= ftv.convert(hl7message); // generated a FHIR output
        System.err.println(output);
    }
    
}
