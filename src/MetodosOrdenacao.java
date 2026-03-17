import java.io.IOException;
import java.io.RandomAccessFile;

public class MetodosOrdenacao {

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
                insercaoDireta(arquivoOrdenado);
                insercaoDireta(arquivoAleatorio);
                insercaoDireta(arquivoReverso);
                break;
            case 2:
                insercaoBinaria(arquivoOrdenado);
                insercaoBinaria(arquivoAleatorio);
                insercaoBinaria(arquivoReverso);
                break;
            case 3:
                selecaoDireta(arquivoOrdenado);
                selecaoDireta(arquivoAleatorio);
                selecaoDireta(arquivoReverso);
                break;
            case 4:
                bolha(arquivoOrdenado);
                bolha(arquivoAleatorio);
                bolha(arquivoReverso);
                break;
            case 5:
                shake(arquivoOrdenado);
                shake(arquivoAleatorio);
                shake(arquivoReverso);
                break;
            case 6:
                break;
        }


    }

    //LEMBRETE - COMPARACAO SIGNIFICA TODA COMPARACAO ENTRE VARIAVEIS E NAO
    //EM RELACAO A TL, COMO i<tl
    public void insercaoDireta(Arquivo arq) throws IOException {

        int tl = arq.filesize();
        int i = 1,posAtual;
        Registro regAtual = new Registro(), regAnterior = new Registro();

        int comparacao = 0;
        int movimentacao = 0;

        while(i<tl){
            arq.seekArq(i);
            regAtual.leDoArq(arq.getArquivo());
            posAtual = i;
            if(posAtual-1>=0){
                arq.seekArq(i-1);
                regAnterior.leDoArq(arq.getArquivo());
            }
            comparacao++;
            while(posAtual-1>=0 && regAnterior.getCodigo() > regAtual.getCodigo()){

                movimentacao++;

                //Escreve o anterior na frente (no atual)
                arq.seekArq(posAtual);
                regAnterior.gravaNoArq(arq.getArquivo());
                posAtual--;
                if(posAtual-1>=0){

                    //Le o proximo elemento, no caso o anterior dele
                    arq.seekArq(posAtual-1);
                    regAnterior.leDoArq(arq.getArquivo());
                }
                comparacao++;

            }
            movimentacao++;
            arq.seekArq(posAtual);
            regAtual.gravaNoArq(arq.getArquivo());
            i++;
        }
        arq.setComparacao(comparacao);
        arq.setMovimentacao(movimentacao);
    }

    public int[] buscaBinaria(Arquivo arq,int chave, int tl){
        int posIni = 0, posFim = tl - 1, posMeio = tl/2;
        Registro registro = new Registro();
//        arq.seekArq(posMeio);
//        registro.leDoArq(arq.getArquivo());
        int comp = 0;
        while(chave!=registro.getCodigo() && posIni < posFim){

            comp++;
            arq.seekArq(posMeio);
            registro.leDoArq(arq.getArquivo());
            if(chave < registro.getCodigo())
                posFim = posMeio -1;
            else
                posIni = posMeio + 1;
            //Comparacao if
            comp++;

            posMeio = (posIni+posFim)/2;
        }
        arq.seekArq(posMeio);
        registro.leDoArq(arq.getArquivo());

        if(chave > registro.getCodigo())
            posMeio+=1;
        comp++; //Comparacao do if
        //int posEComp[] = {posMeio,comp};
        return new int[]{posMeio,comp};

    }

    public void insercaoBinaria(Arquivo arq) throws IOException {
        int tl = arq.filesize();
        int pos;
        int[] retornoVetor = {};

        Registro regAux = new Registro(), leitor = new Registro();
        int movimentacao = 0, comparacao = 0;
        for(int i=1;i<tl;i++){
            //comparacao++; //Comparacao do i<tl
            arq.seekArq(i);
            regAux.leDoArq(arq.getArquivo());
            //regAux.leDoArq(arq.getArquivo());
            //elemento = regAux.getCodigo();
            retornoVetor = buscaBinaria(arq,leitor.getCodigo(),i);
            pos = retornoVetor[0];
            comparacao += retornoVetor[1];
            for(int j=i;j>pos;j--){
                movimentacao++;
                //comparacao++; // Comparacao do j>pos

                arq.seekArq(j-1);
                leitor.leDoArq(arq.getArquivo());
                arq.seekArq(j);
                //regAux.leDoArq(arq.getArquivo());
                leitor.gravaNoArq(arq.getArquivo());
            }
            movimentacao++;
            arq.seekArq(pos);
            leitor.gravaNoArq(arq.getArquivo());

        }
        arq.setMovimentacao(movimentacao);
        arq.setComparacao(comparacao);
    }

    public int buscaExaustiva(Arquivo arq, int chave) throws IOException {
        int i=0;
        arq.seekArq(i);
        Registro reg = new Registro();
        reg.leDoArq(arq.getArquivo());
        while(i<arq.filesize() && reg.getCodigo()!=chave)
        {
            arq.seekArq(i);
            reg.leDoArq(arq.getArquivo());
            i++;
        }

        if(reg.getCodigo()==chave)
            return i;
        return -1;
    }
    public void selecaoDireta(Arquivo arq) throws IOException {
        int tl = arq.filesize();
        int posMenor;

        Registro regMenor = new Registro();
        Registro leitor = new Registro();
        Registro leitorAux = new Registro();
        int comparacao = 0;
        int movimentacao = 0;

        for(int i=0;i<tl-1;i++){
            arq.seekArq(i);
            leitor.leDoArq(arq.getArquivo());
            regMenor = leitor;
            posMenor = i;

            for(int j=i+1;j<tl;j++){

                arq.seekArq(j);
                leitorAux.leDoArq(arq.getArquivo());
                if(leitorAux.getCodigo() < regMenor.getCodigo()){
                    regMenor = leitorAux;
                    posMenor = j;
                }
                comparacao++;

            }

            arq.seekArq(posMenor);
            regMenor.leDoArq(arq.getArquivo());

            arq.seekArq(i);
            regMenor.gravaNoArq(arq.getArquivo());

            arq.seekArq(posMenor);
            leitor.gravaNoArq(arq.getArquivo());


        }
        arq.setMovimentacao(movimentacao);
        arq.setComparacao(comparacao);
    }

    public void bolha(Arquivo arq) throws IOException {

        int tl = arq.filesize();
        Registro atual = new Registro();
        Registro prox = new Registro();

        int comparacao = 0;
        int movimentacao = 0;

        for(int i=tl;i>0;i++)
            for(int j=0;j<tl-1;j++){
                arq.seekArq(j);
                atual.leDoArq(arq.getArquivo());

                arq.seekArq(j+1);
                prox.leDoArq(arq.getArquivo());

                if(atual.getCodigo()>prox.getCodigo()){
                    movimentacao+=2;
                    arq.seekArq(j);
                    prox.gravaNoArq(arq.getArquivo());

                    arq.seekArq(j+1);
                    atual.gravaNoArq(arq.getArquivo());
                }

            }

        arq.setMovimentacao(movimentacao);
        arq.setComparacao(comparacao);
    }
    public void shake(Arquivo arq) throws IOException {
        int inicio = 0,fim = arq.filesize()-1;
        Registro atual = new Registro();
        Registro prox = new Registro();
        Registro regAux = new Registro();
        boolean flag = true;

        int movimentacao=0;
        int comparacao=0;
        while(inicio<fim){
            flag = false;
            for(int i = inicio;i<fim;i++){
                arq.seekArq(i);
                atual.leDoArq(arq.getArquivo());

                arq.seekArq(i+1);
                prox.leDoArq(arq.getArquivo());
                comparacao++;
                if(atual.getCodigo() > prox.getCodigo()){

                    movimentacao++;
                    arq.seekArq(i);
                    prox.gravaNoArq(arq.getArquivo());

                    movimentacao++;
                    arq.seekArq(i+1);
                    atual.gravaNoArq(arq.getArquivo());
                    flag = true;
                }
            }
            fim--;
            if(flag){
                flag = false;
                for(int i=fim;i>inicio;i--){
                    arq.seekArq(i);
                    atual.leDoArq(arq.getArquivo());

                    arq.seekArq(i-1);
                    prox.leDoArq(arq.getArquivo());
                    comparacao++;
                    if(atual.getCodigo()<prox.getCodigo()){
                        movimentacao++;

                        arq.seekArq(i);
                        prox.gravaNoArq(arq.getArquivo());
                        movimentacao++;
                        arq.seekArq(i-1);
                        atual.gravaNoArq(arq.getArquivo());
                    }

                }
                inicio++;
            }
        }
        arq.setComparacao(comparacao);
        arq.setMovimentacao(movimentacao);

    }
    public void heapSort(Arquivo arq) throws IOException {
        int tl = arq.filesize();
        int posPai;
        Registro F1 = new Registro();
        Registro F2 = new Registro();
        Registro maior = new Registro();
        Registro pai = new Registro();
        Registro inicio = new Registro(), fim = new Registro();

        int posF1, posF2, posMaior;

        int comparacao = 0;
        int movimentacao = 0;

        while(tl>1){
            for(posPai = tl/2-1;posPai>=0;posPai--){
                posF1 = 2*posPai+1;
                posF2 = posF1+1;
                posMaior = posF1;

                arq.seekArq(posF1);
                F1.leDoArq(arq.getArquivo());

                arq.seekArq(posF2);
                F2.leDoArq(arq.getArquivo());

                comparacao++;
                if(posF2 < tl && F1.getCodigo()<F2.getCodigo()){
                    posMaior = posF2;
                    maior= F2;
                    movimentacao++;
                }
                arq.seekArq(posMaior);
                maior.leDoArq(arq.getArquivo());

                arq.seekArq(posPai);
                pai.leDoArq(arq.getArquivo());

                comparacao++;
                if(pai.getCodigo()<maior.getCodigo()){
                    arq.seekArq(posMaior);
                    pai.gravaNoArq(arq.getArquivo());

                    arq.seekArq(posPai);
                    maior.gravaNoArq(arq.getArquivo());

                    movimentacao++;
                }
            }

            tl--;
            arq.seekArq(0);
            inicio.leDoArq(arq.getArquivo());

            arq.seekArq(tl);
            fim.leDoArq(arq.getArquivo());

            arq.seekArq(0);
            fim.gravaNoArq(arq.getArquivo());

            arq.seekArq(tl);
            inicio.gravaNoArq(arq.getArquivo());

        }
        arq.setMovimentacao(movimentacao);
        arq.setComparacao(comparacao);
    }

    public void shellSort(Arquivo arq) throws IOException {
        int dist = 1;
        int tl = arq.filesize();
        while(dist<tl)
            dist = dist*3+1;
        dist = dist/3;
        while(dist>0){
            for(){
                
            }
        }
    }
}
