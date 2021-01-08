package Serves;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import Utility.InputListener;
import Utility.Message;

/***
 * This is Client Handler class it. Handles all the client that have been accepted by the server and passed down to this class.
 * This class is responsible for redirecting the the messages and client chat. This Class is notified whenever there is a need to send messages 
 * or game data is being send 
 * @author 708114
 *
 */

public class ClientHandler  implements Runnable, PropertyChangeListener {
	private Socket cl1 =null;
	private Socket cl2 =null;
	private ObjectOutputStream oos1 = null;
	private static ArrayList<ObjectOutputStream> oosList = new ArrayList<>();
//	private ObjectOutputStream oos2 = null;
	private InputListener list = null;
//	private ArrayList<InputListener> list = new ArrayList<>();
	private ArrayList<Socket> socList = new ArrayList<>();
	private static int count = 0;
	private Thread inputListner1 = null;
	private Thread inputLister2 = null;
	
	/***
	 * This constructor takes in two different sockets from the server.
	 * @param cl1 This is a socket which takes in String and integer for client one.
//	 * @param cl2 This is a socket which takes in String and integer for client one.
	 */
	public ClientHandler(Socket cl1) {
		this.cl1 = cl1;
		socList.add(cl1);
//		list.add(new InputListener(count, this.cl1, this));
		list = new InputListener(count,this.cl1, this);
		count++;
	}

	/***
	 * This abstract method being overrided That inherited from the Runnable. This method runs Threads. In this Method create 
	 * Threads, make instantiate Inputlistener for Client socket #1, give it a unique id, and listener for ClientHandler class. 
	 */
	@Override
	public void run() {
//		InputListener listener1 = new InputListener(1,cl1,this);
//		InputListener listener2 = new InputListener(2,cl2,this);
//
//		//Making thread
//		Thread t1 = new Thread(listener1);
//
//		//Starting Thread
//		t1.start();
//		Thread t2 = new Thread(listener2);
//		t2.start();
//		for (int i = 0; i < list.size(); i++ ) {
//			Thread ti = new Thread(list.get(i));
//			if(!ti.isAlive()) {
//				ti.start();
//			}
//		}
		Thread t1 = new Thread(list);
		t1.start();

		try {
			for (int i = 0; i < socList.size(); i++) {
				oosList.add(i, new ObjectOutputStream(socList.get(i).getOutputStream()));
				oosList.get(i).flush();
			}

//			oos1 = new ObjectOutputStream(cl1.getOutputStream());
//			oos1.flush();

			
//			oos2 = new ObjectOutputStream(cl2.getOutputStream());
//			oos2.flush();

			boolean yes = true;
			while(yes) {
				for (Socket so: socList) {
					if (!so.isConnected()) {
						yes = false;
					}
				}
			}


//			oos2.close();
			for (Socket s: socList ) {
				s.close();
			}
			for (ObjectOutputStream o : oosList ) {
				o.close();
			}
//			cl2.close();
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		
	}
	

	
	/***
	 * This method is invoke from the Inputlistener to notify that have changes made. This 
	 */
	@Override
	synchronized public void propertyChange(PropertyChangeEvent event) {
		Message message = (Message) event.getNewValue();
		InputListener l = (InputListener) event.getSource();
		System.out.println(l);
		try {
			for (int i = 0; i < oosList.size(); i++) {
				if(l.getListnerNum() == list.getListnerNum()) {
					oosList.get(i).writeObject(event.getNewValue());
				} else if (l.getListnerNum() != list.getListnerNum()) {
					oosList.get(i).writeObject(event.getNewValue());
				}
			}
//			for (int i = 0; i < list.size(); i++) {
//				System.out.println(list.get(i).getListnerNum());
//				if(l.getListnerNum() == list.get(i).getListnerNum() ) {
//					System.out.println(list.size());
//					for (int j = 0; j < oosList.size(); j++) {
//							oosList.get(j).writeObject(event.getNewValue());
//							System.out.println(event.getNewValue());
//					}
//				}
//			}
			//This tell which way to direct the client messages.
//			if (i.getListnerNum() == 1) {
//
//				if(message.getObj() !=null) {
//
//					oos2.writeObject(event.getNewValue());
//				}else {
//					oos2.writeObject(event.getNewValue());
//				}
//			}else{
//
//
//				oos1.writeObject(event.getNewValue());
//			}
		}catch(IOException e){
			e.printStackTrace();
		}


		
	}	

}
