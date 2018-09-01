package controller;

import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.geometry.*;
import java.util.Random;
import view.Cell;
import view.Board;
import model.Ship;

/**
 * Represents the data and the rules that govern this battleship game.
 * @author Miguel Perez
 * @author Jasmin
 * @author Jason McDonald
 * @author Matthew Weigel
 * @author Erick Flores
 * @version 1.0
 */
public class MainController extends Application {
	/**
	 * A boolean value specifying if this turn is the enemy's turn.
	 */
	private boolean isEnemyTurn = false;

	/**
	 * If true, the game is currently running. If false, both players are currently
	 * placing ships.
	 */
	private boolean run = false;

	/**
	 * A reference to the board that the player is using.
	 */
	private Board player;

	/**
	 * A reference to the board that the enemy is using.
	 */
	private Board enemy;

	/**
	 * An integer value specifying the amount of allowed ships.
	 */
	private int allowedShips = 4;

	/**
	 * A reference to a pseudo-random number generator.
	 */
	private Random rand = new Random();

	/**
	 * A boolean value specifying if it is the enemy's turn.
	 */
	private boolean enemysTurn = false;

	/**
	 * A boolean value specifying if either player has won this game.
	 */
	private boolean victory = false;

	/**
	 * A reference to the text area that displays the desired information.
	 */
	private TextArea info = new TextArea();// added text area

	/**
	 * An integer value specifying the enemy's turn.
	 */
	private int enemyTurnNumber = 0;

	/**
	 * An integer value specifying the players turn.
	 */
	private int playerTurnNumber = 0;

	/**
	 * A label for enemy board.
	 */
	private Label enemyLabel = new Label("Enemy - Turn " + enemyTurnNumber);

	/**
	 * A label for the player board.
	 */
	private Label playerLabel = new Label("Player - Turn " + playerTurnNumber);
	
	/**
	 * A boolean value specifying if the laser button is toggled.
	 */
	private boolean laser = false;

	/**
	 * A boolean value specifying if the missile button is toggled.
	 */
	private boolean missile = false;

	/**
	 * A boolean value specifying if the one shot button is toggled.
	 */
	private boolean oneShot =  true;

	/**
	 * A boolean value specifying if the slash button is toggled.
	 */
	private boolean slash = false;
	
	/**
	 * A boolean value specifying if the scatter bomb button is toggled.
	 */
	private boolean scatter = false;

	/**
	 * A boolean value specifying if the nuke button is toggled.
	 */
	private boolean nuke = false;

	/**
	 * A boolean value specifying if the x button is toggled.
	 */
	private boolean xBomb = false;

	/**
	 * A boolean value specifying if the donut button is toggled.
	 */
	private boolean donut = false;

	/**
	 * Sets the default message at the start of this game.
	 * 
	 * @return A reference to the default message at the start of this game
	 */
	private String getDefaultMessage(){//added default message in text area
		return "The goal of BoatWars is to sink all of the enemy ships before the enemy sinks yours. Each player takes turns firing at the other players board. If a boat has all of it's squares hit, then it is sunk. The winner is the last player with ship(s) remaining. " + 
				"To start: \n\n1) Place ships. \nLeft click the board to place a ship vertically or right click the board to place a ship horizontally. Place a total of four ships." +
				"\n\n2) Select Targets. \nSelect an attack in the Power-Ups section then select squares on the opponents board to \"hit\" that square. Using the right and left mouse buttons change the direction of your attack. If an enemy's boat was there the cell will be marked red, otherwise gray if you missed a boat. If you hit a boat then you can fire again." +
				"\n\n3) The first player to sink all of the enemy ships wins the game." +
				"\n--------------------------------------------------------";
	}

	/**
	 * Starts this game.
	 */
	public void gameStart() {
		int num = 4;

		while (num > 0) {
			int y = rand.nextInt(10);
			int x = rand.nextInt(10);
			if (enemy.placeShip(x, y, new Ship(num, Math.random() < 0.5))) {
				num--;
			}
		}
		run = true;
	}

