package params;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import engine.Engine;

import basic.*;

public class TickParam extends Parameter{
	
	private int prevTick; // -infinity
	private Interval myInterval;	// ticking intervals
	private ReportParam report;		//report parameter
		
	/**
	 * Creates tick parameter of the engine
	 * @param values	tick value of the engine: tuple-, time- or batch-driven.
	 * @param reportParam	report parameter of the engine
	 * @param ratio		can be ignored, no longer used
	 */
	public TickParam(Vector values, ReportParam reportParam, int ratio) {
		super(0, values, ratio);
		myInterval = new Interval(0,0);
		this.report = reportParam;
		prevTick = Integer.MIN_VALUE;
	}
	
	/* (non-Javadoc)
	 * @see params.Parameter#eval(int, int, basic.Stream, basic.Query)
	 * application time t is not used in Tick
	 */
	public Object eval(int t, int tau, Stream s, Query w)
	{
		// window contents reported because of this tick
		String content = "";
		// if time-based windows
		if(w.getType()==0)
		{
			//  check whether system ticks
			if (evalTimeBasedWindow(tau,s, w))
			{
				// get the lower and upper boundaries of ticking tapps
				int low = myInterval.getLowerBound();
				int up  = myInterval.getUpperBound();
				String partialRes;
				
				if(Engine.Debug) 
				{
					System.out.print("Tick at tsys: "+tau+ " is yes! Tick values are ");
					if(low == up)
					{
						System.out.println(up);
					}
					else{
						System.out.println(myInterval.print(1));
					}
				}
				
				// call report for tick values
				if(up == low){
					partialRes = (String) report.eval(up, tau, s, w);
					if(partialRes.length()>2)
					{
						content += partialRes+";";
					}
				}
				for(int j=low; j<up; j++)
				{
					partialRes = (String) report.eval(j, tau, s, w);
					if(partialRes.length()>2){
						content += partialRes+";";
					}
				}	
			}
			else
			{
				if(Engine.Debug) 
				{
					System.out.println("Tick at tsys: "+tau+ " is no!");
				}
			}
		}
		// if tuple-based windows
		else if(w.getType()==1)
		{
			// check whether system ticks
			if(evalTupleBasedWindow(tau, s, w))
			{
				// get the lower and upper boundaries of tids belonging this tick
				int low = myInterval.getLowerBound();
				int up  = myInterval.getUpperBound();
				String partialRes;
				
				if(Engine.Debug) 
				{
					System.out.println("Tick at tsys: "+tau+ " is yes! Tick values are "+myInterval.print(1)+" ");
				}
				
				ArrayList<Integer> tids = new ArrayList<Integer>();
				
				// create tid list to be sent to report
				for(int j=low; j<up; j++)
				{
					tids.add(new Integer(j));
				}	
				
				// call report for tids
				partialRes = (String) report.eval(tids, tau, s, w);
				if(partialRes.length()>2){
					content += partialRes+";";
				}
			}
			else
			{
				if(Engine.Debug) 
				{
					System.out.println("Tick at tsys: "+tau+ " is no!");
				}
			}
			
		}	
		// result of this tick evaluation
		return content;
	}
	
	
	/**
	 * Checks whether system ticks in case of tuple-based window if yes 
	 * sets the ticking intervals
	 * @param tau system time
	 * @param s	stream
	 * @param w	window of the query
	 * @return true if system ticks, false if not
	 */
	public boolean evalTupleBasedWindow(int tau, Stream s, Query w)
	{
		boolean shouldTick = true;
		
		// check whether tick is specified
		if(values!=null)
		{
			int num = values.size();
			int t0 = ((Integer)values.get(0)).intValue();
				
			for(int i=1; i<num; i++)
			{
				// if tuple-driven tick
				if (values.get(i) == EnumTick.TupleDriven)
				{
					// we definitely received a new tuple, yes
					
					// get current tid
					int currentTid = s.getTupleAtTsys(tau).getTid();
					
					int prevTid;
					// if prevTid is not set, set it to i0
					if(s.getTupleAtTsys(prevTick)==null)
						prevTid = t0;
					else	// if it was set before, get it 
						prevTid = s.getTupleAtTsys(prevTick).getTid();
					
					// update prevTick
					prevTick = (int)tau;
					// set tick intervals [prevTid, currentTid) 
					myInterval.setUpperBound(currentTid);
					myInterval.setLowerBound(prevTid);
				
				}
				// if time-driven tick
				else if (values.get(i) == EnumTick.TimeDriven)
				{
					// Step 1: Did we receive a new tapp?
					int currentApp = s.getTupleAtTsys(tau).getTapp();
					int prevTapp;
					
					// if prevTapp is not set, set it to t0
					if(s.getTupleAtTsys(prevTick)==null)
						prevTapp = t0;
					else // if it was set before, get it 
						prevTapp = s.getTupleAtTsys(prevTick).getTapp();
					
					// Not a new tapp, no tick
					if(currentApp <= prevTapp)
					{
						return false;
					}
		
					// Step 2: Find tuple-ids from previous tapp to current tapp
					List<Tuple> currentTuples = s.getTuplesAtTapp(currentApp);
					int minTid = Integer.MAX_VALUE;
					for(int j=0; j<currentTuples.size();j++)
					{
						if(currentTuples.get(j).getTid() < minTid)
						{
							minTid = currentTuples.get(j).getTid();
						}
					}
					
					int prevTid;
					if(s.getTupleAtTsys(prevTick)==null)
						prevTid = t0;
					else
						prevTid = s.getTupleAtTsys(prevTick).getTid();
					
					// update prevTick
					prevTick = tau;
					// set tick intervals [prevTid, minTid) where minTid is the minimum Tid inside the tuples of current tapp
					myInterval.setUpperBound(minTid);
					myInterval.setLowerBound(prevTid);
				}
				// if batch-driven tick
				else if (values.get(i) == EnumTick.BatchDriven)
				{
					//Step 1: Did we receive a new batch?
					int currentBid = s.getTupleAtTsys(tau).getBid();
					int currentTid = s.getTupleAtTsys(tau).getTid();
					int prevTid=t0;
					
					int prevBid=0;
					if(s.getTupleAtTsys(prevTick)!=null){
						prevBid = s.getTupleAtTsys(prevTick).getBid();
						prevTid = s.getTupleAtTsys(prevTick).getTid();
					}
					
					if(Engine.Debug)
					{
						System.out.println("Prev batch id "+prevBid+" current batch id "+currentBid);
					}
					
					// no new batch, no tick
					if(currentBid <= prevBid)
					{
						return false;
					}
					
					// Step 2: Find tuple-ids from previous batch to current batch
					
					// find min tuple id inside current batch tuples
					List<Tuple> currentTuples = s.getTupleAtBid(currentBid);
					int minTid = Integer.MAX_VALUE;
					for(int j=0; j<currentTuples.size();j++)
					{
						if(currentTuples.get(j).getTid() < minTid)
						{
							minTid = currentTuples.get(j).getTid();
						}
					}
					// update prevTick
					prevTick = tau;
					// set tick intervals [prevTid, minTid) where minTid is the minimum Tid inside the tuples of current batch
					myInterval.setUpperBound(minTid);
					myInterval.setLowerBound(prevTid);
				}	
			}
			
			return shouldTick;
		}
		
		return false;
		
	}
	
