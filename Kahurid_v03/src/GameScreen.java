/*
 * todo:
 * 
 * laskude ajalugu .. done!
 * 
 * trajektoor.... ette näidata /vist ei viitsi.../
 */


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
public class GameScreen implements KeyListener{

	private ScreenManager s;
	private Game game;
	private boolean running;
	private int GAME_FPS = 60;
	private int LAIUS = 8;
	private Environment env;
	private Maastik maastik;
	private Image groundImage;
	private Player[] players;
	private String Tuul;
	private boolean gameOver;
	private int n;
	private int aP;
	private boolean showHistory;
	private boolean trajektoor;

	long startingTime;
	long totalTime;
	long timePassed;

	public GameScreen(ScreenManager s, Game game){
		this.s = s;
		this.game = game;
	}

	int sign(double x){
		if (x>=0){
			return 1;
		}
		if (x<0){
			return -1;
		}
		return 0;
	}

	public void keyPressed(KeyEvent e){
		int keyCode = e.getKeyCode();
		update(keyCode);
	}

	public void keyReleased(KeyEvent e){
		if(e.getKeyCode() == KeyEvent.VK_TAB) showHistory = false;
		e.consume();
	}

	public void keyTyped(KeyEvent e){
		e.consume();
	}

	void exit(){
		running = false;
		game.setMsg(2);
	}

	void init(){
		maastik = new Maastik(s.getWidth()/LAIUS, s);
		env = new Environment(-30, 30, 9.8f);
		n = 2;
		aP = 0;
		players = new Player[n];
		players[0] = new Player(false);
		players[1] = new Player(false);
		//players[2] = new Player(false);

		players[0].setX(56);
		players[1].setX(s.getWidth() - 56);
		//players[2].setX(s.getWidth()/2);
		players[aP % n].setActive(true);
		Window w = s.getFullScreenWindow();
		w.setFocusTraversalKeysEnabled(false);
		w.addKeyListener(this);
	}

	void initGame(){
		players[0].setNurk(90);
		players[1].setNurk(90);
		//players[2].setNurk(90);
		maastik.generateHeightMap();
		maastik.generateImage();
		groundImage = maastik.getImage();
		running = true;
		gameOver = false;
		showHistory = false;
		for(int i = 0; i < players.length; i++) players[i].resetAjalugu();
		trajektoor = false;
		gameLoop();
	}


	void gameLoop(){
		startingTime = System.currentTimeMillis();
		totalTime = startingTime;
		while(running){
			timePassed = System.currentTimeMillis() - totalTime;
			totalTime += timePassed;
			update(timePassed);
			draw();
			s.update();			
			/*try{
				Thread.sleep(1000/GAME_FPS);
			}catch(Exception ex){}*/
		}
	}

	void update(int keyCode){
		if(keyCode == KeyEvent.VK_ESCAPE){
			exit();
		}
		if(!gameOver){
			if(keyCode == KeyEvent.VK_MINUS){
				env.decTihedus();
			}
			if(keyCode == KeyEvent.VK_PLUS){
				env.incTihedus();
			}
			if(keyCode == KeyEvent.VK_TAB){
				showHistory = true;
			}
			if(keyCode == KeyEvent.VK_T){
				trajektoor = !trajektoor;
			}
			if(keyCode == KeyEvent.VK_ENTER){
				env.resetTihedus();
				maastik.reset();
				maastik.generateHeightMap();
				maastik.generateImage();
				groundImage = maastik.getImage();
				env.generateTuul();
				//updateMaastik();
			}
			if(keyCode == KeyEvent.VK_UP){
				for(int i = 0; i < players.length; i ++){
					if(players[i].isActive())
						players[i].setKiirus(players[i].getKiirus() + 1);
				}
			}
			if(keyCode == KeyEvent.VK_DOWN){
				for(int i = 0; i < players.length; i ++){
					if(players[i].isActive())
						if(players[i].getKiirus() > 0)
							players[i].setKiirus(players[i].getKiirus() - 1);
				}
			}
			if(keyCode == KeyEvent.VK_LEFT){
				for(int i = 0; i < players.length; i ++){
					if(players[i].isActive())
						if(!isInGround(i, players[i].getNurk() + 1))
							players[i].setNurk(players[i].getNurk() + 1);
				}
			}
			if(keyCode == KeyEvent.VK_RIGHT){
				for(int i = 0; i < players.length; i ++){
					if(players[i].isActive())
						if(!isInGround(i, players[i].getNurk() - 1))
							players[i].setNurk(players[i].getNurk() - 1);

				}
			}
			if(keyCode == KeyEvent.VK_SPACE){
				trajektoor = false;
				for(int i = 0; i < players.length; i++){
					if(players[i].isActive() /*&& !players[i].getMyrsk().isVisible()*/){
						players[i].getMyrsk().setVisible(true);
						players[i].getMyrsk().setX((int)(players[i].getX() + 30*Math.cos(Math.toRadians(players[i].getNurk()))) - players[i].getMyrsk().getWidth()/2);
						players[i].getMyrsk().setY((int)(maastik.getNextMaxHeightFrom(players[i].getX(), 0) - 30*Math.sin(Math.toRadians(players[i].getNurk()))) - players[i].getMyrsk().getHeight()/2);
						players[i].getMyrsk().setVelocityX((float)(Math.cos(Math.toRadians(players[i].getNurk()))*players[i].getKiirus()));
						players[i].getMyrsk().setVelocityY(-(float)(Math.sin(Math.toRadians(players[i].getNurk()))*players[i].getKiirus()));
						players[i].getMyrsk().setAccX(sign(env.getTuul() - players[i].getMyrsk().getVelocityX())*(players[i].getMyrsk().getCoef()*env.getTihedus()*players[i].getMyrsk().getVelocityX()*players[i].getMyrsk().getVelocityX())/players[i].getMyrsk().getMass());
						players[i].getMyrsk().setAccY(env.getG() + sign(-players[i].getMyrsk().getVelocityY())*players[i].getMyrsk().getCoef()*env.getTihedus()*players[i].getMyrsk().getVelocityY()*players[i].getMyrsk().getVelocityY()/players[i].getMyrsk().getMass());
						players[i].addAjalugu(env.getTuul());
					}
				}
			}
		}
	}

