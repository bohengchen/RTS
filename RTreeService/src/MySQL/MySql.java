package MySQL;
import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.PreparedStatement; 
import java.sql.ResultSet; 
import java.sql.SQLException; 
import java.sql.Statement; 

public class MySql { 
  private Connection con = null; //Database objects 
  //�s��object 
  private Statement stat = null; 
  //����,�ǤJ��sql������r�� 
  private ResultSet rs = null; 
  //���G�� 
  private PreparedStatement pst = null; 
  
  public MySql() 
  { 
    try { 
      Class.forName("com.mysql.jdbc.Driver"); 
      //���Udriver 
      con = DriverManager.getConnection( 
      "jdbc:mysql://localhost/test?useUnicode=true&characterEncoding=Big5", 
      "root",""); 
      //���oconnection

//jdbc:mysql://localhost/test?useUnicode=true&characterEncoding=Big5
//localhost�O�D���W,test�Odatabase�W
//useUnicode=true&characterEncoding=Big5�ϥΪ��s�X 
      
    } 
    catch(ClassNotFoundException e) 
    { 
      System.out.println("DriverClassNotFound :"+e.toString()); 
    }//���i��|����sqlexception 
    catch(SQLException x) { 
      System.out.println("Exception :"+x.toString()); 
    } 
    
  } 
  //�إ�table���覡 
  //�i�H�ݬ�Statement���ϥΤ覡 
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
  //�s�W��� 
  //�i�H�ݬ�PrepareStatement���ϥΤ覡 
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
  //�R��Table, 
  //��إ�table�ܹ� 
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
  //�d�߸�� 
  //�i�H�ݬݦ^�ǵ��G���Ψ��o��Ƥ覡 
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
  //����ϥΧ���Ʈw��,�O�o�n�����Ҧ�Object 
  //�_�h�b����Timeout��,�i��|��Connection poor�����p 
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
    //���ݬݬO�_���` 
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