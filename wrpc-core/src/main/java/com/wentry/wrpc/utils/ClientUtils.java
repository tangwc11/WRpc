package com.wentry.wrpc.utils;

import com.wentry.wrpc.registry.Directory;

/**
 * @Description:
 * @Author: tangwc
 */
public class ClientUtils {

    private static Directory innerDirectory;

    public static void setDirectory(Directory directory) {
        innerDirectory = directory;
    }

    public static Directory getDict(){
        return innerDirectory;
    }

}
