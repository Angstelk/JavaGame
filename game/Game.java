package game;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import javax.swing.JFrame;
import javax.swing.Timer;

public class Game implements ActionListener, MouseListener, KeyListener
{
	public boolean gOver_flag, gStart_flag;

	public int max_score_num;
	public int ticks, y_pos, score;
	public int WIDTH = 800, HEIGHT = 800;

	public static String player_name;

	public static Game flappyBird;
	public Renderer renderer;
	public Rectangle bird;
	public Random rand;
	public static JFrame window = new JFrame();

	public ArrayList<Rectangle> obstacle;

	public Game(String pname)
	{
		player_name = pname;
		window = new JFrame();
		renderer = new Renderer();
		rand = new Random();
		Timer timer = new Timer(20, this);

		//Window settings
		window.setSize(WIDTH, HEIGHT);
		window.setTitle("Flappy Bird");
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);

		//Additional components
		window.add(renderer);
		window.addMouseListener(this);
		window.addKeyListener(this);

		//New bird object
		bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);

		//Obstacle list
		obstacle = new ArrayList<Rectangle>();

		//generateObstacle and add to list
		generateObstacle(true);
		generateObstacle(true);
		generateObstacle(true);
		generateObstacle(true);

		timer.start();
	}

	public void generateObstacle(boolean start)
	{
		int width = 70;
		int space = 400;
		int height = 50 + rand.nextInt(270);

		if (start)
		{
			obstacle.add(new Rectangle(WIDTH + width + obstacle.size() * 300, HEIGHT - height - 120, width, height));
			obstacle.add(new Rectangle(WIDTH + width + (obstacle.size() - 1) * 300, 0, width, HEIGHT - height - space));
		}
		else
		{
			obstacle.add(new Rectangle(obstacle.get(obstacle.size() - 1).x + 600, HEIGHT - height - 120, width, height));
			obstacle.add(new Rectangle(obstacle.get(obstacle.size() - 1).x, 0, width, HEIGHT - height - space));
		}
	}

	public void paintObstacle(Graphics g, Rectangle column)
	{
		g.setColor(Color.green.darker());
		g.fillRect(column.x, column.y, column.width, column.height);
	}

	//Start Game
	public static void start(String args)
	{
		flappyBird = new Game(args);
	}

	public void fly()
	{
		if(gOver_flag)
		{
			bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
			obstacle.clear();
			y_pos = 0;
			score = 0;

			generateObstacle(true);
			generateObstacle(true);
			generateObstacle(true);
			generateObstacle(true);

			gOver_flag = false;
		}

		if(!gStart_flag) gStart_flag = true;
		else if (!gOver_flag)
		{
			if(y_pos > 0) y_pos = 0;
			y_pos -= 10;
		}
	}

	public void repaint(Graphics g)
	{
		//Backgorund
		g.setColor(Color.yellow);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		//First ground layer`
		g.setColor(Color.black);
		g.fillRect(0, HEIGHT - 120, WIDTH, 120);

		//Grass
		g.setColor(Color.green);
		g.fillRect(0, HEIGHT - 120, WIDTH, 20);

		//Bird
		g.setColor(Color.cyan);
		g.fillRect(bird.x, bird.y, bird.width, bird.height);

		//Obstacle
		for (Rectangle column : obstacle)
			paintObstacle(g, column);

		//Test
		g.setColor(Color.white);
		g.setFont(new Font("Arial", 1, 70));

		//Max score update
		if(score > max_score_num)
			max_score_num = score ;

		//Start message
		if(!gStart_flag)
		{
			g.drawString("Hi " + player_name + " !" , 75, 100 );
			g.drawString("Press space or ", 70 ,  HEIGHT / 2  );
			g.drawString("click to start ", 70,  HEIGHT / 2 + 100);
		}

		//End message
		if (gOver_flag)
		{
			String score_string = String.valueOf(score);
			g.drawString("I'm sorry,  " + player_name, 100, HEIGHT / 2 - 200);
			g.drawString("game over", 100, HEIGHT / 2 - 50);
			g.drawString("Your score :" + score_string  , 100, HEIGHT / 2 + 150);
			String max_score_string = String.valueOf(max_score_num);

			//Records table update
			try
			{
				FileWriter myWriter = new FileWriter(player_name);
      	myWriter.write(max_score_string);
      	myWriter.close();
   		}
			catch (IOException e)
			{
				System.out.println("An error occurred.");
      	e.printStackTrace();
			}
		}

		//Score and Max Score monitor
		if (!gOver_flag && gStart_flag)
		{
			g.drawString(String.valueOf(score), WIDTH / 2 - 25, 75);
			g.drawString(String.valueOf(max_score_num), WIDTH / 2 - 25, 165);
		}
	}

	//Event override
	@Override
	public void actionPerformed(ActionEvent e)
	{
		int speed = 10;
		ticks++;
		if(gStart_flag)
		{
			for(int i = 0; i < obstacle.size(); i++)
			{
				Rectangle column = obstacle.get(i);
				column.x -= speed;
			}

			if(ticks % 2 == 0 && y_pos < 15)
				y_pos += 2;

			for(int i = 0; i < obstacle.size(); i++)
			{
				Rectangle column = obstacle.get(i);

				if (column.x + column.width < 0)
				{
					obstacle.remove(column);
					if (column.y == 0)
						generateObstacle(false);
				}
			}

			bird.y += y_pos;

			for(Rectangle column : obstacle)
			{
				if(column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 - 10 && bird.x + bird.width / 2 < column.x + column.width / 2 + 10)
					score++;

				if(column.intersects(bird))
				{
					gOver_flag = true;

					if (bird.x <= column.x)
						bird.x = column.x - bird.width;
					else
					{
						if (column.y != 0)
							bird.y = column.y - bird.height;
						else if (bird.y < column.height)
							bird.y = column.height;
					}
				}
			}

			if (bird.y > HEIGHT - 120 || bird.y < 0)
				gOver_flag = true;

			if (bird.y + y_pos >= HEIGHT - 120)
			{
				bird.y = HEIGHT - 120 - bird.height;
				gOver_flag = true;
			}
		}

		renderer.repaint();
	}


	//Click, space, mouse_clik handling - fly/close window action
	@Override
	public void mouseClicked(MouseEvent e)
	{
		fly();
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_SPACE)
			fly();
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			window.dispose();
	}

	//Unused but have to stay (don't clean it![J])
	@Override
	public void mousePressed(MouseEvent e)
	{
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void keyTyped(KeyEvent e)
	{

	}

	@Override
	public void keyPressed(KeyEvent e)
	{

	}

}
