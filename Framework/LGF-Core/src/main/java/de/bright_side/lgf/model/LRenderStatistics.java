package de.bright_side.lgf.model;

public class LRenderStatistics {
    private long idleTime;
    private long drawDuration;
    private long updateDuration;

    public LRenderStatistics(long idleTime, long drawDuration, long updateDuration) {
        this.idleTime = idleTime;
        this.drawDuration = drawDuration;
        this.updateDuration = updateDuration;
    }

    public long getIdleTime() {
        return idleTime;
    }

    public void setIdleTime(long idleTime) {
        this.idleTime = idleTime;
    }

    public long getDrawDuration() {
        return drawDuration;
    }

    public void setDrawDuration(long drawDuration) {
        this.drawDuration = drawDuration;
    }

    public long getUpdateDuration() {
        return updateDuration;
    }

    public void setUpdateDuration(long updateDuration) {
        this.updateDuration = updateDuration;
    }
}
