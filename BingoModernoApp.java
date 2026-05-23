package org.example.projetobingo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BingoModernoApp extends Application {

    // Área de configuração de textos e cores

    // Textos do Jogo
    private final String NOME_DO_JOGO = "BINGO DA DANI";
    private final String LEGENDA_ROLETA = "BOLINHA SORTEADA";
    private final String TITULO_HISTORICO = "Bolas já Sorteadas (Ordem Crescente)";
    private final String TEXTO_BOTAO_GIRAR = "GIRAR ROLETA";
    private final String TEXTO_BOTAO_BINGO = "💥 BINGO! 💥";
    private final String TEXTO_GRITO_BINGO = "BINGO!!!";
    private final String TEXTO_BOTAO_NOVO_JOGO = "Novo Jogo";
    private final String TEXTO_BOTAO_CONTINUAR = "Continuar Cartela";

    // Cores (Códigos Hexadecimais como em sites web)
    private final String COR_FUNDO_PRINCIPAL = "#121214";   // Fundo escuro
    private final String COR_FUNDO_PANEIS = "#1a1a24";      // Fundo das caixas
    private final String COR_TEXTO_TITULO = "#a5a6c9";      // Título do jogo
    private final String COR_TEXTO_COMUM = "#f8f8f2";       // Textos normais
    private final String COR_BORDAS = "#6272a4";            // Bordas das caixas
    private final String COR_ROLETA_NUMERO = "#50fa7b";     // Verde neon do número grande
    private final String COR_BOTAO_GIRAR = "#ff79c6";       // Rosa do botão girar
    private final String COR_BOTAO_BINGO = "#ff5555";       // Vermelho do botão bingo
    private final String COR_BOLINHA_SORTEADA = "#bd93f9";  // Roxo do histórico

    // Fim da área de configuração

    private int maxNumeros;
    private List<Integer> numerosDisponiveis;
    private List<Integer> numerosSorteados;

    private Label lblNumeroAtual;
    private FlowPane painelHistorico;
    private Button btnSortear;
    private Button btnBingo;

    // Camadas da Janela (Para não abrir pop-ups novos)
    private VBox camadaJogo;
    private VBox camadaConfiguracao;
    private VBox camadaBingo;

    @Override
    public void start(Stage primaryStage) {
        StackPane raizRaiz = new StackPane();
        raizRaiz.setStyle("-fx-background-color: " + COR_FUNDO_PRINCIPAL + ";");

        // Constrói as 3 camadas da nossa interface
        construirCamadaJogo();
        construirCamadaConfiguracao();
        construirCamadaBingo();

        // Adiciona todas as camadas uma por cima da outra
        raizRaiz.getChildren().addAll(camadaJogo, camadaConfiguracao, camadaBingo);

        // Inicia mostrando apenas a configuração
        mostrarCamada(camadaConfiguracao);

        Scene cena = new Scene(raizRaiz, 900, 700);

        // 🌟 CONFIGURAÇÃO DO F11 PARA TELA CHEIA 🌟
        cena.setOnKeyPressed(evento -> {
            if (evento.getCode() == KeyCode.F11) {
                primaryStage.setFullScreen(!primaryStage.isFullScreen());
            }
        });

        primaryStage.setTitle("Bingo Premium Edition");
        primaryStage.setScene(cena);
        primaryStage.show();
    }

    private void mostrarCamada(VBox camadaVisivel) {
        camadaJogo.setVisible(false);
        camadaConfiguracao.setVisible(false);
        camadaBingo.setVisible(false);
        camadaVisivel.setVisible(true);
    }

    private void construirCamadaConfiguracao() {
        camadaConfiguracao = new VBox(20);
        camadaConfiguracao.setAlignment(Pos.CENTER);
        camadaConfiguracao.setStyle("-fx-background-color: rgba(18, 18, 20, 0.95);"); // Fundo levemente transparente

        Label lblBoasVindas = new Label("Configuração Inicial");
        lblBoasVindas.setFont(Font.font("Segoe UI", FontWeight.BOLD, 30));
        lblBoasVindas.setTextFill(Color.web(COR_TEXTO_COMUM));

        Label lblInstrucao = new Label("Digite até qual número o Bingo vai (Ex: 75):");
        lblInstrucao.setFont(Font.font("Segoe UI", 16));
        lblInstrucao.setTextFill(Color.web(COR_TEXTO_COMUM));

        TextField txtNumeros = new TextField("75");
        txtNumeros.setMaxWidth(100);
        txtNumeros.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        txtNumeros.setAlignment(Pos.CENTER);

        Button btnIniciar = new Button("COMEÇAR O JOGO");
        btnIniciar.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        btnIniciar.setStyle("-fx-background-color: " + COR_ROLETA_NUMERO + "; -fx-text-fill: #000; -fx-padding: 10 20; -fx-cursor: hand; -fx-background-radius: 8;");

        btnIniciar.setOnAction(e -> {
            try {
                maxNumeros = Integer.parseInt(txtNumeros.getText());
                if (maxNumeros <= 1) throw new NumberFormatException();
                prepararNovoJogo();
                mostrarCamada(camadaJogo);
            } catch (NumberFormatException ex) {
                lblInstrucao.setText("Por favor, digite um número válido maior que 1!");
                lblInstrucao.setTextFill(Color.web(COR_BOTAO_BINGO));
            }
        });

        camadaConfiguracao.getChildren().addAll(lblBoasVindas, lblInstrucao, txtNumeros, btnIniciar);
    }

    private void construirCamadaBingo() {
        camadaBingo = new VBox(30);
        camadaBingo.setAlignment(Pos.CENTER);
        camadaBingo.setStyle("-fx-background-color: rgba(18, 18, 20, 0.95);");

        Label lblGrito = new Label(TEXTO_GRITO_BINGO);
        lblGrito.setFont(Font.font("Segoe UI", FontWeight.EXTRA_BOLD, 100));
        lblGrito.setTextFill(Color.web(COR_BOTAO_BINGO));

        HBox botoesBingo = new HBox(20);
        botoesBingo.setAlignment(Pos.CENTER);

        Button btnNovoJogo = new Button(TEXTO_BOTAO_NOVO_JOGO);
        Button btnContinuar = new Button(TEXTO_BOTAO_CONTINUAR);

        String estiloBotoes = "-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 15 30; -fx-cursor: hand; -fx-background-radius: 10;";
        btnNovoJogo.setStyle(estiloBotoes + "-fx-background-color: " + COR_BORDAS + "; -fx-text-fill: white;");
        btnContinuar.setStyle(estiloBotoes + "-fx-background-color: " + COR_BOTAO_GIRAR + "; -fx-text-fill: #121214;");

        btnNovoJogo.setOnAction(e -> mostrarCamada(camadaConfiguracao));
        btnContinuar.setOnAction(e -> mostrarCamada(camadaJogo));

        botoesBingo.getChildren().addAll(btnNovoJogo, btnContinuar);
        camadaBingo.getChildren().addAll(lblGrito, botoesBingo);
    }

    private void construirCamadaJogo() {
        camadaJogo = new VBox(20);
        camadaJogo.setPadding(new Insets(30));
        camadaJogo.setAlignment(Pos.TOP_CENTER);

        // --- TÍTULO ---
        Label lblTitulo = new Label(NOME_DO_JOGO);
        lblTitulo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        lblTitulo.setTextFill(Color.web(COR_TEXTO_TITULO));

        // --- ÁREA DA ROLETA ---
        VBox containerRoleta = new VBox(5);
        containerRoleta.setAlignment(Pos.CENTER);
        containerRoleta.setPrefSize(350, 250);
        containerRoleta.setStyle("-fx-background-color: " + COR_FUNDO_PANEIS + "; -fx-background-radius: 20; -fx-border-color: " + COR_BORDAS + "; -fx-border-radius: 20; -fx-border-width: 2;");

        lblNumeroAtual = new Label("?");
        lblNumeroAtual.setFont(Font.font("Segoe UI", FontWeight.EXTRA_BOLD, 130));
        lblNumeroAtual.setTextFill(Color.web(COR_ROLETA_NUMERO));

        Label lblSub = new Label(LEGENDA_ROLETA);
        lblSub.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        lblSub.setTextFill(Color.web(COR_BORDAS));

        containerRoleta.getChildren().addAll(lblNumeroAtual, lblSub);

        // --- HISTÓRICO ---
        Label lblHistTitulo = new Label(TITULO_HISTORICO);
        lblHistTitulo.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 16));
        lblHistTitulo.setTextFill(Color.web(COR_TEXTO_COMUM));

        painelHistorico = new FlowPane();
        painelHistorico.setHgap(10);
        painelHistorico.setVgap(10);
        painelHistorico.setPadding(new Insets(15));
        painelHistorico.setAlignment(Pos.TOP_LEFT);

        ScrollPane scrollPane = new ScrollPane(painelHistorico);
        scrollPane.setFitToWidth(true);
        // Oculta a barra de rolagem horizontal e foca na vertical
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: " + COR_FUNDO_PANEIS + "; -fx-border-color: " + COR_BORDAS + "; -fx-border-radius: 10; -fx-background-radius: 10;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS); // Faz o histórico crescer para preencher a tela

        // --- BOTÕES DE AÇÃO ---
        HBox containerBotoes = new HBox(30);
        containerBotoes.setAlignment(Pos.CENTER);

        btnSortear = new Button(TEXTO_BOTAO_GIRAR);
        btnSortear.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        btnSortear.setPrefSize(250, 60);
        btnSortear.setStyle("-fx-background-color: " + COR_BOTAO_GIRAR + "; -fx-text-fill: #121214; -fx-background-radius: 12; -fx-cursor: hand;");

        btnBingo = new Button(TEXTO_BOTAO_BINGO);
        btnBingo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        btnBingo.setPrefSize(250, 60);
        btnBingo.setStyle("-fx-background-color: " + COR_BOTAO_BINGO + "; -fx-text-fill: white; -fx-background-radius: 12; -fx-cursor: hand;");

        btnSortear.setOnAction(e -> iniciarEfeitoRoleta());
        btnBingo.setOnAction(e -> mostrarCamada(camadaBingo));

        containerBotoes.getChildren().addAll(btnSortear, btnBingo);

        camadaJogo.getChildren().addAll(lblTitulo, containerRoleta, lblHistTitulo, scrollPane, containerBotoes);
    }

    private void prepararNovoJogo() {
        numerosDisponiveis = new ArrayList<>();
        numerosSorteados = new ArrayList<>();
        for (int i = 1; i <= maxNumeros; i++) {
            numerosDisponiveis.add(i);
        }
        Collections.shuffle(numerosDisponiveis);

        painelHistorico.getChildren().clear();
        lblNumeroAtual.setText("?");
        btnSortear.setDisable(false);
    }

    private void iniciarEfeitoRoleta() {
        if (numerosDisponiveis.isEmpty()) {
            lblNumeroAtual.setText("FIM");
            btnSortear.setDisable(true);
            return;
        }

        btnSortear.setDisable(true);
        btnBingo.setDisable(true);

        Timeline timeline = new Timeline();
        int[] ciclos = {0};

        KeyFrame frame = new KeyFrame(Duration.millis(50), e -> {
            int randomTemp = (int) (Math.random() * maxNumeros) + 1;
            lblNumeroAtual.setText(String.valueOf(randomTemp));
            ciclos[0]++;

            if (ciclos[0] >= 20) {
                timeline.stop();
                exibirNumeroReal();
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.setCycleCount(20);
        timeline.play();
    }

    private void exibirNumeroReal() {
        int sorteado = numerosDisponiveis.remove(0);
        numerosSorteados.add(sorteado);

        lblNumeroAtual.setText(String.valueOf(sorteado));
        atualizarPainelHistorico();

        btnSortear.setDisable(false);
        btnBingo.setDisable(false);
    }

    private void atualizarPainelHistorico() {
        painelHistorico.getChildren().clear();
        List<Integer> ordenada = new ArrayList<>(numerosSorteados);
        Collections.sort(ordenada);

        for (int num : ordenada) {
            Label lblNum = new Label(String.valueOf(num));
            lblNum.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
            lblNum.setAlignment(Pos.CENTER);
            lblNum.setPrefSize(50, 50);
            lblNum.setStyle("-fx-background-color: " + COR_BOLINHA_SORTEADA + "; -fx-text-fill: #121214; -fx-background-radius: 50%;");
            painelHistorico.getChildren().add(lblNum);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}