import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.List;

public class Game{

	//properties
	public static boolean showGrid = true;
	public static boolean addTrees = true;
	public static boolean addWater = false;
	public static boolean game_over = false;

	//values
	public static int sprite_size = 8;
	public static int scale = 2;
	public static int map_size = 25;
	public static int sq_size = sprite_size*scale;
	public static int grid_size = sq_size*map_size;
	public static int btn_size = 36;

	//map info
	public static int[][] terrain_map = new int[map_size][map_size];
	public static int[][] item_map = new int[map_size][map_size];
	
	//image
	static Image bipi;
	static Image bridge_h;
	static Image bridge_v;
	static Image castle;
	static Image hammer;
	static Image key;
	static Image tree;
	static Image yeet;

	//player info
	public static Player p1 = new Player();

	//npc info
	public int max_enemy = 7;
	public static List<Enemy> enemies = new ArrayList<Enemy>();

	//frames and panels
	public static JFrame window = new JFrame("Sprite Castle - by Milk");
	public static GridMap grid = new GridMap();
	public static User_Input input = new User_Input();
	public static Settings game_info = new Settings();

	//Player class definition
	public static class Player{
		public int x = map_size/2;
		public int y = map_size/2;

		public int hammer = 0;
		public int key = 0;
		public int castles = 0;
		public int enemies = 0;

		public Player(){}
		public Player(int x, int y){
			this.x = x;
			this.y = y;
		}
		public Player(int x, int y, int h, int k){
			this.x = x;
			this.y = y;
			this.hammer = hammer;
			this.key = key;
		}

		public void walk(String dir){
			//System.out.println("Hey I'm walking here!");
			if(collide(dir)){
				System.out.println("ow");
				return;
			}

			switch(dir){
				case "north": this.y--;break;
				case "south": this.y++;break;
				case "west": this.x--;break;
				case "east": this.x++;break;
				default: break;
			}
			return;
		}
		public boolean collide(String dir){
			if(dir.equals("north") && (this.y == 0 || terrain_map[y-1][x] != 0)){
				return true;
			}else if(dir.equals("south") && (this.y == map_size-1 || terrain_map[y+1][x] != 0)){
				return true;
			}else if(dir.equals("west") && (this.x == 0 || terrain_map[y][x-1] != 0)){
				return true;
			}else if(dir.equals("east") && (this.x == map_size-1 || terrain_map[y][x+1] != 0)){
				return true;
			}else{
				return false;
			}
		}
		public void randomPlace(){
			int rx;
  			int ry;
  			do{
  				rx = (int)(Math.floor(Math.random() * map_size));
  				ry = (int)(Math.floor(Math.random() * map_size));
  			}while(terrain_map[ry][rx] != 0);

  			this.x = rx;
  			this.y = ry;
		}

	}

	public static class Enemy{
		public int x;
		public int y;
		public boolean show = true;

		public Enemy(int x, int y){
			this.x = x;
			this.y = y;
			show = true;
		}
		//movement
		public void walk(){
			List<String> directions = Arrays.asList("north", "west", "east", "south");
			//eliminate some options
			if(x == 0){
				directions.remove("west");
			}if(y == 0){
				directions.remove("north");
			}if(x == map_size-1){
				directions.remove("east");
			}if(y == map_size-1){
				directions.remove("south");
			}

			//walk
			int r = (int)(Math.floor(Math.random()*directions.size()));
			switch(directions.get(r)){
				case "north": y--;break;
				case "south": y++;break;
				case "west": x--;break;
				case "east":x++;break;
				default: break;
			}
			return;
		}
		public void duel(Player p){
			if((p.x == x) && (p.y == y) && show){
				//enemy dead
				if(p.hammer > 0){
					p.hammer--;
					show = false;
				}
				//player dead
				else{
					game_over = true;
				}
			}
			return;
		}
		public boolean collide(String dir){
			if(dir.equals("north") && terrain_map[y-1][x] != 0){
				return true;
			}else if(dir.equals("south") && terrain_map[y+1][x] != 0){
				return true;
			}else if(dir.equals("west") && terrain_map[y][x-1] != 0){
				return true;
			}else if(dir.equals("east") && terrain_map[y][x+1] != 0){
				return true;
			}else{
				return false;
			}
		}
	}

