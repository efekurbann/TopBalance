package xyz.efekurbann.topbalance.objects;

import java.util.UUID;

public class TopPlayer {

    private final String name;
    private final UUID uuid;
    private final double balance;

    public TopPlayer(String name, UUID uuid, double balance){
        this.balance = balance;
        this.uuid = uuid;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public UUID getUUID() {
        return uuid;
    }
}
