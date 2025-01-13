					for (Triangle tri : currentMap.getTriWalls()) {
						tri.getVertices()[0].x += suki.getSpeed();
						tri.getVertices()[1].x += suki.getSpeed();
						tri.getVertices()[2].x += suki.getSpeed();
					}