	public static class GridMap extends JPanel{
		//objects

		public GridMap(){
			getIMG();

			//clear map
            for(int i=0;i<map_size;i++){
            	for(int j=0;j<map_size;j++){
            		terrain_map[j][i] = 0;
            		item_map[j][i] = 0;
            	}
            }

            //water and trees
            if(addWater)
            	addNature(5, 1, 0.25, 0.01);               //water
  			if(addTrees)
  				addNature(4, 2, 0.20, 0.025);              //tree


  			//make objects

  			//make characters
  			setVisible(true);
  			p1.randomPlace();
		}

		//images
		public void getIMG(){
			try{
				bipi = ImageIO.read(new File("assets/bipi.png")).getScaledInstance(sq_size, sq_size, Image.SCALE_DEFAULT);
				bridge_h = ImageIO.read(new File("assets/bridge_h.png")).getScaledInstance(sq_size, sq_size, Image.SCALE_DEFAULT);;
				bridge_v = ImageIO.read(new File("assets/bridge_v.png")).getScaledInstance(sq_size, sq_size, Image.SCALE_DEFAULT);;
				castle = ImageIO.read(new File("assets/castle.png")).getScaledInstance(sq_size, sq_size, Image.SCALE_DEFAULT);;
				hammer = ImageIO.read(new File("assets/hammer.png")).getScaledInstance(sq_size, sq_size, Image.SCALE_DEFAULT);;
				key = ImageIO.read(new File("assets/key.png")).getScaledInstance(sq_size, sq_size, Image.SCALE_DEFAULT);;
				tree = ImageIO.read(new File("assets/tree.png")).getScaledInstance(sq_size, sq_size, Image.SCALE_DEFAULT);;
				yeet = ImageIO.read(new File("assets/yeet.png")).getScaledInstance(sq_size, sq_size, Image.SCALE_DEFAULT);;
			}catch(IOException e){}
		}


		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			/*
			for (Point fillCell : fillCells) {
				int cellX = sq_size + (fillCell.x * sq_size);
				int cellY = sq_size + (fillCell.y * sq_size);
				g.setColor(Color.RED);
				g.fillRect(cellX, cellY, sq_size, sq_size);
			}
			*/

			//grass
            g.setColor(new Color(106, 190, 48));
            g.fillRect(sq_size, sq_size, grid_size, grid_size);

 			//terrain and items
            for(int y=0;y<map_size;y++){
            	for(int x=0;x<map_size;x++){
       				int tm = terrain_map[y][x];
       				int im = item_map[y][x];

       				int cellX = sq_size + (x * sq_size);
					int cellY = sq_size + (y * sq_size);
					
					//water
            		if(tm == 1){
            			g.setColor(new Color(142, 207, 221));
						g.fillRect(cellX, cellY, sq_size, sq_size);
            		}
            		//tree
            		else if(tm == 2){
            			g.drawImage(tree, cellX, cellY, null);
            		}		
            	}
            }

            //player
            int px = sq_size + (p1.x * sq_size);
            int py = sq_size + (p1.y * sq_size);
            g.drawImage(bipi, px, py, null);


			//grid
			if(showGrid){
				//outer rectangle
				g.setColor(Color.BLACK);
				g.drawRect(sq_size, sq_size, grid_size, grid_size);

				//horizontal
				for (int i = sq_size; i <= grid_size; i += sq_size) {
					g.drawLine(i, sq_size, i, grid_size + sq_size);
				}
				//vertical
				for (int i = sq_size; i <= grid_size; i += sq_size) {
					g.drawLine(sq_size, i, grid_size + sq_size, i);
				}
			}




		}

		public void fillCell(int x, int y) {
			//fillCells.add(new Point(x, y));
			repaint();
		}

