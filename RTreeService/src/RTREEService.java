import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import MySQL.MainSqlOper;

import com.infomatiq.jsi.Rectangle;
import com.infomatiq.jsi.SpatialIndex;
import com.infomatiq.jsi.rtree.RTree;

public class RTREEService extends Thread
{
	private SpatialIndex rTree = null;
	private HashObjStr hash = null;
	private int curAddTotalNode = 0;
	private Socket clientSocket = null;
	public RTREEService(Socket clientSocket)
	{
		System.out.println("New Thread");
		this.clientSocket = clientSocket;
		this.start();
	}
	
	public void ACT001_selectRule(String getMsg){
		switch(getMsg.charAt(0))
		{
		case Constant.RULE_INITIAL:
			this.ACT002_initRTree();
			break;
		case Constant.RULE_INSERT:
			this.ACT003_insertNode(getMsg);
			break;
		case Constant.RULE_QUERY:
			this.ACT004_doRangeQuery(getMsg);
		case Constant.RULE_STORE_DATABASE:
			this.ACT006_addtoMySQL();
			break;
		}
	}
	
	public void ACT002_initRTree(){
		this.curAddTotalNode = 0;
		this.rTree = null;
		this.rTree = new RTree();
		this.rTree.init(null);
		
		this.hash = null;
		this.hash = new HashObjStr();
		
		System.out.println("Initial R-Tree");
	}
	
	public void ACT003_insertNode(String getMsg){
		String[] listSplit = getMsg.split("#");
		Rectangle addRect = new Rectangle(Float.parseFloat(listSplit[1]), Float.parseFloat(listSplit[2]), 
				Float.parseFloat(listSplit[3]), Float.parseFloat(listSplit[4]));
		this.rTree.add(addRect, this.curAddTotalNode);
		
		this.hash.ACT001_add(Integer.toString(this.curAddTotalNode), listSplit[5], listSplit[6], listSplit[7], listSplit[8], Float.parseFloat(listSplit[1]), Float.parseFloat(listSplit[2]));
		
		this.curAddTotalNode = this.curAddTotalNode + 1;
		System.out.println("Insert ok");
	}
	
	public void ACT004_doRangeQuery(String getMsg){
		OutputStream out = null;
		int index = 0;
		String msg = "";
		String[] listSplit = getMsg.split("#");
		Rectangle queryRect = new Rectangle(Float.parseFloat(listSplit[1]), Float.parseFloat(listSplit[2]), 
				Float.parseFloat(listSplit[3]), Float.parseFloat(listSplit[4]));
		SaveToListProcedure myProc = new SaveToListProcedure();
		this.rTree.intersects(queryRect, myProc);
		//group all result
		List<Integer> ids = myProc.getIds();
		msg =  this.hash.ACT002_get(ids);
		//System.out.println(msg);
		this.ACT005_writeMsg(msg);
		System.out.println("Query ok");
	}
	public void ACT005_writeMsg(String msg)
	{
		try {
			OutputStream out = this.clientSocket.getOutputStream();
			out.write(msg.getBytes());
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void ACT006_addtoMySQL()
	{
		MainSqlOper mso = new MainSqlOper();
		mso.ACT005_dropAll();
		for(int i = 0;i < this.hash.numofGroup();i++)
			for(int j = 0;j < this.hash.numofGroupid(i);j++)
				mso.ACT004_allInsert(this.hash.ACT009_getInformation(i, j));
		System.out.print(mso.ACT003_getTableResult());
	}
	public void run()
	{
		String strMessage = null;
		while(this.clientSocket.isConnected()){
	        try {
	        	DataInputStream reader = new DataInputStream(this.clientSocket.getInputStream());
	        	while((strMessage = reader.readLine()) != null)
	        	{
		        	System.out.println(strMessage);
					this.ACT001_selectRule(strMessage);
	        	}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
		}
	}
}