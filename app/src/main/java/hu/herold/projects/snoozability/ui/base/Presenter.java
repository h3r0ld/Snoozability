package hu.herold.projects.snoozability.ui.base;

import hu.herold.projects.snoozability.interactor.base.EventBase;

public abstract class Presenter<T extends Screen> {

    protected T screen;

    public void attachScreen(T screen) {
        this.screen = screen;
    }

    public  void detachScreen() {
        this.screen = null;
    }

    protected boolean handleEventException(EventBase event) {
        boolean isExceptionPresent = event.getThrowable() != null;

        if (isExceptionPresent) {
            event.getThrowable().printStackTrace();

            if (screen != null) {
                screen.showError(event.getThrowable().getMessage());
            }
        }

        return isExceptionPresent;
    }
}
