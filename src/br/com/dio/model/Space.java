package br.com.dio.model;


//Space vai representar o espaço unico no tabuleiro
//cada quadradinho tem
//expected - o numero que deveria estar nele (correto)
//actual - numero que o jogador colocou ali (pode ser nulo ou diferente do esperado)
//fixed - se esse quadradinho já vem preenchido (não pode mudar) ou se o jogador pode editar.

public class Space {
    private Integer actual;
    private final int expected;
    private final boolean fixed;

    public Space(int expected, boolean fixed) {
        this.expected = expected;
        this.fixed = fixed;

        if(fixed){
            actual = expected;
        }
    }


    public Integer getActual() {
        return actual;
    }

    public void setActual(Integer actual) {
        if(fixed) return;//p n haver edição
        this.actual = actual;
    }

    public void clearSpace(){
        setActual(null);
    }


    public int getExpected() {
        return expected;
    }

    public boolean isFixed() {
        return fixed;
    }
}
