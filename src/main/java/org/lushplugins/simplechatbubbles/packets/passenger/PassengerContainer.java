package org.lushplugins.simplechatbubbles.packets.passenger;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetPassengers;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.IntStream;

public class PassengerContainer {
    private final int mountId;
    private final UUID mountUuid;
    private final List<Integer> passengers;

    public PassengerContainer(int mountId, UUID mountUuid) {
        this.mountId = mountId;
        this.mountUuid = mountUuid;
        this.passengers = new ArrayList<>();
    }

    public boolean hasPassengers() {
        return !this.passengers.isEmpty();
    }

    public void addPassenger(int passengerId, Collection<UUID> viewers) {
        this.passengers.add(passengerId);

        sendPacket(viewers);
    }

    public void removePassenger(int passengerId, Collection<UUID> viewers) {
        this.passengers.remove((Integer) passengerId);

        sendPacket(viewers);
    }

    public void sendPacket(UUID viewer) {
        sendPacket(Collections.singletonList(viewer));
    }

    public void sendPacket(Collection<UUID> viewers) {
        if (viewers.isEmpty()) {
            return;
        }

        WrapperPlayServerSetPassengers packet;
        Player player = Bukkit.getPlayer(mountUuid);
        if (player != null) {
            packet = new WrapperPlayServerSetPassengers(
                mountId,
                IntStream.concat(
                    player.getPassengers().stream().mapToInt(Entity::getEntityId),
                    passengers.stream().mapToInt(Integer::intValue)
                ).toArray()
            );
        } else {
            packet = new WrapperPlayServerSetPassengers(
                mountId,
                passengers.stream().mapToInt(Integer::intValue).toArray()
            );
        }

        for (UUID viewer : viewers) {
            Object channel = PacketEvents.getAPI().getProtocolManager().getChannel(viewer);
            if (channel != null) {
                PacketEvents.getAPI().getProtocolManager().sendPacket(channel, packet);
            }
        }
    }
}
