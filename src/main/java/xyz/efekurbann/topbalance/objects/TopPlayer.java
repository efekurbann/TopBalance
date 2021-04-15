package xyz.efekurbann.topbalance.objects;

public class TopPlayer {

    private final String name;
    private final double balance;

    public TopPlayer(String name, double balance){
        this.balance = balance;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }
}