		//randomizes the nature pools all over the map
		public void addNature(int multi, int obj, double prob, double dec){
		  int range = (int)(Math.floor(Math.random() * multi) + 1);
		  for(int a = 0; a < map_size; a+=(range)){
		    for(int b = 0; b < map_size; b+=(range)){
		      generate(obj, prob, dec, b, a);
		    }
		  }
		}

		//generates nature pools
		public void generate(int obj, double prob, double dec, int x, int y){
		  double r = Math.random();
		  if(r < prob){
		    make(obj, x, y);

		    //go in each direction
		    generate(obj, prob - dec, dec, x, y+1);
		    generate(obj, prob - dec, dec, x, y-1);
		    generate(obj, prob - dec, dec, x+1, y);
		    generate(obj, prob - dec, dec, x-1, y);
		  }else{
		    return;
		  }
		}

		//create a terrain tile at the specific pt on the map
		public void make(int obj, int x, int y){
		  if((x >= 0 && x < map_size) && (y >= 0 && y < map_size) && terrain_map[y][x] == 0)
		    terrain_map[y][x] = obj;
		}
	}

	public static class Settings extends JPanel{
		//private JCheckBox waterCheck, treesCheck, enemiesCheck;
		private JLabel hammerLbl, keysLbl, castlesLbl, enemiesLbl;

		public Settings(){
			Border bord = BorderFactory.createLineBorder(Color.black);
			//setBorder(bord);
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			setVisible(true);

			List<JLabel> set_labels = new ArrayList<JLabel>();

			hammerLbl = new JLabel("Hammers: 0");
			set_labels.add(hammerLbl);

			keysLbl = new JLabel("Keys: 0");
			set_labels.add(keysLbl);

			castlesLbl = new JLabel("Castles: 0");
			set_labels.add(castlesLbl);

			enemiesLbl = new JLabel("Enemies: 0");
			set_labels.add(enemiesLbl);

			for(JLabel lbl : set_labels){
				lbl.setFont(new Font("Consolas", Font.BOLD, 12));
				lbl.setAlignmentX(Component.RIGHT_ALIGNMENT);
				add(lbl);
			}


			//DEBUG BASED CHECK BOXES//
			/*
			waterCheck = new JCheckBox("Water");
			waterCheck.setSelected(addWater);

			treesCheck = new JCheckBox("trees");
			treesCheck.setSelected(addTrees);
			*/
		}
	}

	public static class User_Input extends JPanel implements ActionListener{
		private JButton upBtn, downBtn, leftBtn, rightBtn;
		private GridLayout arrowLayout = new GridLayout(3, 3);

		public User_Input(){
			Border bord = BorderFactory.createLineBorder(Color.black);
			setBorder(bord);
			this.setLayout(arrowLayout);
			setVisible(true);

			//create the input buttons
			ImageIcon upIMG = new ImageIcon();
			ImageIcon downIMG = new ImageIcon();
			ImageIcon leftIMG = new ImageIcon();
			ImageIcon rightIMG = new ImageIcon();
			try{
				upIMG = new ImageIcon(ImageIO.read(new File("assets/up.png")).getScaledInstance(btn_size, btn_size, Image.SCALE_DEFAULT));
				downIMG = new ImageIcon(ImageIO.read(new File("assets/down.png")).getScaledInstance(btn_size, btn_size, Image.SCALE_DEFAULT));
				leftIMG = new ImageIcon(ImageIO.read(new File("assets/left.png")).getScaledInstance(btn_size, btn_size, Image.SCALE_DEFAULT));
				rightIMG = new ImageIcon(ImageIO.read(new File("assets/right.png")).getScaledInstance(btn_size, btn_size, Image.SCALE_DEFAULT));
			}catch(IOException e){}
			

			List<JButton> btns = new ArrayList<JButton>();
			List<JLabel> fakebtns = new ArrayList<JLabel>();

			//arrow buttons
			upBtn = new JButton();
			upBtn.setIcon(upIMG);
			upBtn.setMnemonic(KeyEvent.VK_W);
			upBtn.setActionCommand("move up");

			downBtn = new JButton();
			downBtn.setIcon(downIMG);
			downBtn.setMnemonic(KeyEvent.VK_S);
			downBtn.setActionCommand("move down");

			leftBtn = new JButton();
			leftBtn.setIcon(leftIMG);
			leftBtn.setMnemonic(KeyEvent.VK_A);
			leftBtn.setActionCommand("move left");

			rightBtn = new JButton();
			rightBtn.setIcon(rightIMG);
			rightBtn.setMnemonic(KeyEvent.VK_D);
			rightBtn.setActionCommand("move right");

			//listeners
			upBtn.addActionListener(this);
			downBtn.addActionListener(this);
			leftBtn.addActionListener(this);
			rightBtn.addActionListener(this);

			//fake buttons to fill in space in the grid
			JLabel fakeBtn = new JLabel("");
			JLabel fakeBtn2 = new JLabel("");
			JLabel fakeBtn3 = new JLabel("");
			JLabel fakeBtn4 = new JLabel("");
			JLabel fakeBtn5 = new JLabel("");

			//fakebtns
			fakebtns.add(fakeBtn);
			fakebtns.add(fakeBtn2);
			fakebtns.add(fakeBtn3);
			fakebtns.add(fakeBtn4);
			fakebtns.add(fakeBtn5);

			//extra
			btns.add(upBtn);
			btns.add(downBtn);
			btns.add(leftBtn);
			btns.add(rightBtn);

			for(JButton btn : btns){
				btn.setBorderPainted(false);
				btn.setBorder(null);
				btn.setMargin(new Insets(0,0,0,0));
				btn.setContentAreaFilled(false);
			}

			for(JLabel lbl : fakebtns){
				lbl.setSize(btn_size, btn_size);
				lbl.setVisible(false);
			}

			//add it all to the grid
			add(fakeBtn);
			add(upBtn);
			add(fakeBtn2);
			add(leftBtn);
			add(fakeBtn3);
			add(rightBtn);
			add(fakeBtn4);
			add(downBtn);
			add(fakeBtn5);

		}

		//do the thing
		public void actionPerformed(ActionEvent e){
			String action = e.getActionCommand();
			if("move up".equals(action)){
				p1.walk("north");
			}else if("move down".equals(action)){
				p1.walk("south");
			}else if("move left".equals(action)){
				p1.walk("west");
			}else if("move right".equals(action)){
				p1.walk("east");
			}

			if(action.contains("move")){
				grid.repaint();
			}
			
		}

	}

	//put it all together in the window
	public JPanel contentScreen(){
		JPanel allGUI = new JPanel();
		allGUI.setLayout(null);

		//grid.setLayout(new FlowLayout(FlowLayout.CENTER));
		grid.setLocation(0, 0);
		grid.setSize(grid_size+(sq_size*2), grid_size+(sq_size*2));
		allGUI.add(grid);

		System.out.println("Player: " + p1.x + ", " + p1.y);

		int in_size = (int)(grid_size*(2.0/5.0));
		game_info.setLocation(grid_size+(sq_size*2)+(sq_size), sq_size);
		game_info.setSize(in_size, in_size);
		allGUI.add(game_info);

		//input.setLayout(new FlowLayout(FlowLayout.CENTER));
		input.setLocation(grid_size+(sq_size*2)+(sq_size), sq_size+in_size);
		input.setSize(in_size, in_size);
		allGUI.add(input);		

		allGUI.setOpaque(true);
		return allGUI;

	}

	public void createAndShowGUI(){
		//set up the frame properties
		window.setSize((grid_size + sq_size*2) + (grid_size/2), grid_size + 30 + (sq_size*2));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setContentPane(contentScreen());
		window.setLocation(200, 75);
		window.setVisible(true);
	}

	public static void main(String[] args){

		Game game = new Game();

		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				game.createAndShowGUI();
			}
		});

	}
}