	void update(long timePassed){
		for(int i = 0; i < players.length; i++){
			if(players[i].getMyrsk().isVisible()){
				players[i].getMyrsk().update(timePassed/250f);
				players[i].getMyrsk().setAccX(sign(env.getTuul() - players[i].getMyrsk().getVelocityX())*(players[i].getMyrsk().getCoef()*env.getTihedus()*(players[i].getMyrsk().getVelocityX() - env.getTuul())*(players[i].getMyrsk().getVelocityX() - env.getTuul()))/players[i].getMyrsk().getMass());
				players[i].getMyrsk().setAccY(env.getG() + sign(-players[i].getMyrsk().getVelocityY())*players[i].getMyrsk().getCoef()*env.getTihedus()*players[i].getMyrsk().getVelocityY()*players[i].getMyrsk().getVelocityY()/players[i].getMyrsk().getMass());
				if(((players[i].getMyrsk().getY() + players[i].getMyrsk().getHeight()) >= maastik.getNextMaxHeightFrom((int)(players[i].getMyrsk().getX() + players[i].getMyrsk().getWidth()/2), (int)(players[i].getMyrsk().getY() + players[i].getMyrsk().getHeight()/2)))  ||
						players[i].getMyrsk().getY() <= -10000 || players[i].getMyrsk().getX() <= -1*s.getWidth() || players[i].getMyrsk().getX() >= 2*s.getWidth() || players[i].getMyrsk().getY() >= s.getHeight()){
					players[i].getMyrsk().setVelocityX(0);
					players[i].getMyrsk().setVelocityY(0);
					players[i].getMyrsk().setAccX(0);
					players[i].getMyrsk().setAccY(0);
					players[i].getMyrsk().setVisible(false);
					explode((int)(players[i].getMyrsk().getX() + players[i].getMyrsk().getWidth()/2), (int)(players[i].getMyrsk().getY() + players[i].getMyrsk().getHeight()/2), players[i].getMyrsk().getExploRadius());
					if(whoWon() != -2) gameOver = true;
					do{
						players[aP % n].setActive(false);
						aP++;
						players[aP % n].setActive(true);
					}while(!players[aP % n].inGame);
					maastik.generateImage();
					groundImage = maastik.getImage();
					env.generateTuul();
				}
			}
		}
	}

	boolean isInGround(int activePlayer, int nurk){
		for(int i = 5; i < 30; i++){
			if(maastik.isGroundAt((int)(players[activePlayer].getX() + i*Math.cos(Math.toRadians(nurk))), (int)(maastik.getNextMaxHeightFrom(players[activePlayer].getX(), 0) - i*Math.sin(Math.toRadians(nurk)))))
				return true;
		}
		return false;
	}

	int whoWon(){
		int a = 0;
		int b = 0;
		for(int i = 0; i < players.length; i++){
			if(players[i].inGame && !players[i].AI) a++;
			if(players[i].inGame && players[i].AI) b++;
		}
		if(a == 1 && b == 0){
			for(int i = 0; i < players.length; i++){
				if(players[i].inGame) return i;
			}
		}
		if(a == 0) return -1;
		return -2;
	}

