package engine;

import basic.Query;
import params.*;

import java.util.List;
import java.util.Vector;

/**
 * Created by affo on 18/02/16.
 */
public class FlinkParams extends Engine {

    public FlinkParams(String name, int tt1, Query win, int ratio) {
        super(name);
        int w = win.getSize();
        int b = win.getSlide();
        int type = win.getType();

        // if time-based window, calculate t0
        if(type == 0)
        {
            // calculate time as if it is tumbling
            int tt0 = tt1 - (tt1 % b);
            // calculate coefficient
            int k = (tt0 - (tt1 - w + 1)) / b;
            t0 =  tt0 - k * b;
        } // if tuple-based window, calculate i0
        else
        {
            // I don't know
        }

        init(ratio);
    }

    @SuppressWarnings("unchecked")
    protected void init(int ratio)
    {
        Vector scopeValues = new Vector();
        scopeValues.add(EnumDirection.Forward);
        scopeValues.add(EnumWindowType.Single);
        // I have to lie about my t0, because Flink
        // thinks windows [start, end), while
        // SECRET (start, end]
        scopeValues.add(t0 - 1);
        ScopeParam scopeParams = new ScopeParam(scopeValues, ratio);
        params.add(scopeParams);

        // ----------------------------------------------------

        Vector contentValues = new Vector();
        ContentParam contentParams = new ContentParam(contentValues, scopeParams, ratio);
        params.add(contentParams);

        // ----------------------------------------------------

        Vector evalValues = new Vector();
        evalValues.add(EnumEval.NonEmpty);
        evalValues.add(EnumEval.WindowClose);
        ReportParam evalParams = new ReportParam(evalValues, contentParams, ratio);
        params.add(evalParams);

        // ----------------------------------------------------

        Vector tickValues = new Vector();
        tickValues.add(t0 - 1);
        tickValues.add(EnumTick.TimeDriven);
        TickParam tickParams = new TickParam(tickValues, evalParams, ratio);
        params.add(tickParams);

        // ----------------------------------------------------
    }

    public List<Parameter> getParams() {
        return params;
    }
}
