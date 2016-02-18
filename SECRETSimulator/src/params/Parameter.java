package params;

import java.util.Vector;
import basic.*;

public abstract class Parameter{
	
	private int paramaterType;
	protected Vector<Object> values;
	protected int ratio;
	
	/**
	 * Create a SECRET parameter
	 * @param type	type of the parameter
	 * @param values	it's values
	 * @param ratio		can be ignored
	 */
	public Parameter(int type, Vector values, int ratio) {
		super();
		this.paramaterType = type;
		this.values = values;
		this.ratio = ratio;
	}

	/**
	 * @param tapp	application time
	 * @param tau	system time
	 * @param stream		Stream
	 * @param window		window of the query
	 * @return	content of the windows reported due to evaluation, empty if nothing is reported
	 */
	public abstract Object eval(int tapp, int tau, Stream stream, Query window); 
}
