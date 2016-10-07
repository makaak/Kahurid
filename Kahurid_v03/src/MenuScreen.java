/*
 * hover color: R:60 G:231 B: 56
 * 
 * 
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MenuScreen implements KeyListener{

	private ScreenManager s;
	private Game game;
	private boolean inMenu;
	private Menu menus[][] = {
			{new Menu()},
			{null, null}
	};
	private int tase;
	private int MENU_FPS = 30;

	public MenuScreen(ScreenManager s, Game game){
		this.s = s;
		this.game = game;
	}

	public void keyPressed(KeyEvent e){
		int keyCode = e.getKeyCode();
		if(inMenu){
			updateMenu(keyCode);
		}			
		e.consume();
	}

	public void keyTyped(KeyEvent e){
		e.consume();
	}

	public void keyReleased(KeyEvent e){
		e.consume();
	}
	
	void exit(int msg){
		inMenu = false;
		game.setMsg(msg);
	}
	
	void init(){
		Window w = s.getFullScreenWindow();
		w.setFocusTraversalKeysEnabled(false);
		w.addKeyListener(this);
		
		Image defPlay = new ImageIcon("defPlay.png").getImage();
		Image hoverPlay = new ImageIcon("hoverPlay.png").getImage();
		Image pressedPlay = new ImageIcon("pressedPlay.png").getImage();
		Image defExit = new ImageIcon("defExit.png").getImage();
		Image hoverExit = new ImageIcon("hoverExit.png").getImage();
		Image pressedExit = new ImageIcon("pressedExit.png").getImage();
		Image defProfile = new ImageIcon("defProfile.png").getImage();
		Image hoverProfile = new ImageIcon("hoverProfile.png").getImage();
		Image defOptions = new ImageIcon("defOpt.png").getImage();
		Image hoverOptions = new ImageIcon("hoverOpt.png").getImage();

		menus[0][0].addNupp(defPlay, hoverPlay, pressedPlay, 75, 100, true, "play");
		//menus[0][0].addNupp(defProfile, hoverProfile, null, 75, 220);
		//menus[0][0].addNupp(defOptions, hoverOptions, null, 75, 340);
		menus[0][0].addNupp(defExit, hoverExit, pressedExit, 75, 460, true, "exit");
		//menus[1][1].addNupp(defPlay, hoverPlay, pressedPlay, 400, 100);
		//menus[1][1].addNupp(defPlay, hoverPlay, pressedPlay, 400, 220);
		}

	void initMenu(){		
		menus[0][0].setVisible(true);
		menus[0][0].activateNupp(0);
		tase = 1;			
		drawMenu();
		inMenu = true;
		menuLoop();
	}

	void menuLoop(){

		while(inMenu){
			s.update();
			try{
				Thread.sleep(1000/MENU_FPS);
			}catch(Exception ex){}
		}

	}

	void drawMenu(){
		Graphics2D g = s.getGraphics();
		g.setColor(Color.PINK);
		g.fillRect(0, 0, s.getWidth(), s.getHeight());
		for(int i = 0; i < menus.length; i++){
			for(int j = 0; j < menus[i].length; j++){
				if(menus[i][j] != null){
					if(menus[i][j].isVisible()){
						int hulk = menus[i][j].getHulk();
						for(int k = 0; k < hulk; k++){
							if(menus[i][j].isNuppActive(k)){
								g.drawImage(menus[i][j].getHoverImage(k), menus[i][j].getX(k), menus[i][j].getY(k), null);
							}else if(menus[i][j].isNuppPressed(i)){
								g.drawImage(menus[i][j].getPressedImage(k), menus[i][j].getX(k), menus[i][j].getY(k), null);
							}else{
								g.drawImage(menus[i][j].getDefImage(k), menus[i][j].getX(k), menus[i][j].getY(k), null);
							}
						}
					}
				}
			}
		}
		g.dispose();
		System.out.println();
	}

	void updateMenu(int keyCode){

		if(keyCode == KeyEvent.VK_ENTER){
			for(int i = 0; i < menus[tase - 1].length; i++){
				if(menus[tase - 1][i] != null){
					if(menus[tase - 1][i].isVisible()){
						if(menus[tase - 1][i].hasNuppTask(menus[tase - 1][i].getActiveNuppIndex())){
							if(menus[tase - 1][i].getNuppTaskString(menus[tase - 1][i].getActiveNuppIndex()).equals("exit")){
								exit(0);
								break;
							}
							if(menus[tase - 1][i].getNuppTaskString(menus[tase - 1][i].getActiveNuppIndex()).equals("play")){
								exit(1);
								break;
							}
						}
					}
				}
			}
			if(tase < menus.length){
				int sum = 0;
				for(int i = 0; i < menus[tase - 1].length; i++){
					if(menus[tase - 1][i] != null){
						if(menus[tase - 1][i].getActiveNuppIndex() != -1){
							sum += menus[tase - 1][i].getActiveNuppIndex();
							break;
						}else{
							sum += menus[tase - 1][i].getHulk();
						}
					}else{
						sum++;
					}
				}
				if(menus[tase][sum] != null){
					menus[tase][sum].setVisible(true);
					menus[tase][sum].activateNupp(0);
					tase++;
				}				
				drawMenu();
			}

		}
		if(keyCode == KeyEvent.VK_ESCAPE){
			if(tase != 1){
				for(int i = 0; i < menus[tase - 1].length; i++){
					if(menus[tase - 1][i] != null){
						if(menus[tase - 1][i].isVisible()){
							menus[tase - 1][i].setVisible(false);
							menus[tase - 1][i].deActivateNupp(menus[tase - 1][i].getActiveNuppIndex());
							break;
						}
					}
				}
				tase--;
				drawMenu();
			}else{
				//exit();
			}
		}
		if(keyCode == KeyEvent.VK_UP){
			for(int i = 0; i < menus[tase - 1].length; i++){
				if(menus[tase - 1][i] != null){
					if(menus[tase - 1][i].isVisible()){
						if(menus[tase - 1][i].getActiveNuppIndex() == 0){
							menus[tase - 1][i].deActivateNupp(0);
							menus[tase - 1][i].activateNupp(menus[tase - 1][i].getHulk() - 1);
							drawMenu();
							break;
						}else{
							menus[tase - 1][i].activateNupp(menus[tase - 1][i].getActiveNuppIndex() - 1);
							menus[tase - 1][i].deActivateNupp(menus[tase - 1][i].getActiveNuppIndex() + 1);
							drawMenu();
							break;
						}
					}
				}
			}

		}
		if(keyCode == KeyEvent.VK_DOWN){
			for(int i = 0; i < menus[tase - 1].length; i++){
				if(menus[tase - 1][i] != null){
					if(menus[tase - 1][i].isVisible()){
						if(menus[tase - 1][i].getActiveNuppIndex() == menus[tase - 1][i].getHulk() - 1){
							menus[tase - 1][i].deActivateNupp(menus[tase - 1][i].getHulk() - 1);
							menus[tase - 1][i].activateNupp(0);
							drawMenu();
							break;
						}else{
							menus[tase - 1][i].activateNupp(menus[tase - 1][i].getActiveNuppIndex() + 1);
							menus[tase - 1][i].deActivateNupp(menus[tase - 1][i].getActiveNuppIndex());
							drawMenu();
							break;
						}
					}
				}
			}
		}
	}
}
