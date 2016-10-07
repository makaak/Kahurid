import java.awt.Image;
import javax.swing.ImageIcon;


public abstract class ImageSprite {
	
	private Image img;
	private float x;
	private float y;
	private float vx;
	private float vy;
	private float ax;
	private float ay;
	private boolean visible;
	
	public ImageSprite(Image img){
		this.img = img;
		visible = false;
	}
	
	public void  update(float timePassed){
		x += ax*timePassed*timePassed/2f + vx * timePassed;
		y += ay*timePassed*timePassed/2f + vy * timePassed;
		vx += timePassed*ax;
		vy += timePassed*ay;
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public void setX(float x){
		this.x = x;
	}
	
	public void setY(float y){
		this.y = y;
	}
	
	public int getWidth(){
		return img.getHeight(null);
	}
	
	public int getHeight(){
		return img.getHeight(null);
	}
	
	public float getVelocityX(){
		return vx;
	}
	
	public float getVelocityY(){
		return vy;
	}
	
	public void setVelocityX(float vx){
		this.vx = vx;
	}
	
	public void setVelocityY(float vy){
		this.vy = vy;
	}
	
	public Image getImage(){
		return img;
	}
	
	public void setAccX(float a){
		this.ax = a;
	}
	
	public float getAccX(){
		return ax;
	}
	
	public void setAccY(float a){
		this.ay = a;
	}
	
	public float getAccY(){
		return ay;
	}
	
	public boolean isVisible(){
		return visible;
	}
	
	public void setVisible(boolean visible){
		this.visible = visible;
	}
}