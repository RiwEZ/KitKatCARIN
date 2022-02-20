package carin.entities;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class GeneticEntityFactory {

    public static ArrayList<Virus> getAvailableVirus() {
        try {
            DirectoryStream<Path> stream =
                    Files.newDirectoryStream(Path.of("genetic_codes/virus"), entry -> entry.toString().contains(".in"));

            ArrayList<Virus> res = new ArrayList<>();
            stream.forEach(p -> {
                String name = p.getFileName().toString();
                name = name.substring(0, name.lastIndexOf('.'));
                res.add(new Virus(name, p));
            });
            return res;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static ArrayList<Antibody> getAvailableAntibody() {
        try {
            DirectoryStream<Path> stream =
                    Files.newDirectoryStream(Path.of("genetic_codes/antibody"), entry -> entry.toString().contains(".in"));

            ArrayList<Antibody> res = new ArrayList<>();
            stream.forEach(p -> {
                String name = p.getFileName().toString();
                name = name.substring(0, name.lastIndexOf('.'));
                res.add(new Antibody(name, p));
            });
            return res;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}
