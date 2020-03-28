package Tasks;
import javafx.print.PageLayout;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.Varps;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.tab.Prayer;
import org.rspeer.runetek.api.component.tab.Prayers;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;

import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.task.Task;

public class GoRoald extends Task {
    private static final Area castleRoom = Area.rectangular(3219, 3473, 3224, 3470);
    private static final Area dogRoom = Area.rectangular(3400, 9908, 3443, 9879);

    @Override
    public boolean validate() {
        return Varps.get(302) == 0 || Varps.get(302) == 3;
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
            Dialog.process(a-> a.contains("Sure."));
            // handle chat options
            return 100;
        }
        if(Prayers.getActive().length > 0)
        {
            Prayers.toggle(false, Prayer.PROTECT_FROM_MELEE);
            return 500;
        }
        Npc roald = Npcs.getNearest(a-> a.getName().contains("King Roald") && a.isPositionInteractable());
        if(roald == null)
        {
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
            Movement.walkToRandomized(castleRoom.getCenter());
            if(Movement.getRunEnergy() > 30)
                Movement.toggleRun(true);
            Time.sleepUntil(()-> castleRoom.contains(Players.getLocal()) || !Players.getLocal().isMoving(), 1000);
            return 100;
        }
        roald.click();
        Time.sleepUntil(()-> Dialog.isOpen(), 3000);
        return 100;
    }
}
