package skunkjets.server;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.util.Timer;

import skunkjets.client.BeamProjectile;
import skunkjets.client.GameObject;
import skunkjets.client.ProjectileType;
import skunkjets.client.RocketProjectile;


class LogicThread extends Thread {
	private JetServer js;
	ListenerThread zero, one;
	public Timer mainTimer = new Timer();

	
	private Integer nextNewID = 2;
	public int getNewID() {
		synchronized (nextNewID) {
			return nextNewID++;
		}
	}
	
	ProjectileType rocket = new RocketProjectile();
	ProjectileType beam = new BeamProjectile();
	
	public LogicThread(JetServer js, ListenerThread zero, ListenerThread one) {
		this.js = js;
		
		this.zero = zero;
		zero.logicThread = this;
		
		this.one = one;
		one.logicThread = this;
	}

	public void run() {
		float lastUpdate = mainTimer.getTime();
		
		while (true) {
			synchronized (zero.playerData) {
				synchronized (one.playerData) {
					float now = mainTimer.getTime();
					float timeDelta = now - lastUpdate;
					
					HashMap<Integer, GameObject> gameObjects = new HashMap<Integer, GameObject>();
					gameObjects.putAll(zero.playerData.gameObjects);
					gameObjects.putAll(one.playerData.gameObjects);
					
					for (GameObject gameObject : gameObjects.values())
						gameObject.update(timeDelta);
	
					ArrayList<GameObject> destroyedObjects = new ArrayList<GameObject>();
					
					for (int i = 0; i < gameObjects.size(); i++) {
						for (int j = i + 1; j < gameObjects.size(); j++) {
							if (gameObjects.get(i) == zero.playerData.redCannon)
								continue;
							if (gameObjects.get(i) == one.playerData.redCannon)
								continue;
							if (gameObjects.get(j) == zero.playerData.redCannon)
								continue;
							if (gameObjects.get(j) == one.playerData.redCannon)
								continue;
							if (gameObjects.get(i).collide(gameObjects.get(j))) {
								destroyedObjects.add(gameObjects.get(i));
								destroyedObjects.add(gameObjects.get(j));
							}
						}
					}
					
					// explode the objects
					for (GameObject obj : destroyedObjects) {
						gameObjects.remove(obj);
					}

					zero.sendInfo(one);
					one.sendInfo(zero);
				}
			}
		}
	}
}
/*
*/