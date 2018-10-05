package ScrabbleServer;

public class GameAssistant {
	
	private boolean startGame = false;
	private ConnectionToClient inviter;
	private ConnectionToClient starter;
	
	public GameAssistant(boolean startGame, ConnectionToClient inviter, ConnectionToClient starter) {
		this.startGame = startGame;
		this.inviter = inviter;
		this.starter = starter;
	}
	
	public void updateStarter(ConnectionToClient starter) {
		this.starter = starter;
	}
	
	public void updateInviter(ConnectionToClient inviter) {
		this.inviter = inviter;
	}
	
	public void startGame(boolean start) {
		this.startGame = start;
	}
	
	protected boolean getStarted() {
		return this.startGame;
	}
	
	protected int getStarterId() {
		return this.starter.getClientId();
	}
	
	protected String getStarterName() {
		return this.starter.getUserName();
	}
	
	protected int getInviterId() {
		return this.inviter.getClientId();
	}
	
	protected String getInviterName() {
		return this.inviter.getUserName();
	}
	
}
