package com.othelle.samples.imageresize;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * author: v.vlasov
 */
public class App {

    public static void main(String[] args) {
        Set<String> options = new HashSet<String>();
        if (args.length < 3) {
            printUsages();
            System.exit(0);
        } else {
            for (int i = 0; i < args.length - 3; i++) {
                options.add(args[i].trim().toLowerCase());
            }
        }

        String[] dimensions = args[args.length - 3].split(",");
        String source = args[args.length - 2];
        String destination = args[args.length - 1];

        ResizeService resizeService = new ResizeService(new File(source), new File(destination), Integer.parseInt(dimensions[0]), Integer.parseInt(dimensions[1]));
        resizeService.setRecursive(options.contains("-r"));
        resizeService.setKeepAspectRatio(!options.contains("-iar"));
        resizeService.setIgnorePictureRotations(options.contains("-i"));

        resizeService.run();

    }

    private static void printUsages() {
        System.err.println("USAGE: ");
        System.err.println("\tjava -Xmx512m -jar image-resize-app.jar [options] WIDTH,HEIGHT SOURCE DEST");
        System.err.println("");
        System.err.println("OPTIONS: ");
        System.err.println("\t-r\tRecursively traverse through the directories.");
        System.err.println("\t-iar\tIgnore aspect ratio.");

        System.err.println("");
        System.err.println("Recommended WIDTH,HEIGHT parameters: ");
        System.err.println("\t- Small [800x600] ");
        System.err.println("\t- Medium [1366x1024] ");
        System.err.println("\t- Large [1920x1440] ");
        System.err.println("\t- Mobile [320x480] ");


        System.err.println("");
        System.err.println("Example:");
        System.err.println("\tjava -Xmx512m -jar image-resize-app.jar -r 1920,1440 /home/user/photos/ /home/user/resized_photos");
    }

}
