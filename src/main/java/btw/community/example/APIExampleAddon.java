package btw.community.example;

import api.AddonHandler;
import api.BTWAddon;

public class APIExampleAddon extends BTWAddon {
    private static APIExampleAddon instance;

    public APIExampleAddon() {
        super();
    }

    @Override
    public void initialize() {
        AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");

        ExampleAchievementTrigger.init();
        ExampleAchievements.init();

        ExampleTags.init();
    }
}