	/**
	 * Checks whether system ticks in case of time-based window and if yes 
	 * sets the ticking intervals
	 * @param tau system time
	 * @param s	stream
	 * @param w window of the query
	 * @return true if system ticks, false if not
	 */
	public boolean evalTimeBasedWindow(int tau, Stream s, Query w) 
	{
	
		boolean shouldTick = true;
	
		// check whether tick is specified
		if(values!=null)
		{
			int num = values.size();
			int t0 = ((Integer)values.get(0)).intValue();
			
			for(int i=1; i<num; i++)
			{
				// if tuple-driven tick
				if (values.get(i) == EnumTick.TupleDriven)
				{
					int currentTapp = s.getTupleAtTsys(tau).getTapp();
					int prevTapp;
					if(s.getTupleAtTsys(prevTick)==null)
					{
						// initial value for prevTapp is t0
						prevTapp = t0;
					}
					else	// get prevTapp
						prevTapp = s.getTupleAtTsys(prevTick).getTapp();
					
					// update prevTick
					prevTick = (int)tau;
					// set tick intervals [prevTapp, currentTapp)
					myInterval.setUpperBound(currentTapp*ratio);
					myInterval.setLowerBound(prevTapp*ratio);
				
				}
				// if time-driven tick
				else if (values.get(i) == EnumTick.TimeDriven)
				{
					// Step 1: Did we receive a new tapp?
					int currentTapp = s.getTupleAtTsys((int)tau).getTapp();
					int prevTapp;
					
					if(s.getTupleAtTsys(prevTick)==null)
						prevTapp = t0;
					else
						prevTapp = s.getTupleAtTsys(prevTick).getTapp();
					
					if(Engine.Debug)
					{
						System.out.println("current-tapp: "+currentTapp+" prev-tapp: "+prevTapp);
					}
					// no new tapp, no tick
					if(currentTapp <= prevTapp)
					{
						return false;
					}
					// update prevTick
					prevTick = tau;
					// set tick intervals [prevTapp, currentTapp)
					myInterval.setUpperBound(currentTapp*ratio);
					myInterval.setLowerBound(prevTapp*ratio);
				}
				// if batch-driven tick
				else if (values.get(i) == EnumTick.BatchDriven)
				{
					// Step 1: Did we receive a new batch?
					int currentBid = s.getTupleAtTsys(tau).getBid();
					int currentTapp = s.getTupleAtTsys(tau).getTapp();
					int prevTapp=t0;
					
					int prevBid=0;
					if(s.getTupleAtTsys(prevTick)!=null){
						prevBid = s.getTupleAtTsys(prevTick).getBid();
						prevTapp = s.getTupleAtTsys(prevTick).getTapp();
					}
					
					if(Engine.Debug)
					{
						System.out.println("Prev batch id "+prevBid+" current batch id "+currentBid);
					}
					// no new batch, no tick
					if(currentBid <= prevBid)
					{
						return false;
					}
					// update prevTick
					prevTick = (int)tau;
					// set tick intervals [prevTapp, currentTapp)
					myInterval.setUpperBound(currentTapp*ratio);
					myInterval.setLowerBound(prevTapp*ratio);
				}	
			}
			
			return shouldTick;
		}
		return false;
	}
	
	static String print(Vector<Tuple> content)
	{
		String res="{";
		int n = content.size();
		for(int i=0; i<n; i++)
		{
			res += content.get(i).print(); 	
			if(i<n-1)
				res += ",";
		}
		res += "}";
		return res;
	}
	
}
	
