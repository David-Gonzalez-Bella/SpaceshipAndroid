package dadm.scaffold.counter;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dadm.scaffold.BaseFragment;
import dadm.scaffold.R;
import dadm.scaffold.ScaffoldActivity;
import dadm.scaffold.space.SpaceShipPlayer;

public class ResultsFragment extends BaseFragment implements View.OnClickListener {

    private int theme;
    public MediaPlayer resultsTheme;
    private ScaffoldActivity myActivity;

    public ResultsFragment() {
    }


    @SuppressLint("ValidFragment")
    public ResultsFragment(int victoryOrDefeat) {
        theme = victoryOrDefeat == 0 ? R.raw.victory : R.raw.game_over;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myActivity = (ScaffoldActivity)getActivity();
        return inflater.inflate(R.layout.fragment_results, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        resultsTheme = MediaPlayer.create(getActivity(), theme);
        resultsTheme.start();
        view.findViewById(R.id.goToMainMenu).setOnClickListener(this);
        ((TextView)view.findViewById(R.id.score)).setText("Score: " + SpaceShipPlayer.score);
        ((TextView)view.findViewById(R.id.stars)).setText("Stars: " + SpaceShipPlayer.stars);
    }

    @Override
    public void onClick(View v) {
        myActivity.buttonPressedSnd.start();
        if (v.getId() == R.id.goToMainMenu) {
            ((ScaffoldActivity)getActivity()).navigateToFragment(new MainMenuFragment());
        }
    }
}