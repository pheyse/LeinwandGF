package de.bright_side.lgf.view;

import de.bright_side.lgf.model.LColor;
import de.bright_side.lgf.model.LFont;
import de.bright_side.lgf.model.LImage;
import de.bright_side.lgf.model.LVector;

public interface LCanvas {
    void fillRect(LVector pos, LVector size, LColor color, boolean considerCameraPos);

    void fillRectCentered(LVector pos, LVector size, LColor color, double opacity, boolean considerCameraPos);

    void drawTextCentered(LVector pos, String text, double textSize, LColor color, LColor outlineColorOrNull
            , LFont fontOrNull, LVector shadowOffset, LColor shadowColor, double opacity, boolean considerCameraPos);

    void drawImageCentered(LVector pos, LVector size, LImage image, double rotation, double opacity, boolean considerCameraPos);

}
