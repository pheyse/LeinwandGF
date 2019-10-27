package de.bright_side.lgf.presenter;

import de.bright_side.lgf.model.LInput;
import de.bright_side.lgf.model.LRenderStatistics;

public interface LScreenPresenter {
    void update(LInput input, double secondsSinceLastUpdate, LRenderStatistics renderStatistics);
    void onClose();
}
