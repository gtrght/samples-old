package com.othelle.samples.imageresize;

import org.junit.Test;

import java.io.File;

/**
 * author: v.vlasov
 */
public class ResizeServiceManualTest {

    @Test
    public void testResizeService(){
        new ResizeService(new File("C:\\temp\\photos"), new File("C:\\temp\\resized_photos"), 800, 600).run();
    }

}
