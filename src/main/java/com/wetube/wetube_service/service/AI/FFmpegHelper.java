package com.wetube.wetube_service.service.AI;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class FFmpegHelper {

    public static File applyInstructions(File inputVideo, String instructions) throws IOException, InterruptedException {
//        File outputVideo = new File(inputVideo.getParent(), "processed-" + inputVideo.getName());
        File outputVideo = new File(
                System.getProperty("java.io.tmpdir"),
                "processed-" + System.currentTimeMillis() + ".mp4"
        );

        String inputPath = inputVideo.getAbsolutePath().replace("\\", "/");
        String outputPath = outputVideo.getAbsolutePath().replace("\\", "/");

        String safeText = instructions
                .replace(":", "\\:")
                .replace("'", "\\\\'")
                .replace("\"", "\\\"");

        String[] command = {
                "ffmpeg",
                "-y",
                "-i", inputPath,
                "-t", "5",
                "-vf", String.format("drawtext=text='%s':x=10:y=10:fontsize=24:fontcolor=white:fontfile=/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf", safeText),
                "-c:a", "copy",
                outputPath
        };

        System.out.println("[FFmpeg CMD] " + Arrays.toString(command));

        ProcessBuilder builder = new ProcessBuilder(command);
        builder.redirectErrorStream(true); // gộp stderr vào stdout
        Process process = builder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[FFmpeg LOG] " + line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("FFmpeg failed with exit code " + exitCode);
        }

        if (!outputVideo.exists() || outputVideo.length() == 0) {
            throw new RuntimeException("Processed file not created: " + outputVideo.getAbsolutePath());
        }

        System.out.printf("[FFmpeg DONE] Input size=%d bytes, Output size=%d bytes%n",
                inputVideo.length(), outputVideo.length());

        return outputVideo;
    }
}

