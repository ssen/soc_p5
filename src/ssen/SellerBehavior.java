package ssen;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class SellerBehavior extends Behaviour{
	
	public void action()
	{
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		ACLMessage RequestFotQuote = myAgent.receive(mt);
		if (RequestFotQuote != null) {
				P5Message p5RequestFotQuote = new P5Message();
				try {
					p5RequestFotQuote = (P5Message) RequestFotQuote.getContentObject();
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				PrintToConsole(p5RequestFotQuote);
				//****TODO*****
				//have to check whether message can be accepted or not
				//if YES
				String price= GenerateRandomNumber();
				ACLMessage aclmsg = RequestFotQuote.createReply();
				
				myAgent.send(aclmsg);
				
				//SendMessage("quote", RequestFotQuote, ACLMessage.INFORM, p5RequestFotQuote.getParam("itemID"), price);
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
	private void SendMessage(String messageType, ACLMessage aclmessage, int performat, String itemID,
			String itemPrice) {
		ACLMessage aclmsg = aclmessage.createReply();
		aclmsg.setPerformative(performat);
		AID r = new AID("BuyerAgent",AID.ISLOCALNAME);
		aclmsg.addReceiver(r);
		P5Message p5msg = new P5Message();
		HashMap<String, String> hash = new HashMap<String, String>();
		p5msg.setName(messageType);
		hash.put("price", itemPrice);
		hash.put("itemID", itemID);
		p5msg.setParams(hash);
		try {
			aclmsg.setContentObject(p5msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myAgent.send(aclmsg);	
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}
}
