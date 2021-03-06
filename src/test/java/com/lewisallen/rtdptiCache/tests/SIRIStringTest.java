package com.lewisallen.rtdptiCache.tests;

import com.lewisallen.rtdptiCache.busInterfacer.SIRIString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SIRIStringTest {

    @Test
    void testXmlBuilder() {
        String[] naptans = new String[]{"123456789", "987654321"};
        SIRIString xmlString = new SIRIString();

        xmlString.generateXml(naptans);

        for (String s : naptans) {
            Assertions.assertTrue(xmlString.getXml().contains(s));
        }
    }
}
