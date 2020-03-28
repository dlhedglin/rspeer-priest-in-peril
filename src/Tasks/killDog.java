package Tasks;

import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.api.Varps;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.tab.Prayer;
import org.rspeer.runetek.api.component.tab.Prayers;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.task.Task;



public class killDog extends Task {
    private static Area trapDoorSpot = Area.rectangular(3404, 3507, 3407, 3501);
    @Override
    public boolean validate() {
        return Varps.get(302) == 2;
    }

    @Override
    public int execute() {
        if(Dialog.isOpen())
        {
            if(Dialog.canContinue())
            {
                Dialog.processContinue();
                return 500;
            }
            Dialog.process("Yes");
            return 500;

        }
        Npc dog = Npcs.getNearest("Temple guardian");
        if(dog == null)
        {
            if(trapDoorSpot.contains(Players.getLocal()))
            {
                SceneObjects.getNearest("Trapdoor").interact(a-> a.contains("Open") | a.contains("Climb"));
                return 500;
            }
            Movement.walkToRandomized(trapDoorSpot.getCenter());
            Time.sleepUntil(()-> trapDoorSpot.contains(Players.getLocal()) || !Players.getLocal().isMoving(), 1000);
            return 500;
        }
        if(!Players.getLocal().isHealthBarVisible())
        {
            dog.interact("Attack");
            Time.sleepUntil(()-> Players.getLocal().isHealthBarVisible(), 3000);
            return 500;
        }
        if(Prayers.getActive().length == 0)
        {
            Prayers.toggle(true, Prayer.PROTECT_FROM_MELEE);
            return 100;
        }

        return 500;
    }
}
