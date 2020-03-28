package Tasks;

import javafx.print.PageLayout;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.Varps;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.task.Task;

public class burnVampire extends Task {
    private static Area insideCell = Area.rectangular(3416, 3493, 3418, 3483, 2);
    @Override
    public boolean validate() {
        return Varps.get(302) == 6 || Varps.get(302) == 7;
    }

    @Override
    public int execute() {
        if(Inventory.contains("Blessed water"))
        {
            if(insideCell.contains(Players.getLocal()))
            {
                SceneObjects.getNearest("Cell door").interact("Open");
                Time.sleepUntil(()-> !insideCell.contains(Players.getLocal()), 3000);
                return 500;
            }
            Inventory.use(a-> a.getName().contains("Blessed"), SceneObjects.getNearest("Coffin"));
            Time.sleepUntil(()-> !Inventory.contains("Blessed water"), 3000);
            return 500;
        }
        if(!insideCell.contains(Players.getLocal()))
        {
            SceneObjects.getNearest("Cell door").interact("Open");
            Time.sleepUntil(()-> insideCell.contains(Players.getLocal()), 3000);
            return 500;
        }
        if(Dialog.isOpen())
        {
            Dialog.processContinue();
            return 500;
        }
        Npcs.getNearest("Drezel").click();
        Time.sleepUntil(()-> Dialog.isOpen(), 3000);
        return 500;
    }
}
