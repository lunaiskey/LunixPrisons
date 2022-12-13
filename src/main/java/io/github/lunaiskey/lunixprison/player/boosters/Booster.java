package io.github.lunaiskey.lunixprison.player.boosters;

public class Booster {

    private BoosterType type;
    private double multiplier;
    private int length;
    private long startTime;
    private boolean active;

    public Booster(BoosterType type, double multiplier, int length, long startTime, boolean active) {
        this.type = type;
        this.multiplier = multiplier;
        this.length = length;
        this.startTime = startTime;
        this.active = active;
    }

    public BoosterType getType() {
        return type;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public int getLength() {
        return length;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return startTime + (length*1000L);
    }

    public boolean isActive() {
        return active;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setActive(boolean active) {
        this.active = active;
        if (active) {
            this.length = (int) (System.currentTimeMillis()-startTime/1000);
            this.startTime = System.currentTimeMillis();
        }
    }
}