	/**
	 * Completes the enemy's move.
	 * @throws InterruptedException 
	 */
	private void enemysMove(){
		if (!victory) {
			while (enemysTurn) {
				int y = rand.nextInt(10);
				int x = rand.nextInt(10);
				boolean isVertical = true;
				
				Cell c = player.getCell(x, y);
				if(laser) {
					if((c.y + 1 > 10 || c.y - 1 < 0) && isVertical){
						continue;
					}else if((c.x + 1 > 10 || c.x - 1 < 0) && !isVertical){
						continue;
					}
					System.out.println("Laser" + enemyTurnNumber);
					enemyDisplay(LaserButton.laser(c, isVertical));
				}
				
				if(oneShot) {
					if (c.shot) {
						continue;
					}
					System.out.println("single" + enemyTurnNumber);
					enemyDisplay(SingleShotButton.singleShot(c));
				}
				
				if(slash){
					if (c.y + 1 > 10 || c.y - 1 < 0 || c.x - 1 < 0 || c.x+1 > 10) {
						continue;
					}
					System.out.println("Slash" + enemyTurnNumber);
					enemyDisplay(SlashButton.slash(c, isVertical));
				}
				
				if(scatter){
					if (c.y + 1 > 10 || c.y - 1 < 0 || c.x - 1 < 0 || c.x+1 > 10) {
						continue;
					}
					System.out.println("Scatter" + enemyTurnNumber);
					enemyDisplay(ScatterBombButton.scatterBomb(c));
				}
				
				if(missile){
					if (c.y + 1 > 10 || c.y - 1 < 0 || c.x - 1 < 0 || c.x+1 > 10) {
						continue;
					}
					System.out.println("Missile" + enemyTurnNumber);
					enemyDisplay(MissileButton.missile(c));
				}
				
				if(nuke){
					if (c.y + 1 > 10 || c.y - 1 < 0 || c.x - 1 < 0 || c.x+1 > 10) {
						continue;
					}
					System.out.println("nuke" + enemyTurnNumber);
					enemyDisplay(NukeButton.nuke(c));
				}
				
				if(xBomb){
					if (c.y + 1 > 10 || c.y - 1 < 0 || c.x - 1 < 0 || c.x+1 > 10) {
						continue;
					}
					System.out.println("xBomb" + enemyTurnNumber);
					enemyDisplay(XButton.x(c));
				}
				if(donut){
					if (c.y + 1 > 10 || c.y - 1 < 0 || c.x - 1 < 0 || c.x+1 > 10) {
						continue;
					}
					System.out.println("Donut" + enemyTurnNumber);
					enemyDisplay(DonutButton.donut(c));
				}
				if (player.getNumShips() == 0 && !victory) {
					// print loss screen or images
					info.appendText("\n\n\n\t\t\t\tYou Lose!");
					this.victory = true;
				}
				
				if (!enemysTurn){
					enemyTurnNumber++;
					enemyLabel.setText("Enemy - Turn " + enemyTurnNumber);
					enemysTurn = false;
				}
			}
		}
	}
	
	/**
	 * Displays the specified information to the text box for the enemy.
	 * @param sunkShip An integer value specifying which cells were already hit, hit, sunk, or missed.
	 */
	private void enemyDisplay(int sunkShip) {
		enemysTurn = true;
		if(sunkShip == 2) {
			info.appendText("\nCRITICAL HIT!\nOne of your SHIPS have been SUNK! \n\nYou have " + player.getNumShips() + " ship(s) REMAINING!\n");
		}else if (sunkShip == 1) {
			info.appendText("\nOne of your SHIPS have been HIT!");
		}
		else {
			enemysTurn = false;
		}
	}
	
	/**
	 * Displays the specified information to the text box for the enemy.
	 * @param sunkShips A reference to an integer array specifying which cells were already hit, hit, sunk, or missed.
	 */
	private void enemyDisplay(int[] sunkShips){
		//0 = already hit, 1 = hit, 2 = sunk, 3 = miss
		enemysTurn = true;
		for(int i = 0; i < sunkShips.length; i++){
			if(sunkShips[i] > 0){
				enemysTurn = false;
			}
		}
		for(int i = 0; i < sunkShips.length; i++){
			if(sunkShips[i] == 2) {
				enemysTurn = true;
				info.appendText("\nCRITICAL HIT!\nOne of your SHIPS have been SUNK! \n\nYou have " + player.getNumShips() + " ship(s) REMAINING!\n");
			}else if (sunkShips[i] == 1) {
				enemysTurn = true;
				info.appendText("\nOne of your SHIPS have been HIT!");
			}
		}
	}

