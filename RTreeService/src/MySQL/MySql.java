package MySQL;
import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.PreparedStatement; 
import java.sql.ResultSet; 
import java.sql.SQLException; 
import java.sql.Statement; 

public class MySql { 
  private Connection con = null; //Database objects 
  //連接object 
  private Statement stat = null; 
  //執行,傳入之sql為完整字串 
  private ResultSet rs = null; 
  //結果集 
  private PreparedStatement pst = null; 
  
  public MySql() 
  { 
    try { 
      Class.forName("com.mysql.jdbc.Driver"); 
      //註冊driver 
      con = DriverManager.getConnection( 
      "jdbc:mysql://localhost/test?useUnicode=true&characterEncoding=Big5", 
      "root",""); 
      //取得connection

//jdbc:mysql://localhost/test?useUnicode=true&characterEncoding=Big5
//localhost是主機名,test是database名
//useUnicode=true&characterEncoding=Big5使用的編碼 
      
    } 
    catch(ClassNotFoundException e) 
    { 
      System.out.println("DriverClassNotFound :"+e.toString()); 
    }//有可能會產生sqlexception 
    catch(SQLException x) { 
      System.out.println("Exception :"+x.toString()); 
    } 
    
  } 
  //建立table的方式 
  //可以看看Statement的使用方式 
  public void createTable(String createdbSQL) 
  { 
    try 
    { 
      stat = con.createStatement(); 
      stat.executeUpdate(createdbSQL); 
      
    } 
    catch(SQLException e) 
    { 
      System.out.println("CreateDB Exception :" + e.toString()); 
    } 
    finally 
    { 
      Close(); 
    } 
  } 
  //新增資料 
  //可以看看PrepareStatement的使用方式 
  public void insertTable(String insertdbSQL, String[] list) 
  { 
    try 
    { 
      pst = con.prepareStatement(insertdbSQL); 
      
      int index = 1;
      while(index <= list.length)
      {
    	  pst.setString(index, list[index - 1]);
    	  index = index + 1;
      }
      pst.executeUpdate(); 
    } 
    catch(SQLException e) 
    { 
      System.out.println("InsertDB Exception :" + e.toString()); 
    } 
    finally 
    { 
      Close(); 
    } 
  } 
  //刪除Table, 
  //跟建立table很像 
  public void dropTable(String drop) 
  { 
    try 
    { 
      stat = con.createStatement(); 
      stat.executeUpdate(drop); 
    } 
    catch(SQLException e) 
    { 
      System.out.println("DropDB Exception :" + e.toString()); 
    } 
    finally 
    { 
      Close(); 
    } 
  } 
  //查詢資料 
  //可以看看回傳結果集及取得資料方式 
  public String SelectTable(String selSQL, String[] listAttr) 
  { 
	String msg = "";
    try 
    { 
      stat = con.createStatement(); 
      rs = stat.executeQuery(selSQL); 
      //System.out.println("ID\t\tName\t\tPASSWORD"); 
      int index = 0;
      while(rs.next()) 
      { 
    	  index = 0;
    	  while(index < listAttr.length)
    	  {
    		  msg = msg + rs.getString(listAttr[index]) + "#";
    		  index = index + 1;
    	  }
    	  msg = msg + "\n";
    	  /*
        System.out.println(rs.getInt("id")+"\t\t"+ 
            rs.getString("name")+"\t\t"+rs.getString("passwd"));
            */ 
      } 
    } 
    catch(SQLException e) 
    { 
      System.out.println("DropDB Exception :" + e.toString()); 
    } 
    finally 
    { 
      Close(); 
    } 
    return msg;
  } 
  //完整使用完資料庫後,記得要關閉所有Object 
  //否則在等待Timeout時,可能會有Connection poor的狀況 
  public void Close() 
  { 
    try 
    { 
      if(rs!=null) 
      { 
        rs.close(); 
        rs = null; 
      } 
      if(stat!=null) 
      { 
        stat.close(); 
        stat = null; 
      } 
      if(pst!=null) 
      { 
        pst.close(); 
        pst = null; 
      } 
    } 
    catch(SQLException e) 
    { 
      System.out.println("Close Exception :" + e.toString()); 
    } 
  } 

  public boolean isExist(String tableName)
  {
	  try { 
		  Statement   stmt   =   this.con.createStatement(); 
		  stmt.executeQuery( "select   count(*)   from   " + tableName); 
	  } 
	  catch(SQLException   e) 
	  { 
	      return false;
	  }
	  return true;
  }

  public static void main(String[] args) 
  { 
    //測看看是否正常 
	/*
    MySql test = new MySql(); 
    test.dropTable(); 
    test.createTable(); 
    test.insertTable("yku", "12356"); 
    test.insertTable("yku2", "7890"); 
    test.SelectTable(); 
    */
  
  } 
}