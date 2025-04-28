package br.com.dio;

import br.com.dio.model.Board;
import br.com.dio.model.Space;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Main {
    private final static Scanner scanner = new Scanner(System.in);
    private static Board board;
    private final static int BOARD_LIMIT = 9;


    public static void main(String[] args){
        final var positions = Stream.of(args)
                .collect(Collectors.toMap(
                        k->k.split(";")[0],
                        v->v.split(";")[1]

                ));
        //menu
        var option = -1;
        while (true){
            System.out.println("Selecione uma das opções a seguir");
            System.out.println("1 - Iniciar um novo Jogo");
            System.out.println("2 - Colocar um novo número");
            System.out.println("3 - Remover um número");
            System.out.println("4 - Visualizar jogo atual");
            System.out.println("5 - Verificar status do jogo");
            System.out.println("6 - limpar jogo");
            System.out.println("7 - Finalizar jogo");
            System.out.println("8 - Sair");

            option = scanner.nextInt();

            switch (option){
                case 1 -> startGame(positions);
                case 2 -> inputNumber();
                case 3 -> removeNumber();
                case 4 -> showCurrentGame();
                case 5 -> showGameStatus();
                case 6 -> clearGame();
                case 7 -> finishGame();
                case 8 -> System.exit(0);
                default -> System.out.println("Opção inválida, selecione uma das opções do menu");
            }            }
        }

    private static void finishGame() {
    }

    private static void clearGame() {
    }

    private static void showGameStatus() {
    }

    private static void showCurrentGame() {
        if (isNull(board)){
            System.out.println("O jogo ainda não foi iniciado iniciado");
            return;
        }

        // Cria um array de objetos com 81 posições (9x9),
        // onde serão guardados os valores que vão ser exibidos no tabuleiro.
        var args = new Object[81];
        var argPos = 0;
        for (int i = 0; i < BOARD_LIMIT; i++) {
            for (var col: board.getSpaces()){
                args[argPos ++] = " " + ((isNull(col.get(i).getActual())) ? " " : col.get(i).getActual());
            }
        }
        System.out.println("Seu jogo se encontra da seguinte forma");
        System.out.printf((BOARD_TEMPLATE) + "\n", args);
    }

    private static void inputNumber() {
        // Primeiro, verificamos se o tabuleiro (board) está nulo.
        // Se estiver nulo, significa que o jogo ainda não foi iniciado
        if (isNull(board)){
            System.out.println("O jogo ainda não foi iniciado iniciado");
            return;
        }


        System.out.println("Informe a coluna que em que o número será inserido");
        // Usamos uma função que obriga o usuário a informar um número válido entre 0 e 8 (inclusive), que são as colunas do Sudoku.
        var col = runUntilGetValidNumber(0, 8);
        System.out.println("Informe a linha que em que o número será inserido");

        var row = runUntilGetValidNumber(0, 8);
        System.out.printf("Informe o número que vai entrar na posição [%s,%s]\n", col, row);

        var value = runUntilGetValidNumber(1, 9);

        //  alterar o valor no tabuleiro chamando o met "changeValue" do board.
        // Esse metodo retorna "false" se a posição é de um número fixo (não pode ser alterado)
        if (!board.changeValue(col, row, value)){
            System.out.printf("A posição [%s,%s] tem um valor fixo\n", col, row);
        }
    }

    private static void removeNumber() {
        if (isNull(board)){
            System.out.println("O jogo ainda não foi iniciado iniciado");
            return;
        }


        System.out.println("Informe a coluna que em que o número será inserido");
        var col = runUntilGetValidNumber(0, 8);
        System.out.println("Informe a linha que em que o número será inserido");

        var row = runUntilGetValidNumber(0, 8);
        System.out.printf("Informe o número que vai entrar na posição [%s,%s]\n", col, row);

        var value = runUntilGetValidNumber(1, 9);


        if (!board.clearValue(col, row)){
            System.out.printf("A posição [%s,%s] tem um valor fixo\n", col, row);
        }

    }



    private static int runUntilGetValidNumber(final int min, final int max) {
        var current = scanner.nextInt();
        // Enquanto o número informado for menor que o mínimo ou maior que o máximo permitido...
        while (current < min || current > max){
            // Informa ao usuário que ele precisa digitar um número dentro do intervalo permitido.
            System.out.printf("Informe um número entre %s e %s\n", min, max);
            current = scanner.nextInt();
        }
        return current;
    }

    private static void startGame(Map<String, String> positions) {
        //verificar se o boad nao é nulo
        if (nonNull(board)){
            System.out.println("O jogo já foi iniciado");
            return;
        }

        //caso o jogo nao tenha sido iniciado precisa iniciar a estrutura
        List<List<Space>> spaces = new ArrayList<>();
        //iteração sobre o arraylist
        for (int i = 0; i < BOARD_LIMIT; i++) {// pra cada linha do tabuleiro (0 até 8)
            spaces.add(new ArrayList<>());// adicionamos uma nova linha (lista vazia) na lista de espaços.
            for (int j = 0; j < BOARD_LIMIT; j++) {// pra cada coluna dentro da linha atual
                // Buscamos a configuração da posição no mapa "positions".
                // A chave é no formato "linha,coluna", exemplo "0,0", "1,5", etc
                var positionConfig = positions.get("%s,%s".formatted(i, j));//

                // "positionConfig" é uma String no formato "valor,fixed", exemplo "5,true"
                // Agora, separamos essa string:
                var expected = Integer.parseInt(positionConfig.split(",")[0]);// Pega o número esperado (ex: 5)
                var fixed = Boolean.parseBoolean(positionConfig.split(",")[1]);// Pega se o número é fixo ou não (true/false)
                // Criamos um novo espaço (quadradinho) do tabuleiro com o número esperado e se ele é fixo ou não.
                var currentSpace = new Space(expected, fixed);

                // Adicionamos o espaço criado dentro da linha correspondente.
                //coluna na posicao i e adicona no currentSpace
                spaces.get(i).add(currentSpace);
            }
        }

        board = new Board(spaces);
        System.out.println("O jogo está pronto para começar");
    }

}


