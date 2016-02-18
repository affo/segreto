package engine;
import java.util.ArrayList;
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
 *	SECRET parameters of Coral8
 */
public class Coral8Params extends Engine{

	public Coral8Params(String name, int tstart, Query win, int ratio)
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
		} // if tuple-based window set i0
		else
		{	
			t0 = 0;
		}
		// initialize the engine ratio can be ignored
		init(ratio);
	}
	
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
		evalValues.add(EnumEval.ContentChange);
		evalValues.add(EnumEval.NonEmpty);
		ReportParam evalParams = new ReportParam(evalValues, contentParams, ratio);
		params.add(evalParams);
		
		// ----------------------------------------------------
		
		Vector tickValues = new Vector();
		tickValues.add(new Integer(t0));
		tickValues.add(EnumTick.BatchDriven);
		TickParam tickParams = new TickParam(tickValues, evalParams, ratio);
		params.add(tickParams);
		
		// ----------------------------------------------------
	}
	
	public List<Parameter> getParams() {
		return params;
	}
	

}
