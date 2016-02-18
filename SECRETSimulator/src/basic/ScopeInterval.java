package basic;

public class ScopeInterval {
	private float lowerBound;
	private float upperBound;
	private float epsilon = (float)0.1;
	
	
	public ScopeInterval(float lowerBound, float upperBound) {
		super();
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}
	
	public float getSize()
	{
		int size = (int)(upperBound*10 - lowerBound*10);
		size = size + (int)(epsilon*10);
		return size/10;
	}
	
	
	public float getLowerBound() {
		return lowerBound;
	}



	public void setLowerBound(float lowerBound) {
		this.lowerBound = lowerBound;
	}



	public float getUpperBound() {
		return upperBound;
	}



	public void setUpperBound(float upperBound) {
		this.upperBound = upperBound;
	}



	public String print()
	{		
		if(lowerBound > upperBound){
			return "ERROR: "+"["+lowerBound+","+upperBound+"]";
		}
		else
		{
			return "["+lowerBound+","+upperBound+"]";
		}
	}
}
