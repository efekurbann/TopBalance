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

    public UpdateChecker(TopBalancePlugin plugin) {
        this.plugin = plugin;
    }

    public void isUpToDate(final Consumer<Boolean> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try {
                HttpsURLConnection con = (HttpsURLConnection) (new URL("https://api.spigotmc.org/legacy/update.php?resource=91372")).openConnection();
                con.setRequestMethod("GET");
                InputStreamReader reader = new InputStreamReader(con.getInputStream());
                this.latestVersion = (new BufferedReader(reader)).readLine();
                String version = plugin.getDescription().getVersion();

                consumer.accept(latestVersion.equals(version));
            } catch (IOException exception) {
                this.plugin.getLogger().info("Cannot look for updates: " + exception.getMessage());
            }
        });
    }
}