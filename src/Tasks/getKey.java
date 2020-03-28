package Tasks;

import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.Varps;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.task.Task;
import org.rspeer.ui.Log;

public class getKey extends Task {
    private static final Area dogRoom = Area.rectangular(3400, 9908, 3443, 9879);
    private static Area trapDoorSpot = Area.rectangular(3404, 3507, 3407, 3501);
    private static Area monumentSpot = Area.rectangular(3415, 9898, 3430, 9881);
    @Override
    public boolean validate() {
        return (Varps.get(302) == 5) && (!Inventory.contains("Iron key"));
    }

    @Override
    public int execute() {
        if(!dogRoom.contains(Players.getLocal()))
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
        if(!monumentSpot.contains(Players.getLocal()))
        {
            Movement.walkToRandomized(monumentSpot.getCenter());
            Time.sleepUntil(()-> monumentSpot.contains(Players.getLocal()) || !Players.getLocal().isMoving(), 1000);
        }
        if(Inventory.contains("Bucket"))
        {
            Inventory.use(a-> a.getName().equals("Bucket"), SceneObjects.getNearest("Well"));
            Time.sleepUntil(()-> !Inventory.contains("Bucket"), 3000);
            return 500;
        }
        if(!Inventory.contains("Iron key"))
        {
            SceneObject[] monuments = SceneObjects.getLoaded(a-> a.getName().contains("Monument") && a.containsAction("Study"));
            Log.info(monuments.length);
            for(SceneObject s : monuments)
            {
                s.click();
                Time.sleepUntil(()-> Interfaces.isOpen(272), 10000);
                if(Interfaces.getComponent(272, 7).getText().contains("key"))
                {
                    Interfaces.getComponent(272, 1, 11).click();
                    Time.sleepUntil(()-> !Interfaces.isOpen(272), 3000);
                    Time.sleep(2000);
                    Inventory.getFirst("Golden key").interact("Use");
                    Time.sleep(1000);
                    s.interact("Use");
                    Time.sleep(3000);
                    return 500;
                }
                Interfaces.getComponent(272, 1, 11).click();
                Time.sleepUntil(()-> !Interfaces.isOpen(272), 3000);
            }
            return 500;
        }


        return 500;
    }
}
