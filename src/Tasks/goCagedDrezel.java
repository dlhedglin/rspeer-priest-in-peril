package Tasks;

import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.api.Varps;
import org.rspeer.runetek.api.commons.BankLocation;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.component.tab.Prayer;
import org.rspeer.runetek.api.component.tab.Prayers;
import org.rspeer.runetek.api.local.Health;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Pickables;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.task.Task;

public class goCagedDrezel extends Task {
    private static Area insideTemple = Area.rectangular(3409, 3494, 3418, 3484);
    private static Area upstairsTemple = Area.rectangular(3409, 3493, 3417, 3484, 2);
    private static final Area outsideTemple = Area.rectangular(3408, 3487, 3404, 3490);
    boolean haveItems = false;
    @Override
    public boolean validate() {
        return Varps.get(302) == 4;
    }

    @Override
    public int execute() {
        if(Inventory.getCount("Pure essence") != 25)
        {
            if(Bank.isOpen())
            {
                Bank.depositInventory();
                Time.sleep(500);
                Bank.withdraw("Pure essence", 25);
                Time.sleep(500);
                Bank.withdraw("Bucket", 1);
                Time.sleep(500);
                Bank.withdraw("Lobster", 5);
                Time.sleep(500);
                haveItems = true;
                return 500;

            }
            Bank.open(BankLocation.VARROCK_EAST);
            return 1500;
        }
        if(Inventory.contains("Golden key"))
        {
            if(upstairsTemple.contains(Players.getLocal()))
            {
                if(Dialog.isOpen())
                {
                    if(Dialog.canContinue())
                    {
                        Dialog.processContinue();
                        return 500;
                    }
                    Dialog.process(a-> a.contains("Tell me") || a.contains("Yes"));
                    return 500;
                }
                SceneObjects.getNearest("Cell door").interact("Talk-through");
                Time.sleepUntil(()->Dialog.isOpen(), 3000);
                return 500;

            }
            Movement.walkToRandomized(upstairsTemple.getCenter());
            Time.sleepUntil(()-> upstairsTemple.contains(Players.getLocal()) || !Players.getLocal().isMoving(), 1000);
            return 500;
        }
        if(Pickables.getNearest("Golden key") != null)
        {
            if(Inventory.isFull())
            {
                Inventory.getFirst("Lobster").click();
                return 1000;
            }
            Pickables.getNearest("Golden key").interact("Take");
            Time.sleepUntil(()->Inventory.contains("Golden key"), 3000);

        }
        Npc monk = Npcs.getNearest(a-> a.getName().contains("Monk of Zamorak") && a.getCombatLevel() == 30 && a.isPositionWalkable());
        if(Health.getPercent() < 50 && Inventory.contains("Lobster"))
        {
            Inventory.getFirst("Lobster").click();
            return 1000;
        }
        if(!insideTemple.contains(Players.getLocal()))
        {
            if(outsideTemple.contains(Players.getLocal()))
            {
                SceneObjects.getNearest("Large door").click();
                return 1000;
            }
            Movement.walkToRandomized(outsideTemple.getCenter());
            Time.sleepUntil(()-> outsideTemple.contains(Players.getLocal()) || !Players.getLocal().isMoving(), 1000);
            return 1000;
        }
        if(!Players.getLocal().isHealthBarVisible())
        {
            monk.interact("Attack");
            Time.sleepUntil(()-> Players.getLocal().isHealthBarVisible(), 3000);
            return 1000;
        }
        if(Prayers.getActive().length == 0)
        {
            Prayers.toggle(true, Prayer.PROTECT_FROM_MAGIC);
            return 1000;
        }

        return 500;
    }
}
