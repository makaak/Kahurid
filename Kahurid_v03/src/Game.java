/*
 * 
 */



import java.awt.DisplayMode;


public class Game {
	
	private int msg;
	private ScreenManager s;
	private DisplayMode modes[] = {
			new DisplayMode(1600,900,32,0),
			new DisplayMode(1600,900,24,0),
			new DisplayMode(1600,900,16,0),
			new DisplayMode(1024,768,32,0),
			new DisplayMode(1024,768,24,0),
			new DisplayMode(1024,768,16,0),
			new DisplayMode(800,600,32,0),
			new DisplayMode(800,600,24,0),
			new DisplayMode(800,600,16,0),
			new DisplayMode(640,480,32,0),
			new DisplayMode(640,480,24,0),
			new DisplayMode(640,480,16,0)
	};
	private DisplayMode dm;
	private MenuScreen ms;
	private GameScreen gs;
	private boolean running;
	
	public Game(int msg){
		this.msg = msg;
	}
	
	public Game(){
		msg = -1;
	}	
	
	public void setMsg(int msg){
		this.msg = msg;
	}	
	
	void run(){
		try{
			init();
			loop();
		}finally{
			s.restoreScreen();
		}
	}
	
	void init(){
		s = new ScreenManager();
		dm = s.findFirstCompatibleMode(modes);
		s.setFullScreen(dm);
		ms = new MenuScreen(s, this);
		gs = new GameScreen(s, this);
		running = true;
		ms.init();
		gs.init();
		msg = 2;
	}
	
	void loop(){
		
		while(running){
			if(msg == 0){
				msg = -1;
				running = false;
				//System.out.println("exit to windows");
			}
			if(msg == 1){
				msg = -1;
				gs.initGame();
				//System.out.println("enter game");
			}
			if(msg == 2){
				msg = -1;
				ms.initMenu();
				//System.out.println("enter menu");
			}
		}		
	}
	
	public static void main(String[] args){
		new Game().run();
	}
}
