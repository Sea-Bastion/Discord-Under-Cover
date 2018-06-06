package DiscordUnderCover;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.apache.commons.collections4.Get;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;

public class InputBox extends Application implements Runnable {

	private Controller controller = new Controller();
	private Stage PrimaryStage;

	InputBox(){
		super();
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start(Stage primaryStage) throws IOException {
		PrimaryStage = primaryStage;
		FXMLLoader loader = FXMLLoader.load(getClass().getResource("InputBox.fxml"));
		loader.setController(controller);
		primaryStage.setScene(loader.load());
		primaryStage.show();
	}

	String GetInputStream(){
		String Return = "";
		synchronized (Return){
			try {
				Return.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return Return;

	}

	public void close(){
		PrimaryStage.close();
	}

	@Override
	public void run() {
		launch();
	}


	private class Controller {

		TextField TextBox;

		void Send(){



		}

		public void Input(KeyEvent event){
			if(event.getCode() == KeyCode.ENTER){
				Send();
			}
		}
	}
}
