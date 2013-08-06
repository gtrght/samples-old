package com.othelle.samples.imageresize;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * author: v.vlasov
 */
public class ResizeWorker implements Runnable {
    private int expectedWidth;
    private int expectedHeight;

    private boolean ignorePictureRotation;

    private File input;
    private File output;
    private boolean keepAspectRatio;

    @Override
    public void run() {
        try {
            BufferedImage img = ImageIO.read(input); // load image


            int originalWidth = img.getWidth();
            int originalHeight = img.getHeight();

            if (!ignorePictureRotation && originalWidth < originalHeight)
                swapDimensions();

            BufferedImage scaledImg = Scalr.resize(img, Scalr.Method.QUALITY,
                    keepAspectRatio ? Scalr.Mode.AUTOMATIC : Scalr.Mode.FIT_EXACT, expectedWidth, expectedHeight, Scalr.OP_ANTIALIAS);

            ImageIO.write(scaledImg, extractFormat(input), output);

            System.out.println("Successfully re-sized the image: " + this.input);
        } catch (Exception e) {
            System.out.println("Unable to re-size: " + this.input);
        } catch (OutOfMemoryError error) {
            System.out.println("Unable to finish operation, try to restart the app with Xmx1024m option:\n\t" +
                    "java -Xmx1024m -jar image-resize-app.jar ...");
        }
    }

    private String extractFormat(File file) {
        String fileName = file.getName();
        int beginIndex = fileName.lastIndexOf('.') + 1;
        if (beginIndex == 0) return "jpg"; //default format

        String extension = fileName.substring(beginIndex);
        return extension.toLowerCase();
    }

    protected void swapDimensions() {
        //swapping without 3rd variable, no good reason for that, just for fun :)
        expectedWidth ^= expectedHeight;
        expectedHeight ^= expectedWidth;
        expectedWidth ^= expectedHeight;
    }


    public int getExpectedHeight() {
        return expectedHeight;
    }

    public void setExpectedHeight(int expectedHeight) {
        this.expectedHeight = expectedHeight;
    }

    public int getExpectedWidth() {
        return expectedWidth;
    }

    public void setExpectedWidth(int expectedWidth) {
        this.expectedWidth = expectedWidth;
    }

    public File getInput() {
        return input;
    }

    public void setInput(File input) {
        this.input = input;
    }

    public boolean isIgnorePictureRotation() {
        return ignorePictureRotation;
    }

    public void setIgnorePictureRotation(boolean ignorePictureRotation) {
        this.ignorePictureRotation = ignorePictureRotation;
    }

    public File getOutput() {
        return output;
    }

    public void setOutput(File output) {
        this.output = output;
    }

    public void setKeepAspectRatio(boolean keepAspectRatio) {
        this.keepAspectRatio = keepAspectRatio;
    }
}
