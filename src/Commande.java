public class Commande {
	
	private long orderAt;
	private int type;
	private int state;
	
	Commande(int type) {
		this.orderAt = System.currentTimeMillis()/1000;
		this.type = type;
		this.state = 0;
	}
	
}
