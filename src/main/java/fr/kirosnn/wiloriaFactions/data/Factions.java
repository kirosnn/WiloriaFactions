package fr.kirosnn.wiloriaFactions.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.kirosnn.wiloriaFactions.Role;
import fr.kirosnn.wiloriaFactions.WiloriaFactions;
import fr.kirosnn.wiloriaFactions.claims.FLocation;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.*;

public class Factions {
    private final Map<String, Faction> factions = new HashMap<>(); // Stocke les factions par ID
    private final Map<String, List<UUID>> factionInvitations = new HashMap<>(); // Invitations pour rejoindre les factions
    private final File file = new File(WiloriaFactions.getInstance().getDataFolder(), "data/dataFactions.json"); // Fichier JSON
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create(); // GSON pour JSON

    // Chargement des données des factions depuis le fichier JSON
    public void load() {
        if (!file.exists()) {
            file.getParentFile().mkdirs(); // Création des dossiers si nécessaire
            return;
        }

        try (Reader reader = new FileReader(file)) {
            FactionData[] data = gson.fromJson(reader, FactionData[].class);
            if (data != null) {
                for (FactionData factionData : data) {
                    // Création d'une nouvelle instance Faction à partir des données JSON
                    Faction faction = new Faction(factionData.name, factionData.leader);
                    faction.setDescription(factionData.description);
                    faction.setClaims(factionData.claims);

                    // Ajout des membres avec leurs rôles respectifs
                    for (Map.Entry<String, String> entry : factionData.members.entrySet()) {
                        UUID memberUUID = UUID.fromString(entry.getKey());
                        Role role = Role.valueOf(entry.getValue());
                        faction.addMemberWithRole(memberUUID, role);
                    }

                    factions.put(faction.getId(), faction); // Ajoute la faction dans la map
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void forceSave() {
        List<FactionData> data = new ArrayList<>();
        for (Faction faction : factions.values()) {
            data.add(new FactionData(faction));
        }

        try (Writer writer = new FileWriter(file)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean createFaction(String name, Player player) {
        if (name.length() < 4 || name.length() > 9 || !name.matches("[a-zA-Z0-9]+") || getFactionByName(name) != null) {
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
        private String id;
        private String name;
        private String description;
        private UUID leader;
        private Map<String, String> members;
        private Set<FLocation> claims;

        public FactionData(Faction faction) {
            this.id = faction.getId();
            this.name = faction.getName();
            this.description = faction.getDescription();
            this.leader = faction.getLeader();
            this.claims = faction.getClaims();
            this.members = new HashMap<>();

            for (UUID memberUUID : faction.getMembers()) {
                Role role = faction.getRole(memberUUID);
                members.put(memberUUID.toString(), role.name());
            }
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

    public void addInvitation(String factionName, UUID playerUUID) {
        factionInvitations.computeIfAbsent(factionName, k -> new ArrayList<>()).add(playerUUID);
    }

    public boolean isPlayerInvited(String factionName, UUID playerUUID) {
        return factionInvitations.getOrDefault(factionName, Collections.emptyList()).contains(playerUUID);
    }

    public void removeInvitation(String factionName, UUID playerUUID) {
        List<UUID> invitations = factionInvitations.get(factionName);
        if (invitations != null) {
            invitations.remove(playerUUID);
            if (invitations.isEmpty()) {
                factionInvitations.remove(factionName);
            }
        }
    }

    public void addPlayerToFaction(UUID playerUUID, Faction faction) {
        if (faction != null) {
            faction.addMemberWithRole(playerUUID, Role.RECRUIT);
            forceSave();
        }
    }
}
