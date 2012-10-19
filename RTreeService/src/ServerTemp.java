import gnu.trove.TIntProcedure;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class SaveToListProcedure implements TIntProcedure {
    private List<Integer> ids = new ArrayList<Integer>();
    
    @Override
    public boolean execute(int id) {
      ids.add(id);
      return true;
    }; 
    
    public List<Integer> getIds() {
      return ids;
    }
  };

public class ServerTemp extends Thread{
	
	private ServerSocket serverSocket = null; 
	public ServerTemp(int port){
		try {
			this.serverSocket = new ServerSocket(port);
			this.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		try {
			Socket clientSocket = serverSocket.accept();
			new RTREEService(clientSocket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		new ServerTemp(Constant.PORT);
	}
}