package com.othelle.samples.imageresize;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * author: v.vlasov
 */
public class ResizeService implements Runnable {
    private File source;
    private File destination;
    private int width;
    private int height;
    private boolean recursive;
    private boolean keepAspectRatio;
    private boolean ignorePictureRotations;

    public ResizeService(File source, File destination, int width, int height) {
        this.source = source;
        this.destination = destination;
        this.width = width;
        this.height = height;
    }

    @Override
    public void run() {
        ExecutorService service = Executors.newFixedThreadPool(10);
        traverse(source, destination, service);
        try {
            service.shutdown();
            service.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void traverse(File source, File destination, ExecutorService service) {

        File[] files = source.listFiles();

        if (!destination.exists() && !destination.mkdirs()) {
            System.err.println("Unable to create directory: " + destination.getAbsolutePath());
            System.exit(1);
        }

        for (File file : files) {
            if (!file.isDirectory()) {
                ResizeWorker worker = new ResizeWorker();
                worker.setInput(file);
                worker.setOutput(new File(destination, file.getName()));
                worker.setExpectedWidth(width);
                worker.setExpectedHeight(height);
                worker.setIgnorePictureRotation(ignorePictureRotations);
                worker.setKeepAspectRatio(keepAspectRatio);
                service.execute(worker);
            }
        }

        if (recursive) {
            for (File file : files) {
                if (file.isDirectory()){
                    System.out.println("Traversing subfolder: " + file.getAbsolutePath().replace(this.source.getAbsolutePath(), ""));
                    traverse(new File(source, file.getName()), new File(destination, file.getName()), service);
                }
            }
        }


    }

    public void setRecursive(boolean recursive) {
        this.recursive = recursive;
    }

    public void setKeepAspectRatio(boolean keepAspectRatio) {
        this.keepAspectRatio = keepAspectRatio;
    }

    public void setIgnorePictureRotations(boolean ignorePictureRotations) {
        this.ignorePictureRotations = ignorePictureRotations;
    }
}
