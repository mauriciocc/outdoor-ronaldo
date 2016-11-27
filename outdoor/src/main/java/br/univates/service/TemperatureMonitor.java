package br.univates.service;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Component
public class TemperatureMonitor {


    public static final String AIN1 = "AIN1";

    public TemperatureMonitor() throws IOException, InterruptedException {
        runCommand("echo cape-bone-iio > /sys/devices/bone_capemgr.*/slots");
    }

    public int readPort(String port) {
        Path path = Paths.get("/sys/devices/ocp.2/helper.14/" + port);
        try (Stream<String> lines = Files.lines(path)) {
            return lines.limit(1)
                    .mapToInt(Integer::parseInt)
                    .findFirst()
                    .orElse(0);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static void runCommand(String cmd) throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec(cmd);
        p.waitFor();
    }
}
