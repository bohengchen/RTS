import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientTemp extends Thread {

	private Socket clientSocket = null;
	public ClientTemp(String addr, int port)
	{
		this.clientSocket = new Socket();
        InetSocketAddress isa = new InetSocketAddress(addr, port);
        try {
        	this.clientSocket.connect(isa, 10000);
            this.start();
        } catch (java.io.IOException e) {
            System.out.println("Socket連線有問題 !");
            System.out.println("IOException :" + e.toString());
        }
	}
	public void run() {
        while(this.clientSocket.isConnected()) {
        	String strMessage = null;
	        try {
	        	DataInputStream reader = new DataInputStream(this.clientSocket.getInputStream());
	        	while((strMessage = reader.readLine()) != null)
	        	{
		        	System.out.println(strMessage);
	        	}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
	public void ACT001_writeMsg(String msg)
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
	public static void main(String[] args) {
		ClientTemp client = new ClientTemp("127.0.0.1", Constant.PORT);
		client.ACT001_writeMsg("0\n");
		client.ACT001_writeMsg("1#0#0#0#0#0xASJDJFFF001#2001/5/6#http://apple.com#cat\n");
		client.ACT001_writeMsg("1#2#2#2#2#0xASJDJFFF001#2051/5/6#http://www.google.com.tw#car\n");
		client.ACT001_writeMsg("1#1#2#1#2#0xASJDJFFF001#2601/5/6#http://www.google.com.tw#tree\n");
		client.ACT001_writeMsg("1#6#7#6#7#0xASJDJFFF001#20331/5/6#http://www.google.com.tw#pig\n");
		client.ACT001_writeMsg("1#6#6#6#6#0xASJDJFFF001#25601/5/6#http://www.google.com.tw#sun\n");
		client.ACT001_writeMsg("2#6#6#10.4#10.4\n");
		client.ACT001_writeMsg("1#0#0#0#0#0xASJDJFFF001#2001/5/6#http://apple.com#cat\n");
		client.ACT001_writeMsg("1#2#2#2#2#0xASJDJFFF001#2051/5/6#http://www.google.com.tw#car\n");
		client.ACT001_writeMsg("1#1#2#1#2#0xASJDJFFF001#2601/5/6#http://www.google.com.tw#tree\n");
		client.ACT001_writeMsg("1#6#7#6#7#0xASJDJFFF001#20331/5/6#http://www.google.com.tw#pig\n");
		client.ACT001_writeMsg("1#6#6#6#6#0xASJDJFFF001#25601/5/6#http://www.google.com.tw#sun\n");
		client.ACT001_writeMsg("2#0#0#4#4\n");
	}
}
