package com.maurice.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.maurice.GameWorld.GameRenderer;
import com.maurice.GameWorld.GameWorld;
import com.maurice.GameHelpers.MyInputGestureListener;
import com.maurice.planar.PlanarGame;

public class RestartScreen implements Screen {
	 

    PlanarGame game; // Note it's "GyroGame" not "Game"
    private SpriteBatch batch;
    private BitmapFont font;
    float screenWidth;
    float screenHeight;
    int yourScore;
    int highScore;
    //String highScoreline;
    //private int score=0;//useful for final display of score only
    // constructor to keep a reference to the main Game class
     public RestartScreen(PlanarGame planarGame){
            this.game = planarGame;
            yourScore=game.gameScreen.getScore();
            System.out.println("text");
            screenWidth = Gdx.graphics.getWidth();
     		screenHeight = Gdx.graphics.getHeight();
     		batch = new SpriteBatch();  //text display
            font = new BitmapFont(Gdx.files.internal("data/devgothic.fnt"));
            font.setColor(Color.WHITE);
            font.setScale((float) 1);
            
            Preferences prefs = Gdx.app.getPreferences( "user" );
            //prefs.putInteger( "Hisg", 1000 );
            highScore=prefs.getInteger("high1");
            if(highScore>yourScore){
            	highScore=yourScore;
            	prefs.putInteger( "high1", yourScore );
            }
     }
     
     @Override
     public void render(float delta) {
             // update and draw stuff
    	 //score=game.gameScreen.getScore();
    	 //yourScore="YOURSCORE = "+score;
    	 //if(gigh)
    	 //highScoreline="HIGHSCORE = "+highScore;
          if (Gdx.input.justTouched()) // use your own criterion here
              {
        	  	//game.gameScreen.setScore(0);
        	  	//game.setLevel(game.getLevel()+1);
        	  	game.nextLevel();
        	  	game.setGameScreen();
        	  	//game.setScreen(new GameScreen(game));
        	  	System.out.println("just touched....!");
              }
          Color bgcolor=colorFromHex(0x29A329);
          Gdx.gl.glClearColor(bgcolor.r, bgcolor.g, bgcolor.b, 1);
          Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
          //add score
          batch.begin();
          font.setScale((float) 3);
          font.draw(batch, "PLANARITY", 10, screenHeight-20);
          font.setScale((float) 1);
          font.draw(batch, "CONGRATS YOU FINISHED LEVEL "+game.getLevel(), 10, screenHeight*3/4);//text display
          //font.draw(batch, "YOUR SCORE="+yourScore, 10, screenHeight-120);//text display
          //font.draw(batch, "HIGHSCORE="+highScore, 10, screenHeight-160);
          font.draw(batch, "TAP SCREEN TO START NEXT PUZZLE", 10, screenHeight/2);//text display
          font.draw(batch, "DESIGNED BY : MAURICE", 10, (screenHeight/2)-40);//text display
          batch.end();
     }
     public void setScore(int score){
    	 this.yourScore=score;
     }

    @Override
     public void resize(int width, int height) {
     }


    @Override
     public void show() {
          // called when this screen is set as the screen with game.setScreen();
    	System.out.println("mainmenu screen set");
     }


    @Override
     public void hide() {
          // called when current screen changes from this to a different screen
     }


    @Override
     public void pause() {
     }


    @Override
     public void resume() {
     }
    
    @Override
     public void dispose() {
             // never called automatically
     }
	private Color colorFromHex(long hex){
        //float a = (hex & 0xFF000000L) >> 24;
        float r = (hex & 0xFF0000L) >> 16;
        float g = (hex & 0xFF00L) >> 8;
        float b = (hex & 0xFFL);
                        
        return new Color(r/255f, g/255f, b/255f, 255f/255f);
	}
}