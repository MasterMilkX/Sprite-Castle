import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Game{

	//properties
	public static boolean showGrid = true;
	public static boolean addTrees = true;
	public static boolean addWater = false;

	//values
	public static int sprite_size = 8;
	public static int scale = 2;
	public static int map_size = 25;
	public static int sq_size = sprite_size*scale;
	public static int grid_size = sq_size*map_size;

	//map info
	public static int[][] terrain_map = new int[map_size][map_size];
	public static int[][] item_map = new int[map_size][map_size];
	
	//image
	static BufferedImage bipi;
	static BufferedImage bridge_h;
	static BufferedImage bridge_v;
	static BufferedImage castle;
	static BufferedImage hammer;
	static BufferedImage key;
	static BufferedImage tree;
	static BufferedImage yeet;


	public static class GridMap extends JPanel{
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
  				addNature(4, 2, 0.20, 0.02);              //tree
		}

		//images
		public void getIMG(){
			try{
				bipi = ImageIO.read(new File("assets/bipi.png"));
				bridge_h = ImageIO.read(new File("assets/bridge_h.png"));
				bridge_v = ImageIO.read(new File("assets/bridge_v.png"));
				castle = ImageIO.read(new File("assets/castle.png"));
				hammer = ImageIO.read(new File("assets/hammer.png"));
				key = ImageIO.read(new File("assets/key.png"));
				tree = ImageIO.read(new File("assets/tree.png"));
				yeet = ImageIO.read(new File("assets/yeet.png"));
			}catch(IOException e){}
		}

		private List<Point> waterCells;


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
            			Image tree2 = tree.getScaledInstance(sq_size, sq_size, Image.SCALE_DEFAULT);
            			g.drawImage(tree2, cellX, cellY, null);
            		}		
            	}
            }

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

	public static void main(String[] args){
		JFrame window = new JFrame("Sprite Castle - by Milk");
		GridMap grip = new GridMap();
		window.setSize(grid_size + (grid_size/2), grid_size + 30 + (sq_size*2));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.add(grip);
		window.setVisible(true);
	}
}