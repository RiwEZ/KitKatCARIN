package carin.entities;

import carin.GameStates;
import de.gurkenlabs.litiengine.entities.Creature;

import java.util.ArrayList;

public class Controller {

    public static final int minX = GameStates.minX, maxX = GameStates.maxX;
    public static final int maxY = GameStates.maxY, minY = GameStates.minY;

    private Creature entity;

    public Controller(Creature entity){
        if(entity.getClass().equals(Virus.class)){
            this.entity = (Virus) entity;
        }
        else if(entity.getClass().equals(Antibody.class)){
            this.entity = (Antibody) entity;
        }
    }

    public void move(double x, double y){
        ArrayList<Virus> checkPosVirus = GameStates.getListVirus();
        ArrayList<Antibody> checkPosAnti = GameStates.getListAnti();
        boolean allPassed = true;
        if((this.entity.getX() + x*36 >= minX) && (this.entity.getX() + x*36 <= maxX) && (this.entity.getY() - y*36 <= minY) && (this.entity.getY() - y*36 >= maxY)){
            for (Antibody antibody : checkPosAnti) {
                boolean checkEnemy = this.entity.getX() + x * 36 == antibody.getX() && this.entity.getY() - y * 36 == antibody.getY();
                if (checkEnemy) {
                    allPassed = false;
                    break;
                }
            }
            for (Virus virus : checkPosVirus){
                if(allPassed){
                    boolean checkFriend = this.entity.getX() + x*36 == virus.getX() && this.entity.getY() - y*36 == virus.getY();
                    if(checkFriend){
                        allPassed = false;
                        break;
                    }
                }
                else break;
            }
            if(allPassed){
                this.entity.setLocation(this.entity.getX() + x*36, this.entity.getY() - y*36);
            }
        }
    }
}
