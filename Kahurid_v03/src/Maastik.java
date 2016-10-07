import java.awt.Image;
import java.awt.image.MemoryImageSource;



public class Maastik {
	
	private int heightMap[];
	private boolean groundMap[][];
	private int min;
	private int max;
	private double scale;
	private ScreenManager s;
	private int tihedus;
	private Image image;
	private int pix[];
	
	
	public Maastik(int tihedus, ScreenManager s){
		heightMap = new int[tihedus + 1];
		this.s = s;
		this.tihedus = tihedus;
		this.groundMap = new boolean[s.getWidth()][s.getHeight()];
		this.pix = new int[s.getWidth() * s.getHeight()];
		reset();
	}
	
	public void reset(){
		for(int i = 0; i < groundMap.length; i++){
			for(int j = 0; j < groundMap[i].length; j++){
				groundMap[i][j] = false;
			}
		}
	}
	
	public void generateHeightMap(){
		
		heightMap[0] = 0;
		max = 0;
		min = 0;
		
		for(int i = 1; i < heightMap.length; i++){
			heightMap[i]  = heightMap[i - 1] + (int)Math.round(Math.random()*20 - 10);
		}
		
		for(int i = 2; i < 7; i = i + 2){
			for(int j = 0; j < heightMap.length; j++){
				heightMap[j] += (int)(Math.round(Math.random()*i - i/2));
				if(i == 6){
					if(heightMap[j] > max){
						max = heightMap[j];
					}
					if(heightMap[j] < min){
						min = heightMap[j];
					}
				}
			}
		}
 		for(int i = 0; i < heightMap.length; i++){
			if(700/(max - min) > 7){
				heightMap[i] = 7*heightMap[i];
			}else{
				heightMap[i] = (700/(max - min))*heightMap[i];
			}
		}
		
		for(int i = 0; i < 3; i++){
			for(int j = 1; j < heightMap.length - 1; j++){
				heightMap[j] = (heightMap[j - 1] + heightMap[j + 1])/2;
				if(i == 2){
					if(heightMap[j] > max){
						max = heightMap[j];
					}
					if(heightMap[j] < min){
						min = heightMap[j];
					}
				}
			}
		}
		scale = (s.getHeight()-200)*1.0/(max - min);
		for(int i = 0; i < heightMap.length; i++){
			if(scale >= 1){
				heightMap[i] = heightMap[i] + s.getHeight() - max;
			}else{
				heightMap[i] = (int)(scale*heightMap[i] + s.getHeight() - max*scale);
			}
		}
		generateGroundMap();
	}
	
	public void generateGroundMap(){
		for(int i = 0; i < groundMap.length; i++){
			for(int j = 0; j < groundMap[i].length; j++){
				if(getHeightAt(i) == j){
					groundMap[i][j] = true;
					pix[j * s.getWidth() + i] = (120 << 24) | (142 << 16) | (176 << 8);
				}else if(getHeightAt(i) < j){
					groundMap[i][j] = true;
					pix[j * s.getWidth() + i] = (255 << 24) | (142 << 16) | (176 << 8);
					
				}else
					pix[j * s.getWidth() + i] = (255 << 24) | (225 << 16) | (236 << 8) | (242);
			}
		}
	}
	
	public void generateImage(){
		image = s.getFullScreenWindow().createImage(new MemoryImageSource(s.getWidth(), s.getHeight(), pix, 0, s.getWidth()));
	}
	
	public boolean isGroundAt(int x, int y){
		return groundMap[x][y];
	}
	
	public int getMax(){
		return max;
	}
	
	public int getMin(){
		return min;
	}
	
	public double getScale(){
		return scale;
	}
	
	public Image getImage(){
		return image;
	}
	
	public void eraseGroundAt(int x, int y){
		if(x >= 0 && x < s.getWidth() && y >= 0 && y < s.getHeight()){
			groundMap[x][y] = false;
			pix[y * s.getWidth() + x] = (255 << 24) | (225 << 16) | (236 << 8) | (242);
		}
	}
	
	public void createGroundAt(int x, int y){
		if(x >= 0 && x < s.getWidth() && y >= 0 && y < s.getHeight()){
			groundMap[x][y] = true;
			pix[y * s.getWidth() + x] = (255 << 24) | (142 << 16) | (176 << 8);
		}
	}
	
	public int getNextMaxHeightFrom(int x, int y){
		if(y < 0) y = 0;
		if(y >= s.getHeight()) y = s.getHeight() - 1;
		if(x < 0) x = 0;
		if(x >= s.getWidth()) x = s.getWidth() - 1;
		for(int i = y; i < groundMap[x].length; i++){
			if(groundMap[x][i]){
				return i;
			}
		}
		return s.getHeight();
	}
	
	public int getHeightAt(float x){
		int x1 = (int)Math.floor(x/(1/(tihedus*1f/s.getWidth())));
		int x2 = (int)Math.ceil(x/(1/(tihedus*1f/s.getWidth())));
		int a = heightMap[x1];
		int b = heightMap[x2];
		if(a == b){
			return a;
		}else{
			return (int)(Math.abs(x - x1*(1/(tihedus*1f/s.getWidth())))*1f/(x2*(1/(tihedus*1f/s.getWidth())) - x1*(1/(tihedus*1f/s.getWidth())))*b + Math.abs(x - x2*(1/(tihedus*1f/s.getWidth())))*1f/(x2*(1/(tihedus*1f/s.getWidth())) - x1*(1/(tihedus*1f/s.getWidth())))*a);
		}
	}

}
