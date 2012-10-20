import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


public class ObjStructure {
	final String delama = "^"; //the string for separating each value
	String objID = null;  //the ID of this object
	String takenTime = null;  //taken time of the picture
	String imgWebSite = null;  //the picture web site
	String objType = null;  //the type of this object
	//int group = -1;
	
	public ObjStructure(String objID, String takenTime, String imgWebSite, String objType)
	{
		this.objID = objID;
		this.takenTime = takenTime;
		this.imgWebSite = imgWebSite;
		this.objType = objType;
	}

	public String GET001_ObjType() {
		return objType;
	}

	public String GET002_ObjID() {
		return objID;
	}

	public String GET003_TakenTime() {
		return takenTime;
	}

	public String GET004_ImgWebSite() {
		return imgWebSite;
	}
	
	public String ACT001_getObjInfo() {
		return this.objID + this.delama + 
			   this.takenTime + this.delama +
			   this.imgWebSite + this.delama +
			   this.objType;
	}
	public static void main(String[] args) {
		HashObjStr str = new HashObjStr();
		str.ACT003_add("0.01", "0.02", "ID1", "objID1", "20110101", "jdoaoafd", "flower");
		str.ACT003_add("0.05", "0.04", "ID2", "objID1", "20110101", "jdoaoafd", "flower");
		str.ACT003_add("0.02", "0.015", "ID3", "objID1", "20110101", "jdoaoafd", "flower");
		str.ACT003_add("0.10", "0.04", "ID4", "objID1", "20110101", "jdoaoafd", "flower");
		str.ACT003_add("0.11", "0.05", "ID5", "objID1", "20110101", "jdoaoafd", "flower");
		str.ACT003_add("0.12", "0.09", "ID6", "objID1", "20110101", "jdoaoafd", "flower");
		str.ACT003_add("0.01", "0.02", "ID7", "objID1", "20110101", "jdoaoafd", "flower");
		str.ACT005_listPrint();
		str.ACT007_getTotalCenter();
		//str.ACT008_testCount();
		float[] str1 = str.ACT006_getXY(0);
		float[] str2 = str.ACT006_getXY(1);
		String[] id1 = str.ACT004_getID(0);
		String[] id2 = str.ACT004_getID(1);
	}
}



class HashObjStr extends Hashtable<String, ObjStructure> {
	float threshold = 0.02f;
	private List<String> groupList = new ArrayList<String>();
	private List<Integer> group_count = new ArrayList<Integer>();
	
	//build hash
	public void ACT001_add(String refID, String objID, String takenTime, String imgWebSite, String objType, float i, float j){
		//ObjStructure obj = new ObjStructure(objID, takenTime, imgWebSite, objType);
		//this.put(refID, obj);
		String lati = String.valueOf(i);
		String longi = String.valueOf(j);
		ACT003_add(lati, longi, refID, objID, takenTime, imgWebSite, objType);
	}
	
	public String ACT002_get(List<Integer> listID){
		String listInfo = "";
		int index = 0;
		while(index < listID.size())
		{
			listInfo = listInfo + this.get(Integer.toString(listID.get(index))).ACT001_getObjInfo() + "\n";
			index = index + 1;
		}
		return listInfo;
	}
	
	public void ACT003_add(String latitude, String longitude, String refID, String objID, String takenTime, String imgWebSite, String objType){
		ObjStructure obj = new ObjStructure(objID, takenTime, imgWebSite, objType);
		this.put(refID, obj);	//put obj to hash
		
		float f1 = Float.parseFloat(latitude);
		float f2 = Float.parseFloat(longitude);
		float minDist = this.threshold;
		int record = -1;
		String[] gStringList;
		String[] centerXY;
		
		for(int i = 0; i < groupList.size(); i++)
		{
			gStringList = groupList.get(i).split("#");
			centerXY = gStringList[0].split(",");		//i-th list element
			float f3 = Float.parseFloat(centerXY[0]);	//x
			float f4 = Float.parseFloat(centerXY[1]);	//y
			//int group_num = Integer.parseInt(centerXY[2]);
			// using one norm to evaluate
			float xDist = f1-f3;
			float yDist = f2-f4;
			if(xDist < 0)
				xDist = -xDist;
			if(yDist < 0)
				yDist = -yDist;
			float currentDist = xDist + yDist;
			if( currentDist < minDist )
			{
				minDist = currentDist;	//find out which distance is the miniDist from centers
				record = i;		//record which 
			}
		}
		
		
		if(record < 0)
		{
			groupList.add(latitude+","+longitude+"#"+refID);
			group_count.add(1);
		}
		else
		{
			gStringList = groupList.get(record).split("#");
			centerXY = gStringList[0].split(",");		//i-th list element
			float f3 = Float.parseFloat(centerXY[0]);	//x
			float f4 = Float.parseFloat(centerXY[1]);	//y
			float temp = (float)group_count.get(record);
			f3 = (temp*f3+f1)/(temp+1.0f);				//adjustment center
			f4 = (temp*f4+f2)/(temp+1.0f);				//
			
			groupList.set(record, f3+","+f4+"#"+gStringList[1]+","+refID);
			
			group_count.set(record, group_count.get(record)+1);
		}
		
		
		//reset the minDist
		//minDist = this.threshold;
		
	}
	public String[] ACT004_getID(int groupNum)
	{
		String[] str = groupList.get(groupNum).split("#");
		//System.out.println("Center:\t"+str[0]);
		System.out.println("Group:"+groupNum+" ID:\t"+str[1]);
		String[] ids = str[1].split(",");
		return ids;
	}
	public void ACT005_listPrint()
	{
		for(int i = 0; i<groupList.size();i++)
		{
			String[] str = groupList.get(i).split("#");
			System.out.println("GroupID: "+i);
			System.out.println("Center: ("+str[0]+")");
			System.out.println("MappingID: "+str[1]);
			System.out.println("------------------------");
		}
	}
	public float[] ACT006_getXY(int groupNum)
	{
		String[] str = groupList.get(groupNum).split("#");
		
		String[] ids = str[0].split(",");
		float[] id = new float[2];
		id[0] = Float.parseFloat(ids[0]);
		id[1] = Float.parseFloat(ids[1]);
		System.out.println("Group:"+groupNum+" Center:\t"+id[0]+","+id[1]);
		return id;
	}
	public float[] ACT007_getTotalCenter()
	{
		float totalCenterX = 0.0f;
		float totalCenterY = 0.0f;
		float[] totalCenter = new float[2];
		
		for(int i = 0; i < this.groupList.size();i++)
		{				
			String[] str = this.groupList.get(i).split("#");
			String[] ids = str[0].split(",");
			
			totalCenterX += Float.parseFloat(ids[0])*(float)this.group_count.get(i) ;
			totalCenterY += Float.parseFloat(ids[1])*(float)this.group_count.get(i) ;
		}
		
		totalCenter[0] = totalCenterX / (float)(this.groupList.size()) ;
		totalCenter[1] = totalCenterY / (float)(this.groupList.size()) ;
		System.out.println("Total Center:\t"+totalCenter[0]+","+totalCenter[1]);
		return totalCenter;
	}
	public void ACT008_testCount()
	{
		
		for(int i = 0; i < this.groupList.size();i++)
		{				
			System.out.println("count: "+this.group_count.get(i));
		}
	}
	
}
