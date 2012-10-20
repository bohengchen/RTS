package MySQL;



public class MainSqlOper extends MySql {
	//table2
	private String dropTable2 = "DROP TABLE Image ";
	private String createTable2 = "CREATE TABLE Image (" + 
			"    GroupId     INTEGER " +
		    "  , photoId     VARCHAR(30) " +
			"  , takenTime   VARCHAR(20) " +
			"  , imgURL      VARCHAR(100) " +
			"  , longitude   VARCHAR(20) " +
			"  , latitude    VARCHAR(20) " +
			"  , distance    VARCHAR(20) " +
			"  , time        VARCHAR(15)) ";
	private String insertdbTable2 = "insert into Image(GroupId,photoId,takenTime,imgURL,longitude,latitude,distance,time) " + 
		      "values(?,?,?,?,?,?,?,?)";
	private String[] listAttrTable2 = {"GroupId", "photoId", "takenTime", "imgURL", "longitude", "latitude", "distance", "time"};
	private String selectSQL2 = "select * from Image "; 
	
	public MainSqlOper(){ 
	}
	
	public void ACT002_insertTable2(String[] list){
		if(this.isExist("Image")) {
			this.insertTable(this.insertdbTable2, list);
		}
		else{
			this.createTable(this.createTable2);
			this.insertTable(this.insertdbTable2, list);
		}
	}
	
	public String ACT003_getTableResult(){
		return this.SelectTable(this.selectSQL2, this.listAttrTable2);
	}
	
	public void ACT004_allInsert(String[] listEle2){
		this.ACT002_insertTable2(listEle2);
	}
	
	public void ACT005_dropAll(){
		this.dropTable(this.dropTable2);
	}

	public static void main(String[] args){
		String[] a1 = {"1", "HJAHFDJS", "2010/3/4", "www.google.com", "3.1123", "4.223", "4445", "334511"};
		MainSqlOper mso = new MainSqlOper();
		//mso.ACT005_dropAll();
		mso.ACT004_allInsert(a1);
		System.out.print(mso.ACT003_getTableResult());
		mso.Close();
	}
}
