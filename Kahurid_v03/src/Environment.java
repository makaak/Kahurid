
public class Environment {

	private int tuul;
	private float G;
	private int min;
	private int max;
	private float tihedus;

	public Environment(int min, int max, float G){
		tuul = (int)(Math.round(Math.random()*(max - min) + min));
		this.G = G;
		this.min = min;
		this.max = max;
		tihedus = 1;
	}

	public void generateTuul(int min, int max){
		tuul = (int)(Math.round(Math.random()*(max - min) + min));
	}

	public void generateTuul(){
		tuul = (int)(Math.round(Math.random()*(max - min) + min));
	}

	public float getG(){
		return G;
	}

	public int getTuul(){
		return tuul;
	}
	
	public float getTihedus(){
		return tihedus;
	}
	
	public void decTihedus(){
		tihedus = tihedus - 0.1f;
	}
	
	public void incTihedus(){
		tihedus = tihedus + 0.1f;
	}
	
	public void resetTihedus(){
		tihedus = 1;
	}
}