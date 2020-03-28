import Tasks.*;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.script.task.Task;
import org.rspeer.script.task.TaskScript;


@ScriptMeta(name = "peril",  desc = "Script description", developer = "Developer's Name", category = ScriptCategory.MONEY_MAKING)
public class peril extends TaskScript {
    public static int VARP = 302;
    private static final Task[] TASKS = { new GoRoald(), new knockOnTemple(), new killDog(),
            new goCagedDrezel(), new getKey(), new freeDrezel(), new burnVampire(), new giveEssence()};

    @Override
    public void onStart() {
        submit(TASKS);
    }
}
