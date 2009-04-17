package ssen;

import java.io.IOException;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

@SuppressWarnings("serial")
public class SellerAgent extends Agent {
	private MessageTemplate template = MessageTemplate
	.MatchPerformative(ACLMessage.CFP);
	boolean payFirst;

	protected void setup() {
		System.out.println("Setting Seller Agent Starting");
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			String argSeller = (String) args[0];
			if (argSeller.equals("pay") || argSeller.equals("goods")) {
				// Register the Seller to the yellow pages
				if (register() == false)
					doDelete();
				else {// successfully registered
					payFirst = argSeller.equals("pay") ? true : false;
					addBehaviour(new ItemRequestsServer());
				}
			} else {
				System.out.println("Wrong arguments provided");
				doDelete();
			}
		} else {
			System.out.println("No Argument specified");
			doDelete();
		}
	}

	private boolean register() {
		try {
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			ServiceDescription sd = new ServiceDescription();
			sd.setName("ronnsen's service");
			sd.setType("SELLER");// need to add ontologies, languages etc?
			dfd.addServices(sd);
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
			return false;
		}
		return true;
	}

	protected void takeDown() {
		// Deregister from the yellow pages
		try {
			DFService.deregister(this);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

		// Printout a dismissal message
		System.out.println("Seller-agent " + getAID().getName()
				+ " terminating.");
	}

	private class ItemRequestsServer extends CyclicBehaviour {
		public void action() {
			MessageTemplate mt = MessageTemplate
			.MatchPerformative(ACLMessage.CFP);
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null)// CFP received
			{

				String convId = msg.getConversationId();
				P5Message rfqP5;
				try {
					rfqP5 = (P5Message) msg.getContentObject();

					String itemId = rfqP5.getParam("itemId");
					Integer price = 50;// Should use random here

					ACLMessage reply = msg.createReply();
					reply.setPerformative(ACLMessage.INFORM);
					reply.setConversationId(convId);

					P5Message quoteReply = new P5Message();
					quoteReply.setName("quote");
					quoteReply.setParam("itemId", itemId);
					quoteReply.setParam("price", price.toString());
					reply.setContentObject(quoteReply);

					myAgent.send(reply);
					if (payFirst)
						addBehaviour(new PaymentFirstPurchaseServer(convId,
								itemId, price));
					else
						addBehaviour(new GoodsFirstPurchaseServer(convId));
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				block();
			}
		}
	}

	private class PaymentFirstPurchaseServer extends Behaviour {

		private String convId;
		private String itemId;
		private Integer price;
		private MessageTemplate mt;
		private int step = 0;

		public PaymentFirstPurchaseServer(String convId, String itemId,
				Integer price) {
			super();
			this.convId = convId;
			this.itemId = itemId;
			this.price = price;
			mt = MessageTemplate.and(MessageTemplate
					.MatchConversationId(convId), MessageTemplate
					.MatchPerformative(ACLMessage.INFORM));
		}

		public void action() {
			ACLMessage msg;
			switch (step) {
			case 0:// receive accept quote or reject quote
				msg = myAgent.receive(mt);
				if (msg != null) {
					P5Message quoteReply;
					try {
						quoteReply = (P5Message) msg.getContentObject();

						if (quoteReply.getName().equals("acceptQuote")) {
							step = 1;
						} else if (quoteReply.getName().equals("rejectQuote")) {
							// send a terminate message
							ACLMessage reply = msg.createReply();
							reply.setConversationId(convId);
							P5Message terminateMsg = new P5Message();
							terminateMsg.setName("terminate");
							reply.setContentObject(terminateMsg);
							step = 3;

						} else {
							step = 7;// unknown
						}
					} catch (UnreadableException e) {
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					block();
				}
				break;
			case 1:// received acceptQuote
				msg = myAgent.receive(mt);
				if (msg != null) {
					P5Message paymentReply;
					try {
						paymentReply = (P5Message) msg.getContentObject();

						if (paymentReply.getName().equals("payment"))// send
							// reply
							// as
							// goods
						{
							ACLMessage reply = msg.createReply();
							reply.setConversationId(convId);
							P5Message goodsMsg = new P5Message();
							goodsMsg.setName("goods");
							goodsMsg.setParam("itemID", this.itemId);
							reply.setContentObject(goodsMsg);
							step = 3;// go to terminate

						} else {
							step = 7;// unknown
						}
					} catch (UnreadableException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					block();
				}
				break;

			case 3:// wait for terminate
				msg = myAgent.receive(mt);
				if (msg != null) {
					P5Message terminateReply;
					try {
						terminateReply = (P5Message) msg.getContentObject();

						if (terminateReply.getName().equals("terminate")) {
							step = 4;// terminated
						} else
							step = 7;
					} catch (UnreadableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					block();
				}
			case 7:// message unknown
				break;
			}
		}

		@Override
		public boolean done() {
			if (step == 7 || step == 3)
				return true;
			else
				return false;
		}
	}

	private class GoodsFirstPurchaseServer extends Behaviour {
		private String convId;
		private MessageTemplate mt;
		int step = 0;

		public GoodsFirstPurchaseServer(String convId) {
			super();
			this.convId = convId;
			mt = MessageTemplate.and(MessageTemplate
					.MatchConversationId(convId), MessageTemplate
					.MatchPerformative(ACLMessage.INFORM));
		}

		public void action() {
			ACLMessage msg;
			switch (step) {
			case 0:// receive accept for Quote and send goods
				msg = myAgent.receive(mt);
				if (msg != null) {
					P5Message quoteReply;
					try {
						quoteReply = (P5Message) msg.getContentObject();
						ACLMessage goodsReply = msg.createReply();
						if (quoteReply.getName().equals("acceptQuote")) {
							quoteReply.setName("goods");
							goodsReply.setConversationId(this.convId);
							myAgent.send(goodsReply);
							step = 1;
						} else {
							step = 7;// unknown message
						}
					} catch (UnreadableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					block();
				}
				break;
			case 1:// wait for payment and send terminate
				msg = myAgent.receive(mt);
				if (msg != null) {
					P5Message quoteReply;
					try {
						quoteReply = (P5Message) msg.getContentObject();
						ACLMessage goodsReply = msg.createReply();
						if (quoteReply.getName().equals("payment")) {
							quoteReply.setName("terminate");
							goodsReply.setConversationId(this.convId);
							myAgent.send(goodsReply);
							step = 3;
						} else {
							step = 7;// unknown message
						}
					} catch (UnreadableException e) {
						e.printStackTrace();
					}
				} else {
					block();
				}
				break;
			}
		}

		@Override
		public boolean done() {
			if (step == 7 || step == 3)
				return true;
			else
				return false;
		}

	}

}
