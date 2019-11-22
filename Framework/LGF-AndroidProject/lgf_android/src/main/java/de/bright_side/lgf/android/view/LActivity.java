package de.bright_side.lgf.android.view;

import android.app.Activity;
import android.content.Context;

import de.bright_side.lgf.presenter.LScreenPresenter;

public interface LActivity {

    void setScreenPresenter(LScreenPresenter presenter);

    Context getContext();

    Activity getActivity();
}
