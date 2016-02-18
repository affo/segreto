package engine;

import java.util.ArrayList;
import java.util.List;
import params.Parameter;

public abstract class Engine {
	
	
	protected int t0;
	protected int timeRation;
	// SECRET parameters of the engine
	protected List<Parameter> params;
	protected String name;
	protected List<String> result;
	// flag to get more detailed information about execution of the simulator for engines
	public static boolean Debug = false;
	
	public Engine(String name)
	{
		this.name = name;
		result = new ArrayList<String>();
		params=new ArrayList<Parameter>();
	}

	public List<Parameter> getParams() {
		return params;
	}

	public String getName() {
		return name;
	}
	
	public void addResult(String res)
	{
		if(!res.isEmpty())
			result.add(res);
	}
	
	/**
	 * Prints result of the engine 
	 * @return generates the content of the reported windows in the form of t+tuple-id 
	 */
	public String printResult()
	{
		
		String str="Result of "+name+" engine (t0/i0="+ this.t0 +") :{";
		for(int i=0; i<result.size();i++)
		{
			str += result.get(i);
		}
		str += "}";
		return str;
	}
	
	/**
	 * Set known SECRET parameters of the engine 
	 * @param ratio no more used, can be ignored
	 * 
	 */
	protected abstract void init(int ratio);
}
