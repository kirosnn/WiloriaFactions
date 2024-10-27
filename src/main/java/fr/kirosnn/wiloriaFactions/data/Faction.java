package fr.kirosnn.wiloriaFactions.data;

import fr.kirosnn.wiloriaFactions.Role;
import fr.kirosnn.wiloriaFactions.claims.FLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class Faction {
    private final String id;
    private String name;
    private String description;
    private UUID leader;
    private Map<UUID, Role> members;
    private FLocation firstClaim;
    private Set<FLocation> claims;
    private boolean pendingFirstClaimUnclaim;
    private boolean hasReceivedCore;
    private static final int MAX_MEMBERS = 12;
    private Location home;

    public Faction(String name, UUID leader) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = "Description par défaut.";
        this.leader = leader;
        this.members = new HashMap<>();
        this.members.put(leader, Role.LEADER);
        this.claims = new HashSet<>();
        this.pendingFirstClaimUnclaim = false;
        this.hasReceivedCore = false;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public UUID getLeader() { return leader; }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addMemberWithRole(UUID memberUUID, Role role) {
        if (members.size() < MAX_MEMBERS) {
            members.put(memberUUID, role);
        }
    }

    public Role getRole(UUID memberUUID) {
        return members.getOrDefault(memberUUID, null);
    }

    public void setClaims(Set<FLocation> claims) {
        this.claims = claims != null ? new HashSet<>(claims) : new HashSet<>();
    }

    public Set<FLocation> getClaims() {
        return new HashSet<>(claims);
    }

    public void addClaim(FLocation loc) {
        claims.add(loc);
    }

    public void removeClaim(FLocation loc) {
        claims.remove(loc);
        if (loc.equals(firstClaim)) {
            removeConnectedClaims();
            firstClaim = null;
        }
    }

    public void removeFirstClaim() {
        if (firstClaim != null) {
            claims.remove(firstClaim);
            removeConnectedClaims();
            firstClaim = null;
        }
    }

    private boolean isConnectedToFirstClaim(FLocation loc) {
        Set<FLocation> visited = new HashSet<>();
        Queue<FLocation> queue = new LinkedList<>();
        queue.add(loc);

        while (!queue.isEmpty()) {
            FLocation current = queue.poll();
            if (current.equals(firstClaim)) {
                return true;
            }

            visited.add(current);
            for (FLocation adjacent : current.getAdjacentLocations()) {
                if (claims.contains(adjacent) && !visited.contains(adjacent)) {
                    queue.add(adjacent);
                }
            }
        }
        return false;
    }

    private void removeConnectedClaims() {
        Set<FLocation> toKeep = new HashSet<>();
        for (FLocation claim : claims) {
            if (!isConnectedToFirstClaim(claim)) {
                toKeep.add(claim);
            }
        }
        claims = toKeep;
    }

    public boolean hasClaim(FLocation loc) {
        return claims.contains(loc);
    }

    public boolean isPendingFirstClaimUnclaim() {
        return pendingFirstClaimUnclaim;
    }

    public void setPendingFirstClaimUnclaim(boolean pending) {
        this.pendingFirstClaimUnclaim = pending;
    }

    public boolean hasReceivedCore() {
        return hasReceivedCore;
    }

    public void setHasReceivedCore(boolean hasReceivedCore) {
        this.hasReceivedCore = hasReceivedCore;
    }

    public void addMember(UUID playerUUID) {
        members.put(playerUUID, Role.RECRUIT);
    }

    public List<UUID> getMembers() {
        return new ArrayList<>(members.keySet());
    }

    public void broadcast(String message) {
        for (UUID memberId : members.keySet()) {
            Player player = Bukkit.getPlayer(memberId);
            if (player != null && player.isOnline()) {
                player.sendMessage(message);
            }
        }
    }

    public boolean removeMember(UUID playerUUID) {
        if (members.containsKey(playerUUID)) {
            members.remove(playerUUID);
            return true;
        }
        return false;
    }

    public FLocation getFirstClaim() {
        return firstClaim;
    }

    public void setFirstClaim(FLocation loc) {
        this.firstClaim = loc;
    }

    public boolean promoteMember(UUID memberUUID) {
        Role currentRole = members.get(memberUUID);

        if (currentRole == null || currentRole == Role.LEADER) {
            return false;
        }

        Role[] roles = Role.values();
        int newRoleIndex = currentRole.ordinal() + 1;
        members.put(memberUUID, roles[newRoleIndex]);

        return true;
    }

    public boolean demoteMember(UUID memberUUID) {
        Role currentRole = members.get(memberUUID);

        if (currentRole == null || currentRole == Role.MEMBER) {
            return false;
        }

        Role[] roles = Role.values();
        int newRoleIndex = currentRole.ordinal() - 1;
        members.put(memberUUID, roles[newRoleIndex]);

        return true;
    }

    private Role getNextRole(Role role) {
        switch (role) {
            case RECRUIT: return Role.MEMBER;
            case MEMBER: return Role.MODERATOR;
            case MODERATOR: return Role.COLEADER;
            case COLEADER: return Role.LEADER;
            default: return role;
        }
    }

    public boolean canPromote(UUID playerUUID) {
        Role role = members.get(playerUUID);
        return role == Role.LEADER || role == Role.COLEADER;
    }

    public Location getHome() {
        return home;
    }

    public void setHome(Location home) {
        this.home = home;
    }

    public boolean hasHome() {
        return home != null;
    }
}
