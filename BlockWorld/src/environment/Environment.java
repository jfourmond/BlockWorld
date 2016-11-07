package environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import agent.Agent;

/**
 * L'environnement est composé de trois colonnes de hauteur 4 et de 4 blocs indépendants
 * @author Jérôme
 */
public class Environment extends Thread {
	private final int MAX_HEIGHT = 4;
	private final int MAX_WIDTH = 3;
	
	private boolean world[][];
	
	private Random rand;
	private Map<Agent, Position> agents;
	
	public Environment() {
		super("Environnement");
		
		rand = new Random(System.currentTimeMillis());
		
		world = new boolean[MAX_HEIGHT][MAX_WIDTH];
		agents = new HashMap<>(4);

		init();
		build();
	}
	
	private void init() {
		for(int i=0 ; i < MAX_HEIGHT ; i++)
			for(int j=0 ; j<MAX_WIDTH ; j++)
				world[i][j] = false;
	}
	
	private void build() {
		agents.put(new Agent('d', null, this), new Position(0, 3));
			world[3][0] = true;
		agents.put(new Agent('b', 'c', this), new Position(0, 2));
			world[2][0] = true;
		agents.put(new Agent('c', 'd', this), new Position(0, 1));
			world[1][0] = true;
		agents.put(new Agent('a', 'b', this), new Position(0, 0));
			world[0][0] = true;
		System.out.println(this);
	}
	
	private void update() {
		init();
		Set<Entry<Agent, Position>> entrySet = agents.entrySet();
		Position position;
		for(Entry<Agent, Position> A : entrySet) {
			position = A.getValue();
			world[position.getY()][position.getX()] = true;
		}
		System.out.println(this);
	}
	
	/**
	 * Retourne la {@link Position} de l'{@link Agent} passé en paramètre
	 * @param agent : l'{@link Agent} dont il faut rechercher la position
	 * @return la {@link Position} de l"{@link Agent} passée en paramètre
	 */
	private Position getAgentPosition(Agent agent) {
		Position pos = null;
		Set<Entry<Agent, Position>> entrySet = agents.entrySet();
		for(Entry<Agent, Position> A : entrySet) {
			if(A.getKey().equals(agent))
				pos = A.getValue();
		}
		return pos;
	}
	
	/**
	 * Retourne l'{@link Agent} au coordonnée (x, y)
	 * @param x : abcisse
	 * @param y : ordonnée
	 * @return l'{@link Agent} au coordonnée (x, y)
	 */
	private Agent getAgentAt(int x, int y) {
		Position pos = new Position(x, y);
		Set<Entry<Agent, Position>> entrySet = agents.entrySet();
		for(Entry<Agent, Position> A : entrySet) {
			if(A.getValue().equals(pos))
				return A.getKey();
		}
		return null;
	}
	
	/**
	 * Réponse à l'{@link Agent} du bloc / {@link Agent} en dessous de lui
	 * @param agent : l'{@link Agent} qui fait la demande
	 * @return l'{@link Agent} en dessous de l'{@link Agent} qui fait la demande, <code>null</code> s'il n'y en a pas
	 */
	public Agent askBottomBlock(Agent agent) {
		Set<Entry<Agent, Position>> entrySet = agents.entrySet();
		Position pos = getAgentPosition(agent);
		// Traitement et tentative de récupération de l'agent du dessous
		if(pos.getY() == MAX_HEIGHT-1)
			return null;
		else {
			Position pos_aux = new Position(pos.getX(), pos.getY()+1);
			for(Entry<Agent, Position> A : entrySet) {
				if(A.getValue().equals(pos_aux))
					return A.getKey();
			}
			return null;
		}
	}
	
	/**
	 * Réponse à l'{@link Agent} passée en paramètre sur sa condition de liberté
	 * @param agent : l'{@link Agent} qui fait la demande
	 * @return l'état de liberté de l'{@link Agent} qui fait la demande
	 */
	public boolean askFree(Agent agent) {
		Position pos = getAgentPosition(agent);
		int x = pos.getX();
		int y = pos.getY();
		
		if(y == 0) return true;
		else if(world[y-1][x] == false) return true;
		else return false;
	}
	
