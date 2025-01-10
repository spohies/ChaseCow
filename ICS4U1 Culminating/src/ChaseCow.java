import java.awt.event.*;
import java.util.*;
import java.awt.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.image.*;

/* 
 * screen 0 = main menu
 * screen 1 = start menu
 * screen 2 = about menu
 * screen 3 = settings menu
 * screen 4 = leaderboard
 * screen 5 = game ??????
 */

@SuppressWarnings("serial") // funky warning, just suppress it. It's not gonna do anything.
public class ChaseCow extends JPanel implements Runnable, KeyListener, MouseListener {
	// big boss variable
	int screen = 5;

	// self explanatory variables
	int FPS = 120;
	Thread thread;
	// int mapWidth = 960;
	// int mapHeight = 580;
	int screenWidth = 1080;
	int screenHeight = 720;
	boolean up, down, left, right;

	// main screen
	Rectangle recAbout, recOptions, recPlay, recExit;
	boolean hoverAbout, hoverOptions, hoverPlay, hoverExit;
	BufferedImage titleScreenBG, title, play, play2, about, about2, settings, settings2, options, options2, exit, exit2;

	// start screen
	Rectangle recLB, recNewGame;
	boolean hoverNewGame, hoverLB;
	BufferedImage menuScreenBackground, gameTitle, newGame, leaderboard, newGame2, leaderboard2, back;

	// settings
	ArrayList<Rectangle> recVolume;
	// im thinking we have like 5 buttons and its just 5 levels of volume next to
	// each other so its like a fake slider
	ArrayList<Rectangle> recDifficulty;
	// maybe we just have hp regen vs no regen for difficulty
	BufferedImage volumeImg, difficultyImg;
	// int difficulty = 1; // maybe just 1=easy 2=hard?
	int volume = 3; // 1-5??

	// game
	ArrayList<BufferedImage> cowImages = new ArrayList<BufferedImage>();

	// Rectangle player = new Rectangle(517, 328, 46, 64);
	// HashSet <Rectangle> walls = new HashSet <Rectangle>();
	BufferedImage tempBG, playerImage, cowImage;
	Player suki = new Player(100, 2, new Rectangle(517, 382, 46, 10), new Rectangle(517, 328, 46, 64), 0, 0);
	// HashSet<Cow> cows = new HashSet<Cow>();
	FloorMap currentMap;
	ArrayList<FloorMap> maps = new ArrayList<>();
	String[] cowNames = {
			"baseCow",
			"paceCow",
			"spaceCow",
			"wasteCow"
	};
	int currentCowType = 0;

