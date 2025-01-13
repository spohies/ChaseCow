	for (Wall w : currentMap.getRectWalls()) {
						w.setGamePos(w.getRect().x, w.getRect().y - suki.getSpeed());
					}