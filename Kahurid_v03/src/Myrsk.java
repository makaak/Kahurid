import java.awt.Image;

import javax.swing.ImageIcon;


public class Myrsk extends ImageSprite{
	
	private int mass;
	private float coef;
	private int exploRadius;
	
	public Myrsk(Image i, int mass, float coef, int exploRadius){
		super(i);
		this.mass = mass;
		this.coef = coef;
		this.exploRadius = exploRadius;
	}
	
	public Myrsk(Image i){
		this(i, 0, 0, 0);
	}
	
	public Myrsk(String imgPath, int mass, float coef, int exploRadius){
		this(new ImageIcon(imgPath).getImage(), mass, coef, exploRadius);
	}
	
	public Myrsk(String imgPath){
		this(imgPath, 0, 0, 0);
	}
	
	public int getMass(){
		return mass;
	}
	
	public float getCoef(){
		return coef;
	}
	
	public int getExploRadius(){
		return exploRadius;
	}
}