	/**
	 * Sets up the buttons
	 * 
	 * @param root A reference to the pane that this component is placed within
	 */
	public void setButtons(BorderPane root, Stage primaryStage) {
		VBox box = new VBox();
		HBox row1 = new HBox(10);
		HBox row2 = new HBox(10);
		HBox row3 = new HBox(10);
		HBox row4 = new HBox(10);
		HBox row5 = new HBox(10);
		Label label = new Label("Power-Ups:");
		label.setTextFill(Color.WHITE);
		label.setStyle("-fx-font-weight: bold;");

		Label label2 = new Label("Display:");
		label2.setTextFill(Color.WHITE);
		label2.setStyle("-fx-font-weight: bold;");
		
		Label label3 = new Label("Menu:");
		label3.setTextFill(Color.WHITE);
		label3.setStyle("-fx-font-weight: bold;");
		
		Button scatterBombButton = new Button("Scatter Bomb");
		//scatterBombButton.setOnAction(e-> {ScatterBombButton.scatterBomb();});
		scatterBombButton.setOnAction(e->{laser = false; missile = false; oneShot=false; slash=false; scatter = true; nuke = false; xBomb = false; donut = false;});

		Button laserButton = new Button("Laser");
		//laserButton.setStyle("-fx-base: #E9967A");
		laserButton.setOnAction(e->{laser = true; missile = false; oneShot=false; slash=false; scatter = false; nuke = false; xBomb = false; donut = false;});

		Button missileButton = new Button("Missile");
		missileButton.setOnAction(e->{laser = false; oneShot = false; missile = true; slash=false; scatter = false; nuke = false; xBomb = false; donut = false;});
		// missileButton.setOnAction(e->{MissileButton.missile();});

		Button nukeButton = new Button("Nuke"); // 9 tiles
		nukeButton.setOnAction(e->{laser = false; oneShot = false; missile = false; slash=false; scatter = false; nuke = true; xBomb = false; donut = false;});

		Button singleShotButton = new Button("Single Shot"); // 1 Tile
		singleShotButton.setOnAction(e->{laser = false; oneShot = true; missile = false; slash=false; scatter = false; nuke = false; xBomb = false; donut = false;});

		Button xButton = new Button("X");
		xButton.setOnAction(e->{laser = false; oneShot = false; missile = false; slash=false; scatter = false; nuke = false; xBomb = true; donut = false;});//X pattern

		Button slashButton = new Button("Slash");
		slashButton.setOnAction(e->{laser = false; oneShot = false; missile = false; slash=true; scatter = false; nuke = false; xBomb = false; donut = false;});

		Button donutButton = new Button("Donut");
		donutButton.setOnAction(e->{laser = false; oneShot = false; missile = false; slash= false; scatter = false; nuke = false; xBomb = false; donut = true;});
		
		Button exitButton = new Button("Exit");
		exitButton.setOnAction(e -> {
			this.exit(primaryStage);
		});
		row1.getChildren().add(singleShotButton);
		row1.getChildren().add(missileButton);
		row2.getChildren().add(laserButton);
		row2.getChildren().add(slashButton);
		row3.getChildren().add(scatterBombButton);
		row3.getChildren().add(xButton);
		row4.getChildren().add(donutButton);
		row4.getChildren().add(nukeButton);
		row5.getChildren().add(exitButton);
		
		info = new TextArea();
		info.setEditable(false);
		info.setPrefSize(300, 500);
		info.setWrapText(true);
		info.setText(getDefaultMessage());
		box.setSpacing(10);
		box.setPadding(new Insets(10, 10, 10, 10));
		box.getChildren().addAll(label, row1, row2, row3, row4, label2, info, label3, row5);
		root.setLeft(box);
	}

	/**
	 * Exits this application.
	 */
	private void exit(Stage primaryStage) {
		primaryStage.close();
	
	}

	/**
	 * Displays the specified information to the text box for the player.
	 * @param sunkShip An integer value specifying which cells were already hit, hit, sunk, or missed.
	 */
	private void display(int sunkShip) {
		enemysTurn = false;
		if(sunkShip == 2) {
			info.appendText("\nCRITICAL HIT!\n You SUNK one of the enemy's SHIPS! \n\nThe enemy has "
					+ enemy.getNumShips() + " ship(s) REMAINING!\n");
		}else if (sunkShip == 1) {
			info.appendText("\nYou HIT one of the enemy's SHIPS!");
		}
		else {
			enemysTurn = true;
		}
	}
	
	/**
	 * Displays the specified information to the text box for the player.
	 * @param sunkShips A reference to an integer array specifying which cells were already hit, hit, sunk, or missed.
	 */
	private void display(int[] sunkShips){
		//0 = already hit, 1 = hit, 2 = sunk, 3 = miss
		enemysTurn = false;
		for(int i = 0; i < sunkShips.length; i++){
			if(sunkShips[i] > 0){
				enemysTurn = true;
			}
		}
		for(int i = 0; i < sunkShips.length; i++){
			if(sunkShips[i] == 2) {
				enemysTurn = false;
				info.appendText("\nCRITICAL HIT!\n You SUNK one of the enemy's SHIPS! \n\nThe enemy has "
						+ enemy.getNumShips() + " ship(s) REMAINING!\n");
			}else if (sunkShips[i] == 1) {
				enemysTurn = false;
				info.appendText("\nYou HIT one of the enemy's SHIPS!");
			}
		}
	}
	
