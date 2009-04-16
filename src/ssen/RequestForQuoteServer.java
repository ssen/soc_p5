package ssen;

import java.io.IOException;
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
			
				P5Message p5Msg = new P5Message();
				try {
					p5Msg = (P5Message) msg.getContentObject();
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				PrintToConsole(p5Msg);
				//****TODO*****
				//have to check whether message can be accepted or not
				if(p5Msg.getName().equals("requestForQuote")){
					P5Message p5MsgReply = new P5Message();
					String price= GenerateRandomNumber();
					ACLMessage reply = msg.createReply();
					
					reply.setPerformative(ACLMessage.INFORM);

					p5MsgReply.setName("something");
					p5MsgReply.setParam("price",price);
					try {
						reply.setContentObject(p5MsgReply);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					myAgent.send(reply);//sending quote
					
				}
				
				

			
			myAgent.doDelete();
		}else{
			System.out.println("RequestForQuoteServer blocked");
			block();
		}
			
	}

	private String GenerateRandomNumber() {
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
