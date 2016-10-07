import java.awt.*;
import java.util.ArrayList;

//import Menu.Nupp;

public class Menu {
	
	private ArrayList<Nupp> nupud;
	private int tase;
	private boolean visible;
	
	public Menu(int tase, boolean visible){
		nupud = new ArrayList<Nupp>();
		this.tase = tase;
		this.visible = visible;
	}
	
	public Menu(int tase){
		this(tase, false);
	}
	
	public Menu(){
		this(-1,false);
	}
	
	public void addNupp(Image defImg, Image hoverImg, Image pressedImg, int x, int y, boolean task, String taskString){
		nupud.add(new Nupp(defImg, hoverImg, pressedImg, x, y, task, taskString));
	}
	
	public void addNupp(Image defImg, Image hoverImg, Image pressedImg, int x, int y){
		nupud.add(new Nupp(defImg, hoverImg, pressedImg, x, y));
	}
	
	public int getHulk(){
		return nupud.size();
	}
	
	public Nupp getNupp(int index){
		return (Nupp)nupud.get(index);
	}
	
	public Image getDefImage(int i){
		return getNupp(i).defImg;
	}
	
	public Image getHoverImage(int i){
		return getNupp(i).hoverImg;
	}
	
	public Image getPressedImage(int i){
		return getNupp(i).pressedImg;
	}
	
	public int getX(int i){
		return (int)getNupp(i).getX();
	}
	
	public int getY(int i){
		return (int)getNupp(i).getY();
	}
	
	public boolean isNuppActive(int i){
		return getNupp(i).active;
	}
	
	public void activateNupp(int i){
		getNupp(i).activate();
	}
	
	public void deActivateNupp(int i){
		getNupp(i).deActivate();
	}
	
	public int getActiveNuppIndex(){
		for(int i = 0; i < nupud.size(); i++){
			if(isNuppActive(i)){
				return i;
			}
		}
		return -1;
	}
	
	public boolean isNuppPressed(int i){
		return getNupp(i).pressed;
	}
	
	public void pressNupp(int i){
		getNupp(i).press();
	}
	
	public void dePressNupp(int i){
		getNupp(i).dePress();
	}
	
	public int getPressedNuppIndex(){
		for(int i = 0; i < nupud.size(); i++){
			if(isNuppPressed(i)){
				return i;
			}
		}
		return -1;
	}
	
	public int getTase(){
		return tase;
	}
	
	public boolean isVisible(){
		return visible;
	}
	
	public void setVisible(boolean visible){
		this.visible = visible;
	}

	public boolean hasNuppTask(int i){
		return getNupp(i).task;
	}
	
	public String getNuppTaskString(int i){
		return getNupp(i).taskString;
	}
	
	public class Nupp extends Point{
		
		Image defImg;
		Image hoverImg;
		Image pressedImg;
		boolean active;
		boolean pressed;
		boolean task;
		String taskString;
		
		Nupp(Image defImg, Image hoverImg, Image pressedImg, int x, int y, boolean task, String taskString){
			super(x, y);
			this.defImg = defImg;
			this.hoverImg = hoverImg;
			this.pressedImg = pressedImg;
			this.task = task;
			this.taskString = taskString;
			active = false;
			pressed = false;
		}
		
		Nupp(Image defImg, Image hoverImg, Image pressedImg, int x, int y){
			this(defImg, hoverImg, pressedImg, x, y, false, null);
		}
		
		void activate(){
			active = true;
		}
		
		void deActivate(){
			active = false;
		}
		
		void press(){
			pressed = true;
			active = false;
		}
		
		void dePress(){
			pressed = false;
			active = true;
		}
		
	}

}
