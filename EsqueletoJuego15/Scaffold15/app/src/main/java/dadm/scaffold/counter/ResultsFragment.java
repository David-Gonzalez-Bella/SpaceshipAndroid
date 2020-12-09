package dadm.scaffold.counter;

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

    public ResultsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_results, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.goToMainMenu).setOnClickListener(this);
        ((TextView)view.findViewById(R.id.score)).setText("Score: " + SpaceShipPlayer.score);
        ((TextView)view.findViewById(R.id.stars)).setText("Stars: " + SpaceShipPlayer.stars);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.goToMainMenu) {
            ((ScaffoldActivity)getActivity()).navigateToFragment(new MainMenuFragment());
        }
    }
}