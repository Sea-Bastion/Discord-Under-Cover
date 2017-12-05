package DiscordUnderCover;

import bots.JDAAddon.CJDA;
import bots.JDAAddon.CJDABuilder;
import bots.JDAAddon.Input;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;
import java.net.URL;
import java.util.ResourceBundle;

import static bots.JDAAddon.CJDABuilder.*;


public class Main extends Application {

	//---------------------------------class vars---------------------------------
	private CJDA bot; //user bot
	private Input input;
	private Stage PrimaryStage;
	private Controller controller;
	private String DiscordStore = getDiscordFile() + "/UnderCover/vars.properties";
	private boolean loaded = false;

	//---------------------------------init---------------------------------
	public Main(){

		//get token from database
		String token = getToken(DiscordStore);

		//make bot until it's made
		while (bot == null) {

			//try to make the bot
			try {
				bot = new CJDABuilder(AccountType.CLIENT).addMessageHandlers(this::MessageIncome).setToken(token).buildBlocking();

				//if you fail to make the bot
			}catch (LoginException e){
				RefreshToken(DiscordStore);


			} catch (InterruptedException | RateLimitedException e) {
				System.err.println("unable to connect to discord retrying");

			}
		}

		//set to reconnect
		bot.setAutoReconnect(true);

		input = bot.getInput();


	}

	public void MessageIncome(String msg){
		System.out.println(msg);
	}

	@Override
	public void start(Stage stage) throws Exception {
		PrimaryStage = stage;

		PrimaryStage.setTitle("terminal");
		PrimaryStage.setResizable(true);
		PrimaryStage.setHeight(100);
		PrimaryStage.setWidth(500);

		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Main.fxml"));
		loader.setController(controller = new Controller());

		PrimaryStage.setScene(loader.load());
		loaded = true;
		PrimaryStage.show();

	}

	//---------------------------------public static void main---------------------------------
	public static void main(String args[]){
		launch(args);
	}

	private class Controller implements Initializable {

		@FXML
		TextArea TextBox;


		boolean shift = false;

		void Send(){

			String msg = TextBox.getText();

			if(!msg.isEmpty()) {

				while (msg.endsWith("\n"))
					msg = msg.substring(0, msg.length() - 1);

				while (msg.startsWith("\n"))
					msg = msg.substring(1);

					input.send(msg);
				TextBox.clear();
			}

		}

		void press(KeyEvent event){
			if(event.getCode() == KeyCode.ENTER) {
				if (!shift) {
					Send();

				} else {
					TextBox.appendText("\n");
				}
			}

			if(event.getCode() == KeyCode.SHIFT){
				shift = true;
			}
		}

		void release(KeyEvent event){
			if(event.getCode() == KeyCode.SHIFT){
				shift = false;
			}
		}

		@Override
		public void initialize(URL url, ResourceBundle resourceBundle) {
			TextBox.setOnKeyPressed(this::press);
			TextBox.setOnKeyReleased(this::release);

			PrimaryStage.widthProperty().addListener(e -> {
				TextBox.setPrefWidth(PrimaryStage.getWidth());
			});

			PrimaryStage.heightProperty().addListener(e -> {
				TextBox.setPrefHeight(PrimaryStage.getHeight());
			});
		}
	}

}
