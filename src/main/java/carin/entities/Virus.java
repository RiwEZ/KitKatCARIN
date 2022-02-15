package carin.entities;

import de.gurkenlabs.litiengine.entities.*;

@EntityInfo(width = 32, height = 32)
@AnimationInfo(spritePrefix = "virus")
public class Virus extends Creature implements GeneticEntity {
    private int HP;
    public Virus() {
        this.HP = 100;
    }
}
