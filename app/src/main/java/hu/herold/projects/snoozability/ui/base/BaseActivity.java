package hu.herold.projects.snoozability.ui.base;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;

public abstract class BaseActivity extends AppCompatActivity implements Screen {

    @BindView(android.R.id.content)
    ViewGroup viewGroup;

    @Override
    public void showError(String message) {
        View rootView = viewGroup.getChildAt(0);
        Snackbar.make(rootView, message, Snackbar.LENGTH_INDEFINITE).show();
    }
}
