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

@SuppressWarnings("serial") //funky warning, just suppress it. It's not gonna do anything.
public class ChaseCow extends JPanel implements Runnable, KeyListener, MouseListener{
	
	//self explanatory variables
	int FPS = 120;
	Thread thread;
	int mapWidth = 960;
	int mapHeight = 580;
	int screenWidth = 1080;
	int screenHeight = 720;
	boolean up, down, left, right;
	
	// main screen
	int screen = 0;
	Rectangle recAbout, recOptions, recPlay, recExit;
	boolean hoverAbout, hoverOptions, hoverPlay, hoverExit;
	BufferedImage titleScreenBG, title, play, play2, about, about2, settings, settings2, options, options2, exit, exit2;

	//start screen
	Rectangle recLB, recNewGame;
	boolean hoverNewGame, hoverLB;
	BufferedImage menuScreenBackground, gameTitle, newGame, leaderboard, newGame2, leaderboard2, back;

	// settings
	ArrayList <Rectangle> recVolume; 
	// im thinking we have like 5 buttons and its just 5 levels of volume next to each other so its like a fake slider
	ArrayList <Rectangle> recDifficulty; 
	// maybe we just have hp regen vs no regen for difficulty
	BufferedImage volumeImg, difficultyImg;
	int difficulty = 1; // maybe just 1=easy 2=hard?
	int volume = 3; // 1-5??

	// game
	ArrayList <BufferedImage> cowImages  = new ArrayList<BufferedImage>();
	
	// Rectangle player = new Rectangle(517, 328, 46, 64);
	// HashSet <Rectangle> walls = new HashSet <Rectangle>();
	BufferedImage tempBG, playerImage, cowImage;
	Rectangle border = new Rectangle(100, 100, 880, 520);
	Player suki = new Player(100, 2, new Rectangle (517, 382, 46, 10), new Rectangle(517, 328, 46, 64), 0, 0);
	// HashSet<Cow> cows = new HashSet<Cow>();
	FloorMap currentMap; 
	ArrayList <FloorMap> maps = new ArrayList<>();
	String[] cowNames = {
		"baseCow",
		"paceCow",
		"spaceCow",
		"wasteCow"
	};
	int currentCowType = 0;