	public ChaseCow() throws IOException {

		// sets up JPanel
		setPreferredSize(new Dimension(1080, 720));
		setVisible(true);

		initialize();

		// starting the thread
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		while (true) {
			// main game loop
			update();
			this.repaint();
			try {
				Thread.sleep(1000 / FPS);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// IF SCREEN IS ON A GAME SCREEN ie != screen 0 1 2 3 4 or smth
			// then do stuff that needs to happen at all times during game
		}
	}

	public void initialize() {

		try {
			titleScreenBG = ImageIO.read(getClass().getResource("/titleBG.png"));
			System.out.println("Loaded titleScreenBG image");
			int numCows = cowNames.length;
			for (int i = 0; i < numCows; i++) {
				String cowPath = "/" + cowNames[i] + ".png";
				cowImage = ImageIO.read(getClass().getResource(cowPath));
				cowImages.add(cowImage);
				System.out.println("Loaded cow image: " + cowPath);
			}

			// screen 0 (main menu)
			title = ImageIO.read(getClass().getResource("/title.png"));
			System.out.println("Loaded title image");
			play = ImageIO.read(getClass().getResource("/playbutton.png"));
			System.out.println("Loaded play button image");
			about = ImageIO.read(getClass().getResource("/aboutbutton.png"));
			System.out.println("Loaded about button image");
			play2 = ImageIO.read(getClass().getResource("/playbutton2.png"));
			System.out.println("Loaded play button 2 image");
			about2 = ImageIO.read(getClass().getResource("/aboutbutton2.png"));
			System.out.println("Loaded about button 2 image");
			// DIMENSION NEED TO BE CHANGED!!!!!!
			recPlay = new Rectangle(300, 300, play.getWidth(), play.getHeight());
			recAbout = new Rectangle(300, 420, about.getWidth(), about.getHeight());
			// recOptions = new Rectangle(1, 1, options.getWidth(), options.getHeight());
			// recExit = new Rectangle(1, 1, exit.getWidth(), exit.getHeight());

			// screen 1 (start menu)

			// screen 2 (wtvwtv )... so on

			// screen 5
			// starting room
			HashSet<Rectangle> electricalWalls = new HashSet<>();
			electricalWalls.add(new Rectangle(200, 200, 60, 60));
			electricalWalls.add(new Rectangle(300, 40, 40, 100));
			electricalWalls.add(new Rectangle(450, 100, 80, 35));
			electricalWalls.add(new Rectangle(130, 150, 15, 15));
			electricalWalls.add(new Rectangle(250, 350, 150, 200));
			Point electricalStart = new Point(100, 100);
			tempBG = ImageIO.read(getClass().getResource("/tempBG.png"));
			System.out.println("Loaded tempBG image");

			HashSet<Triangle> tempWalls = new HashSet<>();
			HashSet<Cow> electricalCows = new HashSet<>();

			tempWalls.add(new Triangle(new Point(100, 100), new Point(200, 200), new Point(300, 100)));
			currentMap = new FloorMap(new Point(0, 0), electricalWalls, tempWalls, tempBG, new Rectangle[0],
					electricalCows);

			currentMap.getCows().add(new BaseCow(-100, 200, cowImages.get(currentCowType), currentMap));
			currentMap.getCows().add(new BaseCow(-200, -100, cowImages.get(currentCowType), currentMap));
			currentMap.getCows().add(new BaseCow(300, 300, cowImages.get(currentCowType), currentMap));
			currentMap.getCows().add(new BaseCow(200, -200, cowImages.get(currentCowType), currentMap));
			currentMap.getCows().add(new BaseCow(300, -200, cowImages.get(currentCowType), currentMap));

			// other images
			tempBG = ImageIO.read(getClass().getResource("/tempBG.png"));
			System.out.println("Loaded tempBG image");
			playerImage = ImageIO.read(getClass().getResource("/sukiDown.png"));
			System.out.println("Loaded player image");
			cowImage = ImageIO.read(getClass().getResource("/baseCow.png"));
			System.out.println("Loaded cow image");

		} catch (IOException e) {
			e.printStackTrace();
		}

		initializeMaps();
		// spawnCows();
	}

	public static void resetVariables() {
		// RESETS ALL CHANGING VARIABLES WHEN NEW GAME IS STARTED
	}

	// public void spawnCows() {
		
	// }

	public void update() {
		// update stuff

		if (screen == 5) {

			move();
			keepInBound();
			Iterator<Rectangle> it = currentMap.getRectWalls().iterator();
			while (it.hasNext()) {
				checkCollision(it.next());
			}
			Iterator<Triangle> itr = currentMap.getTriWalls().iterator();
			while (itr.hasNext()) {
				checkCollision(itr.next());
			}
			// Iterator <Cow> iter = currentMap.getCows().iterator();
			// while (iter.hasNext()) {
			// Cow cow = iter.next();
			// checkCollision(iter.next());
			// }

			if (currentMap != null) {
				currentMap.updateCows(suki);
			} else {
				System.out.println("currentMap is null");
			}
			// Iterator cowIter = cows.iterator();
			// while (cowIter.hasNext()) {
			// ((Cow) cowIter.next()).followPlayer(suki);
			// }
			for (Cow cow : currentMap.getCows()) {
				checkCollision(cow);
			}
		}
	}

	private void initializeMaps() {
		// Create and add FloorMap objects to the maps list
	}

	public void changeMap(FloorMap currentMap, int n) {
		FloorMap m = maps.get(n);
		// change map according to what map is next... im not sure how to do this tbh we
		// may need to number everysingle map
		// and add an if statement for every single one
		// spawnCows();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		Graphics2D g3 = (Graphics2D) g;
		// white background
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, screenWidth, screenHeight);

		// main screen
		if (screen == 0) {
			// g.drawImage(titleScreenBG, 0, 0, titleScreenBG.getWidth(),
			// titleScreenBG.getHeight(), this);
			g.drawImage(title, 100, 100, title.getWidth(), title.getHeight(), this);
			g.drawImage(play, 100, 200, play.getWidth(), play.getHeight(), this);
			g.drawImage(about, 100, 300, about.getWidth(), about.getHeight(), this);
		}

		// start game screen
		if (screen == 1) {

		}

		// draw stuff
		if (screen == 5) {
			if (tempBG != null) {
				g.drawImage(currentMap.getBG(), currentMap.getTLLocation().x, currentMap.getTLLocation().y, getWidth(),
						getHeight(), this);
			} else {
				System.out.println("Background image is null");
			}
			g2.setColor(Color.GREEN);
			Iterator<Rectangle> it = currentMap.getRectWalls().iterator();
			while (it.hasNext()) {
				g2.fill(it.next());
			}
			for (Triangle tri : currentMap.getTriWalls()) {
				Point[] vertices = tri.getVertices();
				int[] xPoints = { vertices[0].x, vertices[1].x, vertices[2].x };
				int[] yPoints = { vertices[0].y, vertices[1].y, vertices[2].y };
				g2.fillPolygon(xPoints, yPoints, 3);
			}
			if (playerImage != null) {
				g.drawImage(playerImage, (screenWidth / 2) - (suki.getHitboxC().width / 2),
						(screenHeight / 2) - (suki.getHitboxC().height / 2), 46, 64, this);
			} else {
				System.out.println("Player image is null");
			}
			for (Cow cow : currentMap.getCows()) {
				cow.render(g);
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {

		// game screen
		if (screen == 5) {
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_A) {
				left = true;
				right = false;
				try {
					playerImage = ImageIO.read(getClass().getResource("/sukiLeft.png"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else if (key == KeyEvent.VK_D) {
				right = true;
				left = false;
				try {
					playerImage = ImageIO.read(getClass().getResource("/sukiRight.png"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else if (key == KeyEvent.VK_W) {
				up = true;
				down = false;
				try {
					playerImage = ImageIO.read(getClass().getResource("/sukiUp.png"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else if (key == KeyEvent.VK_S) {
				down = true;
				up = false;
				try {
					playerImage = ImageIO.read(getClass().getResource("/sukiDown.png"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// game screen
		if (screen == 5) {
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_A) {
				left = false;
			} else if (key == KeyEvent.VK_D) {
				right = false;
			} else if (key == KeyEvent.VK_W) {
				up = false;
			} else if (key == KeyEvent.VK_S) {
				down = false;
			}
		}
	}

	public void move() {
		// game screen
		if (screen == 5) {
			int moveX = 0, moveY = 0;
			if (left) {
				moveX = -suki.getSpeed();
			} else if (right) {
				moveX = suki.getSpeed();
			}
			if (up) {
				moveY = -suki.getSpeed();
			} else if (down) {
				moveY = suki.getSpeed();
			}

			// Update in-game coordinates
			suki.move(moveX, moveY);

			// System.out.println(suki.getGamePos().x + " " + suki.getGamePos().y);

			// center player
			suki.getHitboxC().x = (screenWidth / 2) - (suki.getHitboxC().width / 2);
			suki.getHitboxC().y = (screenHeight / 2) - (suki.getHitboxC().height / 2);

			for (Rectangle wall : currentMap.getRectWalls()) {
				wall.x -= moveX;
				wall.y -= moveY;
			}

			for (Triangle wall : currentMap.getTriWalls()) {
				for (Point point : wall.getVertices()) {
					point.x -= moveX;
					point.y -= moveY;
				}
			}

			for (Cow cow : currentMap.getCows()) {
				cow.setMapPos(cow.getMapPos().x - moveX, cow.getMapPos().y - moveY);
			}

			currentMap.setTLLocation(
					new Point(currentMap.getTLLocation().x - moveX, currentMap.getTLLocation().y - moveY));
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	// use for hovering over buttons / changing button colour
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	// use for hovering over buttons / changing button colour
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	// TO BE FIXED
	public void keepInBound() {

		// game screen
		// MUST CHANGE 100 and mapWidth to changing variables -> instance variables for
		// currentMap
		if (screen == 5) {
			if (suki.getHitboxM().x < 100)
				suki.getHitboxM().x = 100;
			else if (suki.getHitboxM().x > currentMap.getBG().getWidth() - suki.getHitboxM().width)
				suki.getHitboxM().x = currentMap.getBG().getWidth() - suki.getHitboxM().width;
			if (suki.getHitboxM().y < 100)
				suki.getHitboxM().y = 100;
			else if (suki.getHitboxM().y > currentMap.getBG().getHeight() - suki.getHitboxM().height)
				suki.getHitboxM().y = currentMap.getBG().getHeight() - suki.getHitboxM().height;
		}

	}

	public void checkCollision(Rectangle wall) {

		// game screen
		if (screen == 5) {
			// check if player touches wall
			if (suki.getHitboxM().intersects(wall)) {
				System.out.println("Ow!");
				// stop the player from moving
				double left1 = suki.getHitboxM().getX();
				double right1 = suki.getHitboxM().getX() + suki.getHitboxM().getWidth();
				double top1 = suki.getHitboxM().getY();
				double bottom1 = suki.getHitboxM().getY() + suki.getHitboxM().getHeight();
				double left2 = wall.getX();
				double right2 = wall.getX() + wall.getWidth();
				double top2 = wall.getY();
				double bottom2 = wall.getY() + wall.getHeight();

				if (right1 > left2 &&
						left1 < left2 &&
						right1 - left2 < bottom1 - top2 &&
						right1 - left2 < bottom2 - top1) {
					// player collides from left side of the wall
					currentMap.setTLLocation(
							new Point(currentMap.getTLLocation().x + suki.getSpeed(), currentMap.getTLLocation().y));
					// stop movement for walls when collision happens
					for (Rectangle w : currentMap.getRectWalls()) {
						w.x += suki.getSpeed();
					}
					// stop movement for cows when collision occurs
					for (Cow cow : currentMap.getCows()) {
						cow.setMapPos(cow.getMapPos().x + suki.getSpeed(), cow.getMapPos().y);
					}
					// stop movement for triangle walls when collision occurs
					for (Triangle tri : currentMap.getTriWalls()) {
						tri.getVertices()[0].x += suki.getSpeed();
						tri.getVertices()[1].x += suki.getSpeed();
						tri.getVertices()[2].x += suki.getSpeed();
					}
				} else if (left1 < right2 &&
						right1 > right2 &&
						right2 - left1 < bottom1 - top2 &&
						right2 - left1 < bottom2 - top1) {
					// player collides from right side of the wall
					currentMap.setTLLocation(
							new Point(currentMap.getTLLocation().x - suki.getSpeed(), currentMap.getTLLocation().y));
					for (Rectangle w : currentMap.getRectWalls()) {
						w.x -= suki.getSpeed();
					}
					for (Cow cow : currentMap.getCows()) {
						cow.setMapPos(cow.getMapPos().x - suki.getSpeed(), cow.getMapPos().y);
					}
					// stop movement for triangle walls when collision occurs
					for (Triangle tri : currentMap.getTriWalls()) {
						tri.getVertices()[0].x -= suki.getSpeed();
						tri.getVertices()[1].x -= suki.getSpeed();
						tri.getVertices()[2].x -= suki.getSpeed();
					}
				} else if (bottom1 > top2 && top1 < top2) {
					// player collides from top side of the wall
					currentMap.setTLLocation(
							new Point(currentMap.getTLLocation().x, currentMap.getTLLocation().y + suki.getSpeed()));
					for (Rectangle w : currentMap.getRectWalls()) {
						w.y += suki.getSpeed();
					}
					for (Cow cow : currentMap.getCows()) {
						cow.setMapPos(cow.getMapPos().x, cow.getMapPos().y + suki.getSpeed());
					}
					// stop movement for triangle walls when collision occurs
					for (Triangle tri : currentMap.getTriWalls()) {
						tri.getVertices()[0].y += suki.getSpeed();
						tri.getVertices()[1].y += suki.getSpeed();
						tri.getVertices()[2].y += suki.getSpeed();
					}
				} else if (top1 < bottom2 && bottom1 > bottom2) {
					// player collides from bottom side of the wall
					currentMap.setTLLocation(
							new Point(currentMap.getTLLocation().x, currentMap.getTLLocation().y - suki.getSpeed()));
					for (Rectangle w : currentMap.getRectWalls()) {
						w.y -= suki.getSpeed();
					}
					for (Cow cow : currentMap.getCows()) {
						cow.setMapPos(cow.getMapPos().x, cow.getMapPos().y - suki.getSpeed());
					}
					// stop movement for triangle walls when collision occurs
					for (Triangle tri : currentMap.getTriWalls()) {
						tri.getVertices()[0].y -= suki.getSpeed();
						tri.getVertices()[1].y -= suki.getSpeed();
						tri.getVertices()[2].y -= suki.getSpeed();
					}
				}
			}
		}
	}

	public void checkCollision(Triangle wall) {
		int intersectionType = wall.intersects(suki.getHitboxM());
		if (intersectionType > 0) {
			// System.out.println("Ow!");
			// get player hitbox vertices
			// point order = TL, TR, BR, BL
			Point[] hitboxVertices = { new Point(suki.getHitboxM().x, suki.getHitboxM().y),
					new Point(suki.getHitboxM().x + suki.getHitboxM().width, suki.getHitboxM().y),
					new Point(suki.getHitboxM().x + suki.getHitboxM().width,
							suki.getHitboxM().y + suki.getHitboxM().height),
					new Point(suki.getHitboxM().getLocation().x,
							suki.getHitboxM().getLocation().y + suki.getHitboxM().height) };
			Point[] wallVertices = wall.getVertices();
			// get wall sides
			Line wallSide1 = new Line(wall.getVertices()[0], wall.getVertices()[1]);
			Line wallSide2 = new Line(wall.getVertices()[1], wall.getVertices()[2]);
			Line wallSide3 = new Line(wall.getVertices()[2], wall.getVertices()[0]);

			// get player sides
			Line playerSide1 = new Line(hitboxVertices[0], hitboxVertices[1]);
			Line playerSide2 = new Line(hitboxVertices[1], hitboxVertices[2]);
			Line playerSide3 = new Line(hitboxVertices[2], hitboxVertices[3]);
			Line playerSide4 = new Line(hitboxVertices[3], hitboxVertices[0]);

			// if player vertex is inside triangle wall
			if (intersectionType == 1) {
				handleTriangularCollisions(wall, new Line[] { wallSide1, wallSide2, wallSide3 }, hitboxVertices);
			}
			// if triangle vertex is inside player
			else if (intersectionType == 2) {
				handleTriangularCollisions(wall, new Line[] { playerSide1, playerSide2, playerSide3, playerSide4 }, wallVertices);
			} else if (intersectionType == 3) {
				// player side intersects wall side

				// find the two sides that touch

				// move them away from each other
			}
		}
	}

	public void handleTriangularCollisions(Triangle wall, Line[] sides, Point[] vertices){
		for (Point p : vertices) {
			if (wall.containsPoint(p)) {
				double[] distances = new double[sides.length];
				for(int i = 0; i < sides.length; i++){
					distances[i] = sides[i].getDistance(p);
				}
				double minDist = Double.MIN_VALUE; 
				for(int i = 0; i < distances.length; i++){
					if(distances[i] < minDist){
						minDist = distances[i];
					}
				}
				for(int i = 0; i < distances.length; i++){
					if (minDist == distances[i]) { // player collides with side1
						Point closest = sides[i].closestPoint(p);
						int dx = (int) (p.x - closest.x);
						int dy = (int) (p.y - closest.y);
						// stop movement for rectangle walls when collision occurs
						for (Rectangle w : currentMap.getRectWalls()) {
							w.x -= dx;
							w.y -= dy;
						}
						// stop movement for cows when collision occurs
						for (Cow cow : currentMap.getCows()) {
							cow.setMapPos(cow.getMapPos().x - dx, cow.getMapPos().y - dy);
						}
						// stop movement for triangle walls when collision occurs
						for (Triangle tri : currentMap.getTriWalls()) {
							for (Point point : tri.getVertices()) {
								point.x -= dx;
								point.y -= dy;
							}
						}
					}
				}
			}
		}
	}

	public void checkCollision(Cow cow) {
		for (Cow cow2 : currentMap.getCows()) {
			if (cow != cow2 && cow.getHitbox().intersects(cow2.getHitbox())) {
				Rectangle intersection = cow.getHitbox().intersection(cow2.getHitbox());
				int dx = intersection.width / 2;
				int dy = intersection.height / 2;

				if (cow.getX() < cow2.getX()) {
					cow.setMapPos(cow.getMapPos().x - dx, cow.getMapPos().y);
					cow2.setMapPos(cow2.getMapPos().x + dx, cow2.getMapPos().y);
				} else {
					cow.setMapPos(cow.getMapPos().x + dx, cow.getMapPos().y);
					cow2.setMapPos(cow2.getMapPos().x - dx, cow2.getMapPos().y);
				}

				if (cow.getY() < cow2.getY()) {
					cow.setMapPos(cow.getMapPos().x, cow.getMapPos().y - dy);
					cow2.setMapPos(cow2.getMapPos().x, cow2.getMapPos().y + dy);
				} else {
					cow.setMapPos(cow.getMapPos().x, cow.getMapPos().y + dy);
					cow2.setMapPos(cow2.getMapPos().x, cow2.getMapPos().y - dy);
				}
			}
		}
	}


	// public void checkCollision(Cow cow) {
	// // check if player touches cow
	// if(suki.getHitboxM().intersects(cow.getHitbox())) {
	// System.out.println("Moo!");
	// }
	// }

	public static void main(String[] args) throws IOException {

		// The following lines creates your window

		// makes a brand new JFrame
		JFrame frame = new JFrame("Example");
		// makes a new copy of your "game" that is also a JPanel
		ChaseCow myPanel = new ChaseCow();
		// so your JPanel to the frame so you can actually see it

		frame.add(myPanel);
		// so you can actually get keyboard input
		frame.addKeyListener(myPanel);
		// so you can actually get mouse input
		frame.addMouseListener(myPanel);
		// self explanatory. You don't want to resize your window because
		// it might mess up your graphics and collisions
		frame.setResizable(false);
		// self explanatory. You want to see your frame
		frame.setVisible(true);
		// some weird method that you must run
		frame.pack();
		// place your frame in the middle of the screen
		frame.setLocationRelativeTo(null);
		// without this, your thread will keep running even when you windows is closed!
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
