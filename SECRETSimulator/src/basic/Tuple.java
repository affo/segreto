package basic;

/*Specify input tuples */

public class Tuple {
	private int tid;
	private int tapp;
	private int tsys;
	private int bid;
	private String name;
	
	
	public Tuple(int tsys, int tid, int tapp, int bid) {
		super();
		this.tid = tid;
		this.tapp = tapp;
		this.tsys = tsys;
		this.bid = bid;
		this.name = "t"+tid;
	}

	
	public String getName() {
		return name;
	}


	public int getTid() {
		return tid;
	}


	public void setTid(int tid) {
		this.tid = tid;
	}


	public int getTapp() {
		return tapp;
	}


	public void setTapp(int tapp) {
		this.tapp = tapp;
	}


	public int getTsys() {
		return tsys;
	}


	public void setTsys(int tsys) {
		this.tsys = tsys;
	}


	public int getBid() {
		return bid;
	}


	public void setBid(int bid) {
		this.bid = bid;
	}
	
	public String print()
	{
		String res="";
		res += "("+tsys+","+tid+","+tapp+","+bid+")";
		
		return res;
	}
	
	public String printSummary()
	{
		String res="";
		res += name;
		
		return res;
	}
}
