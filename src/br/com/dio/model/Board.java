package br.com.dio.model;

import java.util.Collection;
import java.util.List;

import static br.com.dio.model.GameStatusEnum.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static br.com.dio.model.GameStatusEnum.COMPLETE;
import static br.com.dio.model.GameStatusEnum.INCOMPLETE;
import static jdk.internal.org.jline.utils.Status.getStatus;

//final indica que a referencia nao pode ser alterada(mas o conteudo sim)
public class Board {
    // Lista de listas que representa o tabuleiro (9x9)

    private final List<List<Space>> spaces;

    // Construtor da classe que recebe a estrutura do tabuleiro
    public Board(final List<List<Space>> spaces){
        this.spaces = spaces;
    }

    // Getter para acessar os espaços do tabuleiro
    public List<List<Space>> getSpaces() {
        return spaces;
    }


    // Metodo que retorna o status atual do jogo
    public GameStatusEnum getStatus(){
        // Se não houver nenhum espaço alterável (não fixo) preenchido, o jogo nem começou
        if (spaces.stream()
                .flatMap(Collection::stream)// Deixa tudo numa lista só (em linha)
                .noneMatch(s -> !s.isFixed() && nonNull(s.getActual()))){// verifica se nenhum quadradinho editável foi preenchido
            return NON_STARTED;//// Jogo nao começou se nenhum espaço alteravel foi preenchido

        }
        //verifica se qualquer elemento dentro de todas as coleções aninhadas em spaces tem o campo actual igual a null.
        // //Se pelo menos um for null, retorna INCOMPLETE (incompleto), se nenhum for null, retorna COMPLETE (completo).
        return spaces.stream().flatMap(Collection::stream).anyMatch(s -> isNull(s.getActual())) ? INCOMPLETE : COMPLETE;
    }

    // Verifica se há erros no tabuleiro
    public boolean hasErrors(){
        // Se o jogo nao começou não tem erros
        if(getStatus() == NON_STARTED){
            return false;
        }
//verifica se algun espaço tem valor diferente do esperado
        return spaces.stream().flatMap(Collection::stream)
                .anyMatch(s -> nonNull(s.getActual()) && !s.getActual().equals(s.getExpected()));
    }//nonNull(s.getActual()) - verifica se o valor atual não é null
//!s.getActual().equals(s.getExpected()): verifica se o valor atual é diferente do esperado


    //que altera o valor de um espaço do tabuleiro (coluna, linha e valor informados)
    public boolean changeValue(final int col, final int row, final int value) {
        //pega o espaço específico no tabuleiro
        var space = spaces.get(col).get(row);
        // verifica se o espaço é fixo (imutável)
        if(space.isFixed()){
            return false;
        }

        //altera o valor do espaço
        space.setActual(value);
        return true;
    }


    // Limpa o valor de uma célula
    public boolean clearValue(final int col, final int row){
        //pega o espaço específico no tabuleiro
        var space = spaces.get(col).get(row);
        if(space.isFixed()){
            return false;
        }
//limpa o valor do espaço
        space.clearSpace();
        return true;
    }


    //reseta td o tabuleiro(limpasr valores n fixos)
    public void reset(){
        spaces.forEach(c -> c.forEach(Space::clearSpace));
    }

    public boolean gameIsFinished(){
        return !hasErrors() && getStatus().equals(COMPLETE);
    }
}
