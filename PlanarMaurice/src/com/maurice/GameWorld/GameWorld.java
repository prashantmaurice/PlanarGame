package com.maurice.GameWorld;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.maurice.GameObjects.Ball;
import com.maurice.GameObjects.Cluster;
import com.maurice.GameObjects.ConnectLine;
import com.maurice.GameObjects.Pin;
import com.maurice.GameObjects.Wheel;
import com.maurice.planar.PlanarGame;


public class GameWorld{
	
	private int midPointX;
    private int midPointY;
    private int gameHeight;
    private int gameWidth;
    private int pinRadius;
    PlanarGame game;
    private float runtime=0;
    
	Random rand = new Random();
	private ArrayList<Pin> pins = new ArrayList<Pin>();
	private ArrayList<ConnectLine> lines = new ArrayList<ConnectLine>();
	private int score=0;
	public GameWorld(int midPointX,int midPointY, int gameHeight, int gameWidth, PlanarGame game, int level) {
		this.midPointX=midPointX;
		this.midPointY=midPointY;
		this.gameHeight=gameHeight;
		this.gameWidth=gameWidth;
		this.game=game;
		setPinRadius(20);
		try {
            loadMap(level);
        } catch (IOException e) {
            e.printStackTrace();
        }
		System.out.println("level="+level);
		//pins.add(new Pin(midPointX,midPointY));
	}
	public void update(float delta) {
		runtime+=delta;

    }
	public void setLevel(int level) throws IOException{
		loadMap(level);
	}
	public  PlanarGame getGame(){
		return this.game;
	}
	public void move(float pixel) {
	}
	public ArrayList<Pin> getPins() {
		return pins;
	}
	public int getScore(){
		return score;
	}
	public void setScore(int score){
		this.score=score;;
	}
	public void addScore(int x){
		score+=x;
	}
	public int getPinRadius() {
		return pinRadius;
	}
	public void setPinRadius(int pinRadius) {
		this.pinRadius = pinRadius;
	}
	public void loadMap(int level) throws IOException {
			System.out.println("loaded pins level="+level);
	        pins.clear();
	        String levelName="data/levels/level"+level+".txt";
	        FileHandle file = Gdx.files.internal(levelName);
	        String text = file.readString();
	        Scanner reader=new Scanner(text);
	        
	        int numPins = reader.nextInt();
	        for(int i=0;i<numPins;i++){	        	
	        	pins.add(new Pin(reader.nextInt()*gameWidth/100,reader.nextInt()*gameHeight/100));
	        }
	        int numLines = reader.nextInt();
	        for(int i=0;i<numLines;i++){	        	
	        	lines.add(new ConnectLine(reader.nextInt(),reader.nextInt()));
	        }

	}
	public ArrayList<ConnectLine> getLines(){
		return lines;
	}
	public void checkIntersection(){
		for (int i = 0; i < lines.size(); i++){
			ConnectLine m = (ConnectLine) lines.get(i);
			m.setColor(Color.GREEN);
		}
		for (int i = 0; i < lines.size(); i++)
			for (int j = i+1; j < lines.size(); j++){
				ConnectLine m = (ConnectLine) lines.get(i);
				ConnectLine n = (ConnectLine) lines.get(j);
				if(intersectLines(
						((Pin) pins.get(m.a)).getPosition().x,
						((Pin) pins.get(m.a)).getPosition().y,
						((Pin) pins.get(m.b)).getPosition().x,
						((Pin) pins.get(m.b)).getPosition().y,
						((Pin) pins.get(n.a)).getPosition().x,
						((Pin) pins.get(n.a)).getPosition().y,
						((Pin) pins.get(n.b)).getPosition().x,
						((Pin) pins.get(n.b)).getPosition().y)){
					m.setColor(Color.RED);
					n.setColor(Color.RED);
				}
		}
		for (int i = 0; i < lines.size(); i++){
			ConnectLine m = (ConnectLine) lines.get(i);
			//m.setColor(Color.GREEN);
		}
	}
	/** Intersects the two lines and returns the intersection point in intersection.
	 * @param intersection The intersection point, or null.
	 * @return Whether the two lines intersect */
	public static boolean intersectLines (float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4
		) {
		float s1_x, s1_y, s2_x, s2_y;
	    s1_x = x2 - x1;     s1_y = y2 - y1;
	    s2_x = x4 - x3;     s2_y = y4 - y3;

	    float s, t;
	    s = (-s1_y * (x1 - x3) + s1_x * (y1 - y3)) / (-s2_x * s1_y + s1_x * s2_y);
	    t = ( s2_x * (y1 - y3) - s2_y * (x1 - x3)) / (-s2_x * s1_y + s1_x * s2_y);
	    //System.out.println("det="+s+""+t);
	    if (s > 0 && s < 1 && t > 0 && t < 1)
	    {
	        // Collision detected
	        return true;
	    }
	    return false; // No collision
	}
	public boolean checkFinished(){
		System.out.println("checkFinish Called...!");
		if(lines.size()==0) return false;
		for (int i = 0; i < lines.size(); i++){
			ConnectLine m = (ConnectLine) lines.get(i);
			if(m.getColor()!=Color.GREEN){
				return false;
			}
		}
		return true;
	}
	public float getRuntime() {
		return runtime;
	}
	public void setRuntime(float runtime) {
		this.runtime = runtime;
	}
	

}