	/**
	 * Réponse à l'{@link Agent} passée en paramètre sur les colonnes libres de l'{@link Environment}
	 * @param agent : l'{@link Agent} qui fait la demande
	 * @return une {@link List} des colonnes libres
	 */
	public List<Integer> askFreeColumns(Agent agent) {
		Position pos = getAgentPosition(agent);
		List<Integer> columns = new ArrayList<>(3);
		for(int i=0 ; i<MAX_WIDTH ; i++) {
			if(i != pos.getX()) columns.add(i);
		}
		return columns;
	}
	
	/**
	 * Déplacement de l'{@link Agent} passée en paramètre à la colonne précisée en paramètre
	 * @param agent : l'{@link Agent} qui fait la demande
	 * @param column : colonne
	 */
	public void askMove(Agent agent, int column) {
		int y = 0;
		boolean bottom = false;
		while(y != MAX_HEIGHT && bottom == false) {
			agents.get(agent).setPosition(column, y);
			if(y == MAX_HEIGHT-1)
				bottom = true;
			else if(world[y+1][column] == false)
				++y;
			else if(world[y+1][column] == true)
				bottom = true;
		}
		System.out.println("Mouvement de " + agent + " en (" + column + ", " + y + ")");
	}
	
	/**
	 * Réponse et réaction à l'{@link Agent} passée en paramètre lors de sa demande de pousse
	 * @param agent : l'{@link Agent} qui fait la demande
	 */
	public void askPush(Agent agent) {
		Set<Entry<Agent, Position>> entrySet = agents.entrySet();
		Position pos = getAgentPosition(agent);
		
		Position pos_aux;
		for(Entry<Agent, Position> A : entrySet) {
			pos_aux = A.getValue();
			if(pos_aux.getY() < pos.getY() && pos_aux.getX() == pos.getX())
				A.getKey().setPushed(true);
		}
		
		System.out.println("Pousse de " + agent);
	}
	
	/**
	 * Teste si tous les {@link Agent}s / bloc sont satisfaits (bien placés)
	 * @return <code>true</code> si tous les {@link Agent}s / blocs sont bien satisfaits, <code>false</code> sinon
	 */
	private boolean areAllAgentsSatisfied() {
		Set<Entry<Agent, Position>> entrySet = agents.entrySet();
		for(Entry<Agent, Position> A : entrySet) {
			if(!A.getKey().isSatisfied())
				return false;
		}
		return true;
	}
	
	/**
	 * Mise à jour de la perception des agents
	 */
	private void updateStates() {
		Set<Agent> agentSet = agents.keySet();
		for(Agent agent : agentSet)
			agent.update();
	}
	
	@Override
	public void run() {
		int i=0;
		Agent choice;
		Set<Agent> agentSet = agents.keySet();
		// MISE A JOUR DES ETATS INITIALS
		updateStates();
		//	RESOLUTION
		while(!areAllAgentsSatisfied()) {
			System.out.println("Etape : " + i++);
			Object[] agentArray = agentSet.toArray();
			do {
				int n = rand.nextInt(agentArray.length);
				choice = (Agent) agentArray[n];
			} while	(choice.isSatisfied() && !choice.isPushed() ||
					(choice.isPushed() && !choice.isFree()));
			
			System.out.println("Choix : " + choice);
			choice.run();
			// MISE A JOUR DES ETATS APRES CHANGEMENTS
			updateStates();
			update();
			try {
				sleep(750);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Fin du programme.");
	}
	
	@Override
	public String toString() {
		StringBuilder ch = new StringBuilder();
		//	MATRICE
		for(int i=0 ; i < MAX_HEIGHT ; i++) {
			for(int j=0 ; j<MAX_WIDTH ; j++) {
				if(world[i][j] == true)
					ch.append(" " + getAgentAt(j, i).getChar() + " ");
				else 
					ch.append(" . ");
				ch.append(" | ");
			}
			ch.append("\n");
		}
		return ch.toString();
	}
}
