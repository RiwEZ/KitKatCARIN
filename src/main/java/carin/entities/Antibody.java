package carin.entities;

import de.gurkenlabs.litiengine.entities.*;

@EntityInfo(width = 32, height = 32)
@AnimationInfo(spritePrefix = "antibody")
public class Antibody extends Creature implements GeneticEntity {
    private int HP;
    public Antibody() {
        this.HP = 100;
    }
}
