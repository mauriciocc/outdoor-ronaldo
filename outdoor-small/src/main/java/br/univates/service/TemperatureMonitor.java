package br.univates.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class TemperatureMonitor {


    public static final String AIN1 = "AIN1";
    private final Logger LOG = Logger.getLogger(getClass().getName());
    private Random random;

    public TemperatureMonitor() {
        try {
            runCommand("echo cape-bone-iio > /sys/devices/bone_capemgr.*/slots");
            random = null;
        } catch (Exception e) {
            e.printStackTrace();
            random = new Random();
        }
    }

    public int readPort(String port) {
        if (random != null) {
            return random.nextInt(40);
        }

        final Path path = Paths.get("/sys/devices/ocp.2/helper.14/" + port);
        if (Files.notExists(path)) {
            LOG.warning(() -> "LM35 Port not found: " + path);
            return 0;
        }
        try (Stream<String> lines = Files.lines(path)) {
            int temp = lines.limit(1)
                    .mapToInt(Integer::parseInt)
                    .findFirst()
                    .orElse(0);
            return temp / 10;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void runCommand(String cmd) throws IOException, InterruptedException {
        final Path tempFile = Files.createTempFile("outdoor-init-script", ".sh");
        Files.write(tempFile, cmd.getBytes());
        final String filePath = tempFile.toAbsolutePath().toString();

        // Give execution permission to file
        Process p = new ProcessBuilder("chmod", "777", filePath).start();
        p.waitFor();

        // Execute shell
        p = new ProcessBuilder("sh", filePath).start();
        p.waitFor();

        int exitValue = p.exitValue();
        Files.deleteIfExists(tempFile);
        LOG.info(() -> "Command [" + cmd + "] finished with exit value [" + exitValue + "]");
    }

}