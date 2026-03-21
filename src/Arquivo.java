import java.io.IOException;
import java.io.RandomAccessFile;

public class Arquivo {
    private String nomearquivo;
    private RandomAccessFile arquivo;
    private int comparacao, movimentacao;

    public Arquivo(){

    }
    public RandomAccessFile getArquivo() {
        return arquivo;
    }

    public void setArquivo(RandomAccessFile arquivo) {
        this.arquivo = arquivo;
    }

    public int getComparacao() {
        return comparacao;
    }

    public void setComparacao(int comparacao) {
        this.comparacao = comparacao;
    }

    public int getMovimentacao() {
        return movimentacao;
    }

    public void setMovimentacao(int movimentacao) {
        this.movimentacao = movimentacao;
    }

    public String getNomearquivo() {
        return nomearquivo;
    }

    public void setNomearquivo(String nomearquivo) {
        this.nomearquivo = nomearquivo;
    }

    public Arquivo(String nomearquivo) {
        try {
            arquivo = new RandomAccessFile("Dados/" + nomearquivo, "rw");
            this.comparacao = this.movimentacao = 0;
        } catch (IOException e) { }
    }

    public void executa() {
        try {
            leArq();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        exibirArq();
    }

    public void truncate(long pos) {
        try {
            arquivo.setLength(pos * Registro.length());
        } catch (IOException exc) { }
    }

    public boolean eof() {
        boolean retorno = false;
        try {
            if (arquivo.getFilePointer() == arquivo.length())
                retorno = true;
        } catch (IOException e) { }
        return retorno;
    }

    public int filesize() throws IOException {
        return (int) (arquivo.length() / Registro.length());
    }

    public void inserirRegNoFinal(Registro reg) throws IOException {
        seekArq(filesize());
        reg.gravaNoArq(arquivo);
    }

    public void exibirArq() {
        int i;
        Registro aux = new Registro();
        seekArq(0);
        i = 0;
        while (!this.eof()) {
            System.out.println("Posicao " + i);
            aux.leDoArq(arquivo);
            aux.exibirReg();
            i++;
        }
    }

    public void exibirUmRegistro(int pos) {
        Registro aux = new Registro();
        seekArq(pos);
        System.out.println("Posicao " + pos);
        aux.leDoArq(arquivo);
        aux.exibirReg();
    }

    public void seekArq(int pos) {
        try {
            arquivo.seek(pos * Registro.length());
        } catch (IOException e) { }
    }

    public void leArq() throws IOException {
        int codigo, idade;
        String nome;
        codigo = Entrada.leInteger("Digite o codigo");
        while (codigo != 0) {
            nome = Entrada.leString("Digite o nome");
            idade = Entrada.leInteger("Digite a idade");
            inserirRegNoFinal(new Registro(codigo, nome, idade));
            codigo = Entrada.leInteger("Digite o codigo");
        }
    }

    public void zeraComp() {
        this.comparacao = 0;
    }

    public void zeraMov() {
        this.movimentacao = 0;
    }

    public void copiaArquivo(Arquivo auxRev) {
        Registro aux = new Registro();
        auxRev.seekArq(0);
        auxRev.truncate(0);
        seekArq(0);
        while (!eof()) {
            aux.leDoArq(arquivo);
            aux.gravaNoArq(auxRev.arquivo);
        }
    }

    public void geraOrdenado() {
        truncate(0);
        seekArq(0);
        Registro reg = new Registro(0, "nome", 20);
        for (int i = 0; i < 1024; i++) {
            reg.setCodigo(i + 1);
            reg.gravaNoArq(arquivo);
        }
    }

    public void geraReverso() {
        truncate(0);
        seekArq(0);
        Registro reg = new Registro(0, "nome", 20);
        for (int i = 0, j = 1023; i < 1024; i++, j--) {
            reg.setCodigo(j + 1);
            reg.gravaNoArq(arquivo);
        }
    }

    public void geraRandom() {
        truncate(0);
        seekArq(0);
        Registro reg = new Registro(0, "nome", 20);
        for (int i = 0; i < 1024; i++) {
            reg.setCodigo((int) (Math.random() * 1500) + 1);
            reg.gravaNoArq(arquivo);
        }
    }

    // -------------------------------------------------------------------------
    // Métodos de ordenação (migrados de MetodosOrdenacao)
    // -------------------------------------------------------------------------

    public void insercaoDireta() throws IOException {
        int tl = filesize();
        int i = 1, posAtual;
        Registro regAtual = new Registro(), regAnterior = new Registro();

        int comparacao = 0;
        int movimentacao = 0;

        while (i < tl) {
            seekArq(i);
            regAtual.leDoArq(arquivo);
            posAtual = i;
            if (posAtual - 1 >= 0) {
                seekArq(i - 1);
                regAnterior.leDoArq(arquivo);
            }
            comparacao++;
            while (posAtual - 1 >= 0 && regAnterior.getCodigo() > regAtual.getCodigo()) {
                movimentacao++;
                seekArq(posAtual);
                regAnterior.gravaNoArq(arquivo);
                posAtual--;
                if (posAtual - 1 >= 0) {
                    seekArq(posAtual - 1);
                    regAnterior.leDoArq(arquivo);
                }
                comparacao++;
            }
            movimentacao++;
            seekArq(posAtual);
            regAtual.gravaNoArq(arquivo);
            i++;
        }
        this.comparacao = comparacao;
        this.movimentacao = movimentacao;
    }

    public int[] buscaBinaria(int chave, int tl) {
        int posIni = 0, posFim = tl - 1, posMeio = tl / 2;
        Registro registro = new Registro();
        int comp = 0;
        while (chave != registro.getCodigo() && posIni < posFim) {
            comp++;
            seekArq(posMeio);
            registro.leDoArq(arquivo);
            if (chave < registro.getCodigo())
                posFim = posMeio - 1;
            else
                posIni = posMeio + 1;
            comp++;
            posMeio = (posIni + posFim) / 2;
        }
        seekArq(posMeio);
        registro.leDoArq(arquivo);
        if (chave > registro.getCodigo())
            posMeio += 1;
        comp++;
        return new int[]{posMeio, comp};
    }

    public void insercaoBinaria() throws IOException {
        int tl = filesize();
        int pos;
        int[] retornoVetor = {};

        Registro regAux = new Registro(), leitor = new Registro();
        int movimentacao = 0, comparacao = 0;
        for (int i = 1; i < tl; i++) {
            seekArq(i);
            regAux.leDoArq(arquivo);
            retornoVetor = buscaBinaria(leitor.getCodigo(), i);
            pos = retornoVetor[0];
            comparacao += retornoVetor[1];
            for (int j = i; j > pos; j--) {
                movimentacao++;
                seekArq(j - 1);
                leitor.leDoArq(arquivo);
                seekArq(j);
                leitor.gravaNoArq(arquivo);
            }
            movimentacao++;
            seekArq(pos);
            leitor.gravaNoArq(arquivo);
        }
        this.movimentacao = movimentacao;
        this.comparacao = comparacao;
    }

    public int buscaExaustiva(int chave) throws IOException {
        int i = 0;
        seekArq(i);
        Registro reg = new Registro();
        reg.leDoArq(arquivo);
        while (i < filesize() && reg.getCodigo() != chave) {
            seekArq(i);
            reg.leDoArq(arquivo);
            i++;
        }
        if (reg.getCodigo() == chave)
            return i;
        return -1;
    }

    public void selecaoDireta() throws IOException {
        int tl = filesize();
        int posMenor;

        Registro regMenor = new Registro();
        Registro leitor = new Registro();
        Registro leitorAux = new Registro();
        int comparacao = 0;
        int movimentacao = 0;

        for (int i = 0; i < tl - 1; i++) {
            seekArq(i);
            leitor.leDoArq(arquivo);
            regMenor = leitor;
            posMenor = i;
            for (int j = i + 1; j < tl; j++) {
                seekArq(j);
                leitorAux.leDoArq(arquivo);
                if (leitorAux.getCodigo() < regMenor.getCodigo()) {
                    regMenor = leitorAux;
                    posMenor = j;
                }
                comparacao++;
            }
            seekArq(posMenor);
            regMenor.leDoArq(arquivo);
            seekArq(i);
            regMenor.gravaNoArq(arquivo);
            seekArq(posMenor);
            leitor.gravaNoArq(arquivo);
        }
        this.movimentacao = movimentacao;
        this.comparacao = comparacao;
    }

    public void bolha() throws IOException {
        int tl = filesize();
        Registro atual = new Registro();
        Registro prox = new Registro();

        int comparacao = 0;
        int movimentacao = 0;

        for (int i = tl; i > 0; i--)
            for (int j = 0; j < tl - 1; j++) {
                seekArq(j);
                atual.leDoArq(arquivo);
                seekArq(j + 1);
                prox.leDoArq(arquivo);
                if (atual.getCodigo() > prox.getCodigo()) {
                    movimentacao += 2;
                    seekArq(j);
                    prox.gravaNoArq(arquivo);
                    seekArq(j + 1);
                    atual.gravaNoArq(arquivo);
                }
            }
        this.movimentacao = movimentacao;
        this.comparacao = comparacao;
    }

    public void shake() throws IOException {
        int inicio = 0, fim = filesize() - 1;
        Registro atual = new Registro();
        Registro prox = new Registro();
        boolean flag = true;

        int movimentacao = 0;
        int comparacao = 0;

        while (inicio < fim) {
            flag = false;
            for (int i = inicio; i < fim; i++) {
                seekArq(i);
                atual.leDoArq(arquivo);
                seekArq(i + 1);
                prox.leDoArq(arquivo);
                comparacao++;
                if (atual.getCodigo() > prox.getCodigo()) {
                    movimentacao++;
                    seekArq(i);
                    prox.gravaNoArq(arquivo);
                    movimentacao++;
                    seekArq(i + 1);
                    atual.gravaNoArq(arquivo);
                    flag = true;
                }
            }
            fim--;
            if (flag) {
                flag = false;
                for (int i = fim; i > inicio; i--) {
                    seekArq(i);
                    atual.leDoArq(arquivo);
                    seekArq(i - 1);
                    prox.leDoArq(arquivo);
                    comparacao++;
                    if (atual.getCodigo() < prox.getCodigo()) {
                        movimentacao++;
                        seekArq(i);
                        prox.gravaNoArq(arquivo);
                        movimentacao++;
                        seekArq(i - 1);
                        atual.gravaNoArq(arquivo);
                    }
                }
                inicio++;
            }
        }
        this.comparacao = comparacao;
        this.movimentacao = movimentacao;
    }

    public void heapSort() throws IOException {
        int tl = filesize();
        int posPai;
        Registro F1 = new Registro();
        Registro F2 = new Registro();
        Registro maior = new Registro();
        Registro pai = new Registro();
        Registro inicio = new Registro(), fim = new Registro();

        int posF1, posF2, posMaior;
        int comparacao = 0;
        int movimentacao = 0;

        while (tl > 1) {
            for (posPai = tl / 2 - 1; posPai >= 0; posPai--) {
                posF1 = 2 * posPai + 1;
                posF2 = posF1 + 1;
                posMaior = posF1;

                seekArq(posF1);
                F1.leDoArq(arquivo);
                seekArq(posF2);
                F2.leDoArq(arquivo);

                comparacao++;
                if (posF2 < tl && F1.getCodigo() < F2.getCodigo()) {
                    posMaior = posF2;
                    maior = F2;
                    movimentacao++;
                }
                seekArq(posMaior);
                maior.leDoArq(arquivo);
                seekArq(posPai);
                pai.leDoArq(arquivo);

                comparacao++;
                if (pai.getCodigo() < maior.getCodigo()) {
                    seekArq(posMaior);
                    pai.gravaNoArq(arquivo);
                    seekArq(posPai);
                    maior.gravaNoArq(arquivo);
                    movimentacao++;
                }
            }
            tl--;
            seekArq(0);
            inicio.leDoArq(arquivo);
            seekArq(tl);
            fim.leDoArq(arquivo);
            seekArq(0);
            fim.gravaNoArq(arquivo);
            seekArq(tl);
            inicio.gravaNoArq(arquivo);
        }
        this.movimentacao = movimentacao;
        this.comparacao = comparacao;
    }

    public void shellSort() throws IOException {
        int dist = 1;
        int tl = filesize();
        while (dist < tl)
            dist = dist * 3 + 1;
        dist = dist / 3;
        while (dist > 0) {
            for (int i = dist; i < tl; i++) {
                seekArq(i);
                Registro regAtual = new Registro();
                regAtual.leDoArq(arquivo);
                int j = i;
                seekArq(j - dist);
                Registro regAnterior = new Registro();
                regAnterior.leDoArq(arquivo);
                while (j >= dist && regAnterior.getCodigo() > regAtual.getCodigo()) {
                    seekArq(j);
                    regAnterior.gravaNoArq(arquivo);
                    j -= dist;
                    if (j >= dist) {
                        seekArq(j - dist);
                        regAnterior.leDoArq(arquivo);
                    }
                }
                seekArq(j);
                regAtual.gravaNoArq(arquivo);
            }
            dist /= 3;
        }
    }


    public void carregaOrdenacao(int metodo,
                                 Arquivo arquivoOrdenado,
                                 Arquivo arquivoAleatorio,
                                 Arquivo arquivoReverso) throws IOException {

        arquivoOrdenado.geraOrdenado();
        arquivoAleatorio.geraRandom();
        arquivoReverso.geraReverso();
        switch(metodo)
        {
            case 1:
                arquivoOrdenado.insercaoDireta();
                arquivoAleatorio.insercaoDireta();
                arquivoReverso.insercaoDireta();
                break;
            case 2:
                arquivoOrdenado.insercaoBinaria();
                arquivoAleatorio.insercaoBinaria();
                arquivoReverso.insercaoBinaria();
                break;
            case 3:
                arquivoOrdenado.selecaoDireta();
                arquivoAleatorio.selecaoDireta();
                arquivoReverso.selecaoDireta();
                break;
            case 4:
                arquivoOrdenado.bolha();
                arquivoAleatorio.bolha();
                arquivoReverso.bolha();
                break;
            case 5:
                arquivoOrdenado.shake();
                arquivoAleatorio.shake();
                arquivoReverso.shake();
                break;
            case 6:
                break;
        }


    }

}
