package br.univates.service;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class TemperatureMonitor {

    public static void main(String[] args) throws InterruptedException, IOException {
        runCommand("echo cape-bone-iio > /sys/devices/bone_capemgr.*/slots");
        Path portVal = Paths.get("/sys/devices/platform/omap/tsc/ain1");
        System.out.println(Files.readAllLines(portVal));
    }

    private static void runCommand(String cmd) throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec(cmd);
        p.waitFor();
    }
}
