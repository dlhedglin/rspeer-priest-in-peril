package Tasks;

import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.Varps;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.task.Task;

public class freeDrezel extends Task {

    private static final Area dogRoom = Area.rectangular(3400, 9908, 3443, 9879);
    private static Area upstairsTemple = Area.rectangular(3409, 3493, 3417, 3484, 2);
    @Override
    public boolean validate() {
        return Varps.get(302) == 5;
    }

    @Override
    public int execute() {
        if(dogRoom.contains(Players.getLocal()))
        {
            SceneObject ladder = SceneObjects.getNearest(a-> a.getName().contains("Ladder") && a.isPositionInteractable());
            if(ladder != null)
            {
                ladder.click();
                Time.sleep(3000);
                return 500;
            }
            Movement.walkTo(new Position(3405,9905));
            Time.sleep(2555);
            return 500;
        }
        if(!upstairsTemple.contains(Players.getLocal()))
        {
            Movement.walkTo(upstairsTemple.getCenter());
            Time.sleepUntil(()-> upstairsTemple.contains(Players.getLocal()) || !Players.getLocal().isMoving(), 1000);
            return 500;
        }
        if(Inventory.contains("Iron key"))
        {
            Inventory.getFirst("Iron key").interact("Use");
            Time.sleep(1000);
            SceneObjects.getNearest("Cell door").interact("Use");
            Time.sleep(3000);
            return 500;
        }
        return 500;
    }
}
