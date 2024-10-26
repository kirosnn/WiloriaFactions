package fr.kirosnn.wiloriaFactions.data;

import fr.kirosnn.wiloriaFactions.claims.FLocation;

import java.util.*;

public class Faction {
    private final String id;
    private String name;
    private String description;
    private UUID leader;
    private List<UUID> members;
    private FLocation firstClaim;
    private Set<FLocation> claims;
    private boolean pendingFirstClaimUnclaim;
    private Map<String, Faction> factions;
    private boolean hasReceivedCore;

    public Faction(String name, UUID leader) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = "Description par défaut.";
        this.leader = leader;
        this.members = new ArrayList<>();
        this.members.add(leader);
        this.claims = new HashSet<>();
        this.pendingFirstClaimUnclaim = false;
        this.hasReceivedCore = false;

    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public UUID getLeader() { return leader; }
    public List<UUID> getMembers() { return members; }

    public void setDescription(String description) {
        this.description = description;
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

    public FLocation getFirstClaim() {
        return firstClaim;
    }

    public void setFirstClaim(FLocation loc) {
        this.firstClaim = loc;
    }

    public Set<FLocation> getClaims() {
        return new HashSet<>(claims);
    }

    public boolean hasClaim(FLocation loc) {
        return claims.contains(loc);
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

    public boolean hasAdjacentClaim(FLocation loc) {
        return loc.getAdjacentLocations().stream()
                .anyMatch(claims::contains);
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

    public boolean isPendingFirstClaimUnclaim() {
        return pendingFirstClaimUnclaim;
    }

    public void setPendingFirstClaimUnclaim(boolean pending) {
        this.pendingFirstClaimUnclaim = pending;
    }

    public Collection<Faction> getAllFactions() {
        return factions.values();
    }

    public boolean hasReceivedCore() {
        return hasReceivedCore;
    }

    public void setHasReceivedCore(boolean hasReceivedCore) {
        this.hasReceivedCore = hasReceivedCore;
    }
}