	void explode(int x, int y, int r){
		for(int i = 0; i < 2*r + 1; i++){
			for(int j = 0; j < 2*r + 1; j++){
				if(Math.sqrt(Math.pow(i - r, 2) + Math.pow(j - r, 2)) <= r){
					for(int k = 0; k < players.length; k++){
						if(x + j - r == players[k].getX() && y + i - r == maastik.getNextMaxHeightFrom(players[k].getX(), 0)){
							players[k].inGame = false;
						}
					}
					maastik.eraseGroundAt(x + j - r, y + i  - r);
				}
			}
		}
	}


	void draw(){
		Graphics2D g = s.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//g.setColor(new Color(250, 250, 250));
		//g.fillRect(0, 0, s.getWidth(), s.getHeight());
		g.drawImage(groundImage, 0, 0, null);
		g.setColor(Color.BLACK);
		g.setFont(new Font(g.getFont().getName(), Font.PLAIN, 18));
		Tuul = "";
		if(env.getTuul() > 0){
			for(int i = 0; i < env.getTuul(); i++){
				Tuul += ">";
			}
		}else{
			for(int i = 0; i > env.getTuul(); i--){
				Tuul += "<";
			}
		}
		g.drawString(Tuul, s.getWidth()/2 - Tuul.length()*11/2, 70);
		g.drawString(Integer.toString(env.getTuul()),s.getWidth()/2 - Tuul.length()*11/2, 55);
		g.drawString(Double.toString(env.getTihedus()), 10, 15);
		for(int i = 0; i < players.length; i++){
			if(players[i].isActive()){
				g.setColor(Color.BLACK);
			}else{
				g.setColor(Color.RED);
			}

			// test rotate
			//g.rotate(-1*Math.toRadians(players[i].getNurk()), players[i].getX(), maastik.getNextMaxHeightFrom(players[i].getX(), 0));
			//g.drawLine(players[i].getX(), maastik.getNextMaxHeightFrom(players[i].getX(), 0), players[i].getX() + 30, maastik.getNextMaxHeightFrom(players[i].getX(), 0));
			//g.rotate(Math.toRadians(players[i].getNurk()), players[i].getX(), maastik.getNextMaxHeightFrom(players[i].getX(), 0));
			if(players[i].inGame){
				g.drawLine(players[i].getX(), maastik.getNextMaxHeightFrom(players[i].getX(), 0), (int)(players[i].getX() + 30*Math.cos(Math.toRadians(players[i].getNurk()))), (int)(maastik.getNextMaxHeightFrom(players[i].getX(), 0) - 30*Math.sin(Math.toRadians(players[i].getNurk()))));
				g.drawString("N: " + Integer.toString(players[i].getNurk()), players[i].getX(), maastik.getNextMaxHeightFrom(players[i].getX(), 0) - 75);
				g.drawString("K: " + Integer.toString(players[i].getKiirus()), players[i].getX(), maastik.getNextMaxHeightFrom(players[i].getX(), 0) - 100);
				if(players[i].getMyrsk().isVisible()){
					g.setColor(Color.BLACK);
					if(players[i].getMyrsk().getX() < 0)
						g.drawString(Integer.toString((int)players[i].getMyrsk().getX()), 5, Math.max(players[i].getMyrsk().getY(), 30));
					if(players[i].getMyrsk().getX() >= s.getWidth())
						g.drawString(Integer.toString((int)players[i].getMyrsk().getX()), s.getWidth() - Integer.toString((int)players[i].getMyrsk().getX()).length()*11 - 5, Math.max(players[i].getMyrsk().getY(), 30));
					if(players[i].getMyrsk().getY() < 0)
						g.drawString(Integer.toString((int)players[i].getMyrsk().getY()), Math.min(Math.max(players[i].getMyrsk().getX(), 50), s.getWidth() - 50), 15);
					g.drawImage(players[i].getMyrsk().getImage(), Math.round(players[i].getMyrsk().getX()), Math.round(players[i].getMyrsk().getY()), null);
				}
			}
			/*if(trajektoor && players[i].isActive()){

				float tx = (int)(players[i].getX() + 30*Math.cos(Math.toRadians(players[i].getNurk()))) - players[i].getMyrsk().getWidth()/2;
				float ty = (int)(maastik.getNextMaxHeightFrom(players[i].getX(), 0) - 30*Math.sin(Math.toRadians(players[i].getNurk()))) - players[i].getMyrsk().getHeight()/2;
				float tax = sign(env.getTuul() - players[i].getMyrsk().getVelocityX())*(players[i].getMyrsk().getCoef()*(players[i].getMyrsk().getVelocityX() - env.getTuul())*(players[i].getMyrsk().getVelocityX() - env.getTuul()))/players[i].getMyrsk().getMass();
				float tay =env.getG() + sign(-players[i].getMyrsk().getVelocityY())*players[i].getMyrsk().getCoef()*players[i].getMyrsk().getVelocityY()*players[i].getMyrsk().getVelocityY()/players[i].getMyrsk().getMass();
				float tvx = (float)(Math.cos(Math.toRadians(players[i].getNurk()))*players[i].getKiirus());
				float tvy = -(float)(Math.sin(Math.toRadians(players[i].getNurk()))*players[i].getKiirus());
				float time = 0.001f;

				for(int j = 0; j < 5000; j++){
					if(j % 100 == 0)	g.fillRect((int)tx, (int)ty, 1, 1);
					tx += tax*time*time/2f + tvx * time;
					ty += tay*time*time/2f + tvy * time;
					tvx += time*tax;
					tvy += time*tay;
					tax = sign(env.getTuul() - players[i].getMyrsk().getVelocityX())*(players[i].getMyrsk().getCoef()*(players[i].getMyrsk().getVelocityX() - env.getTuul())*(players[i].getMyrsk().getVelocityX() - env.getTuul()))/players[i].getMyrsk().getMass();
					tay =env.getG() + sign(-players[i].getMyrsk().getVelocityY())*players[i].getMyrsk().getCoef()*players[i].getMyrsk().getVelocityY()*players[i].getMyrsk().getVelocityY()/players[i].getMyrsk().getMass();
					System.out.printf("%f.. %f.. %f.. %f.. %f.. %f..\n", tx, ty, tax, tay, tvx ,tvy);
				}



			}*/
		}
		if(showHistory){
			g.setColor(new Color(0,0,0,175));
			for(int i = 0; i < players.length; i++){
				if(players[i].isActive()){
					if(players[i].getX() + 225 > s.getWidth()){
						g.fillRect(players[i].getX() - 225, maastik.getNextMaxHeightFrom(players[i].getX() - 225, 0) - 300, 225, 110);
						g.setColor(Color.WHITE);
						for(int j = 0; j < players[i].ajalugu.length; j++ ){
							g.drawString(players[i].ajalugu[j], players[i].getX() - 220, maastik.getNextMaxHeightFrom(players[i].getX() - 220, 0) - 280 + j*20);
						}
					}else{
						g.fillRect(players[i].getX(), maastik.getNextMaxHeightFrom(players[i].getX(), 0) - 300, 225, 110);
						g.setColor(Color.WHITE);
						for(int j = 0; j < players[i].ajalugu.length; j++ ){
							g.drawString(players[i].ajalugu[j], players[i].getX() + 5, maastik.getNextMaxHeightFrom(players[i].getX() + 5, 0) - 280 + j*20);
						}
					}
				}
			}
		}
		if(gameOver){
			g.setColor(new Color(0, 0, 0, 185));
			g.fillRect(200, 200, s.getWidth() - 400, s.getHeight() - 400);
			g.setColor(Color.WHITE);
			g.drawString("Võitis " + Integer.toString(whoWon()), 700, 425);
		}
		g.dispose();
	}

