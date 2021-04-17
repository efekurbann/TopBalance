package xyz.efekurbann.topbalance;

import org.bukkit.Bukkit;
import org.bukkit.util.Consumer;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class UpdateChecker {

    private final TopBalancePlugin plugin;
    private String latestVersion;
    private boolean isUpToDate;

    public UpdateChecker(TopBalancePlugin plugin) {
        this.plugin = plugin;
    }

    public void checkUpdates() {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try {
                HttpsURLConnection con = (HttpsURLConnection) (new URL("https://api.spigotmc.org/legacy/update.php?resource=91372")).openConnection();
                con.setRequestMethod("GET");
                InputStreamReader reader = new InputStreamReader(con.getInputStream());
                this.latestVersion = (new BufferedReader(reader)).readLine();
                this.isUpToDate = this.latestVersion.equals(plugin.getDescription().getVersion());

                if (!isUpToDate) {
                    plugin.getLogger().info("An update was found for TopBalance!");
                    plugin.getLogger().info("https://www.spigotmc.org/resources/91372/");
                } else {
                    plugin.getLogger().info("Plugin is up to date, no update found.");
                    plugin.getLogger().info("Plugin enabled. Thank you for using.");
                }
            } catch (IOException exception) {
                this.plugin.getLogger().info("Cannot look for updates: " + exception.getMessage());
            }
        });
    }

    public boolean isUpToDate() {
        return isUpToDate;
    }

}