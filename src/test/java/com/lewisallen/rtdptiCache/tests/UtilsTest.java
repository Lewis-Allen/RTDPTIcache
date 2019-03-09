package com.lewisallen.rtdptiCache.tests;

import com.lewisallen.rtdptiCache.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UtilsTest
{

    @Test
    void removeExtension()
    {
        new Utils();

        String[] fileNames = new String[]{
                "default.html",
                "onetrain.html",
                "fourbuses.html",
                "hello",
                "cheese.exe",
                "alpha.bat"
        };

        String[] fileNamesNoExtension = new String[]{
                "default",
                "onetrain",
                "fourbuses",
                "hello",
                "cheese",
                "alpha"
        };

        for(int i = 0; i < fileNames.length; i++)
        {
            Assertions.assertEquals(Utils.removeExtension(fileNames[i]), fileNamesNoExtension[i]);
        }
    }
}