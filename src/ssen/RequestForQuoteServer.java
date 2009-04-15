package ssen;

import java.util.Random;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class RequestForQuoteServer extends CyclicBehaviour{
	public void action()
	{
		ACLMessage msg = myAgent.receive();
		if (msg != null) {
			System.out.println("EUREKA");
			try {
				P5Message p5Msg =  (P5Message) msg.getContentObject();
				P5Message p5MsgReply = null;
				PrintToConsole(p5Msg);
				//****TODO*****
				//have to check whether message can be accepted or not
				if(p5Msg.getName().equals("requestForQuote")){
					String price= GenerateRandomNumber();
					ACLMessage reply = msg.createReply();
					reply.setPerformative(ACLMessage.INFORM);
					p5MsgReply.setName("acceptQuote");
					p5MsgReply.setParam("price",price);
					
					
				}
				
				
			} catch (UnreadableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			myAgent.doDelete();
		}else{
			System.out.println("RequestForQuoteServer blocked");
			block();
		}
			
	}

	private static String GenerateRandomNumber() {
		// TODO Auto-generated method stub
		Random r = new Random();
		int t = 0;
		while(t==0)
		{
			t = r.nextInt(101);
		}
		return "$"+Integer.toString(t);
	}

	private void PrintToConsole(P5Message msg) {
		// TODO Auto-generated method stub
		System.out.println("messages Received:" + msg.getName());
		System.out.println(msg.getName());
		System.out.println(msg.getParam("itemID"));
	}
}
