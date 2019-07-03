package com.lewisallen.rtdptiCache;

public class Utils
{
    /**
     * Removes a file extension from a file.
     *
     * @param fileName Name of file.
     * @return Filename without extension.
     */
    public static String removeExtension(String fileName)
    {
        int pos = fileName.lastIndexOf(".");
        return pos > 0 ? fileName.substring(0, pos) : fileName;
    }
}
