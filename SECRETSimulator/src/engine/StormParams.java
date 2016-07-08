package engine;

import basic.Query;
import params.*;

import java.util.List;
import java.util.Vector;

/**
 * Created by affo on 18/02/16.
 */
public class StormParams extends Engine {
    private boolean timeBased;

    public StormParams(String name, int tt1, Query win, int ratio) {
        super(name);
        int w = win.getSize();
        int b = win.getSlide();
        int type = win.getType();
        timeBased = type == 0;

        // if time-based window, calculate t0
        if (timeBased) {
            // calculate time as if it is tumbling
            int tt0 = tt1 - (tt1 % b);
            // calculate coefficient
            int k = (tt0 - (tt1 - w + 1)) / b;
            t0 = tt0 - k * b;
        }
        // if tuple-based window, calculate i0
        else {
            t0 = b - w;
        }

        init(ratio);
    }

    @SuppressWarnings("unchecked")
    protected void init(int ratio) {
        int startTime = t0;
        if (timeBased) {
            // I have to lie about my t0, because
            // I (yes, because Storm does not give you
            // the start and end of the window)
            // modeled Storm thinking windows as [start, end],
            // while SECRET thinks as (start, end]
            startTime--;
        }

        Vector scopeValues = new Vector();
        scopeValues.add(EnumDirection.Forward);
        scopeValues.add(EnumWindowType.Single);
        scopeValues.add(startTime);
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
        tickValues.add(startTime);
        tickValues.add(EnumTick.TimeDriven);
        TickParam tickParams = new TickParam(tickValues, evalParams, ratio);
        params.add(tickParams);

        // ----------------------------------------------------
    }

    public List<Parameter> getParams() {
        return params;
    }
}
