package carin.entities;

public interface GeneticEntity {

    /** move from host position (x, y)
     * @param x x position from host
     * @param y y position from host
     */
    void move(double x, double y);

    /**
     * run genetic code
     */
    void run();

}