	/**
	 * Shoots the enemy board.
	 * @param event A reference to the mouse event specifying the location to be shot
	 */
	public void shoot(MouseEvent event) {
		if (!victory) {
			if (!run)
				return;
			Cell c = (Cell) event.getSource();
			boolean isVertical;
			if(event.getButton() == MouseButton.PRIMARY){//is vertical
				isVertical = true;
			}else{//is horizontal
				isVertical = false;
			}
			if(laser) {
				if((c.y + 1 > 10 || c.y - 1 < 0) && isVertical){
					return;
				}else if((c.x + 1 > 10 || c.x - 1 < 0) && !isVertical){
					return;
				}
				display(LaserButton.laser(c, isVertical));
			}
			if(oneShot) {
				if (c.shot) {
					return;
				}
				display(SingleShotButton.singleShot(c));
			}
			if(slash){
				if (c.y + 1 > 10 || c.y - 1 < 0 || c.x - 1 < 0 || c.x+1 > 10) {
					return;
				}
				display(SlashButton.slash(c, isVertical));
			}
			
			if(scatter){
				if (c.y + 1 > 10 || c.y - 1 < 0 || c.x - 1 < 0 || c.x+1 > 10) {
					return;
				}
				display(ScatterBombButton.scatterBomb(c));
			}
			
			if(missile){
				if (c.y + 1 > 10 || c.y - 1 < 0 || c.x - 1 < 0 || c.x+1 > 10) {
					return;
				}
				display(MissileButton.missile(c));
			}
			if(nuke){
				if (c.y + 1 > 10 || c.y - 1 < 0 || c.x - 1 < 0 || c.x+1 > 10) {
					return;
				}
				display(NukeButton.nuke(c));
			}
			if(xBomb){
				if (c.y + 1 > 10 || c.y - 1 < 0 || c.x - 1 < 0 || c.x+1 > 10) {
					return;
				}
				display(XButton.x(c));
			}
			if(donut){
				if (c.y + 1 > 10 || c.y - 1 < 0 || c.x - 1 < 0 || c.x+1 > 10) {
					return;
				}
				display(DonutButton.donut(c));
			}
			if (enemy.getNumShips() == 0) {
				// Win message or picture(s)
				info.appendText("\n\n\n\t\t\t\tYou Win!");
				this.victory = true;
			}
			if (enemysTurn)
				playerTurnNumber++;
				playerLabel.setText("Player - Turn " + playerTurnNumber);
				enemysMove();
		}
	}
	
	/**
	 * Places the player's ships.
	 * @param event A reference to the mouse event specifying the location of the new ship
	 */
	public void placeShips(MouseEvent event) {
		if (run)
			return;
		Cell c = (Cell) event.getSource();
		if (player.placeShip(c.x, c.y, new Ship(allowedShips, event.getButton() == MouseButton.PRIMARY)))
			if (--allowedShips == 0) {
				gameStart();
			}
	}
	
	/**
	 * Creates this Boat wars game.
	 * @return A reference to the parent root for this application
	 */

	public Parent create(Stage primaryStage) {
		BorderPane root = new BorderPane();
		root.setPrefSize(800, 800);
		setButtons(root, primaryStage);
		playerLabel.setTextFill(Color.WHITE);
		playerLabel.setStyle("-fx-font-weight: bold;");
		enemyLabel.setTextFill(Color.WHITE);
		enemyLabel.setStyle("-fx-font-weight: bold;");

		enemy = new Board(event -> {shoot(event);}, true);
		player = new Board(event -> {placeShips(event);}, false);
		root.setId("root");
		VBox vbox = new VBox(10, enemyLabel, enemy, playerLabel, player);
		vbox.setAlignment(Pos.CENTER);
		root.setCenter(vbox);
		return root;
	}
	
	/**
	 * 
	 * Creates the start screen.
	 * 
	 * @return Returns the pane that this start screen is placed within
	 * 
	 */
	public Parent startScreen(Stage primaryStage) {
		BorderPane pane = new BorderPane();
		pane.setPrefSize(800, 800);
		pane.setId("pane");
		Button button = new Button("START");
		button.setStyle("-fx-font-weight: bold;");
		button.setOnAction(e-> {
			Scene scene = new Scene(create(primaryStage));
			scene.getStylesheets().addAll(this.getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
		});
		pane.setCenter(button);
		return pane;
	}
	/**
	 * 
	 * Starts this BoatWars application.
	 * 
	 * @param primaryStage
	 *            A reference to the stage for this BoatWars application
	 * 
	 */
	@Override

	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("Boat Wars");
			primaryStage.setResizable(false);
			Scene scene = new Scene(startScreen(primaryStage));
			scene.getStylesheets().addAll(this.getClass().getResource("application.css").toExternalForm());
			//Scene scene = new Scene(create());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Runs this BoatWars application.
	 * 
	 * @param args
	 *            A reference to command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
