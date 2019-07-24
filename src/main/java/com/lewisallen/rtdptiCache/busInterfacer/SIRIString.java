package com.lewisallen.rtdptiCache.busInterfacer;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.Text;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class SIRIString {

    // Max stops to receive in response from SIRI.
    private static final int MAX_STOP_VISITS = 9;
    // Xml string that will be sent to SIRI
    private String xmlString;

    /**
     * Builds an XML String to send as a POST request to the SIRI Service.
     *
     * @param naptans list of NaPTAN codes to build the request for.
     * @return XML String of request
     */
    public String generateXml(String[] naptans) {
        Document doc = new Document();

        Namespace ns = Namespace.getNamespace("http://www.siri.org.uk/siri");
        Namespace ns2 = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");

        Element siri = new Element("Siri", ns);
        siri.setAttribute("schemaLocation", "http://www.siri.org.uk/schema/1.3/siri.xsd", ns2);
        siri.addNamespaceDeclaration(ns2);
        siri.setAttribute("version", "1.3");

        doc.setRootElement(siri);

        Element serviceRequest = new Element("ServiceRequest", ns);
        siri.addContent(serviceRequest);

        for (String s : naptans) {
            serviceRequest.addContent(individualStopXml(s, ns));
        }

        XMLOutputter out = new XMLOutputter(Format.getCompactFormat().setOmitDeclaration(true));

        this.xmlString = out.outputString(doc);
        return out.outputString(doc);
    }

    /**
     * Builds an XML element for an individual stop.
     *
     * @param naptan NaPTAN code of the individual stop
     * @param ns     Namespace
     * @return Element for individual stop
     */
    private Element individualStopXml(String naptan, Namespace ns) {
        // Create root "StopMonitoringRequest" element.
        Element stopMonitoringRequest = new Element("StopMonitoringRequest", ns);
        stopMonitoringRequest.setAttribute("version", "1.3");

        // Create preview interval child.
        Element previewInterval = new Element("PreviewInterval", ns);
        previewInterval.setContent(new Text("PT60M"));

        stopMonitoringRequest.addContent(previewInterval);

        // Create monitoring ref child.
        Element monitoringRef = new Element("MonitoringRef", ns);
        monitoringRef.addContent(new Text(naptan));

        stopMonitoringRequest.addContent(monitoringRef);

        // Add Max stop visits
        Element maxStopVisits = new Element("MaximumStopVisits", ns);
        maxStopVisits.addContent(new Text(Integer.toString(MAX_STOP_VISITS)));

        stopMonitoringRequest.addContent(maxStopVisits);

        return stopMonitoringRequest;
    }

    /**
     * Getter for the XML String
     *
     * @return XML String
     */
    public String getXml() {
        return xmlString;
    }
}
