package dadm.scaffold.counter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dadm.scaffold.BaseFragment;
import dadm.scaffold.R;
import dadm.scaffold.ScaffoldActivity;


public class MainMenuFragment extends BaseFragment implements View.OnClickListener {
    public MainMenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_menu, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_start).setOnClickListener(this);
        view.findViewById(R.id.kakugamesLogo).setOnClickListener(this);
        view.findViewById(R.id.sainnyLogo).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String url;
        if(v.getId() == R.id.sainnyLogo){
            url = "https://www.artstation.com/sainny";
            Intent web = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(web);
        }else if(v.getId() == R.id.kakugamesLogo){
            url = "https://kakugames.itch.io/";
            Intent web = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(web);
        }else{
            ((ScaffoldActivity)getActivity()).startGame();
        }
    }
}
