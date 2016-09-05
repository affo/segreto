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
            //t0 = ((tt1 - 1) / b) * b - (w - b - 1);
            // we discovered that, with time-based windows,
            // given that we have not-empty content,
            // t0 = b - w is equivalent to the formula above.
            t0 = b - w;
        }
        // if tuple-based window, calculate i0
        else {
            t0 = b - w;
        }

        init(ratio);
    }

    @SuppressWarnings("unchecked")
    protected void init(int ratio) {
        Vector scopeValues = new Vector();
        scopeValues.add(EnumDirection.Forward);
        scopeValues.add(EnumWindowType.Single);
        scopeValues.add(t0);
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
        tickValues.add(t0);
        if (timeBased) {
            tickValues.add(EnumTick.TimeDriven);
        } else {
            tickValues.add(EnumTick.TupleDriven);
        }
        TickParam tickParams = new TickParam(tickValues, evalParams, ratio);
        params.add(tickParams);

        // ----------------------------------------------------
    }

    public List<Parameter> getParams() {
        return params;
    }
}
