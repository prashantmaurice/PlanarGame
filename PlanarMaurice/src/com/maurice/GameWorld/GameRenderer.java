package com.maurice.GameWorld;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.maurice.GameObjects.Ball;
import com.maurice.GameObjects.Cluster;
import com.maurice.GameObjects.ConnectLine;
import com.maurice.GameObjects.Pin;

public class GameRenderer {
    
    private GameWorld myWorld;
    private OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;
    
    private int midPointX;
    private int midPointY;
    private int gameHeight;
    private int gameWidth;
    //colors-light
    private Color color1=colorFromHex(0xE60000L);//0xaarrggbb
	private Color color2=colorFromHex(0xFF9900L);//orange
	private Color color3=colorFromHex(0x0099FFL);
	private Color color4=Color.WHITE;//health
	private Color[] colors = new Color[]{color1,color2,color3,color4};
	//colors-faded
	private Color color1d=colorFromHex(0xA10000L);//0xaarrggbb
	private Color color2d=colorFromHex(0xB26B00L);
	private Color color3d=colorFromHex(0x005C99L);
	private Color[] colorsfaded = new Color[]{color1d,color2d,color3d};
	
	private SpriteBatch batch, batchTime;
	Texture texture;
	Texture rock;
	int tilesW, tilesH;
	Sprite sprite;
	private TextureRegion region;
	
    private BitmapFont font;
    private int pinRadius ;
    private boolean isCritical=false;
    
    public GameRenderer(GameWorld world, int midPointX,int midPointY,int gameHeight,int gameWidth) {
        myWorld = world;
        this.midPointX=midPointX;
        this.midPointY=midPointY;
        this.gameHeight=gameHeight;
        this.gameWidth=gameWidth;
        this.pinRadius=myWorld.getPinRadius();
        cam = new OrthographicCamera();
        cam.setToOrtho(true, gameWidth, gameHeight);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
        
        batch = new SpriteBatch(); 
        batchTime=new SpriteBatch();
        rock.setEnforcePotImages(false); 
        rock = new Texture("data/bg3.png");
        rock.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        TextureRegion region = new TextureRegion(rock, 0, 0, 320, 480);
        sprite = new Sprite(region);
        //sprite.setSize(320,480);
        //rock.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
        
        sprite.setSize(this.gameWidth, this.gameHeight);
        
        
        
       // rock = new Texture("data/rock.png");//use this for repeated tiles
        //rock.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
        //batch = new SpriteBatch();  //text display
        //texture = new Texture(Gdx.files.internal("data/bg.png"));
        //region = new TextureRegion(texture, 20, 20, 50, 50);
        //sprite=new Sprite(texture);
        //sprite.setSize(gameHeight,gameWidth);
        
        font = new BitmapFont(Gdx.files.internal("data/devgothic.fnt"));
        font.setColor(Color.WHITE);
        font.setScale((float) 1);
        //font.setScale(2);
    }

    public void render() {
    	//System.out.println("GameRenderer - render");

        // 1. We draw a black background. This prevents flickering.
        Gdx.gl.glClearColor(1,1,1, 1);//background
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);// This cryptic line clears the screen.
        //Gdx.gl.glEnable(GL10.GL_BLEND);//enable alpha
		//Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		//batch.begin();
		//sprite.draw(batch);
		//batch.end();
        // Tells shapeRenderer to begin drawing filled shapes
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        sprite.draw(batch);
        //batch.draw(rock, 0, 0,rock.getWidth() * tilesW, rock.getHeight() * tilesH, 
          //0, tilesH, tilesW, 0);//use this for repeated tiles
        //sprite.draw(batch);
        batch.end();  
        
        
        
        
		shapeRenderer.begin(ShapeType.Line);
      //draw lines-------------------
		myWorld.checkIntersection();//refresh lines colors
        ArrayList lines = myWorld.getLines();
        ArrayList pins = myWorld.getPins();
        //Color tempColor;
		for (int i = 0; i < lines.size(); i++) {
			ConnectLine l = (ConnectLine) lines.get(i);
			
			shapeRenderer.setColor(Color.BLACK);
			shapeRenderer.line(
					(float) ((Pin) pins.get(l.a)).getPosition().x, 
					(float) ((Pin) pins.get(l.a)).getPosition().y,
					(float) ((Pin) pins.get(l.b)).getPosition().x,
					(float) ((Pin) pins.get(l.b)).getPosition().y,
					l.getColor(), l.getColor());
			int lineWidth = 5; // pixels
		    Gdx.gl10.glLineWidth(lineWidth / cam.zoom);
								
			//System.out.println("lineIntersect="+l.getColor()+"=="+i);
			//shapeRenderer.line((float)1,(float)1,(float)100,(float)100);
	        //System.out.println("pinned at"+c.getPosition());
		}
		//System.out.println("--------------------");
		shapeRenderer.end();
		shapeRenderer.begin(ShapeType.Filled);
		
        //draw pins-------------------
		for (int i = 0; i < pins.size(); i++) {
			Pin c = (Pin) pins.get(i);			
			shapeRenderer.setColor(Color.GREEN);
	        shapeRenderer.circle(c.getPosition().x, c.getPosition().y , pinRadius);
	        shapeRenderer.setColor(Color.BLACK);
	        shapeRenderer.circle(c.getPosition().x, c.getPosition().y , pinRadius-4);
			if(c.isHighlight()){
				shapeRenderer.setColor(Color.YELLOW);				
				shapeRenderer.circle(c.getPosition().x, c.getPosition().y , pinRadius);
			}
	        //System.out.println("pinned at"+c.getPosition());
		}
        //print curr time---------------------
		
        float time=myWorld.getRuntime();
        
        shapeRenderer.end();
        batchTime.begin();
        font.draw(batchTime, timeString(time), 10, gameHeight-20);
        batchTime.end();


    }
    private String timeString(float time) {
		String temp="";
		int temp2=0;
		temp2=(int)time/3600;
		temp+=((temp2<10)?"0":"")+temp2+":";//hours
		temp2=(int)time/60;
		temp+=((temp2<10)?"0":"")+temp2+":";//minutes
		temp2=(int)time%60;
		temp+=((temp2<10)?"0":"")+temp2;//seconds
		//temp2=(int)(time*100)%60;
		//temp+=((temp2<10)?"0":"")+temp2;//milliseconds
		return temp;
	}

	private Color colorFromHex(long hex){
            //float a = (hex & 0xFF000000L) >> 24;
            float r = (hex & 0xFF0000L) >> 16;
            float g = (hex & 0xFF00L) >> 8;
            float b = (hex & 0xFFL);
                            
            return new Color(r/255f, g/255f, b/255f, 255f/255f);
    }
    public void resize(int width, int height) {  
    	  tilesW = width / rock.getWidth() + 1;
    	  tilesH = height / rock.getHeight() + 1;    
    }
}