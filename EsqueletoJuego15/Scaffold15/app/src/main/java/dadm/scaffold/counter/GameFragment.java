package dadm.scaffold.counter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;

import dadm.scaffold.BaseFragment;
import dadm.scaffold.R;
import dadm.scaffold.ScaffoldActivity;
import dadm.scaffold.engine.GUI;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.GameView;
import dadm.scaffold.engine.Parallax;
import dadm.scaffold.input.JoystickInputController;
import dadm.scaffold.space.GameController;
import dadm.scaffold.space.SpaceShipPlayer;


public class GameFragment extends BaseFragment implements View.OnClickListener {
    private GameEngine theGameEngine;
    private int shipInGameSkin;
    private int shipInGameSkinShielded;

    private ConstraintLayout pauseLayout;
    private ImageButton pauseButton;

    private ScaffoldActivity myActivity;

    public GameFragment() {
    }

    @SuppressLint("ValidFragment")
    public GameFragment(int shipSkin, int shipSkinShielded) {
        shipInGameSkin = shipSkin;
        shipInGameSkinShielded = shipSkinShielded;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);
        myActivity = (ScaffoldActivity)getActivity();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pauseLayout = view.findViewById(R.id.pauseLayout);
        pauseButton = view.findViewById(R.id.btn_play_pause);

        pauseLayout.setVisibility(View.GONE); //We deactivate pause layout
        pauseButton.setOnClickListener(this);
        view.findViewById(R.id.resumeButton).setOnClickListener(this);
        view.findViewById(R.id.mainMenuButton).setOnClickListener(this);


        final ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                observer.removeOnGlobalLayoutListener(this);
                GameView gameView = (GameView) getView().findViewById(R.id.gameView);
                theGameEngine = new GameEngine(getActivity(), gameView);
                theGameEngine.setSoundManager(getScaffoldActivity().getSoundManager());
                theGameEngine.setTheInputController(new JoystickInputController(getView()));
                theGameEngine.addGameObject(new Parallax(theGameEngine, 30, R.drawable.background_parallax));
                theGameEngine.addGameObject(new SpaceShipPlayer(theGameEngine, shipInGameSkin, shipInGameSkinShielded));
                theGameEngine.addGameObject(new GUI(theGameEngine, getActivity()));
                theGameEngine.addGameObject(new GameController(theGameEngine));
                theGameEngine.startGame();
            }
        });
    }

    @Override
    public void onClick(View v) {
        myActivity.buttonPressedSnd.start();
        switch (v.getId()) {
            case (R.id.btn_play_pause):
                pauseGameAndShowPauseDialog();
                break;
            case (R.id.resumeButton):
                resumeGame();
                break;
            case (R.id.mainMenuButton):
                goToMainMenu();
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (theGameEngine.isRunning()) {
            pauseGameAndShowPauseDialog();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        theGameEngine.stopGame();
    }

    @Override
    public boolean onBackPressed() {
        if (theGameEngine.isRunning()) {
            pauseGameAndShowPauseDialog();
            return true;
        }
        return false;
    }

    public void pauseGameAndShowPauseDialog() {
        theGameEngine.pauseGame();
        pauseButton.setVisibility(View.GONE);
        pauseLayout.setVisibility(View.VISIBLE);
    }

    public void resumeGame() {
        Log.i("Game Resumed", "Game Resumed");
        theGameEngine.resumeGame();
        pauseButton.setVisibility(View.VISIBLE);
        pauseLayout.setVisibility(View.GONE);
    }

    public void goToMainMenu() {
        Log.i("Go To Main Menu", "Go To Main Menu");
        pauseButton.setVisibility(View.VISIBLE);
        pauseLayout.setVisibility(View.GONE);
        theGameEngine.stopGame();
        ((ScaffoldActivity) getActivity()).navigateBack();
    }
}
