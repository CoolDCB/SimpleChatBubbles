package org.lushplugins.simplechatbubbles.packets.passenger;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class PassengerManager {
    private final HashMap<Integer, PassengerContainer> containers = new HashMap<>();

    public @Nullable PassengerContainer getPassengerContainer(int mountId) {
        return this.containers.get(mountId);
    }

    public void addPassenger(int mountId, UUID mountUuid, int passengerId, Collection<UUID> viewers) {
        PassengerContainer container = this.containers.computeIfAbsent(mountId, (ignored) -> new PassengerContainer(mountId, mountUuid));
        container.addPassenger(passengerId, viewers);
    }

    public void removePassenger(int mountId, int passengerId, Collection<UUID> viewers) {
        PassengerContainer container = this.containers.get(mountId);
        if (container == null) {
            return;
        }

        container.removePassenger(passengerId, viewers);
    }

    public void invalidateEntity(int mountId) {
        this.containers.remove(mountId);
    }

    public void invalidateAll() {
        this.containers.clear();
    }
}
