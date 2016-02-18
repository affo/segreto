package params;

import java.util.Vector;

import basic.Interval;
import basic.ScopeInterval;
import basic.Stream;
import basic.Query;

public class ScopeParam extends Parameter{
	
	private EnumWindowType wtype;
	private int t0;
	private final float epsilon = (float) 0.1;
	
	/**
	 * Creates scope parameter
	 * @param values	values of scopes
	 * @param ratio		can be ignored, no longer used
	 */
	public ScopeParam(Vector values, int ratio) {
		super(3, values, ratio);
		//extract values
		if(values!=null)
		{
			wtype = (EnumWindowType)values.get(1);
			t0 = ((Integer)values.get(2)).intValue();
		}
				
	}

	/* (non-Javadoc)
	 * @see params.Parameter#eval(int, int, basic.Stream, basic.Query)
	 */
	public Object eval(int up, int tau, Stream s, Query w)
	{
		//	time-based windows
		if(w.getType()==0)
		{
			return evalTimeBasedWindow(up,s,w);
		}
		// tuple-based windows
		else if(w.getType()==1)
		{
			return getScopeCountWindow(up,w);
		}	
		
		return null;
	}

	/**
	 * @param t	application time
	 * @param s	stream
	 * @param w window specification of the query
	 * @return
	 */
	public Object evalTimeBasedWindow(int t, Stream s, Query w)
	{
		if(wtype == EnumWindowType.Single)
		{
			return getScopeTimeWindow(t, w);
		}
		else if(wtype == EnumWindowType.Multiple)
		{
			
		}
		
		return null;
	}

	/**
	 * Calculate current scope for tuple-based window at a given tuple id tid
	 * @param tid 	tuple-id
	 * @param w		window specification of the query
	 * @return 		current scope interval if there is, null otherwise
	 */
	public ScopeInterval getScopeCountWindow(int tid, Query w)
	{
		
		int n = (int)Math.ceil((double)(tid-t0-w.getSize())/(double)w.getSlide());
		
		if(n<0)
			n=0;
		
		if(tid<t0)
		{
			return null;
		}
		else
		{
			//Interval myInterval = new Interval(n*w.getSlide()*ratio+t0,t);
			ScopeInterval myInterval = new ScopeInterval(n*w.getSlide()+t0+1,tid); 
			//System.out.println("Scope at time "+t+" is "+myInterval.printInterval());
			return myInterval;
		}	
	
	}
	
	/**
	 * Calculate current scope for time-based window at a given application time t
	 * @param t		application time
	 * @param w		window specification of the query
	 * @return		current scope interval if there is, null otherwise
	 */
	public ScopeInterval getScopeTimeWindow(int t, Query w){
		
		//System.out.println("Scope talks: t is "+t);
		//int n = (int)Math.ceil((double)(t-t0-w.getSize()*ratio+epsilon)/ ((double)w.getSlide()*ratio));
		
		// calculate current window id at application t
		int n = (int)Math.ceil((double)(t-t0-w.getSize())/(double)w.getSlide());
		// minimum window id is 0
		if(n<0)
			n=0;
		// minimum t is t0
		if(t<t0)
		{
			return null;
		}
		else
		{
			//Interval myInterval = new Interval(n*w.getSlide()*ratio+t0,t);
			ScopeInterval myInterval = new ScopeInterval(n*w.getSlide()+t0+epsilon,t); 
			//System.out.println("Scope at time "+t+" is "+myInterval.printInterval());
			return myInterval;
		}	
		
	} 
	
}
