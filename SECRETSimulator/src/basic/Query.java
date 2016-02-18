package basic;

public class Query {
	 private int size;
	 private int slide;
	 private int type;
	 
	public Query(int size, int slide, int type) {
		super();
		this.size = size;
		this.slide = slide;
		this.type = type;
	}

	public int getSize() {
		return size;
	}

	public int getSlide() {
		return slide;
	}

	public int getType() {
		return type;
	}
}
