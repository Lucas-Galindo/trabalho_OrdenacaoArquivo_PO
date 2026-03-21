import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class Arquivo {

    private String nomearquivo;
    private RandomAccessFile arquivo;
    private long comparacao, movimentacao;

    public RandomAccessFile getArquivo() {
        return arquivo;
    }

    public void setArquivo(RandomAccessFile arquivo) {
        this.arquivo = arquivo;
    }

    public long getComparacao() {
        return comparacao;
    }

    public void setComparacao(long comparacao) {
        this.comparacao = comparacao;
    }

    public long getMovimentacao() {
        return movimentacao;
    }

    public void setMovimentacao(long movimentacao) {
        this.movimentacao = movimentacao;
    }

    public String getNomearquivo() {
        return nomearquivo;
    }

    public void setNomearquivo(String nomearquivo) {
        this.nomearquivo = nomearquivo;
    }

    public Arquivo() {
        this.comparacao = this.movimentacao = 0;
    }

    public Arquivo(String nomearquivo) {
        try {
            java.io.File pasta = new java.io.File("Dados");
            if (!pasta.exists()) pasta.mkdirs();
            this.nomearquivo = nomearquivo;
            arquivo = new RandomAccessFile("Dados/" + nomearquivo, "rw");
            this.comparacao = this.movimentacao = 0;
        } catch (IOException e) {
            System.err.println("Erro ao abrir arquivo '" + nomearquivo + "': " + e.getMessage());
        }
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

    public int filesize() {
        try {
            return (int) (arquivo.length() / Registro.length());
        } catch (IOException e) {
            return 0;
        }
    }

    public void inserirRegNoFinal(Registro reg) {
        seekArq(filesize());
        reg.gravaNoArq(arquivo);
    }

    public void exibirArq() {
        int i = 0;
        Registro aux = new Registro();
        seekArq(0);
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

    public void seekArq(long pos) {
        try {
            arquivo.seek(pos * Registro.length());
        } catch (IOException e) { }
    }

    public void leArq() {
        int numero = Entrada.leInteger("Digite o numero (0 para sair)");
        while (numero != 0) {
            inserirRegNoFinal(new Registro(numero));
            numero = Entrada.leInteger("Digite o numero (0 para sair)");
        }
    }

    public void zeraComp() {
        this.comparacao = 0;
    }

    public void zeraMov() {
        this.movimentacao = 0;
    }

    public void copiaArquivo(Arquivo destino) {
        Registro aux = new Registro();
        seekArq(0);
        destino.truncate(0);
        destino.seekArq(0);
        while (!eof()) {
            aux.leDoArq(arquivo);
            aux.gravaNoArq(destino.arquivo);
        }
    }

    public void geraOrdenado() {
        truncate(0);
        seekArq(0);
        Registro reg = new Registro();
        for (int i = 0; i < 1024; i++) {
            reg.setNumero(i + 1);
            reg.gravaNoArq(arquivo);
        }
    }

    public void geraReverso() {
        truncate(0);
        seekArq(0);
        Registro reg = new Registro();
        for (int i = 1024; i > 0; i--) {
            reg.setNumero(i);
            reg.gravaNoArq(arquivo);
        }
    }

    public void geraRandom() {
        truncate(0);
        seekArq(0);
        Registro reg = new Registro();
        for (int i = 0; i < 1024; i++) {
            reg.setNumero((int) (Math.random() * 1500) + 1);
            reg.gravaNoArq(arquivo);
        }
    }

    // -------------------------------------------------------------------------
    // Métodos de ordenação
    // -------------------------------------------------------------------------

    public void insercaoDireta() {
        int tl = filesize(), pos;
        Registro regAtual = new Registro(), regAnterior = new Registro();
        long comparacao = 0, movimentacao = 0;

        for (int i = 1; i < tl; i++) {
            seekArq(i);
            regAtual.leDoArq(arquivo);
            pos = i;
            seekArq(pos - 1);
            regAnterior.leDoArq(arquivo);
            comparacao++;
            while (pos > 0 && regAnterior.getNumero() > regAtual.getNumero()) {
                movimentacao++;
                seekArq(pos);
                regAnterior.gravaNoArq(arquivo);
                pos--;
                if (pos > 0) {
                    seekArq(pos - 1);
                    regAnterior.leDoArq(arquivo);
                }
                comparacao++;
            }
            movimentacao++;
            seekArq(pos);
            regAtual.gravaNoArq(arquivo);
        }
        this.comparacao = comparacao;
        this.movimentacao = movimentacao;
    }

    public int buscaBinaria(int chave, int ini, int fim) {
        Registro reg = new Registro();
        int meio = (ini + fim) / 2;
        seekArq(meio);
        reg.leDoArq(arquivo);
        comparacao++;
        while (ini <= fim && chave != reg.getNumero()) {
            if (chave > reg.getNumero())
                ini = meio + 1;
            else
                fim = meio - 1;
            meio = (ini + fim) / 2;
            seekArq(meio);
            reg.leDoArq(arquivo);
            comparacao++;
        }
        if (chave > reg.getNumero())
            return meio + 1;
        return meio;
    }

    public void insercaoBinaria() {
        int tl = filesize();
        Registro regAtual = new Registro(), regAux = new Registro();
        this.comparacao = 0;
        this.movimentacao = 0;

        for (int i = 1; i < tl; i++) {
            int j = i - 1;
            seekArq(i);
            regAtual.leDoArq(arquivo);
            int pos = buscaBinaria(regAtual.getNumero(), 0, j);
            while (j >= pos) {
                this.movimentacao++;
                seekArq(j);
                regAux.leDoArq(arquivo);
                seekArq(j + 1);
                regAux.gravaNoArq(arquivo);
                j--;
            }
            this.movimentacao++;
            seekArq(j + 1);
            regAtual.gravaNoArq(arquivo);
        }
    }

    public int buscaExaustiva(int chave) {
        int i = 0;
        Registro reg = new Registro();
        seekArq(i);
        reg.leDoArq(arquivo);
        while (i < filesize() && reg.getNumero() != chave) {
            seekArq(i);
            reg.leDoArq(arquivo);
            i++;
        }
        if (reg.getNumero() == chave)
            return i;
        return -1;
    }

    public void selecaoDireta() {
        int tl = filesize(), posMenor;
        Registro leitor = new Registro(), leitorAux = new Registro(), regMenor = new Registro();
        long comparacao = 0, movimentacao = 0;

        for (int i = 0; i < tl - 1; i++) {
            seekArq(i);
            leitor.leDoArq(arquivo);
            regMenor = leitor;
            posMenor = i;
            for (int j = i + 1; j < tl; j++) {
                seekArq(j);
                leitorAux.leDoArq(arquivo);
                comparacao++;
                if (leitorAux.getNumero() < regMenor.getNumero()) {
                    regMenor = leitorAux;
                    posMenor = j;
                }
            }
            if (posMenor != i) {
                movimentacao += 2;
                seekArq(posMenor);
                regMenor.leDoArq(arquivo);
                seekArq(i);
                regMenor.gravaNoArq(arquivo);
                seekArq(posMenor);
                leitor.gravaNoArq(arquivo);
            }
        }
        this.movimentacao = movimentacao;
        this.comparacao = comparacao;
    }

    public void bolha() {
        int tl = filesize();
        Registro atual = new Registro(), prox = new Registro();
        boolean flag = true;
        long comparacao = 0, movimentacao = 0;

        while (tl > 1 && flag) {
            flag = false;
            for (int j = 0; j < tl - 1; j++) {
                seekArq(j);
                atual.leDoArq(arquivo);
                seekArq(j + 1);
                prox.leDoArq(arquivo);
                comparacao++;
                if (atual.getNumero() > prox.getNumero()) {
                    movimentacao += 2;
                    seekArq(j);
                    prox.gravaNoArq(arquivo);
                    seekArq(j + 1);
                    atual.gravaNoArq(arquivo);
                    flag = true;
                }
            }
            tl--;
        }
        this.movimentacao = movimentacao;
        this.comparacao = comparacao;
    }

    public void shake() {
        int inicio = 0, fim = filesize() - 1;
        Registro atual = new Registro(), prox = new Registro();
        boolean flag = true;
        long comparacao = 0, movimentacao = 0;

        while (inicio < fim) {
            flag = false;
            for (int i = inicio; i < fim; i++) {
                seekArq(i);
                atual.leDoArq(arquivo);
                seekArq(i + 1);
                prox.leDoArq(arquivo);
                comparacao++;
                if (atual.getNumero() > prox.getNumero()) {
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
                    if (atual.getNumero() < prox.getNumero()) {
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

    public void shellSort() {
        int dist = 1, tl = filesize();
        Registro regAtual = new Registro(), regAnterior = new Registro();
        long comparacao = 0, movimentacao = 0;

        while (dist < tl)
            dist = dist * 3 + 1;
        dist = dist / 3;

        while (dist > 0) {
            for (int i = dist; i < tl; i++) {
                seekArq(i);
                regAtual.leDoArq(arquivo);
                int j = i;
                seekArq(j - dist);
                regAnterior.leDoArq(arquivo);
                comparacao++;
                while (j >= dist && regAnterior.getNumero() > regAtual.getNumero()) {
                    movimentacao++;
                    seekArq(j);
                    regAnterior.gravaNoArq(arquivo);
                    j -= dist;
                    if (j >= dist) {
                        seekArq(j - dist);
                        regAnterior.leDoArq(arquivo);
                        comparacao++;
                    }
                }
                movimentacao++;
                seekArq(j);
                regAtual.gravaNoArq(arquivo);
            }
            dist /= 3;
        }
        this.comparacao = comparacao;
        this.movimentacao = movimentacao;
    }

    public void heapSort() {
        int tl = filesize(), posPai, posF1, posF2, posMaior;
        Registro F1 = new Registro(), F2 = new Registro();
        Registro maior = new Registro(), pai = new Registro();
        Registro ini = new Registro(), fim = new Registro();
        long comparacao = 0, movimentacao = 0;

        while (tl > 1) {
            for (posPai = tl / 2 - 1; posPai >= 0; posPai--) {
                posF1 = 2 * posPai + 1;
                posF2 = posF1 + 1;
                posMaior = posF1;

                seekArq(posF1);
                F1.leDoArq(arquivo);

                if (posF2 < tl) {
                    seekArq(posF2);
                    F2.leDoArq(arquivo);
                    comparacao++;
                    if (F2.getNumero() > F1.getNumero())
                        posMaior = posF2;
                }

                seekArq(posMaior);
                maior.leDoArq(arquivo);
                seekArq(posPai);
                pai.leDoArq(arquivo);

                comparacao++;
                if (pai.getNumero() < maior.getNumero()) {
                    seekArq(posMaior);
                    pai.gravaNoArq(arquivo);
                    seekArq(posPai);
                    maior.gravaNoArq(arquivo);
                    movimentacao += 2;
                }
            }
            tl--;
            seekArq(0);
            ini.leDoArq(arquivo);
            seekArq(tl);
            fim.leDoArq(arquivo);
            seekArq(0);
            fim.gravaNoArq(arquivo);
            seekArq(tl);
            ini.gravaNoArq(arquivo);
        }
        this.movimentacao = movimentacao;
        this.comparacao = comparacao;
    }

    public void quickSortComPivo() {
        this.comparacao = 0;
        this.movimentacao = 0;
        quickCPRec(0, filesize() - 1);
    }

    private void quickCPRec(int ini, int fim) {
        int i = ini, j = fim;
        Registro reg1 = new Registro(), reg2 = new Registro();

        seekArq((ini + fim) / 2);
        reg1.leDoArq(arquivo);
        int pivo = reg1.getNumero();

        while (i < j) {
            seekArq(i);
            reg1.leDoArq(arquivo);
            while (reg1.getNumero() < pivo && i < fim) {
                comparacao++;
                i++;
                seekArq(i);
                reg1.leDoArq(arquivo);
            }

            seekArq(j);
            reg2.leDoArq(arquivo);
            while (reg2.getNumero() > pivo && j > ini) {
                comparacao++;
                j--;
                seekArq(j);
                reg2.leDoArq(arquivo);
            }

            if (i <= j) {
                seekArq(i);
                reg2.gravaNoArq(arquivo);
                seekArq(j);
                reg1.gravaNoArq(arquivo);
                i++;
                j--;
                movimentacao += 2;
            }
        }

        if (ini < j) quickCPRec(ini, j);
        if (i < fim) quickCPRec(i, fim);
    }

    public void quickSortSemPivo() {
        this.comparacao = 0;
        this.movimentacao = 0;
        quickSPRec(0, filesize() - 1);
    }

    private void quickSPRec(int ini, int fim) {
        int i = ini, j = fim;
        Registro reg = new Registro(), regAux = new Registro();

        seekArq(i);
        reg.leDoArq(arquivo);
        seekArq(j);
        regAux.leDoArq(arquivo);

        while (i < j) {
            while (i < j && reg.getNumero() <= regAux.getNumero()) {
                comparacao++;
                i++;
                seekArq(i);
                reg.leDoArq(arquivo);
            }
            seekArq(i);
            regAux.gravaNoArq(arquivo);
            seekArq(j);
            reg.gravaNoArq(arquivo);
            movimentacao += 2;

            while (i < j && reg.getNumero() <= regAux.getNumero()) {
                comparacao++;
                j--;
                seekArq(j);
                regAux.leDoArq(arquivo);
            }
            seekArq(i);
            regAux.gravaNoArq(arquivo);
            seekArq(j);
            reg.gravaNoArq(arquivo);
            movimentacao += 2;
        }

        if (ini < i - 1) quickSPRec(ini, i - 1);
        if (j + 1 < fim) quickSPRec(j + 1, fim);
    }

    public void mergeSort() {
        this.comparacao = 0;
        this.movimentacao = 0;
        Arquivo aux = new Arquivo("auxMerge.dat");
        aux.truncate(filesize());
        mergeSortRec(aux, 0, filesize() - 1);
    }

    private void mergeSortRec(Arquivo aux, int esq, int dir) {
        if (esq < dir) {
            int meio = (esq + dir) / 2;
            mergeSortRec(aux, esq, meio);
            mergeSortRec(aux, meio + 1, dir);
            fusao(aux, esq, meio, meio + 1, dir);
        }
    }

    public void mergeSort2() {
        this.comparacao = 0;
        this.movimentacao = 0;
        int tl = filesize();
        Arquivo aux = new Arquivo("auxMerge2.dat");
        aux.truncate(tl);

        for (int tam = 1; tam < tl; tam *= 2) {
            for (int esq = 0; esq < tl; esq += 2 * tam) {
                int meio = esq + tam - 1;
                int dir = Math.min(esq + 2 * tam - 1, tl - 1);
                if (meio < tl - 1)
                    fusao(aux, esq, meio, meio + 1, dir);
            }
        }
    }

    private void fusao(Arquivo aux, int ini1, int fim1, int ini2, int fim2) {
        int i = ini1, j = ini2, k = 0;
        Registro regI = new Registro(), regJ = new Registro();
        aux.truncate(fim2 - ini1 + 1);

        while (i <= fim1 && j <= fim2) {
            seekArq(i);
            regI.leDoArq(arquivo);
            seekArq(j);
            regJ.leDoArq(arquivo);
            comparacao++;
            if (regI.getNumero() < regJ.getNumero()) {
                aux.seekArq(k++);
                regI.gravaNoArq(aux.arquivo);
                i++;
            } else {
                aux.seekArq(k++);
                regJ.gravaNoArq(aux.arquivo);
                j++;
            }
            movimentacao++;
        }
        while (i <= fim1) {
            seekArq(i++);
            regI.leDoArq(arquivo);
            aux.seekArq(k++);
            regI.gravaNoArq(aux.arquivo);
            movimentacao++;
        }
        while (j <= fim2) {
            seekArq(j++);
            regJ.leDoArq(arquivo);
            aux.seekArq(k++);
            regJ.gravaNoArq(aux.arquivo);
            movimentacao++;
        }
        for (i = 0; i < k; i++) {
            aux.seekArq(i);
            regJ.leDoArq(aux.arquivo);
            seekArq(i + ini1);
            regJ.gravaNoArq(arquivo);
            movimentacao++;
        }
    }

    public int maiorElem() {
        Registro reg = new Registro();
        int maior;
        seekArq(0);
        reg.leDoArq(arquivo);
        maior = reg.getNumero();
        for (int i = 1; i < filesize(); i++) {
            reg.leDoArq(arquivo);
            if (reg.getNumero() > maior)
                maior = reg.getNumero();
        }
        return maior;
    }

    public void countingSort() {
        Arquivo arqAux = new Arquivo("auxCounting.dat");
        Registro reg = new Registro();
        int maior = maiorElem(), tl = filesize();
        int[] vet = new int[maior + 1];

        copiaArquivo(arqAux);

        arqAux.seekArq(0);
        for (int i = 0; i < tl; i++) {
            reg.leDoArq(arqAux.arquivo);
            vet[reg.getNumero()]++;
        }
        for (int i = 0; i < maior; i++) {
            vet[i + 1] = vet[i] + vet[i + 1];
        }
        arqAux.seekArq(0);
        for (int i = 0; i < tl; i++) {
            reg.leDoArq(arqAux.arquivo);
            seekArq(vet[reg.getNumero()] - 1);
            vet[reg.getNumero()]--;
            reg.gravaNoArq(arquivo);
            movimentacao++;
        }
    }

    public void bucketSort() {
        int intervalo = 0, tl = filesize(), numBaldes = 0, i;
        Registro reg = new Registro();
        ArrayList<Arquivo> baldes = new ArrayList<>();
        int maior = maiorElem();

        while (intervalo <= maior) {
            baldes.add(new Arquivo("balde" + numBaldes + ".dat"));
            baldes.get(numBaldes).truncate(0);
            for (i = 0; i < tl; i++) {
                seekArq(i);
                reg.leDoArq(arquivo);
                comparacao++;
                if (reg.getNumero() >= intervalo && reg.getNumero() < intervalo + 10) {
                    reg.gravaNoArq(baldes.get(numBaldes).arquivo);
                    movimentacao++;
                }
            }
            intervalo += 10;
            numBaldes++;
        }

        for (i = 0; i < numBaldes; i++)
            baldes.get(i).bolha();

        truncate(0);
        seekArq(0);
        for (i = 0; i < numBaldes; i++) {
            for (int j = 0; j < baldes.get(i).filesize(); j++) {
                baldes.get(i).seekArq(j);
                reg.leDoArq(baldes.get(i).arquivo);
                reg.gravaNoArq(arquivo);
                movimentacao++;
            }
        }
    }

    public void radixSort() {
        this.comparacao = 0;
        this.movimentacao = 0;
        Arquivo arqAux = new Arquivo("auxRadix.dat");
        int maior = maiorElem();
        arqAux.truncate(filesize());

        for (int i = 1; maior / i > 0; i *= 10)
            countingSortRadix(arqAux, i);
    }

    private void countingSortRadix(Arquivo arqAux, int num) {
        int[] vet = new int[10];
        int tl = filesize(), i;
        Registro reg = new Registro();

        for (i = 0; i < tl; i++) {
            seekArq(i);
            reg.leDoArq(arquivo);
            vet[(reg.getNumero() / num) % 10]++;
        }
        for (i = 1; i < vet.length; i++)
            vet[i] = vet[i] + vet[i - 1];

        for (i = tl - 1; i >= 0; i--) {
            seekArq(i);
            reg.leDoArq(arquivo);
            int pos = vet[(reg.getNumero() / num) % 10] - 1;
            arqAux.seekArq(pos);
            reg.gravaNoArq(arqAux.arquivo);
            vet[(reg.getNumero() / num) % 10]--;
            movimentacao++;
        }
        for (i = 0; i < tl; i++) {
            arqAux.seekArq(i);
            reg.leDoArq(arqAux.arquivo);
            seekArq(i);
            reg.gravaNoArq(arquivo);
            movimentacao++;
        }
    }

    public void combSort() {
        int tl = filesize(), inter = tl, i;
        Registro reg = new Registro(), regAux = new Registro();
        boolean flag = true;
        long comparacao = 0, movimentacao = 0;

        while (inter != 1 || flag) {
            inter = (inter * 10) / 13;
            if (inter < 1) inter = 1;
            flag = false;

            for (i = 0; i < tl - inter; i++) {
                seekArq(i);
                reg.leDoArq(arquivo);
                seekArq(i + inter);
                regAux.leDoArq(arquivo);
                comparacao++;
                if (reg.getNumero() > regAux.getNumero()) {
                    seekArq(i + inter);
                    reg.gravaNoArq(arquivo);
                    seekArq(i);
                    regAux.gravaNoArq(arquivo);
                    movimentacao += 2;
                    flag = true;
                }
            }
        }
        this.comparacao = comparacao;
        this.movimentacao = movimentacao;
    }

    public void gnomeSort() {
        int i = 0, tl = filesize();
        Registro reg1 = new Registro(), reg2 = new Registro();
        long comparacao = 0, movimentacao = 0;

        while (i < tl) {
            if (i == 0) {
                i++;
            } else {
                seekArq(i);
                reg1.leDoArq(arquivo);
                seekArq(i - 1);
                reg2.leDoArq(arquivo);
                comparacao++;
                if (reg1.getNumero() >= reg2.getNumero()) {
                    i++;
                } else {
                    seekArq(i);
                    reg2.gravaNoArq(arquivo);
                    seekArq(i - 1);
                    reg1.gravaNoArq(arquivo);
                    movimentacao += 2;
                    i--;
                }
            }
        }
        this.comparacao = comparacao;
        this.movimentacao = movimentacao;
    }

    private void insercaoDiretaTim(int ini, int fim) {
        Registro regAtual = new Registro(), regAnt = new Registro();
        int pos, i = ini + 1;
        while (i < fim) {
            seekArq(i);
            regAtual.leDoArq(arquivo);
            pos = buscaBinaria(regAtual.getNumero(), ini, i - 1);
            for (int j = i; j > pos; j--) {
                seekArq(j - 1);
                regAnt.leDoArq(arquivo);
                seekArq(j);
                regAnt.gravaNoArq(arquivo);
                movimentacao++;
            }
            seekArq(pos);
            regAtual.gravaNoArq(arquivo);
            movimentacao++;
            i++;
        }
    }

    public void timSort() {
        this.comparacao = 0;
        this.movimentacao = 0;
        int div = 32, tl = filesize(), dir, meio;
        Arquivo aux = new Arquivo("auxTim.dat");
        aux.truncate(tl);

        for (int i = 0; i < tl; i += div) {
            int fim = (i + div < tl) ? i + div : tl;
            insercaoDiretaTim(i, fim);
        }

        for (int tam = div; tam < tl; tam *= 2) {
            for (int esq = 0; esq < tl; esq += 2 * tam) {
                dir = Math.min(esq + 2 * tam - 1, tl - 1);
                meio = esq + tam - 1;
                if (meio < tl - 1)
                    fusao(aux, esq, meio, meio + 1, dir);
            }
        }
    }

    public void carregaOrdenacao(int metodo,
                                 Arquivo arquivoOrdenado,
                                 Arquivo arquivoAleatorio,
                                 Arquivo arquivoReverso) {
        arquivoOrdenado.geraOrdenado();
        arquivoAleatorio.geraRandom();
        arquivoReverso.geraReverso();

        switch (metodo) {
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
                arquivoOrdenado.shellSort();
                arquivoAleatorio.shellSort();
                arquivoReverso.shellSort();
                break;
            case 7:
                arquivoOrdenado.heapSort();
                arquivoAleatorio.heapSort();
                arquivoReverso.heapSort();
                break;
            case 8:
                arquivoOrdenado.quickSortComPivo();
                arquivoAleatorio.quickSortComPivo();
                arquivoReverso.quickSortComPivo();
                break;
            case 9:
                arquivoOrdenado.quickSortSemPivo();
                arquivoAleatorio.quickSortSemPivo();
                arquivoReverso.quickSortSemPivo();
                break;
            case 10:
                arquivoOrdenado.mergeSort();
                arquivoAleatorio.mergeSort();
                arquivoReverso.mergeSort();
                break;
            case 11:
                arquivoOrdenado.mergeSort2();
                arquivoAleatorio.mergeSort2();
                arquivoReverso.mergeSort2();
                break;
            case 12:
                arquivoOrdenado.countingSort();
                arquivoAleatorio.countingSort();
                arquivoReverso.countingSort();
                break;
            case 13:
                arquivoOrdenado.bucketSort();
                arquivoAleatorio.bucketSort();
                arquivoReverso.bucketSort();
                break;
            case 14:
                arquivoOrdenado.radixSort();
                arquivoAleatorio.radixSort();
                arquivoReverso.radixSort();
                break;
            case 15:
                arquivoOrdenado.combSort();
                arquivoAleatorio.combSort();
                arquivoReverso.combSort();
                break;
            case 16:
                arquivoOrdenado.gnomeSort();
                arquivoAleatorio.gnomeSort();
                arquivoReverso.gnomeSort();
                break;
            case 17:
                arquivoOrdenado.timSort();
                arquivoAleatorio.timSort();
                arquivoReverso.timSort();
                break;
        }
    }

}