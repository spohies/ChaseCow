import java.awt.event.*;
import java.util.*;
import java.awt.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.image.*;

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
	// Rectangle player = new Rectangle(517, 328, 46, 64);
	HashSet <Rectangle> walls = new HashSet <Rectangle>();
	BufferedImage bg, playerImage;
	Rectangle border = new Rectangle(100, 100, 880, 520);
	ArrayList <BufferedImage> cowImages;
	BufferedImage cowSprite;
	Player suki = new Player(100, 2, new Rectangle (517, 328, 46, 64));

	public ChaseCow() {
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
		}
	}
	
	public void initialize() {
		
		//setups before the game starts running
		walls.add(new Rectangle(200, 200, 60, 60));
		walls.add(new Rectangle(300, 40, 40, 100));
		walls.add(new Rectangle(450, 100, 80, 35));
		walls.add(new Rectangle(130, 150, 15, 15));
		walls.add(new Rectangle(250, 350, 150, 200));
		cowImages = new ArrayList <BufferedImage>();
		try {
			// cow images
			cowImages.add(ImageIO.read(new File("ICS4U1 Culminating/src/tempCow.png")));
			// other images
			bg = ImageIO.read(new File("ICS4U1 Culminating/src/tempBG.png"));
			playerImage = ImageIO.read(new File("ICS4U1 Culminating/src/sukiDown.png"));
		} catch (IOException e) {}

        // Initialize the cow list and add some cows
        cows = new ArrayList<Cow>();

		// cows.add()
	}
	
	public void update() {
		//update stuff
		move();
		keepInBound();
		Iterator <Rectangle> it = walls.iterator();
		while (it.hasNext()) {
			checkCollision(it.next());
		}

		Iterator<Cow> iterator = cows.iterator();

        // Iterate through cows and update their behavior
        while (iterator.hasNext()) {
            Cow cow = iterator.next();

            // Move the cow toward the player
            cow.followPlayer(suki.getHitbox());

            // // Simulate damage (example: reduce HP for demonstration purposes)
            // cow.hp -= 1; // Decrease HP to simulate gameplay (you can replace this logic)

            // Remove cow if it's dead
            if (!cow.isAlive()) {
                iterator.remove();
            }
        }
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		//white background
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, screenWidth, screenHeight);
		//draw stuff		
		g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
		g.drawImage(playerImage, suki.getHitbox().x, suki.getHitbox().y, 46, 64, this);
		g2.setColor(Color.GREEN);
		Iterator <Rectangle> it = walls.iterator();
		while (it.hasNext()) {
			g2.fill(it.next());
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_A) {
			left = true;
			right = false;
			try {
				playerImage = ImageIO.read(new File("ICS4U1 Culminating/src/sukiDown.png"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}else if(key == KeyEvent.VK_D) {
			right = true;
			left = false;
			try {
				playerImage = ImageIO.read(new File("ICS4U1 Culminating/src/sukiRight.png"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}else if(key == KeyEvent.VK_W) {
			up = true;
			down = false;
			try {
				playerImage = ImageIO.read(new File("ICS4U1 Culminating/src/sukiUp.png"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}else if(key == KeyEvent.VK_S) {
			down = true;
			up = false;
			try {
				playerImage = ImageIO.read(new File("ICS4U1 Culminating/src/sukiDown.png"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
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
	}

	void move() {
		if(left)
			suki.getHitbox().x -= suki.getSpeed();
		else if(right)
			suki.getHitbox().x += suki.getSpeed();
		
		if(up)
			suki.getHitbox().y -= suki.getSpeed();
		else if(down)
			suki.getHitbox().y += suki.getSpeed();
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

	void keepInBound() {
		if (suki.getHitbox().x < 100)
			suki.getHitbox().x = 100;
		else if (suki.getHitbox().x > mapWidth - suki.getHitbox().width)
			suki.getHitbox().x = mapWidth - suki.getHitbox().width;
		if (suki.getHitbox().y < 100)
			suki.getHitbox().y = 100;
		else if (suki.getHitbox().y > mapHeight - suki.getHitbox().height)
			suki.getHitbox().y = mapHeight - suki.getHitbox().height;
	}

	void checkCollision(Rectangle wall) {
		//check if player touches wall
		if(suki.getHitbox().intersects(wall)) {
			System.out.println("Ow!");
			//stop the player from moving
			double left1 = suki.getHitbox().getX();
			double right1 = suki.getHitbox().getX() + suki.getHitbox().getWidth();
			double top1 = suki.getHitbox().getY();
			double bottom1 = suki.getHitbox().getY() + suki.getHitbox().getHeight();
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
				suki.getHitbox().x = wall.x - suki.getHitbox().width;
	        }
	        else if(left1 < right2 &&
	        		right1 > right2 && 
	        		right2 - left1 < bottom1 - top2 && 
	        		right2 - left1 < bottom2 - top1)
	        {
	            //player collides from right side of the wall
	        	suki.getHitbox().x = wall.x + wall.width;
	        }
	        else if(bottom1 > top2 && top1 < top2)
	        {
	            //player collides from top side of the wall
	        	suki.getHitbox().y = wall.y - suki.getHitbox().height;
	        }
	        else if(top1 < bottom2 && bottom1 > bottom2)
	        {
	            //player collides from bottom side of the wall
	        	suki.getHitbox().y = wall.y + wall.height;
	        }
		}
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
