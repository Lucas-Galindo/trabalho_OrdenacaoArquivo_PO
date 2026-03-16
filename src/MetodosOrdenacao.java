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
                selecaoDireta(arquivoAleatorio);
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
    }
}
