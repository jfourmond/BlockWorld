package agent;

import java.util.List;
import java.util.Random;

import environment.Environment;

public class Agent {
	private Random rand;
	
	private Character id;
	private Character goal;
	private Environment env;
	
	private boolean pushed;
	private boolean satisfied;
	private boolean free;
	
	
	public Agent(Character c, Character goal, Environment env) {
		rand = new Random(System.currentTimeMillis());
		
		this.id = c;
		this.goal = goal;
		this.env = env;
		
		this.satisfied = false;
		this.pushed = false;
		this.free = false;
	}
	
	//	GETTERS
	public Character getChar() { return id; }
	
	public Character getGoal() { return goal; }
	
	public boolean isPushed() { return pushed; }
	
	public boolean isSatisfied() { return satisfied; }
	
	public boolean isFree() { return free; }
	
	//	SETTERS
	public void setChar(Character c) { this.id = c; }
	
	public void setGoal(Character goal) { this.goal = goal; }
	
	public void setPushed(boolean pushed) { this.pushed = pushed; }
	
	public void setSatisfied(boolean satisfied) { this.satisfied = satisfied; }
	
	public void setFree(boolean free) { this.free = free; }
	
	//	METHODES
	public void update() {
		Agent bottom = bottomBlock();
		// Satisfait ?
		if(goal == null)
			satisfied = (bottom == null);
		else
			satisfied = (bottom != null && bottom.getChar() == goal);
		// Libre ?
		free = amIfree();
	}
	
	/**
	 * Perception du bloc / agent en dessous
	 * @return l({@link Agent} en dessous de l'{@link Agent} courant, s'il existe, <code>null</code> sinon.
	 */
	private Agent bottomBlock() { return env.askBottomBlock(this); }
	
	/**
	 * Perception de la possibilité de déplacement de l'{@link Agent} courant
	 * @return <code>true</code> si l'{@link Agent} peut se déplacer, <code>false</code> sinon
	 */
	private boolean amIfree() { return env.askFree(this); }
	
	//	REGLES DE COMPORTEMENT
	/**
	 * Demande de déplacement à l'{@link Environment}
	 */
	private void move() {
		List<Integer> places = env.askFreeColumns(this);
		int n = rand.nextInt(places.size());
		int move = places.get(n);
		env.askMove(this, move);
		pushed = false;
	}
	
	/**
	 * Demande de pousse à l'{@link Environment}
	 */
	private void push() { env.askPush(this); }
	
	/**
	 * Exécution de l'{@link Agent}
	 */
	public void run() {
		Agent bottom = bottomBlock();
		// Satisfait ?
		if(goal == null)
			satisfied = (bottom == null);
		else
			satisfied = (bottom != null && bottom.getChar() == goal);
		// Libre ?
		free = amIfree();
		if((!satisfied || pushed) && free)
			move();
		else if(!satisfied && !free)
			push();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || getClass() != obj.getClass())
			return false;
		else {
			Agent agent = (Agent) obj;
			return (id == agent.id);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder ch = new StringBuilder("" + id);
		return ch.toString();
	}
}
