package DiscordUnderCover;

import bots.JDAAddon.CJDA;
import bots.JDAAddon.Input;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;

import static bots.JDAAddon.CJDABuilder.getDiscordFile;
import static bots.JDAAddon.CJDABuilder.getToken;
import bots.JDAAddon.CJDABuilder;

import java.net.URL;
import java.util.ResourceBundle;


public class Main extends Application {

	//---------------------------------class vars---------------------------------
	private CJDA bot; //user bot
	private Input input;
	private Stage PrimareStage;

	//---------------------------------init---------------------------------
	public Main(){

		//get token from database
		String token = getToken(getDiscordFile() + "/UnderCover/vars.properties");

		//make bot until it's made
		while (bot == null) {

			//try to make the bot
			try {
				bot = new CJDABuilder(AccountType.CLIENT).setToken(token).buildBlocking();

			//if you fail to make the bot
			} catch (LoginException | InterruptedException | RateLimitedException e) {
				e.printStackTrace();

			}
		}

		//set to reconnect
		bot.setAutoReconnect(true);

		input = bot.getInput();


	}

	@Override
	public void start(Stage stage) throws Exception {
		PrimareStage = stage;

		PrimareStage.setTitle("terminal");
		PrimareStage.setResizable(true);
		PrimareStage.setHeight(400);
		PrimareStage.setWidth(500);

		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Main.fxml"));
		loader.setController(new Controller());

		PrimareStage.setScene(loader.load());
		PrimareStage.show();

	}

	//---------------------------------public static void main---------------------------------
	public static void main(String args[]){
		launch(args);
	}

	private class Controller implements Initializable {

		@FXML
		TextArea TextBox;
		@FXML
		TextArea MessageBox;


		boolean shift = false;

		void Send(){

			String msg = TextBox.getText();

			while(!msg.isEmpty() && msg.substring(msg.length()-1).equals("\n")){
				msg = msg.substring(0, msg.length()-1);
			}

			input.send(msg);
			TextBox.clear();

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

			PrimareStage.widthProperty().addListener(e -> {
				TextBox.setPrefWidth(PrimareStage.getWidth());
				MessageBox.setPrefWidth(PrimareStage.getWidth());
			});

			PrimareStage.heightProperty().addListener(e -> {
				TextBox.setPrefHeight(PrimareStage.getHeight() * 0.2);
				MessageBox.setPrefHeight(PrimareStage.getHeight() * 0.8);
			});
		}
	}

}
