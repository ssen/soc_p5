package ssen;
import java.awt.print.Book;
import java.io.IOException;

import ssen.BuyerAgent;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class BuyerBehaviour extends CyclicBehaviour{
	private String buyer_arg2;
	public BuyerBehaviour(String buyer_arg2) {
		// TODO Auto-generated constructor stub
		//super();
		this.buyer_arg2 = buyer_arg2;
	}
	
	public BuyerBehaviour(){
		//super();
	}

	public void action(){
		P5Message p5Msg = new P5Message();
		ACLMessage msg = new ACLMessage(ACLMessage.CFP);
		AID r = new AID("SellerAgent",AID.ISLOCALNAME);
		msg.addReceiver(r);
		p5Msg.setName("requestForQuote");
		p5Msg.setParam("itemID", buyer_arg2);
		try {
			msg.setContentObject(p5Msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myAgent.send(msg);
		// The qoute is got back from the seller 
		ACLMessage quotemessage = myAgent.receive();
		if (quotemessage  != null) {
			System.out.println("Yes got themessage");
		}
		else
		{
			block();
		}
		
		
		
		
	}
}

