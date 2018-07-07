package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RChatWindow extends Application {
	protected static String title = "Mo-Cha";
	protected static String myName = "@Name1";
	protected static String otherName = "@Name2";

	// アプリ名
	protected Text titleText = new Text(title);
	// 自分の名前
	protected Text myNameText = new Text();
	// 相手の名前
	protected Text otherNameText = new Text();
	// 送信ボタン
	protected Button sendButton = new Button("送信");
	// メッセージ入力エリア
	protected TextArea inputTextField = new TextArea();
	// メッセージ表示エリア
	protected TextArea outputTextField = new TextArea();

	protected Stage root = null;

	public RChatWindow() {
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// 画面の大きさ固定
		primaryStage.setResizable(false);
		// タイトルバー
		primaryStage.setTitle(title);
		// 初期処理
		init(primaryStage);
		primaryStage.show();

		initializeCreation();
	}

	protected void init(Stage stage) {
		root = stage;
		myNameText.setText(myName + "\n\n");
		myNameText.setFill(Color.LIGHTSEAGREEN);
		otherNameText.setText(otherName);

		BorderPane pane = new BorderPane();

		VBox vb = addVBox();
		VBox vbCenter = addVBoxCenter();

		pane.setLeft(vb);
		pane.setCenter(vbCenter);

		Scene scene = new Scene(pane, 640, 400);

		root.setScene(scene);
	}

	/**
	 * 画面下部
	 */
	private VBox addVBoxCenter() {
		// TODO 自動生成されたメソッド・スタブ
		VBox vbox = new VBox();
		HBox hbox = new HBox();

		vbox.setSpacing(20);
		vbox.setStyle("-fx-background-color:#ffffff");

		hbox.setPadding(new Insets(10));
		hbox.setStyle("-fx-background-color:#ffffff");
		hbox.setSpacing(10);

		// 送信ボタン
		sendButton.setMinWidth(60);
		sendButton.setMinHeight(40);
		sendButton.setStyle("-fx-base: #008080");

		// メッセージ入力エリア
		inputTextField.setMaxWidth(450);
		inputTextField.setMaxHeight(60);
		inputTextField.setWrapText(true);

		// メッセージ表示用エリア
		outputTextField.setMinSize(500, 300);
		outputTextField.setWrapText(true);
		outputTextField.setEditable(false);
		outputTextField.setBorder(null);

		hbox.getChildren().addAll(inputTextField, sendButton);
		vbox.getChildren().addAll(outputTextField, hbox);
		return vbox;
	}

	/**
	 * 画面左側のVBox
	 */
	private VBox addVBox() {
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(5, 20, 10, 10));
		vbox.setStyle("-fx-background-color:#383c3c");

		// 画面設計書①部分
		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		titleText.setFill(Color.WHITE);
		vbox.getChildren().add(titleText);

		// 画面設計書②部分		
		StackPane stack = new StackPane();

		Text userNamesList[] = new Text[] {
				myNameText,
				otherNameText
		};

		for (int i = 0; i < userNamesList.length; i++) {
			userNamesList[i].setFont(Font.font("Arial", FontWeight.NORMAL, 16));
			userNamesList[i].setFill(Color.WHITE);
		}

		stack.getChildren().addAll(userNamesList);
		stack.setAlignment(Pos.BOTTOM_LEFT);

		vbox.getChildren().add(stack);
		VBox.setVgrow(stack, Priority.ALWAYS);

		return vbox;
	}

	public void initializeCreation() {
		// TODO 自動生成されたメソッド・スタブ
	}
}
