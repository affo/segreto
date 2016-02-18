package engine;

import java.util.List;
import java.util.Vector;

import params.ContentParam;
import params.EnumDirection;
import params.EnumEval;
import params.EnumTick;
import params.EnumWindowType;
import params.Parameter;
import params.ReportParam;
import params.ScopeParam;
import params.TickParam;
import basic.Query;

public class OracleCEPParams extends Engine{

	public OracleCEPParams(String name, int tstart, Query win, int ratio)
	{
		super(name);
		int size = win.getSize();
		int slide = win.getSlide();
		int type = win.getType();
		
		// if time-based window, calculate t0
		if(type == 0)
		{
			int tmp = (int) Math.ceil((double)(tstart*ratio)/((double)slide*ratio));
			//TODO: t0 needs to be defined
			t0 =  tmp*slide*ratio-size;
		} // if tuple-based window, calculate i0
		else
		{	
			t0 = - size + slide;
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
		evalValues.add(EnumEval.ContentChange);
		//evalValues.add(EnumEval.NonEmpty);
		evalValues.add(EnumEval.WindowClose);
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
