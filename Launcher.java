package org.example.projetobingo;

public class Launcher {
    public static void main(String[] args) {
        // Essa linha "engana" o Java e faz ele carregar os gráficos antes de abrir o Bingo
        BingoModernoApp.main(args);
    }
}