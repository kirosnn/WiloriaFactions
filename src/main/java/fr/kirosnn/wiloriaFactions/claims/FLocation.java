package fr.kirosnn.wiloriaFactions.claims;

import org.bukkit.Chunk;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FLocation {
    private final int x;
    private final int z;
    private final String world;

    public FLocation(String world, int x, int z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    public FLocation(Location location) {
        this(location.getWorld().getName(), location.getBlockX() >> 4, location.getBlockZ() >> 4);
    }

    public FLocation(Chunk chunk) {
        this(chunk.getWorld().getName(), chunk.getX(), chunk.getZ());
    }

    public String getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public List<FLocation> getAdjacentLocations() {
        List<FLocation> adjacent = new ArrayList<>();
        adjacent.add(new FLocation(world, x + 1, z)); // Est
        adjacent.add(new FLocation(world, x - 1, z)); // Ouest
        adjacent.add(new FLocation(world, x, z + 1)); // Nord
        adjacent.add(new FLocation(world, x, z - 1)); // Sud
        return adjacent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FLocation fLocation = (FLocation) o;
        return x == fLocation.x &&
                z == fLocation.z &&
                Objects.equals(world, fLocation.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, x, z);
    }
}