	public ChaseCow() throws IOException {

		//sets up JPanel
		setPreferredSize(new Dimension(1080, 720));
		setVisible(true);
		//starting the thread
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run() {
		initialize();
		while(true) {
			//main game loop
			update();
			this.repaint();
			try {
				Thread.sleep(1000/FPS);
			}catch(Exception e) {
				e.printStackTrace();
			}

			// IF SCREEN IS ON A GAME SCREEN ie != screen 0 1 2 3 4  or smth
			// then do stuff that needs to happen at all times during game
		}
	}
	
	public void initialize() {
		
		try {
			titleScreenBG = ImageIO.read(getClass().getResource("/titleBG.png"));
			System.out.println("Loaded titleScreenBG image");
			int numCows = cowNames.length;
		for (int i = 0; i < numCows; i++) {
			String cowPath = "/"+ cowNames[i] + ".png"; 
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
		Point electricalStart = new Point (100, 100); 
		tempBG = ImageIO.read(getClass().getResource("/tempBG.png"));
		System.out.println("Loaded tempBG image");

		HashSet<Triangle> tempWalls = new HashSet<>();
		tempWalls.add(new Triangle(new Point(100, 100), new Point(200, 200), new Point(300, 100)));

		HashSet<Cow> electricalCows = new HashSet<>();
		electricalCows.add(new BaseCow(300, 300, cowImages.get(currentCowType)));
		electricalCows.add(new BaseCow(400, 400, cowImages.get(currentCowType)));
		electricalCows.add(new BaseCow(500, 500, cowImages.get(currentCowType)));
		electricalCows.add(new BaseCow(600, 600, cowImages.get(currentCowType)));
		electricalCows.add(new BaseCow(700, 700, cowImages.get(currentCowType)));
		
		currentMap = new FloorMap(new Point(0,0), electricalWalls, tempWalls, tempBG, new Rectangle[0], electricalCows);

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

		//setups before the game starts running
		// walls.add(new Rectangle(200, 200, 60, 60));
		// walls.add(new Rectangle(300, 40, 40, 100));
		// walls.add(new Rectangle(450, 100, 80, 35));
		// walls.add(new Rectangle(130, 150, 15, 15));
		// walls.add(new Rectangle(250, 350, 150, 200));

		// make a method for initializing all maps. call method here
		spawnCows();
	}
	
	public static void resetVariables() {
		// RESETS ALL CHANGING VARIABLES WHEN NEW GAME IS STARTED 
	}


	public void spawnCows() {
	    // Random rand = new Random();
	    // for (int i = 0; i < 5; i++) { // spawn 5 cows for example
	    //     int x = rand.nextInt(mapWidth - 100) + 100; // ensure cows spawn within bounds
	    //     int y = rand.nextInt(mapHeight - 100) + 100;
	    //     cows.add(new BaseCow(x, y, cowImages.get(currentCowType)));
	    // }
	}

	public void update() {
		//update stuff
		move();
		keepInBound();
		Iterator <Rectangle> it = currentMap.getRectWalls().iterator();
		while (it.hasNext()) {
			checkCollision(it.next());
		}
		Iterator <Triangle> itr = currentMap.getTriWalls().iterator();
		while (itr.hasNext()) {
			checkCollision(itr.next());
		}

		if (currentMap != null) {
			currentMap.updateCows(suki);
		} else {
			System.out.println("currentMap is null");
		}
		// Iterator cowIter = cows.iterator();
		// while (cowIter.hasNext()) {
		// 	((Cow) cowIter.next()).followPlayer(suki);
		// }
	}

	public void changeMap(FloorMap currentMap) {
		currentMap.getTriWalls().clear();
		currentMap.getRectWalls().clear();
   		currentMap.getCows().clear();
		// change map according to what map is next... im not sure how to do this tbh we may need to number everysingle map
		// and add an if statement for every single one
		spawnCows();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		Graphics2D g3 = (Graphics2D) g;
		//white background
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, screenWidth, screenHeight);
		//draw stuff	
		if (tempBG != null) {
			g.drawImage(currentMap.getBG(), currentMap.getTLLocation().x, currentMap.getTLLocation().y, getWidth(), getHeight(), this);
		} else {
			System.out.println("Background image is null");
		}
		g2.setColor(Color.GREEN);
		Iterator <Rectangle> it = currentMap.getRectWalls().iterator();
		while (it.hasNext()) {
			g2.fill(it.next());
		}
		for (Triangle tri : currentMap.getTriWalls()) {
			Point[] vertices = tri.getVertices();
			int[] xPoints = {vertices[0].x, vertices[1].x, vertices[2].x};
			int[] yPoints = {vertices[0].y, vertices[1].y, vertices[2].y};
			g2.fillPolygon(xPoints, yPoints, 3);
		}
		if (playerImage != null) {
			g.drawImage(playerImage,  (screenWidth / 2) - (suki.getHitboxC().width / 2), (screenHeight / 2) - (suki.getHitboxC().height / 2), 46, 64, this);
		} else {
			System.out.println("Player image is null");
		}
		for (Cow cow : currentMap.getCows()) {
			cow.render(g, currentMap.getTLLocation().x, currentMap.getTLLocation().y);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {

		// game screen
		// if (screen == 5) {
			int key = e.getKeyCode();
			if(key == KeyEvent.VK_A) {
				left = true;
				right = false;
				try {
					playerImage = ImageIO.read(getClass().getResource("/sukiLeft.png"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}else if(key == KeyEvent.VK_D) {
				right = true;
				left = false;
				try {
					playerImage = ImageIO.read(getClass().getResource("/sukiRight.png"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}else if(key == KeyEvent.VK_W) {
				up = true;
				down = false;
				try {
					playerImage = ImageIO.read(getClass().getResource("/sukiUp.png"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}else if(key == KeyEvent.VK_S) {
				down = true;
				up = false;
				try {
					playerImage = ImageIO.read(getClass().getResource("/sukiDown.png"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		// }
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// game screen
		// if (screen == 5) {
			int key = e.getKeyCode();
			if(key == KeyEvent.VK_A) {
				left = false;
			}else if(key == KeyEvent.VK_D) {
				right = false;
			}else if(key == KeyEvent.VK_W) {
				up = false;
			}else if(key == KeyEvent.VK_S) {
				down = false;
			}
		// }
	}

	public void move() {
	    // game screen
	    // if (screen == 5) {
	        int moveX = 0, moveY = 0;
	        if (left) {
	            moveX = -suki.getSpeed();
	        } 
	        else if (right) {
	            moveX = suki.getSpeed();
	        } 
	        if (up) {
	            moveY = -suki.getSpeed();
	        } 
	        else if (down) {
	            moveY = suki.getSpeed();
	        }

	        // Update in-game coordinates
	        suki.move(moveX, moveY);

	        System.out.println(suki.getGamePos().x + " " + suki.getGamePos().y);

	        // center player
	        suki.getHitboxC().x = (screenWidth / 2) - (suki.getHitboxC().width / 2);
	        suki.getHitboxC().y = (screenHeight / 2) - (suki.getHitboxC().height / 2);

	        for (Rectangle wall : currentMap.getRectWalls()) {
	            wall.x -= moveX;
	            wall.y -= moveY;
	        }

	        for (Triangle wall : currentMap.getTriWalls()) {
	            for(Point point : wall.getVertices()) {
	                point.x -= moveX;
	                point.y -= moveY;
	            }
	        }

			// for some reason this doesn't need to be here... ??????
	        for (Cow cow : currentMap.getCows()) {
	            cow.getMapPos().x -= moveX;
	            cow.getMapPos().y -= moveY;
				System.out.println(cow.getGamePos());
	        }
	        
	        currentMap.setTLLocation(new Point(currentMap.getTLLocation().x - moveX, currentMap.getTLLocation().y - moveY));
	    // }
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

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	// TO BE FIXED
	public void keepInBound() {
		
		// game screen
		// MUST CHANGE 100 and mapWidth to changing variables -> instance variables for currentMap
		if (screen == 5) {
			if (suki.getHitboxM().x < 100)
				suki.getHitboxM().x = 100;
			else if (suki.getHitboxM().x > mapWidth - suki.getHitboxM().width)
				suki.getHitboxM().x = mapWidth - suki.getHitboxM().width;
			if (suki.getHitboxM().y < 100)
				suki.getHitboxM().y = 100;
			else if (suki.getHitboxM().y > mapHeight - suki.getHitboxM().height)
				suki.getHitboxM().y = mapHeight - suki.getHitboxM().height;
		}

	}

	public void checkCollision(Rectangle wall) {
		
		// game screen
		// if (screen == 5) {
			//check if player touches wall
			if(suki.getHitboxM().intersects(wall)) {
				System.out.println("Ow!");
				//stop the player from moving
				double left1 = suki.getHitboxM().getX();
				double right1 = suki.getHitboxM().getX() + suki.getHitboxM().getWidth();
				double top1 = suki.getHitboxM().getY();
				double bottom1 = suki.getHitboxM().getY() + suki.getHitboxM().getHeight();
				double left2 = wall.getX();
				double right2 = wall.getX() + wall.getWidth();
				double top2 = wall.getY();
				double bottom2 = wall.getY() + wall.getHeight();
				
				if(right1 > left2 && 
				   left1 < left2 && 
				   right1 - left2 < bottom1 - top2 && 
				   right1 - left2 < bottom2 - top1)
				{
					//player collides from left side of the wall
					currentMap.setTLLocation(new Point(currentMap.getTLLocation().x + suki.getSpeed(), currentMap.getTLLocation().y));
					for (Rectangle w : currentMap.getRectWalls()) {
						w.x += suki.getSpeed();
					}
				}
				else if(left1 < right2 &&
						right1 > right2 && 
						right2 - left1 < bottom1 - top2 && 
						right2 - left1 < bottom2 - top1)
				{
					//player collides from right side of the wall
					currentMap.setTLLocation(new Point(currentMap.getTLLocation().x - suki.getSpeed(), currentMap.getTLLocation().y));
					for (Rectangle w : currentMap.getRectWalls()) {
						w.x -= suki.getSpeed();
					}
				}
				else if(bottom1 > top2 && top1 < top2)
				{
					//player collides from top side of the wall
					currentMap.setTLLocation(new Point(currentMap.getTLLocation().x, currentMap.getTLLocation().y + suki.getSpeed()));
					for (Rectangle w : currentMap.getRectWalls()) {
						w.y += suki.getSpeed();
					}
				}
				else if(top1 < bottom2 && bottom1 > bottom2)
				{
					//player collides from bottom side of the wall
					currentMap.setTLLocation(new Point(currentMap.getTLLocation().x, currentMap.getTLLocation().y - suki.getSpeed()));
					for (Rectangle w : currentMap.getRectWalls()) {
						w.y -= suki.getSpeed();
					}
				}
			}				
		// }
	}
	
	public void checkCollision(Triangle wall) {
		
	}
	
	public static void main(String[] args) throws IOException {
		
		//The following lines creates your window
		
		//makes a brand new JFrame
		JFrame frame = new JFrame ("Example");
		//makes a new copy of your "game" that is also a JPanel
		ChaseCow myPanel = new ChaseCow();
		//so your JPanel to the frame so you can actually see it
		
		frame.add(myPanel);
		//so you can actually get keyboard input
		frame.addKeyListener(myPanel);
		//so you can actually get mouse input
		frame.addMouseListener(myPanel);
		//self explanatory. You don't want to resize your window because
		//it might mess up your graphics and collisions
		frame.setResizable(false);
		//self explanatory. You want to see your frame
		frame.setVisible(true);
		//some weird method that you must run
		frame.pack();
		//place your frame in the middle of the screen
		frame.setLocationRelativeTo(null);
		//without this, your thread will keep running even when you windows is closed!
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
