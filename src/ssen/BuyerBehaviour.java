package ssen;
import java.awt.print.Book;
import java.io.IOException;
import java.util.HashMap;

import ssen.BuyerAgent;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class BuyerBehaviour extends OneShotBehaviour{
	private String buyer_arg1;
	private String buyer_arg2;
	
	public BuyerBehaviour(String buyer_arg1,String buyer_arg2) {
		this.buyer_arg1 = buyer_arg1;
		this.buyer_arg2 = buyer_arg2;
	}
	
	public BuyerBehaviour(){
	}

	public void action(){
		// *****************************************requestforquote***************************
		ACLMessage requestForQuote = new ACLMessage(ACLMessage.CFP);
		P5Message p5requestForQuote = new P5Message();
		AID r = new AID("SellerAgent",AID.ISLOCALNAME);
		requestForQuote.addReceiver(r);
		p5requestForQuote.setName("requestForQuote");
		p5requestForQuote.setParam("itemID", buyer_arg2);
		try {
			requestForQuote.setContentObject(p5requestForQuote);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myAgent.send(requestForQuote);
		// **********************************************************************************
		//*****************************************quote*************************************
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		ACLMessage quote = myAgent.receive(mt);
		
		System.out.println(buyer_arg1);
		if(quote!=null){
			if(buyer_arg1.equalsIgnoreCase("accept"))
			{
				System.out.println("quote accepted");
				P5Message p5Quote = new P5Message();
				try {
					p5Quote = (P5Message) quote.getContentObject();
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//got the quote
				PrintToConsole(p5Quote);//print the quote to screen)
				//Now send acceptQuote
				
				HashMap<String, String> hash = new HashMap<String, String>();
				hash = p5Quote.getParams();
				SendMessage("acceptQuote",quote, ACLMessage.INFORM,hash.get("itemID"),hash.get("itemPrice"));

				
			}
			else if(buyer_arg1.equalsIgnoreCase("reject"))
			{
				System.out.println("quote rejected");
			
			}
		}
		else{
			System.out.println("blocked after quote");
			block();
		}
		// **********************************************************************************	
		if (quote  != null) {
			System.out.println("Yes got themessage");
		}
		else
		{
			block();
		}
		
		
		
	
		
	}

	private void SendMessage(String messageType, ACLMessage aclmessage, int performat, String itemID,
			String itemPrice) {
		ACLMessage aclmsg = aclmessage.createReply();
		aclmsg.setPerformative(performat);
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
	/*
	 * type = 0 for itemID
	 * type = 1 for itemPrice
	 */
	private void SendMessage(String messageType, ACLMessage aclmessage, int performat, String item, int type) {
		ACLMessage aclmsg = aclmessage.createReply();
		aclmsg.setPerformative(performat);
		P5Message p5msg = new P5Message();
		p5msg.setName(messageType);
		if(type ==0)
			p5msg.setParam("itemID", item);
		else if(type ==1)
			p5msg.setParam("itemPrice", item);
		else System.out.println("Wrong choice of type in SendMessge");
		try {
			aclmsg.setContentObject(p5msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myAgent.send(aclmsg);	
	}
	
	private void PrintToConsole(P5Message msg) {
		// TODO Auto-generated method stub
		System.out.println("messages Received:" + msg.getName());
		System.out.println(msg.getName());
		System.out.println(msg.getParam("itemID"));
	}
	
}

