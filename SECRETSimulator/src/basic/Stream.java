package basic;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Stream {
	private List tuples;
	private int time;
	
	public Stream() {
		super();
		this.time = 0;
		tuples = new ArrayList<Tuple>();
	}
	
	public void addTuple(Tuple tuple){
		tuples.add(tuple);
		time = tuple.getTsys();
	}
	
	public boolean hasTupleArrived(int t){	
		if(getTupleAtTsys(t)==null)
			return false;
		return true;
		
	}
	
	public Tuple getTupleAtTsys(int tsys)
	{
		Iterator<Tuple> it = tuples.iterator();
		while(it.hasNext())
		{
			Tuple tpl = it.next();
			if(tpl.getTsys() == tsys)
				return tpl;
		}
		return null;
	}
	
	public List<Tuple> getTuplesAtTapp(int tapp)
	{
		Iterator<Tuple> it = tuples.iterator();
		List<Tuple> retTuples= new ArrayList<Tuple>();
		while(it.hasNext())
		{
			Tuple tpl = it.next();
			if(tpl.getTapp() == tapp)
				retTuples.add(tpl);
		}
		return retTuples;
	}
	
	public List<Tuple> getTupleAtBid(int bid)
	{
		Iterator<Tuple> it = tuples.iterator();
		List<Tuple> retTuples= new ArrayList<Tuple>();
		while(it.hasNext())
		{
			Tuple tpl = it.next();
			if(tpl.getBid() == bid)
				retTuples.add(tpl);
		}
		return retTuples;
	}
	
	public Tuple getTupleAtTid(int tid)
	{
		Iterator<Tuple> it = tuples.iterator();
		while(it.hasNext())
		{
			Tuple tpl = it.next();
			if(tpl.getTid() == tid)
				return tpl;
		}
		return null;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
	
	
}