	public class Player{


		private boolean AI;
		private boolean active;
		private int nurk;
		private int kiirus;
		private Myrsk m;
		private int X;
		private boolean inGame;
		private String[] ajalugu;

		public Player(boolean AI){
			this.AI = AI;
			this.m = new Myrsk("Media\\Myrsk.png", 50, 0.03f, 25);
			active = false;
			kiirus = 150;
			inGame = true;
			ajalugu = new String[5];
		}


		public void addAjalugu(int tuul){
			for(int i = ajalugu.length - 2; i >= 0; i--){
				ajalugu[i + 1] = ajalugu[i];
			}
			ajalugu[0] = "K: " + Integer.toString(kiirus) + " N: " + Integer.toString(nurk) + " T: " + Integer.toString(tuul);
		}

		public void resetAjalugu(){
			for(int i = 0; i < ajalugu.length; i++) ajalugu[i] = "";
		}

		public void setActive(boolean active){
			this.active = active;
		}

		public boolean isActive(){
			return active;
		}

		public int getKiirus(){
			return kiirus;
		}

		public int getNurk(){
			return nurk;
		}

		public void setKiirus(int kiirus){
			this.kiirus = kiirus;
		}

		public void setNurk(int nurk){
			this.nurk = nurk;
		}

		public int getX(){
			return X;
		}

		public void setX(int X){
			this.X = X;
		}

		public Myrsk getMyrsk(){
			return m;
		}

		public boolean isAI(){
			return AI;
		}

	}
}
