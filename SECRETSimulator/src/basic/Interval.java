package basic;

public class Interval {
	private int lowerBound;
	private int upperBound;
	
	public Interval(int lowerBound, int upperBound) {
		super();
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}
	
	public int getLowerBound() {
		return lowerBound;
	}



	public void setLowerBound(int lowerBound) {
		this.lowerBound = lowerBound;
	}



	public int getUpperBound() {
		return upperBound;
	}



	public void setUpperBound(int upperBound) {
		this.upperBound = upperBound;
	}

	
	public String print(int intervalType)
	{

		if(intervalType == 0)
		{
			//lower excluded
			return "("+lowerBound+","+upperBound+"]";
		}
		else if(intervalType == 1)
		{
			//upper excluded
			return "["+lowerBound+","+upperBound+")";
		}
		else if(intervalType == 2)
		{
			//closed interval
			return print();
		}
		
		return "ERROR";
	}

	public String print()
	{
		if(lowerBound < 0 && upperBound < 0)
		{
			return "0";
		}
		else if(lowerBound>0 && upperBound > 0){
			return "["+lowerBound+","+upperBound+"]";
		}
		else{
			return "ERROR";
		}
		
	}
}
