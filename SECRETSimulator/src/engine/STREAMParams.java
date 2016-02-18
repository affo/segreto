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


public class STREAMParams extends Engine{

	//public static List<Parameter> params=new ArrayList<Parameter>();
	
	public STREAMParams(String name, int tstart, Query win, int ratio)
	{
		super(name);
		int size = win.getSize();
		int slide = win.getSlide();
		int type = win.getType();
		
		// time-based window
		if(type == 0)
		{
			t0 = tstart-size*ratio;
			//System.out.println("tstart was: "+ tstart+" t0 calculation tmp: "+tmp
			//System.out.println("t0: "+t0);
		} // TODO: tuple-based windows
		else
		{	
			t0 = 0 - size;
			//System.out.println("i0: "+t0);
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
		evalValues.add(EnumEval.ContentChange);
		ReportParam evalParams = new ReportParam(evalValues, contentParams, ratio);
		params.add(evalParams);
		
		// ----------------------------------------------------
		
		Vector tickValues = new Vector();
		tickValues.add(new Integer(t0));
		tickValues.add(EnumTick.TimeDriven);
		TickParam tickParams = new TickParam(tickValues, evalParams, ratio);
		params.add(tickParams);
		
		// ----------------------------------------------------
	}
	
	public List<Parameter> getParams() {
		return params;
	}

}
