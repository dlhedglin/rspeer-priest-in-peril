package Tasks;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.Varps;
import org.rspeer.runetek.api.commons.BankLocation;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.path.Path;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.task.Task;

public class giveEssence extends Task {
    private static final Area dogRoom = Area.rectangular(3400, 9908, 3443, 9879);
    private static Area trapDoorSpot = Area.rectangular(3404, 3507, 3407, 3501);
    private static Area drezelsRoom = Area.rectangular(3436, 9901, 3443, 9886);
    private static Area insideCell = Area.rectangular(3416, 3493, 3418, 3483, 2);
    @Override
    public boolean validate() {
        return Varps.get(302) >= 8;
    }

    @Override
    public int execute() {
        if(Varps.get(302) == 60)
        {
            return -1;
        }
        if(insideCell.contains(Players.getLocal()))
        {
            SceneObjects.getNearest("Cell door").interact("Open");
            Time.sleepUntil(()-> !insideCell.contains(Players.getLocal()), 3000);
            return 500;
        }
        if(!Inventory.contains("Pure essence"))
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
            if(Bank.isOpen())
            {
                Bank.withdraw("Pure essence", 25);
                Time.sleepUntil(()-> Inventory.contains("Pure essence"), 3000);
                return 500;
            }
            Bank.open(BankLocation.VARROCK_EAST);
            Time.sleepUntil(()-> Bank.isOpen(), 2000);
            return 500;
        }
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
        if(Dialog.isOpen())
        {
            if(Dialog.canContinue())
            {
                Dialog.processContinue();
                return 500;
            }
            return 500;
        }
        if(!drezelsRoom.contains(Players.getLocal()))
        {
            Movement.walkTo(drezelsRoom.getCenter());
            return 2000;
        }
        Npcs.getNearest("Drezel").click();
        return 1500;

    }
}
