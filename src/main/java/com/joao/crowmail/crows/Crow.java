package com.joao.crowmail.crows;


import com.joao.crowmail.utils.Utilities;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;


public class Crow {

    //todo: change to Entity
    @Getter private Parrot crowEntity;

    private final Player receiver;
    private final boolean anonymous;


    public Crow(Player receiver, boolean anonymous){
        this.receiver = receiver;
        this.anonymous = anonymous;

        this.spawn();
    }


    public void spawn() {
        Location location = receiver.getLocation();

        location = getNewLocation(location, location.getYaw(), 1).add(0, 1, 0);
        if (!location.getBlock().isEmpty())
            location = receiver.getLocation().add(0, 1, 0);

        // todo: change to entity
        crowEntity = (Parrot) receiver.getWorld().spawnEntity(location, EntityType.PARROT);

        receiver.playSound(receiver.getLocation(), Sound.ENTITY_PARROT_FLY, 1.0F, 1.0F);

        // crows.put(crowEntity, this);

        // Set Raven if Anonimous
        if (anonymous) {
            crowEntity.setVariant(Parrot.Variant.valueOf(Utilities.pullConfigString("courier.crow-variant")));
            receiver.sendMessage(Utilities.pullMessage("crow-arrived"));
        }

        else {
            crowEntity.setVariant(Parrot.Variant.valueOf(Utilities.pullConfigString("courier.pigeon-variant")));
            receiver.sendMessage(Utilities.pullMessage("pigeon-arrived"));
        }

        // Set the entity to stay still
        crowEntity.setInvulnerable(true);
        crowEntity.setGravity(false);
        crowEntity.setAI(false);

    }

    public void remove() {

        // crows.remove(crowEntity);
        crowEntity.remove();

    }


    public void playFeedbackGood(Player player){
        crowEntity.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, crowEntity.getLocation(), 50, 0.3, 0.3, 0.3, 2);
        player.playSound(player.getLocation(), Sound.ENTITY_PARROT_AMBIENT, 1.0F, 1.0F);
    }

    public void playFeedbackBad(Player player){
        crowEntity.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, crowEntity.getLocation(), 10, 0.3, 0.3, 0.3, 2);
        player.playSound(player.getLocation(), Sound.ENTITY_PARROT_IMITATE_PILLAGER, 1.0F, 1.0F);

    }

    public Location getNewLocation(Location location, double angle, double distance) {

        angle = Math.toRadians(angle);

        location.setX(location.getX() + (distance * Math.cos(angle)));
        location.setZ(location.getZ() + (distance * Math.sin(angle)));

        return location;
    }

}
