package params;

import java.util.ArrayList;
import java.util.Vector;

import engine.Engine;

import basic.Interval;
import basic.ScopeInterval;
import basic.Stream;
import basic.Tuple;
import basic.Query;

public class ReportParam extends Parameter{

	private ContentParam content;	// content parameter
	private int frequency;			// set in case of periodic reporting
	private int prevReportingId;	// previous reporting tapp or tid, needed for content-change 
	private int prevReportingTau;	// previous reporting tsys, needed for content-change

	
	/**
	 * Creates report parameter
	 * @param values	value of the report parameter Rcc, Rwc, Rne, Rpr
	 * @param content	content parameter of the engine
	 * @param ratio		can be ignored, no longer used
	 */
	@SuppressWarnings("unchecked")
	public ReportParam(Vector values, ContentParam content, int ratio) {
		super(1, values, ratio);
		this.content = content;
		prevReportingId = Integer.MIN_VALUE;
		prevReportingTau = Integer.MIN_VALUE;
	}
	
	/**
	 * Creates report parameter in case of periodic reporting
	 * @param values	value of the report parameter Rcc, Rwc, Rne
	 * @param content	content parameter of the engine
	 * @param ratio		can be ignored, no longer used
	 * @param freq		reporting frequency
	 */
	public ReportParam(Vector values, ContentParam content, int ratio, int freq) {
		super(1, values, ratio);
		this.content = content;
		this.frequency = freq;
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see params.Parameter#eval(int, int, basic.Stream, basic.Query)
	 * It is used for time-based windows
	 */
	@Override
	public Object eval(int t, int tau, Stream s, Query w) {
		//	if time-based windows
		if(w.getType()==0)
		{
			// check whether reporting condition is satisfied 
			if(shouldTimeWindowReport(t, tau, s, w))
			{
				// if yes, ask for active window's content from content parameter 
				Vector <Tuple> result = (Vector <Tuple>) content.eval(t, tau, s, w);
				if(result != null)
				{
					if(Engine.Debug)
					{
					
						System.out.println("Report at tapp "+t+": Yes!");
						System.out.println("Content at tapp "+t+" "+print(result));
						System.out.println("Scope at tapp "+t+" "+((ScopeInterval)content.getScope().eval(t, tau, s, w)).print());
				
					}
					// update reporting id (application time) and system time
					prevReportingId = t;
					prevReportingTau = tau;
				}
				// return content of the current window 
				return print(result);
			}
		}
		// tuple-based windows
		else
		{
			throw new IllegalArgumentException
            ("Method must be called only for time-based windows");
		}	
		// empty string if it is not time to report
		return "";
	}
	
	/**
	 * Evaluation of reporting for tuple-based window
	 * 
	 * @param tids	ticking tids sent from ticks
	 * @param tau	system time
	 * @param s		stream
	 * @param w		window of the query
	 * @return	content of the window which will be reported, if reporting condition is met, 
	 * empty string otherwise
	 */
	@SuppressWarnings("unchecked")
	public Object eval(ArrayList<Integer> tids, int tau, Stream s, Query w)
	{
		// if tuple-based windows
		if(w.getType()==1)
		{
			//check whether reporting condition is satisfied
			if(shouldCountWindowReport(tids, tau, s, w))
			{
				// pick a tid
				int tid = pick(giveCandidates(tids, tau, s, w));
				
				// ask the content for picked tid				
				Vector <Tuple> result = (Vector <Tuple>) content.eval(tid, tau, s, w);
				
				// if it returns
				if(result != null)
				{
					if(Engine.Debug)
					{
					
						System.out.println("Report at tid "+tid+": Yes!");
						System.out.println("Content at tid "+tid+" "+print(result));
						System.out.println("Scope at tid "+tid+" "+((ScopeInterval)content.getScope().eval(tid, tau, s, w)).print());
					
					}
					
					prevReportingId = tid;
					prevReportingTau = tau;
				}
				// return content of the window as string
				return print(result);
			}
			else
			{
				if(Engine.Debug)
				{
				
					System.out.println("Report at tids: No!");		
				}
				
			}
		}
		else
		{
			throw new IllegalArgumentException
            ("Method must be called only for tuple-based windows");
		}
		// empty string if it is not time to report
		return "";
		
	}
	

	/**
	 * Returns true if reporting condition of the engine is satisfied for time-based window, 
	 * false otherwise
	 * @param t	application time
	 * @param tau	system time
	 * @param s		stream
	 * @param w		window of the query
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean shouldTimeWindowReport(int t, int tau, Stream s, Query w)
	{
		boolean shouldReport = true;
		
		// check if reporting strategy was set
		if(values!=null)
		{
			int num = values.size();
			for(int i=0; i<num; i++)
			{
				// if content-change is part of reporting strategy
				if (values.get(i) == EnumEval.ContentChange)
				{
					if(Engine.Debug)
					{
						System.out.println("Recent reporting t and tau are as follows: "+prevReportingId + " "+prevReportingTau);
					}
					
					//if contents of the current active window and last reported active windows are the same, return false
					if(compare( (Vector<Tuple>) content.eval(t, tau, s, w), (Vector<Tuple>)content.eval(prevReportingId, prevReportingTau, s, w)))
					{
						return false;
					}
					
					if(Engine.Debug)
					{
						System.out.println("Content is changed at t "+t);
					}
					 
				}
				// if non-empty is part of reporting strategy
				else if (values.get(i) == EnumEval.NonEmpty)
				{
					//check content of the current active window for the emptiness
					if (((Vector <Tuple>) content.eval(t, tau, s, w)).isEmpty())
						return false;
				}
				// if window-close is part of reporting strategy
				else if (values.get(i) == EnumEval.WindowClose)
				{
					//check window-close condition
					ScopeInterval myInterval = (ScopeInterval) content.getScope().eval(t, tau, s, w);
					if ((myInterval.getSize() != w.getSize()*ratio) || t >= s.getTupleAtTsys(tau).getTapp())
						return false;
				}
				// if periodic reporting is part of reporting strategy
				else if (values.get(i) == EnumEval.Periodic)
				{
					//check whether tapp is multiple of frequency
					if(t%frequency!=0)
					{
						return false;
					}
				}
			}
			
			return shouldReport;
		}
		return false;
	}
	
	/**
	 * Returns true if reporting condition of the engine is satisfied for tuple-based window, 
	 * false otherwise
	 * @param tids tuple ids reporting is called
	 * @param tau	system time
	 * @param s		stream	
	 * @param w		window of the query
	 * @return	
	 */
	@SuppressWarnings("unchecked")
	public boolean shouldCountWindowReport(ArrayList<Integer> tids, int tau, Stream s, Query w)
	{
		boolean shouldReport = true;
		String info = "";
		if(Engine.Debug)
		{
			info += "Tids:";	
			
			for(int i=0; i<tids.size();i++)
			{
				int tid = tids.get(i).intValue();
				info += tid + ",";

			}
			System.out.print(info);
		}
		
		// check if reporting strategy was set
		if(values!=null)
		{
			int num = values.size();
			for(int i=0; i<num; i++)
			{
				// if content-change is part of reporting strategy
				if (values.get(i) == EnumEval.ContentChange)
				{
					// content-change is always true as tids can not be true
					
					// pick picks reporting tid
					int tid = pick(tids);
					if(Engine.Debug)
					{
						System.out.println("Previous tid and tau are as follows: "+prevReportingId + " "+prevReportingTau);
					}
					//if contents of the current active window and last reported active windows are the same, return false
					if(compare( (Vector<Tuple>) content.eval(tid, tau, s, w), (Vector<Tuple>)content.eval(prevReportingId, prevReportingTau, s, w)))
					{
						
						shouldReport = false;
						//throw new IllegalArgumentException("Content-change should be always true for tuple-based windows");
						break;
					}
					if(Engine.Debug)
					{
						System.out.println("Content is changed at tid "+tid);
					}
					 
				}
				// if non-empty is part of reporting strategy
				else if (values.get(i) == EnumEval.NonEmpty)
				{
					// non-empty is always true
					//check content for the emptiness			
					int t = pick(tids);
					if (((Vector <Tuple>) content.eval(t, tau, s, w)).isEmpty())
					{
						shouldReport = false;
						//throw new IllegalArgumentException("Non-empty should be always true for tuple-based windows");
						break;
					}
				}
				// if window-close is part of reporting strategy
				else if (values.get(i) == EnumEval.WindowClose)
				{
					// check whether there is any window-closing tuple ids in tid list
					shouldReport = !windowClose(tids, tau, s, w).isEmpty();
					if(!shouldReport)
						break;
				}
				// if periodic reporting is part of reporting strategy
				else if (values.get(i) == EnumEval.Periodic)
				{
					// check whether there is any tuple having id which is multiple of frequency in tid list
					shouldReport = !periodic(tids, tau, s, w).isEmpty();
					if(!shouldReport)
						break;
				}
			}
			
			if(Engine.Debug)
			{
				System.out.println("Reporting condition is "+shouldReport);
			}
			
			return shouldReport;
		}
		return false;
	}
	
	/** 
	 * Returns set of tids which satisfy reporting condition
	 * @param tids	initial list of tids
	 * @param tau	system time
	 * @param s		stream
	 * @param w		window of the query
	 * @return
	 */
	private ArrayList<Integer> giveCandidates(ArrayList<Integer> tids, int tau, Stream s, Query w)
	{
		
		ArrayList<Integer> candidates = null;
		// check if reporting strategy was set
		if(values!=null)
		{
			int num = values.size();
			for(int i=0; i<num; i++)
			{
				// if content-change is part of the reporting strategy
				if (values.get(i) == EnumEval.ContentChange)
				{
					if(candidates == null)
					{
						candidates = tids;
					}
					else
					{
						//get the intersection of the candidates and tids
						candidates = subSet(candidates, tids);
					}
				}
				// if non-empty is part of the reporting strategy
				else if (values.get(i) == EnumEval.NonEmpty)
				{
					if(candidates == null)
					{
						candidates = tids;
					}
					else
					{
						//get the intersection of the candidates and tids
						candidates = subSet(candidates, tids);
					}
					
				}
				// if window-close is part of the reporting strategy
				else if (values.get(i) == EnumEval.WindowClose)
				{
					if(candidates == null)
					{
						//window-closing tuple ids
						candidates = windowClose(tids, tau, s, w);
					}
					else
					{
						//get the intersection of the candidates and window close
						candidates = subSet(candidates, windowClose(tids, tau, s, w));
						
					}
				}
				// if periodic reporting is part of the reporting strategy
				else if (values.get(i) == EnumEval.Periodic)
				{
					if(candidates == null)
					{
						//periodic tuple ids
						candidates = periodic(tids, tau, s, w);
					}
					else
					{
						//get the subset candidates and periodic
						candidates = subSet(candidates, periodic(tids, tau, s, w));
					}
				}
			}
		}
		return candidates;
	}
	
	/**
	 * Returns window-closing tuple ids
	 * 
	 * @param tids	tuple-ids reporting is called
	 * @param tau	system time
	 * @param s		stream
	 * @param w		window of the query
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<Integer> windowClose(ArrayList<Integer> tids, int tau, Stream s, Query w)
	{
		ArrayList<Integer> windowClosingTids = new ArrayList<Integer>();
		String info="";
	
		
		for(int i=0; i<tids.size();i++)
		{
			  
			int tid = tids.get(i).intValue();
			
			ScopeInterval myInterval = (ScopeInterval) content.getScope().eval(tid, tau, s, w);
			
			// check whether i is window close tid
			if((int)myInterval.getSize() == w.getSize()- 1)
			{
				windowClosingTids.add(tids.get(i));
			}
		}
		
		if(Engine.Debug)
		{
			info += "\nWindow Closing Tids: ";
			for(int i=0; i<windowClosingTids.size();i++)
			{
				info += windowClosingTids.get(i).intValue()+",";
			}
			info += "\n";
			
			System.out.print(info);
		}
		
		return windowClosingTids;
	}
	
	/**
	 * Returns periodic reporting tuple ids
	 * 
	 * @param tids	tuple-ids reporting is called
	 * @param tau	system time
	 * @param s		stream
	 * @param w		window of the query
	 * @return
	 */
	private ArrayList<Integer> periodic(ArrayList<Integer> tids, int tau, Stream s, Query w)
	{
		ArrayList<Integer> periodicTids = new ArrayList<Integer>();
		String info="";
	
		
		for(int i=0; i<tids.size();i++)
		{
			  
			int tid = tids.get(i).intValue();
			
			// check whether tid is multiple of frequency
			if(tid%frequency == 0)
			{
				periodicTids.add(tids.get(i));
			}
		}
		
		if(Engine.Debug)
		{
			info += "\nPeriodic Tids: ";
			for(int i=0; i<periodicTids.size();i++)
			{
				info += periodicTids.get(i).intValue()+",";
			}
			info += "\n";
			
			System.out.print(info);
		}
		
		return periodicTids;
	}
	
	/**
	 * Returns max id of the reporting tid candidates
	 * @param reportingCandidates
	 * @return
	 */
	private int pick(ArrayList<Integer> reportingCandidates)
	{
		if(reportingCandidates.size() == 0)
			return -1;
		
		int max = Integer.MIN_VALUE;
		for(int i=0; i<reportingCandidates.size(); i++)
		{
			// picks the most recent candidate which has a higher tid
			if(max < reportingCandidates.get(i).intValue())
			{
				max = reportingCandidates.get(i).intValue();
			}
			
		}
		
		if(Engine.Debug)
		{
			String info= "Pick picks tid = "+max;
			System.out.println(info);
		}
		
		return max;
	}
	
	/**
	 * Returns true if contents of tow vectors are the same
	 * @param s1
	 * @param s2
	 * @return
	 */
	private boolean compare(Vector<Tuple> s1, Vector<Tuple> s2)
	{
		boolean result = false;
	
		if(s1.size() != s2.size())
		{
			result = false;
		}
		else
		{
			result = s1.containsAll(s2);
		}

		
		if(Engine.Debug)
		{
			System.out.println("Compare Content: "+print(s1)+" with Content: "+print(s2)+" result is "+result);
		}
		
		
		return result;
	}
	/**
	 * Returns intersection of two sets
	 * @param list1
	 * @param list2
	 * @return
	 */
	private ArrayList<Integer> subSet(ArrayList<Integer> list1, ArrayList<Integer> list2)
	{
		ArrayList<Integer> subset = new ArrayList<Integer>();
		
		for (int j = 0; j < list2.size(); j++) {
			if (list1.contains(list2.get(j))) {

				subset.add(list2.get(j));
			}
		}

		return subset;
	}
	
	/**
	 * creates the content of the window as string
	 * @param content
	 * @return string having content of the active window, if its content is empty then null
	 */
	static String print(Vector<Tuple> content)
	{
		String res="{";
		int n = content.size();
		for(int i=0; i<n; i++)
		{
			res += content.get(i).printSummary(); 	
			if(i<n-1)
				res += ",";
		}
		res += "}";
		
		
		if(n==0)
		{
			res = "{null}";
		}

		return res;
	}
	
}
