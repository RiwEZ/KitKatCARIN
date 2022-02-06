import de.gurkenlabs.litiengine.Game;

public class Program {
    public static void main(String[] args) {
        Game.info().setName("CARIN");
        Game.info().setSubTitle("Made with LITIENGINE!");
        Game.info().setVersion("v0.0.1");
        Game.info().setWebsite("github");

        Game.init(args);
        Game.screens().add(new TestScreen());

        Game.start();

    }
}
