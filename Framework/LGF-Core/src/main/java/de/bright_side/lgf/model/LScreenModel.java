package de.bright_side.lgf.model;

import java.util.List;

public class LScreenModel {
    private LColor backgroundColor;
    private List<LObject> backgroundObjects;
    private List<LObject> enemyObjects;
    private List<LObject> playerObjects;
    private List<LObject> foregroundObjects;

    private String modelName;

    /** ui objects are not scrolled / influenced by the camera position */
    private List<LObject> uiObjects;

    public LColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(LColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public List<LObject> getBackgroundObjects() {
        return backgroundObjects;
    }

    public void setBackgroundObjects(List<LObject> backgroundObjects) {
        this.backgroundObjects = backgroundObjects;
    }

    public List<LObject> getEnemyObjects() {
        return enemyObjects;
    }

    public void setEnemyObjects(List<LObject> enemyObjects) {
        this.enemyObjects = enemyObjects;
    }

    public List<LObject> getPlayerObjects() {
        return playerObjects;
    }

    public void setPlayerObjects(List<LObject> playerObjects) {
        this.playerObjects = playerObjects;
    }

	public List<LObject> getForegroundObjects() {
		return foregroundObjects;
	}

	public void setForegroundObjects(List<LObject> foregroundObjects) {
		this.foregroundObjects = foregroundObjects;
	}

	public List<LObject> getUiObjects() {
		return uiObjects;
	}

	public void setUiObjects(List<LObject> uiObjects) {
		this.uiObjects = uiObjects;
	}

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}
