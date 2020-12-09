package dadm.scaffold.counter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;

import dadm.scaffold.BaseFragment;
import dadm.scaffold.R;
import dadm.scaffold.ScaffoldActivity;


public class MainMenuFragment extends BaseFragment implements View.OnClickListener {
    private int skinIndex = 0;
    private int[] shipSkins = {R.drawable.ship_00, R.drawable.ship_01, R.drawable.ship_02, R.drawable.ship_03};
    private ImageView shipSkinPreview;

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
        view.findViewById(R.id.nextSkin).setOnClickListener(this);
        view.findViewById(R.id.prevSkin).setOnClickListener(this);
        shipSkinPreview = view.findViewById(R.id.skin);
    }

    @Override
    public void onClick(View v) {
        String url;
        Intent web;
        switch (v.getId()) {
            case (R.id.sainnyLogo):
                url = "https://www.artstation.com/sainny";
                web = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(web);
                break;
            case (R.id.kakugamesLogo):
                url = "https://kakugames.itch.io/";
                web = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(web);
                break;
            case (R.id.nextSkin):
                skinIndex = skinIndex < 3 ? (skinIndex + 1) : 0;
                shipSkinPreview.setImageResource(shipSkins[skinIndex]);
                break;
            case (R.id.prevSkin):
                skinIndex = skinIndex > 0 ? (skinIndex - 1) : 3;
                shipSkinPreview.setImageResource(shipSkins[skinIndex]);
                break;
            case (R.id.btn_start):
                ((ScaffoldActivity) getActivity()).startGame(shipSkins[skinIndex]);
                break;
        }
    }
}
