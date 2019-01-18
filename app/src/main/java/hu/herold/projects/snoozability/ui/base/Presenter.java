package hu.herold.projects.snoozability.ui.base;

public abstract class Presenter<T> {

    protected T screen;

    public void attachScreen(T screen)
    {
        this.screen = screen;
    }

    public  void detachScreen()
    {
        this.screen = null;
    }
}
