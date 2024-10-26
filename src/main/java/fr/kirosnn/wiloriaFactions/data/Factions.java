package fr.kirosnn.wiloriaFactions.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.kirosnn.wiloriaFactions.WiloriaFactions;
import fr.kirosnn.wiloriaFactions.claims.FLocation;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Collection;

public class Factions {
    private final Map<String, Faction> factions = new HashMap<>();
    private final File file = new File(WiloriaFactions.getInstance().getDataFolder(), "data/dataFactions.json");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();


    public void load() {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            return;
        }

        try (Reader reader = new FileReader(file)) {
            FactionData data = gson.fromJson(reader, FactionData.class);
            if (data != null) {
                this.factions.putAll(data.factions);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void forceSave() {
        try (Writer writer = new FileWriter(file)) {
            FactionData data = new FactionData(factions);
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean createFaction(String name, Player player) {
        if (name.length() < 4 || name.length() > 9) {
            return false;
        }
        if (!name.matches("[a-zA-Z0-9]+")) {
            return false;
        }
        if (getFactionByName(name) != null) {
            return false;
        }

        Faction faction = new Faction(name, player.getUniqueId());
        factions.put(faction.getId(), faction);
        forceSave();
        return true;
    }

    public Faction getFactionByName(String name) {
        return factions.values().stream()
                .filter(f -> f.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public Faction getFactionByPlayer(UUID playerUUID) {
        return factions.values().stream()
                .filter(f -> f.getMembers().contains(playerUUID))
                .findFirst()
                .orElse(null);
    }

    public Collection<Faction> getAllFactions() {
        return factions.values();
    }

    private static class FactionData {
        private final Map<String, Faction> factions;

        public FactionData(Map<String, Faction> factions) {
            this.factions = factions;
        }
    }

    public boolean isChunkClaimed(FLocation loc) {
        return factions.values().stream()
                .anyMatch(faction -> faction.hasClaim(loc));
    }

    public Faction getFactionByLocation(FLocation location) {
        return factions.values().stream()
                .filter(faction -> faction.hasClaim(location))
                .findFirst()
                .orElse(null);
    }
}
