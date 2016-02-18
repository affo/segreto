package params;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import basic.ScopeInterval;
import basic.Stream;
import basic.Tuple;
import basic.Query;

public class ContentParam extends Parameter{
	
	private ScopeParam scope;	// scope parameter
	/**
	 * Creates content parameter
	 * @param values	values of content
	 * @param scope		scope parameter
	 * @param ratio		can be ignored, no longer used
	 */
	public ContentParam(Vector values, ScopeParam scope, int ratio) {
		super(2, values, ratio);
		this.scope = scope;
	}
	
	
	
	/* (non-Javadoc)
	 * @see params.Parameter#eval(int, int, basic.Stream, basic.Query)
	 */
	public Object eval(int t, int tau, Stream s, Query w)
	{
		Vector<Tuple> content = new Vector<Tuple>();
		
		// get current scope
		ScopeInterval interval = (ScopeInterval) scope.eval(t, tau, s, w);
		
		if(interval==null)
			return content;
		
		float low = interval.getLowerBound();
		float up = interval.getUpperBound();
		
		//System.out.println("Content at "+t+" Scope is "+interval.print()+" ");
		
		// get tuples inside scope interval
		
		// time-based window
		if (w.getType() == 0) {
			for (int i = (int) Math.ceil(low); i <= (int) Math.floor(up); i++) {
				if (i % ratio != 0) {
					continue;
				}
				List<Tuple> tuples = s.getTuplesAtTapp((int) i / ratio);
				Iterator<Tuple> it = tuples.iterator();

				Tuple tpl = null;

				while (it.hasNext()) {
					Tuple tuple = it.next();
					if (tuple.getTsys() < tau)
						content.add(tuple);
				}

			}
		}
		// tuple-based window
		else if (w.getType() == 1)
		{
			for (int tid = (int) low; tid <= (int)up; tid++) {
			
				Tuple tuple = s.getTupleAtTid(tid);
				
					if (tuple != null && tuple.getTsys() < tau)
						content.add(tuple);
			}
		}
		
//		if(Engine.Debug)
//		{
//			System.out.print("Content at "+t+" Scope"+ interval.print() +"=");
//			System.out.println(this.print(content));
//		}
		return content;
		
	}

	public static String print(Vector<Tuple> content)
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
		
		if(n==0)
		{
			res = "";
		}
		return res;
	}
	public ScopeParam getScope() {
		return scope;
	}

	
}
