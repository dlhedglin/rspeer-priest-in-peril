package Tasks;
import org.rspeer.runetek.api.Varps;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.task.Task;

public class knockOnTemple extends Task {
    private static final Area outsideTemple = Area.rectangular(3408, 3487, 3404, 3490);

    @Override
    public boolean validate() {
        return Varps.get(302) == 1;
    }

    @Override
    public int execute() {
        if(Dialog.isOpen() && outsideTemple.contains(Players.getLocal()))
        {
            if(Dialog.canContinue())
            {
                Dialog.processContinue();
                return 500;
            }
            Dialog.process(a-> a.contains("sent me to check") || a.contains("Sure"));
            //handle chat
            return 100;
        }
        if(!outsideTemple.contains(Players.getLocal()))
        {
            Movement.walkToRandomized(outsideTemple.getCenter());
            Time.sleepUntil(()-> outsideTemple.contains(Players.getLocal()) || !Players.getLocal().isMoving(), 1000);
        }
        SceneObjects.getNearest("Large Door").interact("Knock-at");
        Time.sleepUntil(()-> Dialog.isOpen(), 3000);

        return 100;
    }
}
