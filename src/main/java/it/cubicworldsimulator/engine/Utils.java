package it.cubicworldsimulator.engine;

import it.cubicworldsimulator.engine.Model.Mesh;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utils {

    public static String loadResource(String fileName) throws Exception {
        String result;
        try (InputStream in = Utils.class.getResourceAsStream(fileName);
             Scanner scanner = new Scanner(in, java.nio.charset.StandardCharsets.UTF_8.name())) {
            result = scanner.useDelimiter("\\A").next();
        }
        return result;
    }

    //TODO
    public static List<String> readAllLines(String fileName) {
        return new ArrayList<String>();
    }

}
