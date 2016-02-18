package engine;
import java.util.List;
import java.util.Vector;

import params.ContentParam;
import params.EnumDirection;
import params.EnumTick;
import params.EnumEval;
import params.ReportParam;
import params.Parameter;
import params.ScopeParam;
import params.TickParam;
import params.EnumWindowType;

import basic.Query;


/**
 * @author dindarn
 * SECRET parameters of StreamBase
 */
public class SBParams extends Engine{
	
	public SBParams(String name, int tstart, Query win, int ratio)
	{
		super(name);
		int size = win.getSize();
		int slide = win.getSlide();
		int type = win.getType();
		
		// if time-based window, calculate t0
		if(type == 0)
		{
			int tmp = (int) Math.ceil((double)(tstart-size*ratio)/((double)slide*ratio));
			//System.out.println("tstart was: "+ tstart+" t0 calculation tmp: "+tmp);
			t0 = tmp*slide*ratio-1;
			//System.out.println("t0: "+t0);
		}  
		// if tuple-based window, calculate i0
		else if(type == 1)
		{	
			t0 = 0;
		}
		
		init(ratio);
	}
	

	@SuppressWarnings("unchecked")
	protected void init(int ratio)
	{
		Vector scopeValues = new Vector();
		scopeValues.add(EnumDirection.Forward);
		scopeValues.add(EnumWindowType.Single);
		scopeValues.add(new Integer(t0));
		ScopeParam scopeParams = new ScopeParam(scopeValues, ratio);
		params.add(scopeParams);
		
		// ----------------------------------------------------
		
		Vector contentValues = new Vector();
		ContentParam contentParams = new ContentParam(contentValues, scopeParams, ratio);
		params.add(contentParams);
		
		// ----------------------------------------------------
		
		Vector evalValues = new Vector();
		evalValues.add(EnumEval.WindowClose);
		evalValues.add(EnumEval.NonEmpty);
		ReportParam evalParams = new ReportParam(evalValues, contentParams, ratio);
		params.add(evalParams);
		
		// ----------------------------------------------------
		
		Vector tickValues = new Vector();
		tickValues.add(new Integer(t0));
		tickValues.add(EnumTick.TupleDriven);
		TickParam tickParams = new TickParam(tickValues, evalParams, ratio);
		params.add(tickParams);
		
		// ----------------------------------------------------
	}
	
	public List<Parameter> getParams() {
		return params;
	}
}
